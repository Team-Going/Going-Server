package org.doorip.trip.repository;

import org.doorip.trip.domain.Allocator;
import org.doorip.trip.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllocatorRepository extends JpaRepository<Allocator, Long> {
    List<Allocator> findByTodo(Todo todo);
}
