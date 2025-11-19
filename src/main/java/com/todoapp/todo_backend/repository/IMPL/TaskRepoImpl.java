package com.todoapp.todo_backend.repository.IMPL;

import com.todoapp.todo_backend.models.Task;
import com.todoapp.todo_backend.repository.TaskRepository;
import com.todoapp.todo_backend.repository.TaskRepositoryTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TaskRepositoryTemplate} providing custom optimized queries.
 *
 * <p>This class wraps JPA repository operations to generate response DTOs at the
 * database boundary. Useful when results must be pre-shaped before reaching
 * the service layer.</p>
 */
@Service
public class TaskRepoImpl implements TaskRepositoryTemplate {

    private final TaskRepository taskRepository;

    public TaskRepoImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getLatestCompletedTasks() {
        return taskRepository.getLatestCompletedTasks();
    }

    @Override
    public List<Task> getLatestInCompletedTasks() {
        return taskRepository.getLatestInCompletedTasks();
    }

    @Override
    public Optional<Task> findByTaskId(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
