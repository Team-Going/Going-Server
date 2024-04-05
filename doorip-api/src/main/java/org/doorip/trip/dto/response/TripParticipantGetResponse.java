package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.common.Constants;
import org.doorip.trip.domain.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Builder(access = AccessLevel.PRIVATE)
public record TripParticipantGetResponse(
        List<String> bestPrefer,
        List<TripParticipantResponse> participants,
        List<TripStyleResponse> styles
) {
    public static TripParticipantGetResponse of(List<String> bestPrefer, List<Participant> participants, List<List<Integer>> rates, List<List<Integer>> counts) {
        List<TripStyleResponse> styles = new ArrayList<>();
        IntStream.range(Constants.START_STYLE_POS, Constants.END_STYLE_POS)
                .forEach(i -> styles.add(TripStyleResponse.of(rates.get(i), counts.get(i))));
        return TripParticipantGetResponse.builder()
                .bestPrefer(bestPrefer)
                .participants(participants.stream()
                        .map(TripParticipantResponse::of)
                        .toList())
                .styles(styles)
                .build();
    }
}
