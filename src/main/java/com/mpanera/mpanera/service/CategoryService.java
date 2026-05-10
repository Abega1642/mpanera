package com.mpanera.mpanera.service;

import static java.lang.String.format;
import static org.owasp.encoder.Encode.forJava;

import com.mpanera.mpanera.repository.CategoryRepository;
import com.mpanera.mpanera.repository.model.Category;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public Category getCategoryByName(@NotNull String name) {
    log.info("Fetching category with name={}", forJava(name));

    return categoryRepository
        .findByName(name)
        .orElseThrow(
            () -> new EntityNotFoundException(format("Category not found: %s", forJava(name))));
  }

  public List<String> findAllCategoryNames() {
    var results = categoryRepository.findAllNames();

    if (results.isEmpty()) log.warn("No categories found");

    return results;
  }
}
