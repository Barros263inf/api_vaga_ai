package com.vaga.ai.gs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CURRICULOS")
public class Resume implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Lob // Indica que é um campo grande (CLOB no Oracle)
    @Column(name = "extracted_text", columnDefinition = "CLOB")
    private String extractedText;

    @Lob // Indica que é um campo grande (CLOB no Oracle)
    @Column(name = "extracted_skills", columnDefinition = "CLOB")
    private String extractedSkills; // JSON salvo como String

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}