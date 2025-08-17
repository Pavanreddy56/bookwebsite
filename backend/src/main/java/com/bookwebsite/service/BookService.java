package com.bookwebsite.service;

import com.bookwebsite.model.Book;
import com.bookwebsite.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Book save(Book b) { return repo.save(b); }
    public List<Book> list() { return repo.findAll(); }
    public Book get(Long id) { return repo.findById(id).orElseThrow(); }
    public void delete(Long id) { repo.deleteById(id); }
}
