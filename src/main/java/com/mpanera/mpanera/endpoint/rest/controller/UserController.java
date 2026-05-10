package com.mpanera.mpanera.endpoint.rest.controller;

import com.mpanera.mpanera.endpoint.rest.controller.model.UserUpdateRequest;
import com.mpanera.mpanera.repository.model.User;
import com.mpanera.mpanera.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/{id}")
  public User findUserById(@PathVariable UUID id) {
    return userService.findById(id);
  }

  @PostMapping("/update/{id}")
  public User updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest user) {
    return userService.update(id, user);
  }
}
