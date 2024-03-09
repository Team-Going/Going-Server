package org.doorip.trip.dto.response;

import java.util.List;

public record TripStyleResponse(
        List<Integer> rates,
        List<Integer> counts
) {
    public static TripStyleResponse of(List<Integer> rates, List<Integer> counts) {
        return new TripStyleResponse(rates, counts);
    }
}
