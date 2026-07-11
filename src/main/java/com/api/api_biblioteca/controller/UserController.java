package com.api.api_biblioteca.controller;

import com.api.api_biblioteca.domain.User;
import com.api.api_biblioteca.domain.service.UserService;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> findByEmail(
            @Parameter(description = "The email of the user", required = true, example = "example@mail.com")
            @PathVariable("email") String email) {
        return userService.findByEmail(email)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con correo " + email + " no encontrado."));
    }

    @GetMapping("/contains/{name}")
    @Operation(summary = "Find users whose names contain a specific string")
    @ApiResponse(responseCode = "200", description = "List of users whose names contain the specified string")
    public ResponseEntity<List<User>> findByNameContaining(
            @Parameter(description = "The substring to search for in user names", required = true, example = "John")
            @PathVariable("name") String name) {
        List<User> users = userService.findByNameContaining(name);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron usuarios con el nombre " + name);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> findById(
            @Parameter(description = "The ID of the user", required = true, example = "1")
            @PathVariable("userId") int userId) {
        return userService.findById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado."));
    }

    @GetMapping("/registered-between")
    @Operation(summary = "Get users registered between two dates")
    @ApiResponse(responseCode = "200", description = "List of users registered within the specified date range")
    public ResponseEntity<List<User>> findByRegistrationDateBetween(
            @Parameter(description = "The start date for the range", required = true, example = "2023-01-01T00:00:00")
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "The end date for the range", required = true, example = "2023-12-31T23:59:59")
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<User> users = userService.findByRegistrationDateBetween(start, end);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron usuarios registrados entre las fechas " + start + " y " + end);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/exists/{email}")
    @Operation(summary = "Check if a user exists by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boolean value indicating whether the user exists")
    })
    public ResponseEntity<Boolean> existsByEmail(
            @Parameter(description = "The email to check for existence", required = true, example = "example@mail.com")
            @PathVariable("email") String email) {
        boolean exists = userService.existsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @GetMapping("/count-registered-between")
    @Operation(summary = "Count users registered between two dates")
    @ApiResponse(responseCode = "200", description = "The count of users registered within the specified date range")
    public ResponseEntity<Long> countByRegistrationDateBetween(
            @Parameter(description = "The start date for the range", required = true, example = "2023-01-01T00:00:00")
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "The end date for the range", required = true, example = "2023-12-31T23:59:59")
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        long count = userService.countByRegistrationDateBetween(start, end);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping("/save")
    @Operation(summary = "Save a new user")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    public ResponseEntity<User> save(
            @Parameter(description = "The user object to save")
            @Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "The ID of the user to delete", required = true, example = "1")
            @PathVariable("userId") int userId) {
        if (userService.delete(userId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado.");
        }
    }
}
