package com.mpanera.mpanera.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mpanera.mpanera.service.util.ServletHttpHeaderExtractor;
import java.net.http.HttpHeaders;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ServletHttpHeaderExtractorTest {

  private final ServletHttpHeaderExtractor subject = new ServletHttpHeaderExtractor();

  @Test
  void should_map_all_headers_from_request() {
    var request = new MockHttpServletRequest();
    request.addHeader("svix-id", "msg_123");
    request.addHeader("svix-timestamp", "1614265330");
    request.addHeader("svix-signature", "v1,abc==");

    HttpHeaders headers = subject.apply(request);

    assertEquals("msg_123", headers.firstValue("svix-id").orElseThrow());
    assertEquals("1614265330", headers.firstValue("svix-timestamp").orElseThrow());
    assertEquals("v1,abc==", headers.firstValue("svix-signature").orElseThrow());
  }

  @Test
  void should_return_empty_headers_when_request_has_no_headers() {
    var request = new MockHttpServletRequest();

    HttpHeaders headers = subject.apply(request);

    assertTrue(headers.map().isEmpty());
  }

  @Test
  void should_map_all_values_for_multi_value_header() {
    var request = new MockHttpServletRequest();
    request.addHeader("accept", "application/json");
    request.addHeader("accept", "text/plain");

    HttpHeaders headers = subject.apply(request);

    assertTrue(headers.allValues("accept").containsAll(List.of("application/json", "text/plain")));
  }
}
