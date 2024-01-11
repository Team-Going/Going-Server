package org.doorip.trip.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TripCreateRequest(
    String title,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    LocalDate startDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    LocalDate endDate,
    int styleA,
    int styleB,
    int styleC,
    int styleD,
    int styleE
) {
}
