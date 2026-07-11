package com.api.api_biblioteca.controller;

import com.api.api_biblioteca.domain.Book;
import com.api.api_biblioteca.domain.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.api_biblioteca.exception.ResourceNotFoundException;
import com.api.api_biblioteca.exception.UnauthorizedAccessException;
import com.api.api_biblioteca.exception.GlobalExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/contains/{title}")
    @Operation(summary = "Find books by partial title match")
    @ApiResponse(responseCode = "200", description = "List of books that contain the specified title")
    public ResponseEntity<List<Book>> findByTitleContaining(
            @Parameter(description = "Partial title to search for", required = true, example = "Harry")
            @PathVariable("title") String title) {
        List<Book> books = bookService.findByTitleContaining(title);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron libros con el título " + title);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/exact/{title}")
    @Operation(summary = "Find a book by exact title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Book> findByTitle(
            @Parameter(description = "Exact title of the book", required = true, example = "Harry Potter and the Philosopher's Stone")
            @PathVariable("title") String title) {
        return bookService.findByTitle(title)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el libro con el título " + title));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Find books by author ID")
    @ApiResponse(responseCode = "200", description = "List of books by the specified author")
    public ResponseEntity<List<Book>> findByAuthorId(
            @Parameter(description = "The ID of the author", required = true, example = "1")
            @PathVariable("authorId") int authorId) {
        List<Book> books = bookService.findByAuthorId(authorId);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron libros para el autor con ID " + authorId);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/genre/{genre}")
    @Operation(summary = "Find books by genre")
    @ApiResponse(responseCode = "200", description = "List of books in the specified genre")
    public ResponseEntity<List<Book>> findByGenre(
            @Parameter(description = "The genre of the books", required = true, example = "Fantasy")
            @PathVariable("genre") String genre) {
        List<Book> books = bookService.findByGenre(genre);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron libros del género " + genre);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/available")
    @Operation(summary = "Find books that are available")
    @ApiResponse(responseCode = "200", description = "List of books that are currently available")
    public ResponseEntity<List<Book>> findByAvailableTrue() {
        List<Book> books = bookService.findByAvailableTrue();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/published/after/{date}")
    @Operation(summary = "Find books published after a specific date")
    @ApiResponse(responseCode = "200", description = "List of books published after the specified date")
    public ResponseEntity<List<Book>> findByPublicationDateAfter(
            @Parameter(description = "The date after which books were published", required = true, example = "2020-01-01T00:00:00")
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Book> books = bookService.findByPublicationDateAfter(date);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/published/before/{date}")
    @Operation(summary = "Find books published before a specific date")
    @ApiResponse(responseCode = "200", description = "List of books published before the specified date")
    public ResponseEntity<List<Book>> findByPublicationDateBefore(
            @Parameter(description = "The date before which books were published", required = true, example = "2000-12-31T23:59:59")
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<Book> books = bookService.findByPublicationDateBefore(date);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/save")
    @Operation(summary = "Save a new book")
    @ApiResponse(responseCode = "201", description = "Book successfully created")
    public ResponseEntity<Book> save(@Valid @RequestBody Book book) {
        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{title}")
    @Operation(summary = "Delete a book by title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("title") String title) {
        if (bookService.delete(title)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No se encontró el libro con el título " + title);
        }
    }
}


