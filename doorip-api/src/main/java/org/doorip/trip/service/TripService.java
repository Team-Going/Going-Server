package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.common.Constants;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.exception.InvalidValueException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.Trip;
import org.doorip.trip.dto.response.TripGetResponse;
import org.doorip.trip.repository.TripRepository;
import org.doorip.user.domain.User;
import org.doorip.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TripService {
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    public TripGetResponse getTrips(Long userId, String progress) {
        User findUser = getUser(userId);
        List<Trip> trips = getTripsAccordingToProgress(userId, progress);
        return TripGetResponse.of(findUser.getName(), trips);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    private List<Trip> getTripsAccordingToProgress(Long userId, String progress) {
        if (progress.equals(Constants.INCOMPLETE)) {
            return tripRepository.findInCompleteTripsByUserId(userId, LocalDate.now());
        } else if (progress.equals(Constants.COMPLETE)) {
            return tripRepository.findCompleteTripsByUserId(userId, LocalDate.now());
        }
        throw new InvalidValueException(ErrorMessage.INVALID_REQUEST_PARAMETER_VALUE);
    }
}
