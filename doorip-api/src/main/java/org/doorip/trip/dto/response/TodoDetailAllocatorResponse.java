package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Participant;

@Builder(access = AccessLevel.PRIVATE)
public record TodoDetailAllocatorResponse(
        Long participantId,
        String name,
        boolean isOwner,
        boolean isAllocated
) {
    public static TodoDetailAllocatorResponse of(String name, boolean isOwner, boolean isAllocated, Participant participant) {
        return TodoDetailAllocatorResponse.builder()
                .participantId(participant.getId())
                .name(name)
                .isOwner(isOwner)
                .isAllocated(isAllocated)
                .build();
    }
}
