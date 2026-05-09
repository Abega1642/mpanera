package com.mpanera.mpanera.config;

import com.svix.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClerkConf {

  @Bean
  public Webhook svixWebhook(@Value("${clerk.webhook-secret}") String secret) {
    return new Webhook(secret);
  }
}
