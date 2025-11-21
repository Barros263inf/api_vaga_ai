package com.vaga.ai.gs.controller;

import com.vaga.ai.gs.dto.request.UserRequestDTO;
import com.vaga.ai.gs.dto.response.UserResponseDTO;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> list(
            @PageableDefault(size = 10, sort = "name")
            Pageable pageable
    ) {
        Page<User> page = userService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> search(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @RequestBody @Valid UserRequestDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UserResponseDTO userSaved = userService.save(dto);

        URI uri = uriComponentsBuilder.path("api/users/{id}")
                .buildAndExpand(userSaved.id()).toUri();
        return ResponseEntity.created(uri).body(userSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @RequestBody @Valid User newUser
    ) {
        User userUpdated = userService.update(id, newUser);
        return ResponseEntity.ok(userUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
