package org.doorip.trip.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.doorip.trip.domain.Trip;

import java.time.LocalDate;

import static java.time.Period.between;

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
        return new TripCreateResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getCode(),
                between(LocalDate.now(), trip.getStartDate()).getDays()
        );
    }
}
