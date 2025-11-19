package com.todoapp.todo_backend.repository;

import com.todoapp.todo_backend.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.completed = true ORDER BY t.completedAt  DESC LIMIT 5")
    List<Task> getLatestCompletedTasks();

    @Query("SELECT t FROM Task t WHERE t.completed = false ORDER BY t.createdAt  DESC LIMIT 5")
    List<Task> getLatestInCompletedTasks();
}
