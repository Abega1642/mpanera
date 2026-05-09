package com.mpanera.mpanera.service;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.repository.CategoryRepository;
import com.mpanera.mpanera.repository.ProviderRepository;
import com.mpanera.mpanera.repository.model.Category;
import com.mpanera.mpanera.repository.model.Provider;
import com.mpanera.mpanera.repository.model.UserRole;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProviderServiceIT extends FacadeIT {
  @Autowired private ProviderService subject;

  @Autowired private ProviderRepository repository;

  @Autowired private CategoryRepository categoryRepository;

  public static Category generateTestCategory(String name, String slug, String icon) {
    return Category.builder().name(name).slug(slug).icon(icon).build();
  }

  public static Provider generateTestProvider(Category category, double rating) {
    return Provider.builder()
        .clerkId(randomUUID().toString())
        .email(format("provider-%s@example.com", randomUUID()))
        .emailVerified(true)
        .username("testprovider")
        .firstName("John")
        .lastName("Doe")
        .role(UserRole.PROVIDER)
        .onBoardingComplete(true)
        .bio("Experienced professional")
        .averageRating(rating)
        .completedJobsCount(10)
        .verified(true)
        .category(category)
        .build();
  }

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void should_get_all_top_five_provider_in_given_category() {
    var category = categoryRepository.save(generateTestCategory("Plumbing", "plumbing", "wrench"));

    repository.saveAll(
        List.of(
            generateTestProvider(category, 4.9),
            generateTestProvider(category, 4.7),
            generateTestProvider(category, 4.5),
            generateTestProvider(category, 4.3),
            generateTestProvider(category, 4.1),
            generateTestProvider(category, 3.8)));

    var result = subject.getTop5ProvidersByCategoryId(category.getId());

    assertEquals(5, result.size());
    assertEquals(
        List.of(4.9, 4.7, 4.5, 4.3, 4.1), result.stream().map(Provider::getAverageRating).toList());
  }
}
