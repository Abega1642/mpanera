package com.mpanera.mpanera.endpoint.rest.controller;

import com.mpanera.mpanera.repository.model.Category;
import com.mpanera.mpanera.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping("/{category-name}")
  public Category getCategoryByName(@PathVariable("category-name") String categoryName) {
    return categoryService.getCategoryByName(categoryName);
  }

  @GetMapping("/names")
  public List<String> findAllCategoryNames() {
    return categoryService.findAllCategoryNames();
  }
}
