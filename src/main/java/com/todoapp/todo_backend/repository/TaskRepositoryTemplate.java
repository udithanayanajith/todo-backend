package com.todoapp.todo_backend.repository;

import com.todoapp.todo_backend.models.Task;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Template interface defining custom task retrieval operations beyond standard JPA behavior.
 *
 * <p>Encapsulates optimized or handcrafted database queries, ensuring that the
 * service layer remains independent of specific query mechanisms.</p>
 */
public interface TaskRepositoryTemplate {

    List<Task> getLatestCompletedTasks();
    List<Task> getLatestInCompletedTasks();

    Optional<Task> findByTaskId(Long id);

    Task save(Task task);
}
