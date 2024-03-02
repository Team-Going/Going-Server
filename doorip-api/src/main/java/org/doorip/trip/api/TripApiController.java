package org.doorip.trip.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponseUtil;
import org.doorip.common.BaseResponse;
import org.doorip.message.SuccessMessage;
import org.doorip.trip.dto.request.TripCreateRequest;
import org.doorip.trip.dto.request.TripEntryRequest;
import org.doorip.trip.dto.request.TripUpdateRequest;
import org.doorip.trip.dto.request.TripVerifyRequest;
import org.doorip.trip.dto.request.ParticipantUpdateRequest;
import org.doorip.trip.dto.response.*;
import org.doorip.trip.service.TripDetailService;
import org.doorip.trip.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/trips")
@Controller
public class TripApiController implements TripApi {
    private final TripService tripService;
    private final TripDetailService tripDetailService;

    @PostMapping
    @Override
    public ResponseEntity<BaseResponse<?>> createTrip(@UserId final Long userId,
                                                      @RequestBody final TripCreateRequest request) {
        final TripCreateResponse response = tripService.createTripAndParticipant(userId, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @GetMapping
    @Override
    public ResponseEntity<BaseResponse<?>> getTrips(@UserId final Long userId,
                                                    @RequestParam final String progress) {
        final TripGetResponse response = tripService.getTrips(userId, progress);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PostMapping("/verify")
    @Override
    public ResponseEntity<BaseResponse<?>> verifyCode(@RequestBody final TripVerifyRequest request) {
        final TripResponse response = tripService.verifyCode(request);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PostMapping("/{tripId}/entry")
    @Override
    public ResponseEntity<BaseResponse<?>> entryTrip(@PathVariable final Long tripId,
                                                     @UserId final Long userId,
                                                     @RequestBody final TripEntryRequest request) {
        final TripEntryResponse response = tripService.entryTrip(userId, tripId, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED, response);
    }

    @GetMapping("/{tripId}/my")
    @Override
    public ResponseEntity<BaseResponse<?>> getMyTodoDetail(@UserId final Long userId,
                                                           @PathVariable final Long tripId) {
        final MyTodoResponse response = tripDetailService.getMyTodoDetail(userId, tripId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @GetMapping("/{tripId}/our")
    @Override
    public ResponseEntity<BaseResponse<?>> getOurTodoDetail(@UserId final Long userId,
                                                            @PathVariable final Long tripId) {
        final OurTodoResponse response = tripDetailService.getOurTodoDetail(userId, tripId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @GetMapping("/{tripId}/participants")
    @Override
    public ResponseEntity<BaseResponse<?>> getParticipants(@UserId final Long userId,
                                                           @PathVariable final Long tripId) {
        final TripParticipantGetResponse response = tripDetailService.getParticipants(userId, tripId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PatchMapping("/{tripId}/leave")
    public ResponseEntity<BaseResponse<?>> leaveTrip(@UserId final Long userId,
                                                     @PathVariable final Long tripId) {
        tripDetailService.leaveTrip(userId, tripId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @PatchMapping("/{tripId}")
    public ResponseEntity<BaseResponse<?>> updateTrip(@PathVariable final Long tripId,
                                                      @UserId final Long userId,
                                                      @RequestBody final TripUpdateRequest request) {
        tripService.updateTrip(userId, tripId, request);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @PatchMapping("/{tripId}/participant")
    public ResponseEntity<BaseResponse<?>> updateParticipant(@PathVariable final Long tripId,
                                                             @UserId final Long userId,
                                                             @RequestBody final ParticipantUpdateRequest request) {
        tripService.updateParticipant(userId, tripId, request);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }
}
