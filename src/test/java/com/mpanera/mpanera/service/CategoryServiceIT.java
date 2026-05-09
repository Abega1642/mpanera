package com.mpanera.mpanera.service;

import static com.mpanera.mpanera.service.ProviderServiceIT.generateTestCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryServiceIT extends FacadeIT {
  @Autowired private CategoryService subject;

  @Autowired private CategoryRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void should_get_correct_category_with_name() {
    var subjectCategory =
        repository.save(generateTestCategory("My Cool category", "my-cool-category", "wrench"));

    var actual = subject.getCategoryByName(subjectCategory.getName());

    assertNotNull(actual);
    assertEquals(subjectCategory.getName(), actual.getName());
  }

  @Test
  void should_throw_exception_while_no_category_match() {
    var categoryName = "No category";

    assertThrows(EntityNotFoundException.class, () -> subject.getCategoryByName(categoryName));
  }
}
