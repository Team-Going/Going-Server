package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.Participant;
import org.doorip.trip.domain.Role;
import org.doorip.trip.domain.Trip;
import org.doorip.trip.dto.request.TripCreateRequest;
import org.doorip.trip.dto.response.TripCreateResponse;
import org.doorip.trip.repository.ParticipantRepository;
import org.doorip.common.Constants;
import org.doorip.trip.dto.response.TripGetResponse;
import org.doorip.trip.repository.TripRepository;
import org.doorip.user.domain.User;
import org.doorip.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.doorip.trip.domain.Participant.createParticipant;
import static org.doorip.trip.domain.Trip.createTrip;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TripService {
    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    @Transactional
    public TripCreateResponse createTripAndParticipant(Long userId, TripCreateRequest request) {
        User findUser = getUser(userId);
        validateDate(request);
        String code = createCode(6);
        Trip createdTrip = saveTrip(request, code);
        saveParticipant(request.styleA(), request.styleB(), request.styleC(), request.styleD(), request.styleE(), findUser, createdTrip);

        return TripCreateResponse.of(createdTrip);
    }

    public TripGetResponse getTrips(Long userId, String progress) {
        User findUser = getUser(userId);
        List<Trip> trips = getTripsAccordingToProgress(userId, progress);
        return TripGetResponse.of(findUser.getName(), trips);
    }

    private List<Trip> getTripsAccordingToProgress(Long userId, String progress) {
        if (progress.equals(Constants.INCOMPLETE)) {
            return tripRepository.findInCompleteTripsByUserId(userId, LocalDate.now());
        } else if (progress.equals(Constants.COMPLETE)) {
            return tripRepository.findCompleteTripsByUserId(userId, LocalDate.now());
        }
        throw new InvalidValueException(ErrorMessage.INVALID_REQUEST_PARAMETER_VALUE);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private void validateDate(TripCreateRequest request) {
        if (request.endDate().isBefore(LocalDate.now()) || request.endDate().isBefore(request.startDate())) {
            throw new InvalidValueException(ErrorMessage.INVALID_DATE_TYPE);
        }
    }

    private String createCode(int num) {
        String code;

        do {
            String uuid = UUID.randomUUID().toString();
            System.out.println(uuid);
            code = uuid.substring(0, num);

        } while (validateDuplicateCode(code));

        return code;
    }

    private boolean validateDuplicateCode(String code) {
        return tripRepository.existsByCode(code);
    }

    private Trip saveTrip(TripCreateRequest request, String code) {
        Trip trip = createTrip(request.title(), request.startDate(), request.endDate(), code);
        return tripRepository.save(trip);
    }

    private void saveParticipant(int styleA, int styleB, int styleC, int styleD, int styleE,
                                 User user, Trip trip) {
        Participant participant = createParticipant(Role.HOST, styleA, styleB, styleC, styleD, styleE, user, trip);
        participantRepository.save(participant);
    }
}
