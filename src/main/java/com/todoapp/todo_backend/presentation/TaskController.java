package com.todoapp.todo_backend.presentation;

import com.todoapp.todo_backend.business.TaskService;
import com.todoapp.todo_backend.persistence.requestDTO.TaskRequestDto;
import com.todoapp.todo_backend.persistence.responseDTO.TaskResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller exposing task management endpoints.
 *
 * <p>Acts as the boundary of the application’s presentation layer, converting HTTP
 * requests into business operations and returning structured API responses.
 * Integrates with OpenAPI annotations for API discoverability.</p>
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin("*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Retrieve latest 5 active (Complete) tasks",
            description = "Returns the latest five tasks that are marked as completed.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content = @Content(schema = @Schema(implementation = TaskResponseDto.class))
                    )
            }
    )
    @GetMapping("/completed")
    public ResponseEntity<List<TaskResponseDto>> getLatest() {
        return ResponseEntity.ok(taskService.getLatestTasks());
    }


    @Operation(
            summary = "Retrieve latest 5 active (incomplete) tasks",
            description = "Returns the latest five tasks that are not marked as completed.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content = @Content(schema = @Schema(implementation = TaskResponseDto.class))
                    )
            }
    )
    @GetMapping("/inCompleted")
    public ResponseEntity<List<TaskResponseDto>> getLatestInCompleted() {
        return ResponseEntity.ok(taskService.getLatestInCompleted());
    }


    @Operation(
            summary = "Create a new task",
            description = "Accepts a validated request body and creates a new task in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping
    public ResponseEntity<TaskResponseDto> create(
            @Valid @RequestBody TaskRequestDto dto
    ) {
        TaskResponseDto created = taskService.createTask(dto);
        return ResponseEntity
                .created(URI.create("/api/tasks/" + created.getId()))
                .body(created);
    }

    @Operation(
            summary = "Mark a task as completed",
            description = "Marks a task as done. Completed tasks do not show in the latest task list.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Task marked completed"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    @PostMapping("/{id}/done")
    public ResponseEntity<Void> done(@PathVariable Long id) {
        taskService.markDone(id);
        return ResponseEntity.noContent().build();
    }
}
