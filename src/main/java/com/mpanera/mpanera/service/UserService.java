package com.mpanera.mpanera.service;

import static java.lang.String.format;
import static org.owasp.encoder.Encode.forJava;

import com.mpanera.mpanera.endpoint.rest.controller.model.UserUpdateRequest;
import com.mpanera.mpanera.repository.UserRepository;
import com.mpanera.mpanera.repository.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User findById(@NotNull UUID id) {
    log.info("Fetching user with id={}", id);

    return userRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(format("Cannot find user with id=%s", id)));
  }

  public User findByClerkId(@NotNull String clerkId) {
    log.info("Fetching user with clerkId={}", forJava(clerkId));

    return userRepository
        .findByClerkId(clerkId)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    format("Cannot find user with clerkId=%s", forJava(clerkId))));
  }

  public User update(@NotNull UUID id, @NotNull UserUpdateRequest updateRequest) {
    User subjectUser = findById(id);
    log.info("Perform update on user with id={}", id);

    subjectUser.setDistrict(updateRequest.district());
    subjectUser.setCity(updateRequest.city());
    subjectUser.setOnBoardingComplete(updateRequest.onBoardingComplete());

    return userRepository.save(subjectUser);
  }
}
