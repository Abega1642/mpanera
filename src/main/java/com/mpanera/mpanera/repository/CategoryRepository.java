package com.mpanera.mpanera.repository;

import com.mpanera.mpanera.repository.model.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
  Optional<Category> findByName(String name);

  @Query("SELECT c.name FROM Category c")
  List<String> findAllNames();
}
