package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Trip;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TripGetResponse(
        String name,
        List<TripResponse> trips
) {
    public static TripGetResponse of(String name, List<Trip> trips) {
        return TripGetResponse.builder()
                .name(name)
                .trips(trips.stream()
                        .map(TripResponse::of)
                        .toList())
                .build();

    }
}
