package com.mpanera.mpanera.service;

import static java.lang.String.format;
import static org.owasp.encoder.Encode.forJava;

import com.mpanera.mpanera.repository.CategoryRepository;
import com.mpanera.mpanera.repository.model.Category;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public Category getCategoryByName(String name) {
    return categoryRepository
        .findByName(name)
        .orElseThrow(
            () -> new EntityNotFoundException(format("Category not found: %s", forJava(name))));
  }
}
