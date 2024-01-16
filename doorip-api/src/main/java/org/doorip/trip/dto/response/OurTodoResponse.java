package org.doorip.trip.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Participant;
import org.doorip.trip.domain.Trip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record OurTodoResponse(
        String title,
        int day,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        int progress,
        String code,
        boolean isComplete,
        List<TripParticipantResponse> participants
) {
    public static OurTodoResponse of(Trip trip, int progress, boolean isComplete, List<Participant> participants) {
        return OurTodoResponse.builder()
                .title(trip.getTitle())
                .day((int) ChronoUnit.DAYS.between(LocalDate.now(), trip.getStartDate()))
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .progress(progress)
                .code(trip.getCode())
                .isComplete(isComplete)
                .participants(participants.stream()
                        .map(TripParticipantResponse::of)
                        .toList())
                .build();
    }
}
