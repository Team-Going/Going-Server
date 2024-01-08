package org.doorip.todo.api;

import lombok.RequiredArgsConstructor;
import org.doorip.common.ApiResponse;
import org.doorip.common.ApiResponseUtil;
import org.doorip.message.SuccessMessage;
import org.doorip.todo.dto.request.TodoCreateRequest;
import org.doorip.todo.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/todos")
@Controller
public class TodoApiController {
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createTripTodo(@RequestBody final TodoCreateRequest request) {
        todoService.createTripTodo(request);
        return ApiResponseUtil.success(SuccessMessage.CREATED);
    }
}
