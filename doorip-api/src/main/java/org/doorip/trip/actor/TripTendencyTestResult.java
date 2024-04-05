package org.doorip.trip.actor;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TripTendencyTestResult(
        List<String> bestPrefer,
        List<List<Integer>> rates,
        List<List<Integer>> counts
) {
    public static TripTendencyTestResult of(List<String> bestPrefer, List<List<Integer>> rates, List<List<Integer>> counts) {
        return TripTendencyTestResult.builder()
                .bestPrefer(bestPrefer)
                .rates(rates)
                .counts(counts)
                .build();
    }
}
