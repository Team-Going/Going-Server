package org.doorip.trip.actor;

import java.util.List;

public record TripTendencyTestResult(
        List<String> bestPrefer,
        List<List<Integer>> styles
) {
    public static TripTendencyTestResult of(List<String> bestPrefer, List<List<Integer>> styles) {
        return new TripTendencyTestResult(bestPrefer, styles);
    }
}
