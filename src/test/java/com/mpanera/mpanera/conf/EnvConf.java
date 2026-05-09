package com.mpanera.mpanera.conf;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

import com.mpanera.mpanera.InfraGenerated;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;

/**
 * Test configuration for application-specific environment variables and properties.
 *
 * <p>This configuration provides a centralized location for injecting custom environment variables
 * and application properties that don't belong to specific infrastructure components (PostgreSQL,
 * RabbitMQ, S3, Email). Use this class for test-specific configuration such as API keys, feature
 * flags, external service endpoints, and other application-level settings.
 *
 * <p><b>Purpose:</b> <br>
 * While infrastructure-specific configurations ({@code PersistenceConf}, {@link RabbitMQConf},
 * {@link BucketConf}, {@link EmailConf}) handle container connection properties, {@code EnvConf}
 * manages general application environment variables that would typically reside in a {@code .env}
 * file.
 *
 * <p><b>Common use cases:</b>
 *
 * <ul>
 *   <li>Third-party API keys (e.g., payment gateways, SMS services)
 *   <li>JWT secret keys and token configurations
 *   <li>Feature flags and environment-specific toggles
 *   <li>External service URLs (e.g., OAuth providers, webhooks)
 *   <li>Application-specific timeouts and thresholds
 *   <li>Encryption keys and security-related properties
 * </ul>
 *
 * <p><b>Integration with FacadeIT:</b> <br>
 * This class is automatically discovered and loaded by {@link
 * FacadeIT#configureProperties(DynamicPropertyRegistry)} through reflection. It is invoked after
 * all infrastructure containers are configured. If this class is not present in the project, {@code
 * FacadeIT} will log a warning and continue without error.
 *
 * <pre>{@code
 * public class MyIntegrationTest extends FacadeIT {
 *   @Value("${app.api.stripe.key}")
 *   private String stripeApiKey;
 *
 *   @Test
 *   void testPaymentIntegration() {
 *     // Uses the API key configured in EnvConf
 *     // Test your payment logic here
 *   }
 * }
 * }</pre>
 *
 * <p><b>Example configuration:</b>
 *
 * <pre>{@code
 * @Override
 * public void configureProperties(DynamicPropertyRegistry registry) {
 *   // API Keys
 *   registry.add("app.api.stripe.key", () -> "sk_test_123");
 *   registry.add("app.api.sendgrid.key", () -> "SG.test.456");
 *
 *   // JWT Configuration
 *   registry.add("app.jwt.secret", () -> "test-secret-key");
 *   registry.add("app.jwt.expiration", () -> "3600000");
 *
 *   // Feature Flags
 *   registry.add("app.feature.new-ui.enabled", () -> "true");
 *
 *   // External Services
 *   registry.add("app.oauth.google.client-id", () -> "test-client-id");
 *   registry.add("app.webhook.callback-url", () -> "http://localhost:8080/webhook");
 * }
 * }</pre>
 *
 * <p><b>Important notes:</b>
 *
 * <ul>
 *   <li>This class is optional; {@code FacadeIT} will work without it
 *   <li>Properties are registered dynamically and override static test properties
 *   <li>Use test-safe values; avoid real production credentials
 *   <li>All properties are scoped to the test JVM lifecycle
 *   <li>Properties are available to all Spring components during test execution
 * </ul>
 *
 * <p><b>Security considerations:</b> <br>
 * Never commit real API keys or production credentials. Use placeholder values suitable for
 * testing. For sensitive integration tests requiring real credentials, consider using environment
 * variables or a secrets management solution.
 *
 * @see FacadeIT
 * @see DynamicPropertyRegistry
 */
@InfraGenerated
@TestConfiguration
public class EnvConf {
  public static final UUID JWKS_URI = randomUUID();
  public static final String CLERK_WEBHOOK_SECRET =
      format(
          "whsec_%S",
          Base64.getEncoder()
              .encodeToString(randomUUID().toString().getBytes(StandardCharsets.UTF_8)));

  public void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("clerk.jwks-uri", () -> JWKS_URI);
    registry.add("clerk.webhook-secret", () -> CLERK_WEBHOOK_SECRET);
  }
}
