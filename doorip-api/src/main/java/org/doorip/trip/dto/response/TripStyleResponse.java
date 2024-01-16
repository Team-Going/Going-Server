package org.doorip.trip.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TripStyleResponse(
        int rate,
        boolean isLeft
) {
    public static TripStyleResponse of(int rate, boolean isLeft) {
        return TripStyleResponse.builder()
                .rate(rate)
                .isLeft(isLeft)
                .build();
    }
}
