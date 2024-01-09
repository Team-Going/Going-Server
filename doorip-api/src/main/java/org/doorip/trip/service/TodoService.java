package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.Participant;
import org.doorip.trip.domain.Secret;
import org.doorip.trip.domain.Todo;
import org.doorip.trip.domain.Trip;
import org.doorip.trip.dto.request.TodoCreateRequest;
import org.doorip.trip.repository.ParticipantRepository;
import org.doorip.trip.repository.TodoRepository;
import org.doorip.trip.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.doorip.trip.domain.Allocator.createAllocator;

@RequiredArgsConstructor
@Transactional
@Service
public class TodoService {
    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;
    private final TodoRepository todoRepository;

    public void createTripTodo(TodoCreateRequest request) {
        validateAllocators(request.allocators());
        Trip findTrip = getTrip(request.tripId());
        Todo todo = createTodo(request, findTrip);
        createAllocators(request.allocators(), todo);
        todoRepository.save(todo);
    }

    private void validateAllocators(List<Long> allocators) {
        if (allocators.isEmpty()) {
            throw new InvalidValueException(ErrorMessage.INVALID_ALLOCATOR_COUNT);
        }
    }

    private Trip getTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TRIP_NOT_FOUND));
    }

    private Todo createTodo(TodoCreateRequest request, Trip trip) {
        return Todo.createTodo(request.title(), request.endDate(), request.memo(), Secret.of(request.secret()), trip);
    }

    private void createAllocators(List<Long> allocators, Todo todo) {
        allocators.forEach(participantId -> {
            Participant findParticipant = getParticipant(participantId);
            createAllocator(todo, findParticipant);
        });
    }

    private Participant getParticipant(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PARTICIPANT_NOT_FOUND));
    }
}
