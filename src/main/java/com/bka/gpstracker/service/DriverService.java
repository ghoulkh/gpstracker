package com.bka.gpstracker.service;

import com.bka.gpstracker.common.TripStatus;
import com.bka.gpstracker.config.JwtToken;
import com.bka.gpstracker.entity.CarInfo;
import com.bka.gpstracker.entity.PositionLog;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.CompletedTripEvent;
import com.bka.gpstracker.event.DriverAcceptTripEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.solr.entity.Trip;
import com.bka.gpstracker.solr.entity.UserInfo;
import com.bka.gpstracker.solr.repository.TripRepository;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private TripRepository tripRepository;
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

    public Trip acceptTrip(String tripId) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        UserInfo currentUser = userService.getByUsername(currentUsername);
        if (currentUser.getIsBusy())
            throw new TrackerAppException(ErrorCode.DRIVER_IS_BUSY);

        Trip trip = tripRepository.findById(tripId).orElseThrow(() ->
                new TrackerAppException(ErrorCode.TRIP_NOT_FOUND));
        if (!trip.getStatus().equals(TripStatus.NEW))
            throw new TrackerAppException(ErrorCode.TRIP_CANCELED_OR_IN_PROGRESS);
        trip.setStatus(TripStatus.IN_PROGRESS);
        trip.setDriver(currentUsername);
        Trip result = tripRepository.save(trip);
        currentUser.setIsBusy(true);
        currentUser.setCurrentTripId(tripId);
        userService.save(currentUser);
        applicationEventPublisher.publishEvent(new DriverAcceptTripEvent(result));
        return result;
    }

    public Trip driverCompleteTrip(String tripId) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        UserInfo currentUser = userService.getByUsername(currentUsername);
        if (!tripId.equals(currentUser.getCurrentTripId()))
            throw new TrackerAppException(ErrorCode.CURRENT_TRIP_INVALID);

        Trip trip = tripRepository.findById(tripId).orElseThrow(() ->
                new TrackerAppException(ErrorCode.TRIP_NOT_FOUND));
        trip.setStatus(TripStatus.COMPLECTED);
        Trip result = tripRepository.save(trip);
        currentUser.setIsBusy(false);
        currentUser.setCurrentTripId(null);
        userService.save(currentUser);

        applicationEventPublisher.publishEvent(new CompletedTripEvent(result));
        return trip;
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
