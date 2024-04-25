package org.userservice.common.controller;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.userservice.common.model.dto.CreateRequestUserDto;
import org.userservice.common.model.dto.UserDto;
import org.userservice.common.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Manage users")
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create a user")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateRequestUserDto user) {
        UserDto createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "User not found with the specified ID"))
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserByID(@PathVariable Long userId) {
        UserDto userById = userService.getUserById(userId);
        return ResponseEntity.ok(userById);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "User not found with the specified ID"))
            )
    })
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDto userDto
    ) {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "User not found with the specified ID"))
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " has been deleted successfully.");
    }

    @Operation(summary = "Search users by birth date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "From date must be before To date"))
            )
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByBirthDateRange(@RequestParam LocalDate fromDate,
                                                         @RequestParam LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("From date must be before To date");
        }
        List<UserDto> users = userService.searchUsersByBirthDateRange(fromDate, toDate);
        return ResponseEntity.ok(users);
    }
}
