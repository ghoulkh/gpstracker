package com.bka.gpstracker.service;

import com.bka.gpstracker.common.DriverStatus;
import com.bka.gpstracker.common.TripStatus;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.SetDriverForTripEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.response.UserResponse;
import com.bka.gpstracker.solr.entity.Authority;
import com.bka.gpstracker.solr.entity.Trip;
import com.bka.gpstracker.solr.entity.UserInfo;
import com.bka.gpstracker.solr.repository.AuthorityRepository;
import com.bka.gpstracker.solr.repository.TripRepository;
import com.bka.gpstracker.solr.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripAdminService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    public List<Trip> getActiveTrips(String status) {
        if (!status.equals("*") && !TripStatus.isMatch(status))
            throw new TrackerAppException(ErrorCode.INVALID_STATUS);
        Pageable paging = PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "createdAt"));
        return tripRepository.getAllByStatus(status, paging);
    }

    public List<UserResponse> getAllUsernameDriver() {
        return authorityRepository.getAllByRole(Authority.Role.ROLE_DRIVER.getValue()).stream()
                .map(authority -> userService.findByUsername(authority.getUsername()).get())
                .map(user -> UserResponse.from(user, null))
                .collect(Collectors.toList());
    }

    public void setDriverForTrip(String driver, String tripId) {
        Trip tripToSet = tripRepository.findById(tripId).orElseThrow(() ->
                new TrackerAppException(ErrorCode.TRIP_NOT_FOUND));
        if (!tripToSet.getStatus().equals(TripStatus.NEW))
            throw new TrackerAppException(ErrorCode.BAD_REQUEST);
        if (!isDriver(driver))
            throw new TrackerAppException(ErrorCode.NOT_DRIVER);
        tripToSet.setDriver(driver);
        checkDriverStatus(driver);
        tripToSet.setStatus(TripStatus.SENT_TO_DRIVER);
        tripRepository.save(tripToSet);
        applicationEventPublisher.publishEvent(new SetDriverForTripEvent(tripToSet, driver));
    }

    private void checkDriverStatus(String username) {
        UserInfo userInfo = userInfoRepository.getByUsername(username);
        if (userInfo == null)
            throw new TrackerAppException(ErrorCode.USER_NOT_FOUND);
//        if (userInfo.getDriverStatus().equals(DriverStatus.INACTIVE))
//            throw new TrackerAppException(ErrorCode.DRIVER_INACTIVE);
    }

    private boolean isDriver(String username) {
        List<Authority> authorities = authorityRepository.getAllByUsername(username);
        for (Authority authority : authorities) {
            if (authority.getRole().equals(Authority.Role.ROLE_DRIVER))
                return true;
        }
        return false;
    }
}
