package org.doorip.trip.repository;

import org.doorip.trip.domain.Participant;
import org.doorip.trip.domain.Trip;
import org.doorip.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByUserAndTrip(User user, Trip trip);
}
