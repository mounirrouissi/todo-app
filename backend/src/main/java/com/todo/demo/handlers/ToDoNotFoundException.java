package com.todo.demo.handlers;

public class ToDoNotFoundException extends RuntimeException {
    public ToDoNotFoundException(String s) {
        super(s);
    }
}
