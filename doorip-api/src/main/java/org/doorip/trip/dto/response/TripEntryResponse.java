package org.doorip.trip.dto.response;

import org.doorip.trip.domain.Trip;

public record TripEntryResponse(
        Long tripId
) {

    public static TripEntryResponse of(Trip trip) {
        return new TripEntryResponse(trip.getId());
    }
}
