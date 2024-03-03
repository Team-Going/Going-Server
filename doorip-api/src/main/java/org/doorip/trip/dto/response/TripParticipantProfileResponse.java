package org.doorip.trip.dto.response;

import org.doorip.trip.domain.Participant;
import org.doorip.user.domain.User;

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
        return new TripParticipantProfileResponse(
                user.getName(),
                user.getIntro(),
                validatedResult,
                participant.getStyleA(),
                participant.getStyleB(),
                participant.getStyleC(),
                participant.getStyleD(),
                participant.getStyleE(),
                isOwner
        );
    }
}
