package org.doorip.trip.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Secret;
import org.doorip.trip.domain.Todo;

import java.time.LocalDate;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TodoGetResponse(
        Long todoId,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        List<TodoAllocatorResponse> allocators,
        boolean secret
) {
    public static TodoGetResponse of(Todo todo, List<TodoAllocatorResponse> allocators) {
        return TodoGetResponse.builder()
                .todoId(todo.getId())
                .title(todo.getTitle())
                .endDate(todo.getEndDate())
                .allocators(allocators)
                .secret(Secret.of(todo.getSecret()))
                .build();
    }
}
