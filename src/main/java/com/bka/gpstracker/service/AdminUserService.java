package com.bka.gpstracker.service;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.model.request.AuthorCarInfoRequest;
import com.bka.gpstracker.solr.entity.Authority;
import com.bka.gpstracker.solr.entity.UserInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.solr.repository.AuthorityRepository;
import com.bka.gpstracker.util.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AdminUserService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CarInfoService carInfoService;

    public CarInfo setCarAuthor(AuthorCarInfoRequest request) {
        UserInfo userToSet = userService.getByUsername(request.getUsername());

        if (request.getActiveAreas() == null || request.getActiveAreas().size() == 0) {
            throw new TrackerAppException(ErrorCode.ACTIVE_AREAS_IS_REQUIRE);
        }
        validateAddress(request);
        if (carInfoService.getAllByUsername(request.getUsername()).size() == 1) {
            throw new TrackerAppException(ErrorCode.ADD_CAR_AUTHOR_FAILED);
        }

        handleUpdateRfid(request.getRfid());
        CarInfo carInfoToSet = new CarInfo();
        carInfoToSet.setRfid(request.getRfid());
        carInfoToSet.setUsername(userToSet.getUsername());
        carInfoToSet.setDriver(userToSet.getFullName());
        setRoleDriver(request.getUsername());
        carInfoToSet.setActiveAreas(request.getActiveAreas());
        carInfoToSet.setDrivingLicense(request.getDrivingLicense());
        carInfoToSet.setLicensePlate(request.getLicensePlate());
        return carInfoService.save(carInfoToSet);
    }

    private void handleUpdateRfid(String rfid) {
        try {
            CarInfo carInfo = carInfoService.getByRfid(rfid);
            if (!StringUtils.isEmpty(carInfo.getUsername())) {
                Authority authority = authorityRepository.getByUsernameAndRole(carInfo.getUsername(), "ROLE_DRIVER");
                authorityRepository.delete(authority);
            }
        } catch (Exception e) {
            log.info("Not found carInfo");
        }
    }

    private void validateAddress(AuthorCarInfoRequest request) {
        List<String> districts = addressService.getDistrictByCity("Thành phố Hà Nội");
        for (String activeArea : request.getActiveAreas()) {
            if (!districts.contains(activeArea))
                throw new TrackerAppException(ErrorCode.INVALID_ACTIVE_AREAS);
        }
    }

    private void setRoleDriver(String username) {
        List<String> authorities = authorityRepository.getAllByUsername(username).stream()
                .map(Authority::getRole)
                .map(Authority.Role::getValue)
                .collect(Collectors.toList());
        if (authorities.contains(AuthoritiesConstants.ROLE_DRIVER)) return;
        Authority authority = new Authority();
        authority.setRole(Authority.Role.ROLE_DRIVER);
        authority.setUsername(username);
        authorityRepository.save(authority);
    }
}
