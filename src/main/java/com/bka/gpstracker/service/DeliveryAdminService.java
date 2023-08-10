package com.bka.gpstracker.service;

import com.bka.gpstracker.common.DeliveryStatus;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.NewDeliveryEvent;
import com.bka.gpstracker.event.UpdateDeliveryEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.StatusHistory;
import com.bka.gpstracker.model.request.NewDeliveryRequest;
import com.bka.gpstracker.model.request.UpdateDeliveryRequest;
import com.bka.gpstracker.model.response.DeliveryInfoResponse;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.solr.repository.DeliveryInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeliveryAdminService {

    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private AddressService addressService;

    public DeliveryInfo registrationDelivery(NewDeliveryRequest request, String currentUsername) {
        userService.findByUsername(request.getDriverUsername()).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DRIVER_USERNAME_INVALID));

        List<CarInfo> carInfos = carInfoService.getAllByUsername(request.getDriverUsername());
        if (carInfos.isEmpty()) throw new TrackerAppException(ErrorCode.DRIVER_IS_INACTIVE);
        CarInfo carInfo = carInfos.get(0);
        if (!carInfo.getActiveAreas().isEmpty()) {
            String districtInAddress = addressService.getDistrictInAddress(request.getToAddress());
            if (!carInfo.getActiveAreas().contains(districtInAddress))
                throw new TrackerAppException(ErrorCode.INVALID_DRIVER_AREA);
        }

        DeliveryInfo result = deliveryInfoRepository.save(request.toDeliveryInfo(currentUsername));
        applicationEventPublisher.publishEvent(new NewDeliveryEvent(result));
        return result;
    }

    public List<DeliveryInfo> getAllDeliveryCanceled(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return deliveryInfoRepository.getAllByStatusCanceled(pageable);
    }

    public DeliveryInfo updateDelivery(UpdateDeliveryRequest updateDeliveryRequest, String id) {
        DeliveryInfo deliveryToUpdate = deliveryInfoRepository.findById(id).orElseThrow(() ->
                new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND));

        if (updateDeliveryRequest.getDriverUsername() != null
                && !updateDeliveryRequest.getDriverUsername().equals(deliveryToUpdate.getDriverUsername())
                && deliveryToUpdate.getDeliveryStatus().equals(DeliveryStatus.CANCELED)) {
            addStatusHistory(deliveryToUpdate, DeliveryStatus.NEW_DRIVER);
        }
        updateDeliveryRequest.updateDelivery(deliveryToUpdate);
        if (updateDeliveryRequest.getDriverUsername() == null) {
            userService.findByUsername(updateDeliveryRequest.getDriverUsername()).orElseThrow(() ->
                    new TrackerAppException(ErrorCode.DRIVER_USERNAME_INVALID));
        }

        DeliveryInfo result = deliveryInfoRepository.save(deliveryToUpdate);
        applicationEventPublisher.publishEvent(new UpdateDeliveryEvent(deliveryToUpdate, result));
        return result;
    }

    private void addStatusHistory(DeliveryInfo deliveryInfo, DeliveryStatus statusToAdd) {
        List<StatusHistory> statusHistories = deliveryInfo.getStatusHistories();
        if (statusHistories == null)
            statusHistories = new LinkedList<>();
        StatusHistory statusHistoryToAdd = new StatusHistory();
        statusHistoryToAdd.setCreatedAt(System.currentTimeMillis());
        statusHistoryToAdd.setDeliveryStatus(statusToAdd);
        statusHistories.add(statusHistoryToAdd);

        deliveryInfo.setStatusHistories(statusHistories);
        deliveryInfo.setDeliveryStatus(statusToAdd);
    }

    public List<DeliveryInfo> getDeliveries(String createdBy, String driver, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return deliveryInfoRepository.getAllByCreatedByOrDriverUsername(createdBy, driver, pageable);
    }

    public List<DeliveryInfo> search(String keyword) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("searchData:(");
        String[] wordsInKeyword = keyword.split(" ");
        for (int i = 0; i < wordsInKeyword.length; i ++) {
            queryBuilder.append("*")
                    .append(wordsInKeyword[i])
                    .append("* ");
        }
        queryBuilder.append(")");

        return deliveryInfoRepository.query(queryBuilder.toString());
    }

    public Path getByTime(Long startTime, Long endTime) {
        List<DeliveryInfoResponse> data = deliveryInfoRepository.query(buildRangeTimeQuery(startTime, endTime)).stream()
                .map(DeliveryInfoResponse::from).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(data)) {
            throw new TrackerAppException(ErrorCode.DELIVERY_NOT_FOUND);
        }
        return createNewExcelData(data);
    }

    private String buildRangeTimeQuery(Long startTime, Long endTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("createdAt:[");
        if (startTime != null) {
            stringBuilder.append(startTime);
        } else
            stringBuilder.append("*");
        stringBuilder.append(" TO ");
        if (endTime != null)
            stringBuilder.append(endTime);
        else
            stringBuilder.append("*");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private Path createNewExcelData(List<DeliveryInfoResponse> listDataImport) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = dateFormat.format(new Date(System.currentTimeMillis())).replace(" ", "-").replace(":","-");
        String fileName = "Delivery-" + date + ".xlsx";
        File tempFile = new File(fileName);
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(tempFile);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("File data log DELIVERY");

            Font customFont = workbook.createFont();
            customFont.setBold(true);
            customFont.setFontHeightInPoints((short) 12);
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(customFont);

            Row headerRow = sheet.createRow(0);
            String[] columnNames = {
                    "Thời gian",
                    "Người lên đơn",
                    "Tài xế",
                    "Trạng thái",
                    "Người gửi",
                    "Địa chỉ người gửi",
                    "Email liên hệ người gửi",
                    "Người nhận",
                    "Địa chỉ người nhận",
                    "Email liên hệ người nhận",
                    "Số điện thoại người nhận",
            };
            for (int i = 0; i < columnNames.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(cellStyle);
                sheet.autoSizeColumn(i);
            }
            int row = 0;
            for (DeliveryInfoResponse deliveryInfo : listDataImport) {
                row++;
                Row dataRow = sheet.createRow(row);
                String[] rowData = {
                        dateFormat.format(new Date(deliveryInfo.getCreatedAt())).replace(" ", "-"),
                        deliveryInfo.getCreatedBy(),
                        deliveryInfo.getDriverUsername(),
                        String.valueOf(deliveryInfo.getDeliveryStatus()),
                        deliveryInfo.getSenderFullName(),
                        deliveryInfo.getFromAddress(),
                        deliveryInfo.getSenderEmail(),
                        deliveryInfo.getFullNameReceiver(),
                        deliveryInfo.getToAddress(),
                        deliveryInfo.getEmailReceiver(),
                        deliveryInfo.getPhoneNumberReceiver(),
                };
                for (int j = 0; j < rowData.length; j++) {
                    CellStyle dateCellStyle = workbook.createCellStyle();
                    Cell cell = dataRow.createCell(j);
                    cell.setCellValue(rowData[j]);
                    cell.setCellStyle(dateCellStyle);
                    sheet.autoSizeColumn(j);
                }
            }
            workbook.write(outputStream);
            outputStream.writeTo(fos);
            return tempFile.toPath();
        } catch (Exception e) {
            log.info("Something went wrong with createNewExcelData: " + e.getMessage(), e);
        }
        return null;
    }
}
