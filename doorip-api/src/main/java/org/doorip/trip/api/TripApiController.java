package org.doorip.trip.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.trip.dto.response.TripGetResponse;
import org.doorip.trip.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/api/trips")
@Controller
public class TripApiController {
    private final TripService tripService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getTrips(@UserId final Long userId, @RequestParam final String progress) {
        final TripGetResponse response = tripService.getTrips(userId, progress);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }
}
