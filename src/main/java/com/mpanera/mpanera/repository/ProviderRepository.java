package com.mpanera.mpanera.repository;

import com.mpanera.mpanera.repository.model.Provider;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, UUID> {
  List<Provider> findAllByCategoryId(UUID categoryId);

  List<Provider> findTop5ByCategoryIdOrderByAverageRatingDesc(UUID categoryId);
}
