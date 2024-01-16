package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Participant;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TripParticipantGetResponse(
        List<TripParticipantResponse> participants,
        List<TripStyleResponse> styles
) {
    public static TripParticipantGetResponse of(List<Participant> participants, List<TripStyleResponse> styles) {
        return TripParticipantGetResponse.builder()
                .participants(participants.stream()
                        .map(TripParticipantResponse::of)
                        .toList())
                .styles(styles)
                .build();
    }
}
