package org.doorip.trip.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.trip.dto.request.TodoCreateRequest;
import org.doorip.trip.dto.response.TodoDetailGetResponse;
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

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<ApiResponse<?>> getTripTodo(@UserId final Long userId,
                                                      @PathVariable final Long todoId) {
        final TodoDetailGetResponse response = todoService.getTripTodo(userId, todoId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<ApiResponse<?>> deleteTripTodo(@PathVariable final Long todoId) {
        todoService.deleteTripTodo(todoId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @GetMapping("/todos/{todoId}/complete")
    public ResponseEntity<ApiResponse<?>> completeTripTodo(@PathVariable final Long todoId) {
        todoService.completeTripTodo(todoId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @GetMapping("/todos/{todoId}/incomplete")
    public ResponseEntity<ApiResponse<?>> incompleteTripTodo(@PathVariable final Long todoId) {
        todoService.incompleteTripTodo(todoId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }
}
