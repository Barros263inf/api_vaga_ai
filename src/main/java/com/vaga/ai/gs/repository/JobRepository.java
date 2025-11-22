package com.vaga.ai.gs.repository;

import com.vaga.ai.gs.model.Job;
import com.vaga.ai.gs.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findAllByUser(User user, Pageable pageable);

    // Verifica se o usuário já salvou esta vaga específica (pelo ID externo)
    boolean existsByUserAndJobApiId(User user, String jobApiId);
}