package com.demo.rest.realworld.repository;

import com.demo.rest.realworld.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
