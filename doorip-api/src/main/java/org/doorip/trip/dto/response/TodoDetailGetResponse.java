package org.doorip.trip.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Secret;
import org.doorip.trip.domain.Todo;

import java.time.LocalDate;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TodoDetailGetResponse(
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        List<TodoDetailAllocatorResponse> allocators,
        String memo,
        boolean secret
) {
    public static TodoDetailGetResponse of(Todo todo, List<TodoDetailAllocatorResponse> allocators) {
        return TodoDetailGetResponse.builder()
                .title(todo.getTitle())
                .endDate(todo.getEndDate())
                .allocators(allocators)
                .memo(todo.getMemo())
                .secret(Secret.of(todo.getSecret()))
                .build();
    }
}
