package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Participant;
import org.doorip.user.domain.User;

@Builder(access = AccessLevel.PRIVATE)
public record TripParticipantProfileResponse(
        String name,
        String intro,
        int result,
        int styleA,
        int styleB,
        int styleC,
        int styleD,
        int styleE,
        boolean isOwner

) {

    public static TripParticipantProfileResponse of(User user, int validatedResult, Participant participant, boolean isOwner) {
        return TripParticipantProfileResponse.builder()
                .name(user.getName())
                .intro(user.getIntro())
                .result(validatedResult)
                .styleA(participant.getStyleA())
                .styleB(participant.getStyleB())
                .styleC(participant.getStyleC())
                .styleD(participant.getStyleD())
                .styleE(participant.getStyleE())
                .isOwner(isOwner)
                .build();
    }
}
