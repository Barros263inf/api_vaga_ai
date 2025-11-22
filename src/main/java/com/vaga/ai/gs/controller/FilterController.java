package com.vaga.ai.gs.controller;

import com.vaga.ai.gs.dto.request.FilterRequestPostDTO;
import com.vaga.ai.gs.dto.request.FilterRequestUpdateDTO;
import com.vaga.ai.gs.dto.response.FilterResponseDTO;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/filters")
public class FilterController {

    @Autowired
    private FilterService filterService;

    @GetMapping
    public ResponseEntity<Page<FilterResponseDTO>> list(
            @AuthenticationPrincipal User loggedUser,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(filterService.findAll(loggedUser, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilterResponseDTO> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(filterService.findById(id, loggedUser));
    }

    @PostMapping
    public ResponseEntity<FilterResponseDTO> create(
            @RequestBody @Valid FilterRequestPostDTO dto,
            @AuthenticationPrincipal User loggedUser,
            UriComponentsBuilder uriBuilder
    ) {
        FilterResponseDTO saved = filterService.save(dto, loggedUser);
        URI uri = uriBuilder.path("/api/filters/{id}").buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilterResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid FilterRequestUpdateDTO dto,
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(filterService.update(id, dto, loggedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ) {
        filterService.delete(id, loggedUser);
        return ResponseEntity.noContent().build();
    }
}