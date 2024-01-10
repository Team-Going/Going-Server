package org.doorip.trip.service;

import lombok.RequiredArgsConstructor;
import org.doorip.exception.EntityNotFoundException;
import org.doorip.message.ErrorMessage;
import org.doorip.trip.domain.Progress;
import org.doorip.trip.domain.Trip;
import org.doorip.trip.dto.response.MyTodoResponse;
import org.doorip.trip.repository.TodoRepository;
import org.doorip.trip.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private Trip getTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TRIP_NOT_FOUND));
    }

    private int getIncompleteTodoCount(Long tripId) {
        return todoRepository.countTodoByTripIdAndProgress(tripId, Progress.INCOMPLETE);
    }
}
