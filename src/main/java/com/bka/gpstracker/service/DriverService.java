package com.bka.gpstracker.service;

import com.bka.gpstracker.config.JwtToken;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.entity.PositionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DriverService {

    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private UserService userService;

    Map<String, String> activeDrivers = new ConcurrentHashMap<String, String>();

    public boolean canDrive(String username) {
        if (userService.getByUsername(username).getIsBusy())
            return false;
        if (carInfoService.getAllByUsername(username).size() != 1)
            return false;
        else
            return true;
    }

    public boolean isExistPosition(String username) {
        CarInfo carInfo = carInfoService.getAllByUsername(username).get(0);
        List<PositionLog> positionLogs = positionService.getByRfid(carInfo.getRfid(), 1, 1);
        return !positionLogs.isEmpty();
    }

    public void addActiveDriver(String username, String clientId) {
        activeDrivers.put(clientId, username);
    }

    public String removeActiveDriver(String clientId) {
        return activeDrivers.remove(clientId);
    }
    public Set<String> getActiveUsernames() {
        return activeDrivers.values().stream().collect(Collectors.toSet());
    }
}
