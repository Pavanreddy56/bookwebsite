package com.bookwebsite.repository;

import com.bookwebsite.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> { }
