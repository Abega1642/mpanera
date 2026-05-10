package com.mpanera.mpanera.endpoint.rest.controller;

import static com.mpanera.mpanera.service.ProviderServiceIT.generateTestCategory;
import static java.util.UUID.randomUUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mpanera.mpanera.conf.FacadeIT;
import com.mpanera.mpanera.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class CategoryControllerIT extends FacadeIT {

  @Autowired private MockMvc mvc;

  @Autowired private CategoryRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void should_get_correct_category_by_name() throws Exception {
    var category = repository.save(generateTestCategory("CaTeGoRy", "category", "myIcon"));

    mvc.perform(get("/categories/{category-name}", category.getName()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("CaTeGoRy"))
        .andExpect(jsonPath("$.slug").value("category"))
        .andExpect(jsonPath("$.icon").value("myIcon"));
  }

  @Test
  void should_get_404_when_no_category_match_the_name() throws Exception {
    mvc.perform(get("/categories/{category-name}", randomUUID().toString()))
        .andExpect(status().isNotFound());
  }
}
