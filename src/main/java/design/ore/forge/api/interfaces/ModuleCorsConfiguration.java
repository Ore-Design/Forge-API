package design.ore.forge.api.interfaces;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) configuration for a module.
 * <p>
 * This class allows modules to specify custom CORS settings that will be
 * applied to their endpoints. CORS is used to control which external domains
 * can access the module's resources from a browser.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * @Override
 * public ModuleCorsConfiguration getCorsConfiguration() {
 *     return ModuleCorsConfiguration.builder()
 *         .allowedOrigins(List.of("https://example.com", "https://app.example.com"))
 *         .allowedMethods(List.of("GET", "POST", "PUT", "DELETE"))
 *         .allowedHeaders(List.of("Authorization", "Content-Type"))
 *         .allowCredentials(true)
 *         .maxAge(3600L)
 *         .build();
 * }
 * }</pre>
 *
 * @since 0.3.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleCorsConfiguration
{
    /**
     * List of allowed origins (domains) that can access this module.
     * <p>
     * Examples: {@code "https://example.com"}, {@code "http://localhost:3000"}
     * </p>
     * <p>
     * Use {@code "*"} to allow all origins (not recommended for production with credentials).
     * </p>
     */
    private List<String> allowedOrigins;

    /**
     * List of allowed HTTP methods for CORS requests.
     * <p>
     * Examples: {@code "GET"}, {@code "POST"}, {@code "PUT"}, {@code "DELETE"}, {@code "OPTIONS"}
     * </p>
     * <p>
     * If null or empty, defaults to {@code ["GET", "POST"]}.
     * </p>
     */
    private List<String> allowedMethods;

    /**
     * List of allowed headers in CORS requests.
     * <p>
     * Examples: {@code "Authorization"}, {@code "Content-Type"}, {@code "X-Requested-With"}
     * </p>
     * <p>
     * If null or empty, defaults to common headers.
     * </p>
     */
    private List<String> allowedHeaders;

    /**
     * List of headers that should be exposed to the client.
     * <p>
     * By default, browsers only expose simple response headers. Use this to
     * expose custom headers to JavaScript clients.
     * </p>
     * <p>
     * Examples: {@code "X-Custom-Header"}, {@code "X-Total-Count"}
     * </p>
     */
    private List<String> exposedHeaders;

    /**
     * Whether credentials (cookies, authorization headers) are allowed.
     * <p>
     * When true, the {@code Access-Control-Allow-Credentials} header is set to true,
     * allowing the browser to send cookies and authorization headers.
     * </p>
     * <p>
     * Note: When credentials are allowed, {@code allowedOrigins} cannot be {@code "*"}.
     * </p>
     */
    @Builder.Default
    private boolean allowCredentials = false;

    /**
     * How long (in seconds) the results of a preflight request can be cached.
     * <p>
     * This sets the {@code Access-Control-Max-Age} header, which tells browsers
     * how long they can cache the CORS preflight response.
     * </p>
     * <p>
     * Example: {@code 3600L} (1 hour)
     * </p>
     * <p>
     * If null, no max age is set.
     * </p>
     */
    private Long maxAge;
}
