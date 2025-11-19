package com.todoapp.todo_backend.persistence.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * Data Transfer Object representing inbound task creation requests.
 *
 * <p>This DTO is used by the presentation layer to validate and deserialize
 * client input before the data reaches the service layer. Validation
 * constraints ensure integrity and guard business operations from invalid
 * input.</p>
 */


@Data
public class TaskRequestDto {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title max 200 chars")
    private String title;

    @Size(max = 2000, message = "Description max 2000 chars")
    private String description;

}
