package org.doorip.trip.repository;

import org.doorip.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Boolean existsByCode(String code);
}
