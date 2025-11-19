package com.todoapp.todo_backend.repository;

import com.todoapp.todo_backend.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void testSaveTask() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        // When
        Task savedTask = taskRepository.save(task);

        // Then
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Task");
        assertThat(savedTask.getDescription()).isEqualTo("Test Description");
        assertThat(savedTask.getCompleted()).isFalse();
        assertThat(savedTask.getCreatedAt()).isNotNull();
    }

    @Test
    void getLatestCompletedTasks_ShouldReturnOnlyCompletedTasks() {
        // Given
        Task completedTask1 = createTask("Completed 1", true);
        Task completedTask2 = createTask("Completed 2", true);
        Task incompleteTask = createTask("Incomplete", false);

        taskRepository.saveAll(List.of(completedTask1, completedTask2, incompleteTask));

        // When
        List<Task> result = taskRepository.getLatestCompletedTasks();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(Task::getCompleted);
    }

    @Test
    void getLatestCompletedTasks_ShouldReturnAtMost5Tasks() {
        // Given - Create 7 completed tasks
        for (int i = 1; i <= 7; i++) {
            taskRepository.save(createTask("Completed " + i, true));
        }

        // When
        List<Task> result = taskRepository.getLatestCompletedTasks();

        // Then
        assertThat(result).hasSize(5); // Should return only 5 tasks max
        assertThat(result).allMatch(Task::getCompleted);
    }

    @Test
    void getLatestCompletedTasks_ShouldReturnEmptyListWhenNoCompletedTasks() {
        // Given - Only incomplete tasks
        taskRepository.save(createTask("Incomplete 1", false));
        taskRepository.save(createTask("Incomplete 2", false));

        // When
        List<Task> result = taskRepository.getLatestCompletedTasks();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findById_ShouldReturnTaskWhenExists() {
        // Given
        Task task = taskRepository.save(createTask("Test Task", false));

        // When
        Task found = taskRepository.findById(task.getId()).orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(task.getId());
        assertThat(found.getTitle()).isEqualTo("Test Task");
    }

    private Task createTask(String title, boolean completed) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Description for " + title);
        task.setCompleted(completed);
        task.setCreatedAt(LocalDateTime.now());
        if (completed) {
            task.setCompletedAt(LocalDateTime.now());
        }
        return task;
    }
}