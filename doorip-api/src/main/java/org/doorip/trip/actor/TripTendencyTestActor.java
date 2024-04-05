package org.doorip.trip.actor;

import org.doorip.trip.domain.Participant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class TripTendencyTestActor {
    private static final String TRIP_PLAN = "여행 계획";
    private static final String TRIP_PLACE = "여행 장소";
    private static final String RESTAURANT = "식당";
    private static final String PICTURE = "사진";
    private static final String TRIP_SCHEDULE = "여행 일정";
    private static final int INITIALIZATION = 0;
    private static final int HIGH_RANGE_STYLE = 3;
    private static final int HIGH_RANGE_STYLES = 5;
    private static final int CENTER_POS = 2;
    private final Map<Integer, String> prefer = Map.of(
            0, TRIP_PLAN,
            1, TRIP_PLACE,
            2, RESTAURANT,
            3, PICTURE,
            4, TRIP_SCHEDULE
    );

    public TripTendencyTestResult calculateTripTendencyTest(List<Participant> participants) {
        List<List<Integer>> rates = generateStyles();
        List<List<Integer>> counts = generateStyles();
        int participantCount = participants.size();
        accumulateCounts(participants, counts);
        List<String> bestPrefer = calculateBestPrefer(counts, participantCount);
        calculateRatesAverage(rates, counts, participantCount);
        return TripTendencyTestResult.of(bestPrefer, rates, counts);
    }

    private List<List<Integer>> generateStyles() {
        List<List<Integer>> styles = new ArrayList<>();
        IntStream.range(INITIALIZATION, HIGH_RANGE_STYLES)
                .forEach(i -> styles.add(i, new ArrayList<>(Arrays.asList(INITIALIZATION, INITIALIZATION, INITIALIZATION))));
        return styles;
    }

    private void accumulateCounts(List<Participant> participants, List<List<Integer>> counts) {
        participants.forEach(participant -> {
            accumulateCount(counts, participant.getStyleA(), 0);
            accumulateCount(counts, participant.getStyleB(), 1);
            accumulateCount(counts, participant.getStyleC(), 2);
            accumulateCount(counts, participant.getStyleD(), 3);
            accumulateCount(counts, participant.getStyleE(), 4);
        });
    }

    private List<String> calculateBestPrefer(List<List<Integer>> counts, int participantCount) {
        List<String> bestPrefer = new ArrayList<>();
        IntStream.range(INITIALIZATION, HIGH_RANGE_STYLES)
                .forEach(i -> {
                    List<Integer> count = counts.get(i);
                    if (count.contains(participantCount)) {
                        bestPrefer.add(prefer.get(i));
                    }
                });
        return bestPrefer;
    }

    private void calculateRatesAverage(List<List<Integer>> rates, List<List<Integer>> counts, int participantCount) {
        double percentage = 100.0 / participantCount;
        IntStream.range(INITIALIZATION, HIGH_RANGE_STYLES)
                .forEach(i -> {
                    List<Integer> rate = rates.get(i);
                    List<Integer> count = counts.get(i);
                    IntStream.range(INITIALIZATION, HIGH_RANGE_STYLE)
                            .forEach(j -> rate.set(j, (int) Math.floor(count.get(j) * percentage)));
                });
    }

    private void accumulateCount(List<List<Integer>> counts, int styleValue, int pos) {
        List<Integer> count = counts.get(pos);
        if (styleValue < CENTER_POS) {
            count.set(0, count.get(0) + 1);
        } else if (styleValue == CENTER_POS) {
            count.set(1, count.get(1) + 1);
        } else {
            count.set(2, count.get(2) + 1);
        }
    }
}
