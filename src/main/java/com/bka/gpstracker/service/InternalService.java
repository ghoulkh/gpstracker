package com.bka.gpstracker.service;

import com.bka.gpstracker.model.request.PositionLogRequest;
import com.bka.gpstracker.repository.PositionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalService {
    private final PositionLogRepository positionLogRepository;

    public void importPosition(MultipartFile excelFile, String rfid, String speed) throws IOException {
        InputStream excelInput = excelFile.getInputStream();
        try (Workbook workbook = new XSSFWorkbook(excelInput)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell latCell = row.getCell(0);
                Cell lonCell = row.getCell(1);

                if (latCell != null && lonCell != null) {
                    String lat = latCell.getStringCellValue().trim();
                    String lon = lonCell.getStringCellValue().trim();

                    if (lat.equalsIgnoreCase("lat") && lon.equalsIgnoreCase("long")) {
                        continue;
                    }

                    PositionLogRequest request = new PositionLogRequest();
                    request.setRfid(rfid);
                    request.setSpeed(speed);
                    request.setLat(lat);
                    request.setLon(lon);

                    int delayTime = 30000 + new Random().nextInt(10000); // Từ 30s đến 40s
                    try {
                        positionLogRepository.save(request.toPositionLog(positionLogRepository.getMaxId() + 1));
                        log.info("SUCCESS");
                        Thread.sleep(delayTime);
                    } catch (Exception e) {
                        log.info("ERROR: {}", e.getMessage(), e);
                    }
                }
            }
        }
    }
}
