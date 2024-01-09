package org.doorip.trip.api;

import lombok.RequiredArgsConstructor;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.trip.dto.request.TodoCreateRequest;
import org.doorip.trip.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/trips")
@Controller
public class TodoApiController {
    private final TodoService todoService;

    @PostMapping("/{tripId}/todos")
    public ResponseEntity<ApiResponse<?>> createTripTodo(@PathVariable final Long tripId,
                                                         @RequestBody final TodoCreateRequest request) {
        todoService.createTripTodo(tripId, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }
}
