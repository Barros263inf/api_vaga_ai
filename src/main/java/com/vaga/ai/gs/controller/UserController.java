package com.vaga.ai.gs.controller;

import com.vaga.ai.gs.dto.request.UserRequestPostDTO;
import com.vaga.ai.gs.dto.request.UserRequestUpdateDTO;
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
    // MUDANÇA 1: Retorna Page<UserResponseDTO> em vez de Page<User>
    public ResponseEntity<Page<UserResponseDTO>> list(
            @PageableDefault(size = 10, sort = "name")
            Pageable pageable
    ) {
        Page<User> page = userService.findAll(pageable);
        // MUDANÇA 2: Converte a entidade para DTO
        return ResponseEntity.ok(page.map(UserResponseDTO::fromEntity));
    }

    @GetMapping("/{id}")
    // MUDANÇA 3: Retorna UserResponseDTO em vez de User
    public ResponseEntity<UserResponseDTO> search(@PathVariable Long id) {
        User user = userService.findById(id);
        // MUDANÇA 4: Converte a entidade para DTO
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @RequestBody @Valid UserRequestPostDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UserResponseDTO userSaved = userService.save(dto);

        URI uri = uriComponentsBuilder.path("api/users/{id}")
                .buildAndExpand(userSaved.id()).toUri();
        return ResponseEntity.created(uri).body(userSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestUpdateDTO newUser
    ) {
        UserResponseDTO userUpdated = userService.update(id, newUser);
        return ResponseEntity.ok(userUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}