package com.mpanera.mpanera.service.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.http.HttpHeaders;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class ServletHttpHeaderExtractor implements Function<HttpServletRequest, HttpHeaders> {
  @Override
  public HttpHeaders apply(HttpServletRequest request) {
    Map<String, List<String>> headerMap = new HashMap<>();
    Collections.list(request.getHeaderNames())
        .forEach(name -> headerMap.put(name, Collections.list(request.getHeaders(name))));
    return HttpHeaders.of(headerMap, (k, v) -> true);
  }
}
