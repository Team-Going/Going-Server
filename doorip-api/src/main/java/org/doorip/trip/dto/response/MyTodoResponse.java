package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MyTodoResponse(
        Long participantId,
        String title,
        int count
) {
    public static MyTodoResponse of(Long participantId, String title, int count) {
        return MyTodoResponse.builder()
                .participantId(participantId)
                .title(title)
                .count(count)
                .build();
    }
}
