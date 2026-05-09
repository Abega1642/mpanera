package com.mpanera.mpanera.repository;

import com.mpanera.mpanera.repository.model.ServiceRequestPhoto;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestPhotoRepository extends JpaRepository<ServiceRequestPhoto, UUID> {}
