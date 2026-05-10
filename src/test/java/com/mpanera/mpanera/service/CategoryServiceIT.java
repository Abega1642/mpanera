package com.mpanera.mpanera.service;

import static com.mpanera.mpanera.service.ProviderServiceIT.generateTestCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
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

  @Test
  void should_throws_exception_when_category_name_is_null() {
    assertThrows(ConstraintViolationException.class, () -> subject.getCategoryByName(null));
  }

  @Test
  void should_get_all_categories_name() {
    repository.saveAll(
        List.of(
            generateTestCategory("C", "c", "random"),
            generateTestCategory("A", "a", "also-random")));

    var actual = subject.findAllCategoryNames();

    assertFalse(actual.isEmpty());
    assertEquals(2, actual.size());
  }

  @Test
  void should_get_nothing_when_there_is_no_category_at_all() {
    var actual = subject.findAllCategoryNames();

    assertTrue(actual.isEmpty());
  }
}
