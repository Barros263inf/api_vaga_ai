package com.vaga.ai.gs.controller;

import com.vaga.ai.gs.dto.request.JobRequestDTO;
import com.vaga.ai.gs.dto.response.JobResponseDTO;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.service.JobService;
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
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<Page<JobResponseDTO>> list(
            @AuthenticationPrincipal User loggedUser,
            @PageableDefault(size = 10, sort = "savedAt") Pageable pageable
    ) {
        return ResponseEntity.ok(jobService.findAll(loggedUser, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(jobService.findById(id, loggedUser));
    }

    @PostMapping
    public ResponseEntity<JobResponseDTO> create(
            @RequestBody @Valid JobRequestDTO dto,
            @AuthenticationPrincipal User loggedUser,
            UriComponentsBuilder uriBuilder
    ) {
        JobResponseDTO saved = jobService.save(dto, loggedUser);
        URI uri = uriBuilder.path("/api/jobs/{id}").buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User loggedUser
    ) {
        jobService.delete(id, loggedUser);
        return ResponseEntity.noContent().build();
    }
}