package com.bka.gpstracker.service;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.solr.entity.UserInfo;
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

        CarInfo carInfoToSet = carInfoService.getByRfid(rfid);
        carInfoToSet.setUsername(userToSet.getUsername());
        return carInfoService.save(carInfoToSet);
    }
}
