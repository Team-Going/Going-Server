package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.common.Constants;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.*;
import org.doorip.trip.dto.request.TodoCreateRequest;
import org.doorip.trip.dto.response.TodoAllocatorResponse;
import org.doorip.trip.dto.response.TodoDetailAllocatorResponse;
import org.doorip.trip.dto.response.TodoDetailGetResponse;
import org.doorip.trip.dto.response.TodoGetResponse;
import org.doorip.trip.repository.ParticipantRepository;
import org.doorip.trip.repository.TodoRepository;
import org.doorip.trip.repository.TripRepository;
import org.doorip.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.doorip.trip.domain.Allocator.createAllocator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;

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

    public TodoDetailGetResponse getTripTodo(Long userId, Long tripId, Long todoId) {
        Trip findTrip = getTrip(tripId);
        Todo findTodo = getTodo(todoId);
        return getTripTodoResponse(userId, findTrip, findTodo);
    }

    @Transactional
    public void deleteTripTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }

    @Transactional
    public void completeTripTodo(Long todoId) {
        Todo findTodo = getTodo(todoId);
        findTodo.complete();
    }

    @Transactional
    public void incompleteTripTodo(Long todoId) {
        Todo findTodo = getTodo(todoId);
        findTodo.incomplete();
    }

    private void validateAllocators(List<Long> allocators) {
        if (allocators.isEmpty()) {
            throw new InvalidValueException(ErrorMessage.INVALID_ALLOCATOR_COUNT);
        }
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

    private Trip getTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TRIP_NOT_FOUND));
    }

    private TodoDetailGetResponse getTripTodoResponse(Long userId, Trip findTrip, Todo findTodo) {
        List<TodoDetailAllocatorResponse> allocatorResponses = getAndSortAllocatorResponses(userId, findTrip, findTodo);
        return TodoDetailGetResponse.of(findTodo, allocatorResponses);
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
            return todoRepository.findOurTodoByTripIdAndSecretAndProgress(tripId, Secret.OUR, Progress.INCOMPLETE);
        } else if (progress.equals(Constants.COMPLETE)) {
            return todoRepository.findOurTodoByTripIdAndSecretAndProgress(tripId, Secret.OUR, Progress.COMPLETE);
        }
        throw new InvalidValueException(ErrorMessage.INVALID_REQUEST_PARAMETER_VALUE);
    }

    private List<Todo> getMyTodosAccordingToProgress(Long userId, Long tripId, String progress) {
        if (progress.equals(Constants.INCOMPLETE)) {
            return todoRepository.findMyTodoByTripIdAndUserIdAndProgress(tripId, userId, Progress.INCOMPLETE);
        } else if (progress.equals(Constants.COMPLETE)) {
            return todoRepository.findMyTodoByTripIdAndUserIdAndProgress(tripId, userId, Progress.COMPLETE);
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

    private List<TodoDetailAllocatorResponse> getAndSortAllocatorResponses(Long userId, Trip findTrip, Todo findTodo) {
        List<Participant> participants = findTrip.getParticipants();
        Participant ownerParticipant = getOwnerParticipant(userId, participants);
        sortParticipants(ownerParticipant, participants);
        List<Allocator> allocators = findTodo.getAllocators();
        return getAllocatorResponses(ownerParticipant, participants, allocators);
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
            allocatorResponses.add(Constants.TODO_OWNER_POSITION, ownerAllocator);
        }
    }

    private Participant getOwnerParticipant(Long userId, List<Participant> participants) {
        return participants.stream()
                .filter(participant -> isOwnerParticipant(userId, participant))
                .findFirst()
                .orElse(null);
    }

    private void sortParticipants(Participant ownerParticipant, List<Participant> participants) {
        if (ownerParticipant != null) {
            participants.remove(ownerParticipant);
            participants.add(Constants.TODO_OWNER_POSITION, ownerParticipant);
        }
    }

    private List<TodoDetailAllocatorResponse> getAllocatorResponses(Participant ownerParticipant, List<Participant> participants, List<Allocator> allocators) {
        return participants.stream()
                .map(participant -> {
                    boolean isAllocated = allocators.stream()
                            .anyMatch(allocator -> isAllocatedParticipant(allocator, participant));
                    User findUser = participant.getUser();
                    return TodoDetailAllocatorResponse.of(findUser.getName(), participant == ownerParticipant, isAllocated, participant);
                })
                .toList();
    }

    private boolean isOwnerParticipant(Long userId, Participant participant) {
        User findUser = participant.getUser();
        Long findUserId = findUser.getId();
        return findUserId.equals(userId);
    }

    private boolean isAllocatedParticipant(Allocator allocator, Participant participant) {
        Participant findParticipant = allocator.getParticipant();
        Long findParticipantId = findParticipant.getId();
        return findParticipantId.equals(participant.getId());
    }
}
