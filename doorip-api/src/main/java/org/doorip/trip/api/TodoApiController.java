package org.doorip.trip.api;

import lombok.RequiredArgsConstructor;
import org.doorip.auth.UserId;
import org.doorip.common.ApiResponseUtil;
import org.doorip.common.BaseResponse;
import org.doorip.message.SuccessMessage;
import org.doorip.trip.dto.request.TodoCreateAndUpdateRequest;
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
public class TodoApiController implements TodoApi {
    private final TodoService todoService;

    @PostMapping("/{tripId}/todos")
    @Override
    public ResponseEntity<BaseResponse<?>> createTripTodo(@UserId final Long userId,
                                                          @PathVariable final Long tripId,
                                                          @RequestBody final TodoCreateAndUpdateRequest request) {
        todoService.createTripTodo(userId, tripId, request);
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }

    @GetMapping("/{tripId}/todos")
    @Override
    public ResponseEntity<BaseResponse<?>> getTripTodos(@UserId final Long userId,
                                                        @PathVariable final Long tripId,
                                                        @RequestParam final String category,
                                                        @RequestParam final String progress) {
        final List<TodoGetResponse> response = todoService.getTripTodos(userId, tripId, category, progress);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @GetMapping("/{tripId}/todos/{todoId}")
    @Override
    public ResponseEntity<BaseResponse<?>> getTripTodo(@UserId final Long userId,
                                                       @PathVariable final Long tripId,
                                                       @PathVariable final Long todoId) {
        final TodoDetailGetResponse response = todoService.getTripTodo(userId, tripId, todoId);
        return ApiResponseUtil.success(SuccessMessage.OK, response);
    }

    @PatchMapping("/{tripId}/todos/{todoId}")
    public ResponseEntity<BaseResponse<?>> updateTripTodo(@UserId final Long userId,
                                                          @PathVariable final Long tripId,
                                                          @PathVariable final Long todoId,
                                                          @RequestBody final TodoCreateAndUpdateRequest request) {
        todoService.updateTripTodo(userId, tripId, todoId, request);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @DeleteMapping("/todos/{todoId}")
    @Override
    public ResponseEntity<BaseResponse<?>> deleteTripTodo(@PathVariable final Long todoId) {
        todoService.deleteTripTodo(todoId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @GetMapping("/todos/{todoId}/complete")
    @Override
    public ResponseEntity<BaseResponse<?>> completeTripTodo(@PathVariable final Long todoId) {
        todoService.completeTripTodo(todoId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }

    @GetMapping("/todos/{todoId}/incomplete")
    @Override
    public ResponseEntity<BaseResponse<?>> incompleteTripTodo(@PathVariable final Long todoId) {
        todoService.incompleteTripTodo(todoId);
        return ApiResponseUtil.success(SuccessMessage.OK);
    }
}
