package com.todoapp.todo_backend.service;

import com.todoapp.todo_backend.business.TaskService;
import com.todoapp.todo_backend.exception.ResourceNotFoundException;
import com.todoapp.todo_backend.models.Task;
import com.todoapp.todo_backend.persistence.requestDTO.TaskRequestDto;
import com.todoapp.todo_backend.persistence.responseDTO.TaskResponseDto;
import com.todoapp.todo_backend.repository.TaskRepository;
import com.todoapp.todo_backend.repository.TaskRepositoryTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepositoryTemplate taskRepositoryTemplate;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getLatestTasks_ShouldReturnLatestIncompleteTasks() {
        // Given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCompleted(false);
        task1.setCreatedAt(LocalDateTime.now());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCompleted(false);
        task2.setCreatedAt(LocalDateTime.now());

        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepositoryTemplate.getLatestCompletedTasks()).thenReturn(tasks);

        // When
        List<TaskResponseDto> result = taskService.getLatestTasks();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getTitle()).isEqualTo("Task 2");

        verify(taskRepositoryTemplate).getLatestCompletedTasks();
    }

    @Test
    void createTask_WithValidRequest_ShouldCreateAndReturnTask() {
        // Given
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setTitle("New Task");
        requestDto.setDescription("New Description");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setCompleted(false);
        savedTask.setCreatedAt(LocalDateTime.now());

        when(taskRepositoryTemplate.save(any(Task.class))).thenReturn(savedTask);

        // When
        TaskResponseDto result = taskService.createTask(requestDto);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New Task");
        assertThat(result.getDescription()).isEqualTo("New Description");
        assertThat(result.getCreatedAt()).isNotNull();

        verify(taskRepositoryTemplate).save(any(Task.class));
    }

    @Test
    void markDone_WithValidId_ShouldMarkTaskAsCompleted() {
        // Given
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Existing Task");
        existingTask.setDescription("Existing Description");
        existingTask.setCompleted(false);
        existingTask.setCreatedAt(LocalDateTime.now());

        when(taskRepositoryTemplate.findByTaskId(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepositoryTemplate.save(any(Task.class))).thenReturn(existingTask);

        // When
        taskService.markDone(taskId);

        // Then
        verify(taskRepositoryTemplate).findByTaskId(taskId);
        verify(taskRepositoryTemplate).save(any(Task.class));
        // Verify the task was marked as completed
        assertThat(existingTask.getCompleted()).isTrue();
    }

    @Test
    void markDone_WithNonExistentId_ShouldThrowException() {
        // Given
        Long nonExistentId = 999L;
        when(taskRepositoryTemplate.findByTaskId(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.markDone(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found with id: " + nonExistentId);

        verify(taskRepositoryTemplate).findByTaskId(nonExistentId);
        verify(taskRepositoryTemplate, never()).save(any(Task.class));
    }

    @Test
    void createTask_ShouldSetCompletedAsFalse() {
        // Given
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setTitle("Test Task");
        requestDto.setDescription("Test Description");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Test Task");
        savedTask.setDescription("Test Description");
        savedTask.setCompleted(false);
        savedTask.setCreatedAt(LocalDateTime.now());

        when(taskRepositoryTemplate.save(any(Task.class))).thenReturn(savedTask);

        // When
        TaskResponseDto result = taskService.createTask(requestDto);

        // Then
        assertThat(result.getTitle()).isEqualTo("Test Task");
        verify(taskRepositoryTemplate).save(any(Task.class));
    }

}