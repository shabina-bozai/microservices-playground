package com.demo.rest.realworld;

import com.demo.rest.realworld.model.Category;
import com.demo.rest.realworld.model.Product;
import com.demo.rest.realworld.repository.CategoryRepository;
import com.demo.rest.realworld.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        Category electronics = categoryRepository.save(new Category("Electronics", "Gadgets and electronic devices"));
        Category clothing = categoryRepository.save(new Category("Clothing", "Apparel and fashion items"));
        Category books = categoryRepository.save(new Category("Books", "Physical and digital books"));

        productRepository.save(new Product("Laptop Pro 15", "High-performance laptop with 15\" display", 1299.99, 25, electronics, "https://placehold.co/300x200?text=Laptop"));
        productRepository.save(new Product("Wireless Headphones", "Noise-cancelling over-ear headphones", 249.99, 60, electronics, "https://placehold.co/300x200?text=Headphones"));
        productRepository.save(new Product("Mechanical Keyboard", "Tactile switches, RGB backlit", 119.99, 40, electronics, "https://placehold.co/300x200?text=Keyboard"));

        productRepository.save(new Product("Classic Denim Jacket", "Timeless denim jacket, all sizes", 79.99, 100, clothing, "https://placehold.co/300x200?text=Jacket"));
        productRepository.save(new Product("Running Sneakers", "Lightweight and breathable, great for running", 89.99, 80, clothing, "https://placehold.co/300x200?text=Sneakers"));
        productRepository.save(new Product("Merino Wool Sweater", "Soft merino wool, multiple colors", 69.99, 55, clothing, "https://placehold.co/300x200?text=Sweater"));

        productRepository.save(new Product("Clean Code", "A handbook of agile software craftsmanship by Robert C. Martin", 34.99, 200, books, "https://placehold.co/300x200?text=Clean+Code"));
        productRepository.save(new Product("The Pragmatic Programmer", "Your journey to mastery, 20th anniversary edition", 39.99, 150, books, "https://placehold.co/300x200?text=Pragmatic"));
        productRepository.save(new Product("Designing Data-Intensive Applications", "The big ideas behind reliable, scalable, and maintainable systems", 49.99, 120, books, "https://placehold.co/300x200?text=DDIA"));
    }
}
