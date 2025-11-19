package com.todoapp.todo_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Domain-level exception indicating that a requested resource could not be found.
 *
 * <p>Thrown by the service layer when an operation targets an entity that
 * does not exist in persistence. Handled by {@link GlobalExceptionHandler}
 * to return an appropriate HTTP status and message.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg) { super(msg); }
}
