package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.Participant;
import org.doorip.trip.domain.Progress;
import org.doorip.trip.domain.Trip;
import org.doorip.trip.dto.response.MyTodoResponse;
import org.doorip.trip.dto.response.OurTodoResponse;
import org.doorip.trip.repository.TodoRepository;
import org.doorip.trip.repository.TripRepository;
import org.doorip.user.domain.User;
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
    private final TripRepository tripRepository;
    private final TodoRepository todoRepository;

    public MyTodoResponse getMyTodoDetail(Long tripId) {
        Trip findTrip = getTrip(tripId);
        int count = getIncompleteTodoCount(tripId);
        return MyTodoResponse.of(findTrip.getTitle(), count);
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

    private int getIncompleteTodoCount(Long tripId) {
        return todoRepository.countTodoByTripIdAndProgress(tripId, Progress.INCOMPLETE);
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
        int incompleteTodoCount = todoRepository.countTodoByTripIdAndProgress(tripId, Progress.INCOMPLETE);
        int completeTodoCount = todoRepository.countTodoByTripIdAndProgress(tripId, Progress.COMPLETE);
        int totalTodoCount = incompleteTodoCount + completeTodoCount;
        return round(((float) completeTodoCount / totalTodoCount) * 100);
    }

    private Trip getTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TRIP_NOT_FOUND));
    }

    private boolean isOwnerParticipant(Long userId, User user) {
        return Objects.equals(user.getId(), userId);
    }
}
