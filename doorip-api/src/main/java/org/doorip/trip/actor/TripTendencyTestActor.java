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
        List<List<Integer>> styles = generateStyles();
        int participantCount = participants.size();
        accumulateStyles(participants, styles);
        List<String> bestPrefer = calculateBestPrefer(styles, participantCount);
        calculateStyleAverage(styles, participantCount);
        return TripTendencyTestResult.of(bestPrefer, styles);
    }

    private List<List<Integer>> generateStyles() {
        List<List<Integer>> styles = new ArrayList<>();
        IntStream.range(INITIALIZATION, HIGH_RANGE_STYLES)
                .forEach(i -> styles.add(i, new ArrayList<>(Arrays.asList(INITIALIZATION, INITIALIZATION, INITIALIZATION))));
        return styles;
    }

    private void accumulateStyles(List<Participant> participants, List<List<Integer>> styles) {
        participants.forEach(participant -> {
            accumulateStyle(styles, participant.getStyleA(), 0);
            accumulateStyle(styles, participant.getStyleB(), 1);
            accumulateStyle(styles, participant.getStyleC(), 2);
            accumulateStyle(styles, participant.getStyleD(), 3);
            accumulateStyle(styles, participant.getStyleE(), 4);
        });
    }

    private List<String> calculateBestPrefer(List<List<Integer>> styles, int participantCount) {
        List<String> bestPrefer = new ArrayList<>();
        IntStream.range(INITIALIZATION, HIGH_RANGE_STYLES)
                .forEach(i -> {
                    List<Integer> style = styles.get(i);
                    if (style.contains(participantCount)) {
                        bestPrefer.add(prefer.get(i));
                    }
                });
        return bestPrefer;
    }

    private void calculateStyleAverage(List<List<Integer>> styles, int participantCount) {
        double percentage = 100.0 / participantCount;
        styles.forEach(style ->
                IntStream.range(INITIALIZATION, HIGH_RANGE_STYLE)
                        .forEach(i -> style.set(i, (int) Math.floor(style.get(i) * percentage))));
    }

    private void accumulateStyle(List<List<Integer>> styles, int styleValue, int pos) {
        List<Integer> style = styles.get(pos);
        if (styleValue < CENTER_POS) {
            style.set(0, style.get(0) + 1);
        } else if (styleValue == CENTER_POS) {
            style.set(1, style.get(1) + 1);
        } else {
            style.set(2, style.get(2) + 1);
        }
    }
}
