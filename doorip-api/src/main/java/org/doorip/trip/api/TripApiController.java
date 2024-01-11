package org.doorip.trip.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.trip.dto.request.TripCreateRequest;
import org.doorip.trip.dto.request.TripEntryRequest;
import org.doorip.trip.dto.request.TripVerifyRequest;
import org.doorip.trip.dto.response.*;
import org.doorip.trip.service.TripDetailService;
import org.doorip.trip.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/trips")
@Controller
public class TripApiController {
    private final TripService tripService;
    private final TripDetailService tripDetailService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createTrip(@UserId final Long userId,
                                                     @RequestBody final TripCreateRequest request) {
        final TripCreateResponse response = tripService.createTripAndParticipant(userId, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getTrips(@UserId final Long userId,
                                                   @RequestParam final String progress) {
        final TripGetResponse response = tripService.getTrips(userId, progress);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyCode(@RequestBody TripVerifyRequest request) {
        final TripResponse response = tripService.verifyCode(request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PostMapping("/{tripId}/entry")
    public ResponseEntity<ApiResponse<?>> entryTrip(@PathVariable final Long tripId,
                                                    @UserId final Long userId,
                                                    @RequestBody final TripEntryRequest request) {
        final TripEntryResponse response = tripService.entryTrip(userId, tripId, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @GetMapping("/{tripId}/my")
    public ResponseEntity<ApiResponse<?>> getMyTodoDetail(@PathVariable final Long tripId) {
        final MyTodoResponse response = tripDetailService.getMyTodoDetail(tripId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @GetMapping("/{tripId}/our")
    public ResponseEntity<ApiResponse<?>> getOurTodoDetail(@UserId final Long userId,
                                                           @PathVariable final Long tripId) {
        final OurTodoResponse response = tripDetailService.getOurTodoDetail(userId, tripId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }
}
