package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Allocator;
import org.doorip.trip.domain.Participant;
import org.doorip.user.domain.User;

import java.util.Objects;

@Builder(access = AccessLevel.PRIVATE)
public record TodoAllocatorResponse(
        String name,
        boolean isOwner
) {
    public static TodoAllocatorResponse of(Long userId, Allocator allocator) {
        Participant participant = allocator.getParticipant();
        User user = participant.getUser();
        return TodoAllocatorResponse.builder()
                .name(user.getName())
                .isOwner(Objects.equals(user.getId(), userId))
                .build();
    }
}
