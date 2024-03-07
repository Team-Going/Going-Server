package org.doorip.trip.dto.response;

import java.util.List;

public record TripStyleResponse(
        List<Integer> rates
) {
    public static TripStyleResponse from(List<Integer> rates) {
        return new TripStyleResponse(rates);
    }
}
