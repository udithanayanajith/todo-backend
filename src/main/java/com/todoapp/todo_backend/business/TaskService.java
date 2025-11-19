package com.todoapp.todo_backend.business;

import com.todoapp.todo_backend.exception.ResourceNotFoundException;
import com.todoapp.todo_backend.models.Task;
import com.todoapp.todo_backend.persistence.requestDTO.TaskRequestDto;
import com.todoapp.todo_backend.persistence.responseDTO.TaskResponseDto;
import com.todoapp.todo_backend.repository.TaskRepositoryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer responsible for orchestrating all task-related business logic.
 *
 * <p>This class enforces business policies, coordinates persistence operations,
 * transforms domain entities into DTOs, and ensures the application layer
 * remains free from HTTP or database-specific details.</p>
 */
@Service
public class TaskService {

    private final TaskRepositoryTemplate taskRepositoryTemplate;

    public TaskService(TaskRepositoryTemplate taskRepositoryTemplate) {
        this.taskRepositoryTemplate = taskRepositoryTemplate;
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getLatestTasks() {
        List<Task> tasks= taskRepositoryTemplate.getLatestCompletedTasks();
        return tasks.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getLatestInCompleted() {
        List<Task> tasks= taskRepositoryTemplate.getLatestInCompletedTasks();
        return tasks.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        return mapToResponseDto(taskRepositoryTemplate.save(task));
    }

    @Transactional
    public void markDone(Long id) {
        Task task = taskRepositoryTemplate.findByTaskId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        task.setCompleted(true);
        task.setCompletedAt(LocalDateTime.now());
        taskRepositoryTemplate.save(task);
    }


    private TaskResponseDto mapToResponseDto(Task task) {
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(task.getId());
        responseDto.setTitle(task.getTitle());
        responseDto.setDescription(task.getDescription());
        responseDto.setCreatedAt(task.getCreatedAt());
        responseDto.setCompletedAt(task.getCompletedAt());
        return responseDto;
    }
}
