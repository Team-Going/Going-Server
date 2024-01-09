package org.doorip.trip.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.doorip.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("select t " +
            "from Trip t " +
            "join Participant p " +
            "on p.trip = t " +
            "join User u " +
            "on p.user = u " +
            "where u.id = :userId " +
            "and datediff(t.endDate, :now) >= 0 " +
            "order by datediff(t.startDate, :now)")
    List<Trip> findInCompleteTripsByUserId(@Param("userId") Long userId, @Param("now") LocalDate now);

    @Query("select t " +
            "from Trip t " +
            "join Participant p " +
            "on p.trip = t " +
            "join User u " +
            "on p.user = u " +
            "where u.id = :userId " +
            "and datediff(t.endDate, :now) < 0 " +
            "order by datediff(:now, t.endDate)")
    List<Trip> findCompleteTripsByUserId(@Param("userId") Long userId, @Param("now") LocalDate now);

    Optional<Trip> findByCode(String code);

    boolean existsByCode(String code);
}
