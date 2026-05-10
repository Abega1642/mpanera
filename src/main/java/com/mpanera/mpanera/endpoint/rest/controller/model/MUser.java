package com.mpanera.mpanera.endpoint.rest.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MUser(@NotNull String clerkId, @NotNull @NotBlank String message) {}
