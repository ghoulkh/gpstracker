package com.bka.gpstracker.service;

import com.bka.gpstracker.common.TripStatus;
import com.bka.gpstracker.error.ErrorCode;
import com.bka.gpstracker.exception.TrackerAppException;
import com.bka.gpstracker.solr.entity.Trip;
import com.bka.gpstracker.solr.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripAdminService {
    @Autowired
    private TripRepository tripRepository;

    public List<Trip> getActiveTrips(String status) {
        if (!status.equals("*") && !TripStatus.isMatch(status))
            throw new TrackerAppException(ErrorCode.INVALID_STATUS);
        Pageable paging = PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "createdAt"));
        return tripRepository.getAllByStatus(status, paging);
    }

}
