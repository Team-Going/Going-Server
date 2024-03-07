package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Participant;
import org.doorip.user.domain.User;

@Builder(access = AccessLevel.PRIVATE)
public record TripParticipantResponse(
        Long participantId,
        String name,
        int result
) {
    public static TripParticipantResponse of(Participant participant) {
        User user = participant.getUser();
        return TripParticipantResponse.builder()
                .participantId(participant.getId())
                .name(user.getName())
                .result(user.getResult() == null ? -1 : user.getResult().getNumResult())
                .build();
    }
}
