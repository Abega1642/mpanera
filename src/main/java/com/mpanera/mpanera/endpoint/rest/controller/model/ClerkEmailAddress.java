package com.mpanera.mpanera.endpoint.rest.controller.model;

public record ClerkEmailAddress(
    String id, String emailAddress, ClerkEmailVerification verification) {}
