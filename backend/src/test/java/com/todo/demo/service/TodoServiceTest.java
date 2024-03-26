package com.todo.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import com.todo.demo.dto.TodoDto;
import com.todo.demo.handlers.ToDoNotFoundException;
import com.todo.demo.models.Todo;
import com.todo.demo.repos.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTodos_ShouldReturnListOfTodoDtos() {
        // Arrange
        List<Todo> todos = Arrays.asList(new Todo("Task 1",false), new Todo("Task 2",false));
        when(todoRepository.findAll()).thenReturn(todos);

        // Act
        List<TodoDto> result = todoService.getTodos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).description());
        assertEquals("Task 2", result.get(1).description());
    }


    @Test
    void createToDo_Success() {
        TodoDto newTodoDto = new TodoDto(null, "New Task", false); // id is  auto-generated
        Todo savedTodo = new Todo(1, "New Task", false); // With the generated id

        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        TodoDto createdTodo = todoService.createToDo(newTodoDto);

        assertEquals("New Task", createdTodo.description());
        assertEquals(1, createdTodo.id());
    }

    @Test
    void updateToDo_Success() {

        //given
        int todoId = 2;
        TodoDto updatedTodoDto = new TodoDto(todoId, "Updated Task", true);
        Todo existingTodo = new Todo(todoId, "Old Task", false);
        //when
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo); // Return the updated entity
        //then
        TodoDto result = todoService.updateToDo(todoId, updatedTodoDto);

        assertEquals("Updated Task", result.description());
        assertEquals(true, result.isCompleted());
    }

    @Test
    void deleteToDoById_Success() {
        int todoId = 3;
        when(todoRepository.existsById(todoId)).thenReturn(true);
        doNothing().when(todoRepository).deleteById(todoId);

        todoService.deleteToDoById(todoId);

        verify(todoRepository,times(1)).deleteById(todoId);
    }

    @Test
    void deleteToDoById_NotFound() {
        int todoId = 7;
        when(todoRepository.existsById(todoId)).thenReturn(false);

        assertThrows(ToDoNotFoundException.class, () -> {
            todoService.deleteToDoById(todoId);
        });

        verify(todoRepository, never()).deleteById(anyInt());
    }


}
