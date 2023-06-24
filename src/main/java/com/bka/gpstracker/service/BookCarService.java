package com.bka.gpstracker.service;

import com.bka.gpstracker.auth.AuthoritiesConstants;
import com.bka.gpstracker.common.TripStatus;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.event.CancelTripEvent;
import com.bka.gpstracker.event.NewTripEvent;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.model.request.NewTripRequest;
import com.bka.gpstracker.solr.entity.Trip;
import com.bka.gpstracker.solr.repository.TripRepository;
import com.bka.gpstracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCarService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserService userService;

    public Trip newTrip(NewTripRequest request) {
        if (SecurityUtil.isAuthor(AuthoritiesConstants.ROLE_DRIVER))
            throw new TrackerAppException(ErrorCode.DRIVER_CANT_BOOK_CAR);
        String currentUsername = SecurityUtil.getCurrentUsername();
        Trip tripToSave = request.toTrip(currentUsername);
        applicationEventPublisher.publishEvent(new NewTripEvent(tripToSave));
        return tripRepository.save(tripToSave);
    }


    public List<Trip> getAllTrip() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        Pageable paging = PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Trip> trips = tripRepository.findAllByCreatedBy(currentUsername, paging);
        return trips;
    }

    public void cancelTrip(String tripId) {
        Trip tripToCancel = tripRepository.findById(tripId).orElseThrow(() ->
                new TrackerAppException(ErrorCode.TRIP_NOT_FOUND));
        canCancelTrip(tripToCancel);
        tripToCancel.setStatus(TripStatus.CANCELED);
        applicationEventPublisher.publishEvent(new CancelTripEvent(tripToCancel));
        tripRepository.save(tripToCancel);
    }

    private void canCancelTrip(Trip tripToCancel) {
        if (tripToCancel.equals(TripStatus.CANCELED))
            throw new TrackerAppException(ErrorCode.TRIP_CANCEL_FAIL);
        if (tripToCancel.equals(TripStatus.IN_PROGRESS))
            throw new TrackerAppException(ErrorCode.TRIP_CANCEL_FAIL_01);
        if (tripToCancel.equals(TripStatus.COMPLECTED))
            throw new TrackerAppException(ErrorCode.TRIP_CANCEL_FAIL_02);
    }
}
