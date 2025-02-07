package com.khalil.management.user.controller;

import com.khalil.management.user.dto.UserRequest;
import com.khalil.management.user.dto.UserResponse;
import com.khalil.management.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<Integer> createUser(
            @RequestBody @Valid UserRequest request
    ) {
        return ResponseEntity.ok(service.createUser(request));
    }


    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> findById(
            @PathVariable("user-id") Integer userId
    ) {
        return ResponseEntity.ok(service.findById(userId));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PutMapping("/{user-id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable("user-id") Integer userId,
            @RequestBody @Valid UserRequest request
    ) {
        service.updateUser(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("user-id") Integer userId
    ) {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
