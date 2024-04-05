package org.doorip.trip.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Trip;

import java.time.LocalDate;

@Builder(access = AccessLevel.PRIVATE)
public record TripGetResponse(
        Long tripId,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate
) {

    public static TripGetResponse of(Trip trip) {
        return TripGetResponse.builder()
                .tripId(trip.getId())
                .title(trip.getTitle())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .build();
    }
}
