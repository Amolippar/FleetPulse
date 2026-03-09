package com.fleetmanagement.backend.dao.impl;

import com.fleetmanagement.backend.dao.TripDetailDao;
import com.fleetmanagement.backend.entity.TripDetail;
import com.fleetmanagement.backend.repository.TripDetailRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TripDetailDaoImpl implements TripDetailDao {

    private final TripDetailRepository tripDetailRepository;

    public TripDetailDaoImpl(TripDetailRepository tripDetailRepository) {
        this.tripDetailRepository = tripDetailRepository;
    }

    @Override
    public TripDetail save(TripDetail tripDetail) {
        return tripDetailRepository.save(tripDetail);
    }

    @Override
    public Optional<TripDetail> findByBookingId(Long bookingId) {
        // Since @MapsId is used, the ID of TripDetail is the bookingId
        return tripDetailRepository.findById(bookingId);
    }
}