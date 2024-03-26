package com.todo.demo.service;

import com.todo.demo.handlers.ToDoNotFoundException;
import com.todo.demo.models.Todo;
import com.todo.demo.dto.TodoDto;
import com.todo.demo.repos.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDto> getTodos() {
        List<Todo> result = todoRepository.findAll();
        return result.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private TodoDto mapToDto(Todo todo) {
        TodoDto todoDto = new TodoDto(todo.getId(), todo.getDescription(), todo.isCompleted());
        return todoDto;
    }

    public TodoDto createToDo(TodoDto todoDto) {
        var todo = new Todo(todoDto.description(), todoDto.isCompleted());
        Todo savedToDo = todoRepository.save(todo);
        return mapToDto(savedToDo);
    }


    public TodoDto getTodoById(Integer id) {
        return mapToDto(todoRepository.getReferenceById(id));
    }

    public void deleteToDoById(Integer id) {

        if (!todoRepository.existsById(id)) {
            throw new ToDoNotFoundException( "ToDo not found");
        }
        todoRepository.deleteById(id);
    }
    public TodoDto updateToDo(Integer id, TodoDto toDo) {
        Optional<Todo> existingToDo = todoRepository.findById(id);
        return existingToDo.map(t -> {
                    t.setDescription(toDo.description());
                    t.setCompleted(toDo.isCompleted());
                    return mapToDto(todoRepository.save(t));
                })
                .orElseThrow(() -> new ToDoNotFoundException("ToDo not found"));
    }
}
