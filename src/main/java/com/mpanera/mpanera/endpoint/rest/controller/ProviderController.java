package com.mpanera.mpanera.endpoint.rest.controller;

import com.mpanera.mpanera.repository.model.Provider;
import com.mpanera.mpanera.service.ProviderService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderController {

  private final ProviderService providerService;

  @GetMapping("/{categoryId}")
  public List<Provider> getTop5ProvidersByCategoryId(@PathVariable UUID categoryId) {
    return providerService.getTop5ProvidersByCategoryId(categoryId);
  }
}
