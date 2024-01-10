package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MyTodoResponse(
        String name,
        int count
) {
    public static MyTodoResponse of(String name, int count) {
        return MyTodoResponse.builder()
                .name(name)
                .count(count)
                .build();
    }
}
