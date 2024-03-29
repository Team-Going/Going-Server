package org.doorip.trip.repository;


import org.doorip.trip.domain.Progress;
import org.doorip.trip.domain.Secret;
import org.doorip.trip.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("select d " +
            "from Todo d " +
            "join Trip i " +
            "on d.trip = i " +
            "where i.id = :tripId " +
            "and d.progress = :progress " +
            "and d.secret = :secret " +
            "order by d.endDate")
    List<Todo> findOurTodoByTripIdAndSecretAndProgress(@Param("tripId") Long tripId, @Param("secret") Secret secret, @Param("progress") Progress progress);

    @Query("select d " +
            "from Todo d " +
            "join Trip i " +
            "on d.trip = i " +
            "join Allocator a " +
            "on a.todo = d " +
            "join Participant p " +
            "on a.participant = p " +
            "join User u " +
            "on p.user = u " +
            "where i.id = :tripId " +
            "and u.id = :userId " +
            "and d.secret = :secret")
    List<Todo> findMyTodoByTripIdAndUserIdAndSecret(@Param("tripId") Long tripId, @Param("userId") Long userId, @Param("secret") Secret secret);

    @Query("select d " +
            "from Todo d " +
            "join Trip i " +
            "on d.trip = i " +
            "join Allocator a " +
            "on a.todo = d " +
            "join Participant p " +
            "on a.participant = p " +
            "join User u " +
            "on p.user = u " +
            "where i.id = :tripId " +
            "and u.id = :userId " +
            "and d.progress = :progress " +
            "order by d.endDate")
    List<Todo> findMyTodoByTripIdAndUserIdAndProgress(@Param("tripId") Long tripId, @Param("userId") Long userId, @Param("progress") Progress progress);

    int countTodoByTripIdAndSecretAndProgress(Long tripId, Secret secret, Progress progress);

    @Query("select count(*) " +
            "from Todo d " +
            "join Trip i " +
            "on d.trip = i " +
            "join Allocator a " +
            "on a.todo = d " +
            "join Participant p " +
            "on a.participant = p " +
            "join User u " +
            "on p.user = u " +
            "where i.id = :tripId " +
            "and u.id = :userId " +
            "and d.progress = :progress")
    int countTodoByTripIdAndUserIdAndProgress(@Param("tripId") Long tripId, @Param("userId") Long userId, @Param("progress") Progress progress);
}
