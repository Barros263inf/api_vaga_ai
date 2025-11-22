package com.vaga.ai.gs.controller;

import com.vaga.ai.gs.dto.request.ResumeRequestDTO;
import com.vaga.ai.gs.dto.response.ResumeResponseDTO;
import com.vaga.ai.gs.model.Resume;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.service.ResumeService;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @GetMapping
    public ResponseEntity<Page<ResumeResponseDTO>> list(
            @Parameter(hidden = true) @AuthenticationPrincipal User loggedUser,
            @PageableDefault(size = 5, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(resumeService.findAll(loggedUser, pageable).map(ResumeResponseDTO::fromEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponseDTO> findById(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(ResumeResponseDTO.fromEntity(resumeService.findById(id, loggedUser)));
    }

    @PostMapping
    public ResponseEntity<ResumeResponseDTO> create(
            @RequestBody @Valid ResumeRequestDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal User loggedUser,
            UriComponentsBuilder uriBuilder
    ) {
        Resume saved = resumeService.save(dto, loggedUser);
        URI uri = uriBuilder.path("/api/resumes/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).body(ResumeResponseDTO.fromEntity(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User loggedUser
    ) {
        resumeService.delete(id, loggedUser);
        return ResponseEntity.noContent().build();
    }
}