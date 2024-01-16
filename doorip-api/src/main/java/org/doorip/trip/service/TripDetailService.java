package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.Participant;
import org.doorip.trip.domain.Progress;
import org.doorip.trip.domain.Secret;
import org.doorip.trip.domain.Trip;
import org.doorip.trip.dto.response.MyTodoResponse;
import org.doorip.trip.dto.response.OurTodoResponse;
import org.doorip.trip.dto.response.TripParticipantGetResponse;
import org.doorip.trip.dto.response.TripStyleResponse;
import org.doorip.trip.repository.TodoRepository;
import org.doorip.trip.repository.TripRepository;
import org.doorip.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Math.round;
import static org.doorip.common.Constants.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TripDetailService {
    private final TripRepository tripRepository;
    private final TodoRepository todoRepository;

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
        List<TripStyleResponse> response = calculateAndGetPropensityAverageRates(participants);
        return TripParticipantGetResponse.of(participants, response);
    }

    private Map<String, Integer> createDefaultPropensity() {
        return new HashMap<>(Map.of(STYLE_A, MIN_STYLE_RATE, STYLE_B, MIN_STYLE_RATE,
                STYLE_C, MIN_STYLE_RATE, STYLE_D, MIN_STYLE_RATE, STYLE_E, MIN_STYLE_RATE)) {
        };
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

    private List<TripStyleResponse> calculateAndGetPropensityAverageRates(List<Participant> participants) {
        int participantCount = participants.size();
        Map<String, Integer> propensity = getDefaultPropensity(participants);
        List<String> keys = sortPropensityKeys(propensity);
        List<TripStyleResponse> response = new ArrayList<>();
        calculateAndSetPropensityAverageRate(keys, propensity, participantCount, response);
        return response;
    }

    private Trip getTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TRIP_NOT_FOUND));
    }

    private boolean isOwnerParticipant(Long userId, User user) {
        return Objects.equals(user.getId(), userId);
    }

    private Map<String, Integer> getDefaultPropensity(List<Participant> participants) {
        Map<String, Integer> propensity = createDefaultPropensity();
        participants.forEach(participant -> setDefaultPropensity(participant, propensity));
        return propensity;
    }

    private List<String> sortPropensityKeys(Map<String, Integer> propensity) {
        List<String> keys = new ArrayList<>(propensity.keySet());
        Collections.sort(keys);
        return keys;
    }

    private void calculateAndSetPropensityAverageRate(List<String> keys, Map<String, Integer> propensity, int participantCount, List<TripStyleResponse> response) {
        keys.forEach(key -> {
            int rate = round((float) propensity.get(key) / participantCount);
            boolean isLeft = rate <= MAX_STYLE_RATE - rate;
            response.add(TripStyleResponse.of(rate, isLeft));
        });
    }

    private void setDefaultPropensity(Participant participant, Map<String, Integer> propensity) {
        propensity.put(STYLE_A, propensity.get(STYLE_A) + participant.getStyleA() * PROPENSITY_WEIGHT);
        propensity.put(STYLE_B, propensity.get(STYLE_B) + participant.getStyleB() * PROPENSITY_WEIGHT);
        propensity.put(STYLE_C, propensity.get(STYLE_C) + participant.getStyleC() * PROPENSITY_WEIGHT);
        propensity.put(STYLE_D, propensity.get(STYLE_D) + participant.getStyleD() * PROPENSITY_WEIGHT);
        propensity.put(STYLE_E, propensity.get(STYLE_E) + participant.getStyleE() * PROPENSITY_WEIGHT);
    }
}
