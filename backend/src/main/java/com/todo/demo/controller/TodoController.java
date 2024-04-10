package com.todo.demo.controller;

import com.todo.demo.dto.TodoDto;
import com.todo.demo.handlers.ErrorResponse;
import com.todo.demo.handlers.ToDoNotFoundException;
import com.todo.demo.service.TodoService;

import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/api/v1/todos")
@RestController
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoDto> getTodos() {
        return todoService.getTodos();
    }


    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Integer id) throws ToDoNotFoundException {
        try {
            return ResponseEntity.ok(todoService.getTodoById(id));
        } catch (Exception e) {
            throw new ToDoNotFoundException("ToDo with id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteToDo(@PathVariable Integer id) {
        todoService.deleteToDoById(id);
        return ResponseEntity.noContent().build(); // Success case
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatetoDo(@PathVariable Integer id, @RequestBody TodoDto todoDto) {
        return ResponseEntity.ok().body(todoService.updateToDo(id, todoDto));

    }


    @PostMapping
    public ResponseEntity<TodoDto> addToDo(@RequestBody TodoDto toDo) {
        System.out.println("addToDo called with " + toDo.toString());
        var savedToDo = todoService.createToDo(toDo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedToDo);
    }
}
