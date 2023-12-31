package org.doorip.trip.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.trip.dto.request.TodoCreateRequest;
import org.doorip.trip.dto.response.TodoGetResponse;
import org.doorip.trip.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{tripId}/todos")
    public ResponseEntity<ApiResponse<?>> getTripTodos(@UserId final Long userId,
                                                       @PathVariable final Long tripId,
                                                       @RequestParam final String category,
                                                       @RequestParam final String progress) {
        final List<TodoGetResponse> response = todoService.getTripTodos(userId, tripId, category, progress);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }
}
