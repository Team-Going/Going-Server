package org.doorip.trip.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Trip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder(access = AccessLevel.PRIVATE)
public record TripCreateResponse(
        Long tripId,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        String code,
        int day
) {

    public static TripCreateResponse of(Trip trip) {
        return TripCreateResponse.builder()
                .tripId(trip.getId())
                .title(trip.getTitle())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .code(trip.getCode())
                .day((int) ChronoUnit.DAYS.between(LocalDate.now(), trip.getStartDate()))
                .build();
    }
}
