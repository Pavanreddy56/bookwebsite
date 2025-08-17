package com.bookwebsite.controller;

import com.bookwebsite.model.Book;
import com.bookwebsite.service.BookService;
import com.bookwebsite.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;
    private final FileStorageService fileStorageService;

    public BookController(BookService bookService, FileStorageService fileStorageService) {
        this.bookService = bookService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<Book> list() {
        return bookService.list();
    }

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Book upload(@RequestParam("title") String title,
                       @RequestParam("author") String author,
                       @RequestParam("file") MultipartFile file) throws IOException {
        String stored = fileStorageService.store(file);
        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setFilename(stored);
        return bookService.save(b);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws MalformedURLException {
        Book b = bookService.get(id);
        Path path = fileStorageService.resolve(b.getFilename());
        Resource res = new UrlResource(path.toUri());
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="" + b.getFilename() + """)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(res);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
