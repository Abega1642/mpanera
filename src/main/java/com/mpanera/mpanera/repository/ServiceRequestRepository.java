package com.mpanera.mpanera.repository;

import com.mpanera.mpanera.repository.model.ServiceRequest;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {}
