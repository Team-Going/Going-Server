package org.doorip.trip.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record TripUpdateRequest(
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate
) {
}
