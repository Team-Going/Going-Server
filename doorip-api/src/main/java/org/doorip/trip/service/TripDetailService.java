package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.common.Constants;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.actor.TripTendencyTestActor;
import org.doorip.trip.actor.TripTendencyTestResult;
import org.doorip.trip.domain.*;
import org.doorip.trip.dto.request.ParticipantUpdateRequest;
import org.doorip.trip.dto.response.MyTodoResponse;
import org.doorip.trip.dto.response.OurTodoResponse;
import org.doorip.trip.dto.response.TripParticipantGetResponse;
import org.doorip.trip.dto.response.TripParticipantProfileResponse;
import org.doorip.trip.repository.ParticipantRepository;
import org.doorip.trip.repository.TodoRepository;
import org.doorip.trip.repository.TripRepository;
import org.doorip.user.domain.User;
import org.doorip.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.round;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TripDetailService {
    private final TripTendencyTestActor tripTendencyTestActor;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TodoRepository todoRepository;
    private final ParticipantRepository participantRepository;

    public MyTodoResponse getMyTodoDetail(Long userId, Long tripId) {
        Trip findTrip = getTrip(tripId);
        List<Participant> participants = findTrip.getParticipants();
        Participant ownerParticipant = getOwnerParticipant(userId, participants);
        int count = getIncompleteTodoCount(userId, tripId);
        return MyTodoResponse.of(ownerParticipant.getId(), findTrip.getTitle(), count);
    }

    public OurTodoResponse getOurTodoDetail(Long userId, Long tripId) {
        Trip findTrip = getTrip(tripId);
        boolean isComplete = judgeTrip(findTrip);
        List<Participant> participants = findTrip.getParticipants();
        Participant ownerParticipant = getOwnerParticipant(userId, participants);
        sortParticipants(participants, ownerParticipant);
        int progressRate = calculateTodoProgressRate(tripId);
        return OurTodoResponse.of(findTrip, progressRate, isComplete, participants);
    }

    public TripParticipantGetResponse getParticipants(Long userId, Long tripId) {
        Trip findTrip = getTrip(tripId);
        List<Participant> participants = findTrip.getParticipants();
        Participant ownerParticipant = getOwnerParticipant(userId, participants);
        sortParticipants(participants, ownerParticipant);
        TripTendencyTestResult result = tripTendencyTestActor.calculateTripTendencyTest(participants);
        return TripParticipantGetResponse.of(result.bestPrefer(), participants, result.styles());
    }

    @Transactional
    public void leaveTrip(Long userId, Long tripId) {
        User findUser = getUser(userId);
        Trip findTrip = getTrip(tripId);
        int size = calculateParticipantsCount(findTrip);
        Participant findParticipant = getParticipant(findUser, findTrip);
        List<Todo> todos = todoRepository.findMyTodoByTripIdAndUserIdAndSecret(tripId, userId, Secret.MY);
        todoRepository.deleteAll(todos);
        participantRepository.delete(findParticipant);
        deleteTripIfLastParticipant(size, findTrip);
    }

    @Transactional
    public void updateParticipant(Long userId, Long tripId, ParticipantUpdateRequest request) {
        User findUser = getUser(userId);
        Trip findTrip = getTrip(tripId);
        Participant findParticipant = getParticipant(findUser, findTrip);
        findParticipant.updateStyles(request.styleA(), request.styleB(), request.styleC(), request.styleD(), request.styleE());
    }

    public TripParticipantProfileResponse getParticipantProfile(Long userId, Long participantId) {
        User findUser = getUser(userId);
        Participant findParticipant = getParticipantById(participantId);
        User participantUser = findParticipant.getUser();
        boolean isOwner = isEqualUserAndParticipantUser(findUser, participantUser);
        int validatedResult = getValidatedResult(participantUser);
        return TripParticipantProfileResponse.of(participantUser, validatedResult, findParticipant, isOwner);
    }

    private int getIncompleteTodoCount(Long userId, Long tripId) {
        return todoRepository.countTodoByTripIdAndUserIdAndProgress(tripId, userId, Progress.INCOMPLETE);
    }

    private boolean judgeTrip(Trip trip) {
        LocalDate endDate = trip.getEndDate();
        LocalDate now = LocalDate.now();
        return ChronoUnit.DAYS.between(now, endDate) < 0;
    }

    private Participant getOwnerParticipant(Long userId, List<Participant> participants) {
        return participants.stream()
                .filter(participant -> {
                    User findUser = participant.getUser();
                    return isOwnerParticipant(userId, findUser);
                })
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OWNER_NOT_FOUND));
    }

    private void sortParticipants(List<Participant> participants, Participant ownerParticipant) {
        participants.remove(ownerParticipant);
        participants.add(0, ownerParticipant);
    }

    private int calculateTodoProgressRate(Long tripId) {
        int incompleteTodoCount = todoRepository.countTodoByTripIdAndSecretAndProgress(tripId, Secret.OUR, Progress.INCOMPLETE);
        int completeTodoCount = todoRepository.countTodoByTripIdAndSecretAndProgress(tripId, Secret.OUR, Progress.COMPLETE);
        int totalTodoCount = incompleteTodoCount + completeTodoCount;
        return round(((float) completeTodoCount / totalTodoCount) * 100);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private int calculateParticipantsCount(Trip findTrip) {
        return findTrip.getParticipants().size();
    }

    private Participant getParticipant(User findUser, Trip findTrip) {
        return participantRepository.findByUserAndTrip(findUser, findTrip)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PARTICIPANT_NOT_FOUND));
    }

    private void deleteTripIfLastParticipant(int size, Trip trip) {
        if (size == Constants.MIN_PARTICIPANT_COUNT) {
            tripRepository.delete(trip);
        }
    }

    private Trip getTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TRIP_NOT_FOUND));
    }

    private boolean isOwnerParticipant(Long userId, User user) {
        return Objects.equals(user.getId(), userId);
    }

    private Participant getParticipantById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PARTICIPANT_NOT_FOUND));
    }

    private boolean isEqualUserAndParticipantUser(User user, User participantUser) {
        return user.equals(participantUser);
    }

    private int getValidatedResult(User user) {
        if (user.getResult() == null) {
            return -1;
        }
        else {
            return user.getResult().getNumResult();
        }
    }
}
