package org.doorip.trip.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Trip;

import java.time.LocalDate;

import static java.time.Period.between;

@Builder(access = AccessLevel.PRIVATE)
public record TripResponse(
        Long tripId,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        int day
) {
    public static TripResponse of(Trip trip) {
        return TripResponse.builder()
                .tripId(trip.getId())
                .title(trip.getTitle())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .day(between(LocalDate.now(), trip.getStartDate()).getDays())
                .build();
    }
}
