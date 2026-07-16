package com.demo.rest.simple.repository;

import com.demo.rest.simple.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
