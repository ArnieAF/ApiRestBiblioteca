package com.api.api_biblioteca.controller;

import com.api.api_biblioteca.domain.Author;
import com.api.api_biblioteca.domain.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.api.api_biblioteca.exception.ResourceNotFoundException;
import com.api.api_biblioteca.exception.UnauthorizedAccessException;
import com.api.api_biblioteca.exception.GlobalExceptionHandler;
import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/all")
    @Operation(summary = "Get all authors")
    @ApiResponse(responseCode = "200", description = "List of all authors")
    public ResponseEntity<List<Author>> getAll() {
        List<Author> authors = authorService.getAll();
        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron autores en la base de datos.");
        }
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/contains/{name}")
    @Operation(summary = "Find authors by partial name match")
    @ApiResponse(responseCode = "200", description = "List of authors whose names contain the specified string")
    public ResponseEntity<List<Author>> findByNameContaining(@PathVariable("name") String name) {
        List<Author> authors = authorService.findByNameContaining(name);
        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron autores con el nombre " + name);
        }
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/exact/{name}")
    @Operation(summary = "Find an author by exact name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Author> findByName(@PathVariable("name") String name) {
        Optional<Author> author = authorService.findByName(name);
        if (author.isEmpty()) {
            throw new ResourceNotFoundException("El autor con el nombre " + name + " no fue encontrado.");
        }
        return new ResponseEntity<>(author.get(), HttpStatus.OK);
    }

    @GetMapping("/{nationality}")
    @Operation(summary = "Find authors by nationality")
    @ApiResponse(responseCode = "200", description = "List of authors with the specified nationality")
    public ResponseEntity<List<Author>> findByNationality(@PathVariable("nationality") String nationality) {
        List<Author> authors = authorService.findByNationality(nationality);
        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron autores de nacionalidad " + nationality);
        }
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/allNameAsc")
    @Operation(summary = "Get all authors ordered by name ascending")
    @ApiResponse(responseCode = "200", description = "List of authors ordered by name in ascending order")
    public ResponseEntity<List<Author>> findAllByOrderByNameAsc() {
        List<Author> authors = authorService.findAllByOrderByNameAsc();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/allNameDesc")
    @Operation(summary = "Get all authors ordered by name descending")
    @ApiResponse(responseCode = "200", description = "List of authors ordered by name in descending order")
    public ResponseEntity<List<Author>> findAllByOrderByNameDesc() {
        List<Author> authors = authorService.findAllByOrderByNameDesc();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/count/{nationality}")
    @Operation(summary = "Count authors by nationality")
    @ApiResponse(responseCode = "200", description = "The count of authors with the specified nationality")
    public ResponseEntity<Long> countByNationality(@PathVariable("nationality") String nationality) {
        long count = authorService.countByNationality(nationality);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping("/save")
    @Operation(summary = "Save a new author")
    @ApiResponse(responseCode = "201", description = "Author successfully created")
    public ResponseEntity<Author> save(@Valid @RequestBody Author author) {
        Author savedAuthor = authorService.save(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{name}")
    @Operation(summary = "Delete an author by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Void> delete(@PathVariable("name") String name) {
        if (authorService.delete(name)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("El autor con el nombre " + name + " no fue encontrado.");
        }
    }
}

