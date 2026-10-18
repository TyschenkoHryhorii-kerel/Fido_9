package com.example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quotes, Long> {
    Page<Quotes> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}