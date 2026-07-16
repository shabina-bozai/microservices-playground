package com.demo.graphql.simple.controller;

import com.demo.graphql.simple.model.Author;
import com.demo.graphql.simple.model.Book;
import com.demo.graphql.simple.repository.InMemoryRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    private final InMemoryRepository repository;

    public BookController(InMemoryRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public List<Book> books() {
        return repository.findAllBooks();
    }

    @QueryMapping
    public Book book(@Argument Long id) {
        return repository.findBookById(id);
    }

    @QueryMapping
    public List<Author> authors() {
        return repository.findAllAuthors();
    }

    @QueryMapping
    public Author author(@Argument Long id) {
        return repository.findAuthorById(id);
    }

    @MutationMapping
    public Book addBook(@Argument String title, @Argument Long authorId) {
        return repository.addBook(title, authorId);
    }

    @MutationMapping
    public Author addAuthor(@Argument String name) {
        return repository.addAuthor(name);
    }

    @SchemaMapping(typeName = "Book", field = "author")
    public Author bookAuthor(Book book) {
        return repository.findAuthorById(book.getAuthorId());
    }

    @SchemaMapping(typeName = "Author", field = "books")
    public List<Book> authorBooks(Author author) {
        return repository.findAllBooks().stream()
                .filter(b -> b.getAuthorId().equals(author.getId()))
                .toList();
    }
}
