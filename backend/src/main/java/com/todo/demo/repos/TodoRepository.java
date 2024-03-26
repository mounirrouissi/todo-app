package com.todo.demo.repos;

import com.todo.demo.dto.TodoDto;
import com.todo.demo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Integer> {
}
