package com.mpanera.mpanera.service;

import com.mpanera.mpanera.repository.ProviderRepository;
import com.mpanera.mpanera.repository.model.Provider;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderService {
  private final ProviderRepository providerRepository;

  public List<Provider> getTop5ProvidersByCategoryId(UUID categoryId) {
    return providerRepository.findTop5ByCategoryIdOrderByAverageRatingDesc(categoryId);
  }
}
