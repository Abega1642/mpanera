package com.mpanera.mpanera.endpoint.rest.controller.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MessagePost(@NotNull @Valid MUser userRequest, @NotNull @Valid MAI aiResponse) {}
