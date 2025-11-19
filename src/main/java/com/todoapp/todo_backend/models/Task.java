package com.todoapp.todo_backend.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity representing a Task in the system.
 *
 * <p>Defines the persistent data model for a task, including metadata such as
 * title, description, completion state, and creation timestamp. Mapped to a
 * relational database table using Jakarta Persistence annotations.</p>
 */

@Entity
@Table(name = "task", indexes = {
        @Index(name = "idx_task_created_at", columnList = "createdAt DESC"),
        @Index(name = "idx_task_completed_at", columnList = "completedAt DESC"),
        @Index(name = "idx_task_completed_status", columnList = "completed, createdAt DESC"),
        @Index(name = "idx_task_id", columnList = "id")
})
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true, updatable = true)
    private LocalDateTime completedAt;
}