package com.mpanera.mpanera.endpoint.rest.controller;

import static com.mpanera.mpanera.service.ProviderServiceIT.generateTestCategory;
import static com.mpanera.mpanera.service.ProviderServiceIT.generateTestProvider;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.repository.CategoryRepository;
import com.mpanera.mpanera.repository.ProviderRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

class ProviderControllerIT extends FacadeIT {

  @Autowired private MockMvc mvc;

  @Autowired private ProviderRepository providerRepository;

  @Autowired private CategoryRepository categoryRepository;

  @BeforeEach
  void setUp() {
    providerRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  @Transactional
  void should_get_all_5_top_providers_by_category_correctly() throws Exception {
    var category = categoryRepository.save(generateTestCategory("Plumbing", "plumbing", "wrench"));

    providerRepository.saveAll(
        List.of(
            generateTestProvider(category, 4.9),
            generateTestProvider(category, 4.7),
            generateTestProvider(category, 4.5),
            generateTestProvider(category, 4.3),
            generateTestProvider(category, 4.1),
            generateTestProvider(category, 3.8)));

    mvc.perform(get("/providers/{categoryId}", category.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(5))
        .andExpect(jsonPath("$[0].average_rating").value(4.9))
        .andExpect(jsonPath("$[1].average_rating").value(4.7))
        .andExpect(jsonPath("$[2].average_rating").value(4.5))
        .andExpect(jsonPath("$[3].average_rating").value(4.3))
        .andExpect(jsonPath("$[4].average_rating").value(4.1));
  }
}
