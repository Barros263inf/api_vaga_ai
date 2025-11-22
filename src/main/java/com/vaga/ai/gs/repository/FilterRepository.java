package com.vaga.ai.gs.repository;

import com.vaga.ai.gs.model.Filter;
import com.vaga.ai.gs.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {

    Page<Filter> findAllByUser(User user, Pageable pageable);
}