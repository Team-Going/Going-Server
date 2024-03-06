package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.doorip.trip.domain.Trip;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TripsGetResponse(
        String name,
        List<TripResponse> trips
) {
    public static TripsGetResponse of(String name, List<Trip> trips) {
        return TripsGetResponse.builder()
                .name(name)
                .trips(trips.stream()
                        .map(TripResponse::of)
                        .toList())
                .build();

    }
}
