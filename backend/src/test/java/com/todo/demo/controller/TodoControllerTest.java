package com.todo.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.demo.dto.TodoDto;
import com.todo.demo.models.Todo;
import com.todo.demo.service.TodoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @MockBean
    TodoService todoService;
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;
//    @Mock
//    private TodoService todoService;
//    @InjectMocks
//    TodoController controller;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }



    @org.junit.jupiter.api.Test
    void getTodos() throws Exception {
        //given
        List<TodoDto> expected = new ArrayList<>(
                List.of(
                        new TodoDto(1, "Pray", false),
                        new TodoDto(2, "Pray", true),
                        new TodoDto(3, "Pray", false)

                ));

        //when
        when(todoService.getTodos()).thenReturn(expected);
        //then

        //   Assertions.assertThat(controller.getTodos()).isEqualTo(expected);
        mvc.perform(get("/api/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].id", is(1)))
        ;


    }

    @Test
    void deleteTodo() throws Exception {
        //given
        int todoIdToDelete = 2;
        //when
        // Optionally configure the mock service if needed
        doNothing().when(todoService).deleteToDoById(todoIdToDelete);
        //then
        mvc.perform(delete("/api/v1/todos/{id}", todoIdToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTodoById_Success() throws Exception {

        //given
        int todoId = 1;
        TodoDto expectedTodo = new TodoDto(todoId, "Task 1", false);

        //when
        when(todoService.getTodoById(todoId)).thenReturn(expectedTodo);

        //then
        mvc.perform(get("/api/v1/todos/{id}", todoId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTodo)));
    }

    @Test
    void addTodo_Success() throws Exception {
        TodoDto newTodo = new TodoDto(null, "New Task", false); //  the `id` is auto-generated
        TodoDto savedTodo = new TodoDto(1, "New Task", false);

        when(todoService.createToDo(newTodo)).thenReturn(savedTodo);

        mvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isCreated());

        // ... other assertions as needed
    }

    @Test
    void updateToDo_Success() throws Exception {
        int todoId = 2;
        TodoDto updatedTodoDto = new TodoDto(todoId, "Updated Task", true);

        when(todoService.updateToDo(todoId, updatedTodoDto)).thenReturn(updatedTodoDto);

        mvc.perform(put("/api/v1/todos/{id}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodoDto)))
                .andExpect(status().isOk()) // Or other assertions about the updated ToDo
                .andExpect(content().json(objectMapper.writeValueAsString(updatedTodoDto)));
    }

}