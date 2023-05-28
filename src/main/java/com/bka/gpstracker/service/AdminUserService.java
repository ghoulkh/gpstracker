package com.bka.gpstracker.service;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.entity.UserInfo;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
    @Autowired
    private UserService userService;

    @Autowired
    private CarInfoService carInfoService;

    public CarInfo setCarAuthor(String username, String rfid) {
        UserInfo userToSet = userService.getByUsername(username);

        if (SecurityUtil.isAuthor(AuthoritiesConstants.ROLE_ENTERPRISE_ADMIN) && !SecurityUtil.isAuthor(AuthoritiesConstants.ROLE_ADMIN)) {
            String currentUsername = SecurityUtil.getCurrentUsername();
            UserInfo userInfoCurrentUser = userService.getByUsername(currentUsername);
            if (!userInfoCurrentUser.getEnterpriseCode().equals(userToSet.getEnterpriseCode()))
                throw new TrackerAppException(ErrorCode.SET_CAR_INFO_PERMISSION_DENIED);
        }
        CarInfo carInfoToSet = carInfoService.getByRfid(rfid);
        carInfoToSet.setUsername(userToSet.getUsername());
        return carInfoService.save(carInfoToSet);
    }
}
