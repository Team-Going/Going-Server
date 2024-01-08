package org.doorip.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record TodoCreateRequest(
        Long tripId,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        List<Long> allocators,
        String memo,
        boolean secret
) {
}
