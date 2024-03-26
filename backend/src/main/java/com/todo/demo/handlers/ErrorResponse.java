package com.todo.demo.handlers;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message ) {
}
