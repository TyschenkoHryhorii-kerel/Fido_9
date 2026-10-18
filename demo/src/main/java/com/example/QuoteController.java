package com.example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

    private final QuoteRepository quoteRepository;

    public QuoteController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @PostMapping
    public ResponseEntity <Quotes> createQuote(@Valid @RequestBody Quotes quote) {
        Quotes saved = quoteRepository.save(quote);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quotes> getQuoteById(@PathVariable Long id) {
        return quoteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<Quotes> getAllQuotes(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        if (author != null && !author.isBlank()) {
            return quoteRepository.findByAuthorContainingIgnoreCase(author, pageable);
        }
        return quoteRepository.findAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quotes> updateQuote(@PathVariable Long id, @Valid @RequestBody Quotes updatedQuote) {
        return quoteRepository.findById(id)
                .map(existingQuote -> {
                    existingQuote.setAuthor(updatedQuote.getAuthor());
                    existingQuote.setText(updatedQuote.getText());
                    Quotes saved = quoteRepository.save(existingQuote);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long id) {
        if (quoteRepository.existsById(id)) {
            quoteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}