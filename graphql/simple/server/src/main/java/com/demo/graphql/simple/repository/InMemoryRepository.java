package com.demo.graphql.simple.repository;

import com.demo.graphql.simple.model.Author;
import com.demo.graphql.simple.model.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryRepository {

    private final List<Author> authors = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();
    private final AtomicLong authorIdSeq = new AtomicLong(4);
    private final AtomicLong bookIdSeq = new AtomicLong(7);

    public InMemoryRepository() {
        // Seed authors
        authors.add(new Author(1L, "J.R.R. Tolkien"));
        authors.add(new Author(2L, "George Orwell"));
        authors.add(new Author(3L, "Frank Herbert"));

        // Seed books
        books.add(new Book(1L, "The Fellowship of the Ring", 1L));
        books.add(new Book(2L, "The Two Towers", 1L));
        books.add(new Book(3L, "The Return of the King", 1L));
        books.add(new Book(4L, "1984", 2L));
        books.add(new Book(5L, "Animal Farm", 2L));
        books.add(new Book(6L, "Dune", 3L));
    }

    public List<Book> findAllBooks() {
        return new ArrayList<>(books);
    }

    public Book findBookById(Long id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Author> findAllAuthors() {
        return new ArrayList<>(authors);
    }

    public Author findAuthorById(Long id) {
        return authors.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public Book addBook(String title, Long authorId) {
        Book book = new Book(bookIdSeq.getAndIncrement(), title, authorId);
        books.add(book);
        return book;
    }

    public Author addAuthor(String name) {
        Author author = new Author(authorIdSeq.getAndIncrement(), name);
        authors.add(author);
        return author;
    }
}
