package com.todoapp.todo_backend.controller;

import com.todoapp.todo_backend.business.TaskService;
import com.todoapp.todo_backend.persistence.requestDTO.TaskRequestDto;
import com.todoapp.todo_backend.persistence.responseDTO.TaskResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.todo_backend.presentation.TaskController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getLatestCompleted_ShouldReturnLatestCompletedTasks() throws Exception {
        // Given
        TaskResponseDto task1 = createTaskResponseDto(1L, "Task 1", "Description 1");
        TaskResponseDto task2 = createTaskResponseDto(2L, "Task 2", "Description 2");

        List<TaskResponseDto> tasks = Arrays.asList(task1, task2);

        when(taskService.getLatestTasks()).thenReturn(tasks);

        // When & Then - Update endpoint to /completed
        mockMvc.perform(get("/api/tasks/completed")) // Changed endpoint
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService).getLatestTasks();
    }

    @Test
    void getLatestInCompleted_ShouldReturnLatestInCompletedTasks() throws Exception {
        // Given
        TaskResponseDto task1 = createTaskResponseDto(1L, "Task 1", "Description 1");
        List<TaskResponseDto> tasks = Arrays.asList(task1);

        when(taskService.getLatestInCompleted()).thenReturn(tasks);

        // When & Then - Test the inCompleted endpoint
        mockMvc.perform(get("/api/tasks/inCompleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"));

        verify(taskService).getLatestInCompleted();
    }

    @Test
    void create_WithValidRequest_ShouldCreateTask() throws Exception {
        // Given
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setTitle("New Task");
        requestDto.setDescription("New Description");

        TaskResponseDto responseDto = createTaskResponseDto(1L, "New Task", "New Description");

        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tasks/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));

        verify(taskService).createTask(any(TaskRequestDto.class));
    }

    @Test
    void done_WithValidId_ShouldMarkTaskAsDone() throws Exception {
        // Given
        Long taskId = 1L;

        // When & Then
        mockMvc.perform(post("/api/tasks/{id}/done", taskId))
                .andExpect(status().isNoContent());

        verify(taskService).markDone(taskId);
    }

    @Test
    void done_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;

        doThrow(new com.todoapp.todo_backend.exception.ResourceNotFoundException("Task not found"))
                .when(taskService).markDone(eq(nonExistentId));

        // When & Then
        mockMvc.perform(post("/api/tasks/{id}/done", nonExistentId))
                .andExpect(status().isNotFound());

        verify(taskService).markDone(nonExistentId);
    }

    @Test
    void getLatestCompleted_WhenNoTasks_ShouldReturnEmptyList() throws Exception {
        // Given
        when(taskService.getLatestTasks()).thenReturn(List.of());

        // When & Then - Update endpoint to /completed
        mockMvc.perform(get("/api/tasks/completed")) // Changed endpoint
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(taskService).getLatestTasks();
    }

    @Test
    void create_WhenServiceReturnsNull_ShouldReturnServerError() throws Exception {
        // Given
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setTitle("New Task");
        requestDto.setDescription("New Description");

        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());

        verify(taskService).createTask(any(TaskRequestDto.class));
    }

    // Helper method to create TaskResponseDto with all required fields
    private TaskResponseDto createTaskResponseDto(Long id, String title, String description) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}