package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Participant;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TripParticipantGetResponse(
        List<String> bestPrefer,
        List<TripParticipantResponse> participants,
        List<TripStyleResponse> styles
) {
    public static TripParticipantGetResponse of(List<String> bestPrefer, List<Participant> participants, List<List<Integer>> styles) {
        return TripParticipantGetResponse.builder()
                .bestPrefer(bestPrefer)
                .participants(participants.stream()
                        .map(TripParticipantResponse::of)
                        .toList())
                .styles(styles.stream()
                        .map(TripStyleResponse::from)
                        .toList())
                .build();
    }
}
