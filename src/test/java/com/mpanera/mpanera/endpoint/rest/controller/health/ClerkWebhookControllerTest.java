package com.mpanera.mpanera.endpoint.rest.controller.health;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mpanera.mpanera.config.ClerkConf;
import com.mpanera.mpanera.endpoint.rest.controller.ClerkWebhookController;
import com.mpanera.mpanera.service.ClerkWebhookService;
import com.mpanera.mpanera.service.util.ServletHttpHeaderExtractor;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    value = ClerkWebhookController.class,
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ClerkConf.class))
@AutoConfigureMockMvc(addFilters = false)
class ClerkWebhookControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ClerkWebhookService clerkWebhookService;

  @MockitoBean private ServletHttpHeaderExtractor headerExtractor;

  @MockitoBean private Webhook svixWebhook;

  @Test
  void should_return_200_when_request_is_valid() throws Exception {
    given(headerExtractor.apply(any())).willReturn(HttpHeaders.of(Map.of(), (k, v) -> true));

    mockMvc
        .perform(
            post("/webhooks/clerk")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"user.created\"}"))
        .andExpect(status().isOk());

    verify(clerkWebhookService).handle(anyString(), any(HttpHeaders.class));
  }

  @Test
  void should_return_401_when_signature_verification_fails() throws Exception {
    given(headerExtractor.apply(any())).willReturn(HttpHeaders.of(Map.of(), (k, v) -> true));
    willThrow(WebhookVerificationException.class)
        .given(clerkWebhookService)
        .handle(anyString(), any(HttpHeaders.class));

    mockMvc
        .perform(
            post("/webhooks/clerk")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"user.created\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void should_return_400_when_payload_is_malformed() throws Exception {
    given(headerExtractor.apply(any())).willReturn(HttpHeaders.of(Map.of(), (k, v) -> true));
    willThrow(IOException.class)
        .given(clerkWebhookService)
        .handle(anyString(), any(HttpHeaders.class));

    mockMvc
        .perform(
            post("/webhooks/clerk")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"user.created\"}"))
        .andExpect(status().isBadRequest());
  }
}
