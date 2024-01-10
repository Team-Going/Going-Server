package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.common.Constants;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.*;
import org.doorip.trip.dto.request.TodoCreateRequest;
import org.doorip.trip.dto.response.TodoAllocatorResponse;
import org.doorip.trip.dto.response.TodoGetResponse;
import org.doorip.trip.repository.ParticipantRepository;
import org.doorip.trip.repository.TodoRepository;
import org.doorip.trip.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.doorip.trip.domain.Allocator.createAllocator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TodoService {
    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public void createTripTodo(Long tripId, TodoCreateRequest request) {
        validateAllocators(request.allocators());
        Trip findTrip = getTrip(tripId);
        Todo todo = createTodo(request, findTrip);
        createAllocators(request.allocators(), todo);
        todoRepository.save(todo);
    }

    public List<TodoGetResponse> getTripTodos(Long userId, Long tripId, String category, String progress) {
        List<Todo> todos = getTripTodosAccordingToCategoryAndProgress(userId, tripId, category, progress);
        return getTripTodosResponse(userId, todos);
    }

    public TodoGetResponse getTripTodo(Long userId, Long todoId) {
        Todo findTodo = getTodo(todoId);
        List<TodoAllocatorResponse> allocatorResponses = getAndSortAllocatorsResponses(userId, findTodo);
        return TodoGetResponse.of(findTodo, allocatorResponses);
    }

    @Transactional
    public void deleteTripTodo(Long todoId) {
        todoRepository.deleteById(todoId);
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

    private List<Todo> getTripTodosAccordingToCategoryAndProgress(Long userId, Long tripId, String category, String progress) {
        if (category.equals(Constants.OUR)) {
            return getOurTodosAccordingToProgress(tripId, progress);
        } else if (category.equals(Constants.MY)) {
            return getMyTodosAccordingToProgress(userId, tripId, progress);
        }
        throw new InvalidValueException(ErrorMessage.INVALID_REQUEST_PARAMETER_VALUE);
    }

    private List<TodoGetResponse> getTripTodosResponse(Long userId, List<Todo> todos) {
        List<TodoGetResponse> response = new ArrayList<>();
        todos.forEach(todo -> {
            List<TodoAllocatorResponse> allocatorResponses = getAndSortAllocatorsResponses(userId, todo);
            response.add(TodoGetResponse.of(todo, allocatorResponses));
        });
        return response;
    }

    private Todo getTodo(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TODO_NOT_FOUND));
    }

    private Participant getParticipant(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PARTICIPANT_NOT_FOUND));
    }

    private List<Todo> getOurTodosAccordingToProgress(Long tripId, String progress) {
        if (progress.equals(Constants.INCOMPLETE)) {
            return todoRepository.findOurTodoByTripId(tripId, Secret.OUR, Progress.INCOMPLETE);
        } else if (progress.equals(Constants.COMPLETE)) {
            return todoRepository.findOurTodoByTripId(tripId, Secret.OUR, Progress.COMPLETE);
        }
        throw new InvalidValueException(ErrorMessage.INVALID_REQUEST_PARAMETER_VALUE);
    }

    private List<Todo> getMyTodosAccordingToProgress(Long userId, Long tripId, String progress) {
        if (progress.equals(Constants.INCOMPLETE)) {
            return todoRepository.findMyTodoByTripId(tripId, userId, Progress.INCOMPLETE);
        } else if (progress.equals(Constants.COMPLETE)) {
            return todoRepository.findMyTodoByTripId(tripId, userId, Progress.COMPLETE);
        }
        throw new InvalidValueException(ErrorMessage.INVALID_REQUEST_PARAMETER_VALUE);
    }

    private List<TodoAllocatorResponse> getAndSortAllocatorsResponses(Long userId, Todo todo) {
        List<Allocator> allocators = todo.getAllocators();
        List<TodoAllocatorResponse> allocatorResponses = getAllocatorResponses(userId, allocators);
        TodoAllocatorResponse ownerAllocator = getOwnerAllocator(allocatorResponses);
        sortAllocators(ownerAllocator, allocatorResponses);
        return allocatorResponses;
    }


    private List<TodoAllocatorResponse> getAllocatorResponses(Long userId, List<Allocator> allocators) {
        return new ArrayList<>(allocators.stream()
                .map(allocator -> TodoAllocatorResponse.of(userId, allocator))
                .toList());
    }

    private TodoAllocatorResponse getOwnerAllocator(List<TodoAllocatorResponse> allocatorResponses) {
        return allocatorResponses.stream()
                .filter(TodoAllocatorResponse::isOwner)
                .findFirst()
                .orElse(null);
    }

    private void sortAllocators(TodoAllocatorResponse ownerAllocator, List<TodoAllocatorResponse> allocatorResponses) {
        if (ownerAllocator != null) {
            allocatorResponses.remove(ownerAllocator);
            allocatorResponses.add(0, ownerAllocator);
        }
    }
}
