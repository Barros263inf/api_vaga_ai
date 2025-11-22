package com.vaga.ai.gs.repository;

import com.vaga.ai.gs.model.Resume;
import com.vaga.ai.gs.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Page<Resume> findAllByUser(User user, Pageable pageable);
}