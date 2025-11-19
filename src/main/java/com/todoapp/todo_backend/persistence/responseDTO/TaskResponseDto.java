package com.todoapp.todo_backend.persistence.responseDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object representing outbound task responses.
 *
 * <p>Used to expose task information outside the service boundary without
 * leaking internal domain models. Helps maintain separation between
 * persistence structures and externally visible response types.</p>
 */


@Data
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

}
