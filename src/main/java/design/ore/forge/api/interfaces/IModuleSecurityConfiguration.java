package design.ore.forge.api.interfaces;

import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.Set;

/**
 * Interface for module-specific security configuration.
 * <p>
 * Modules can implement this interface to provide custom security settings
 * that will be applied at runtime. This provides a type-safe, declarative
 * approach to security configuration as an alternative to annotation fields.
 * </p>
 * <p>
 * Implementations can be Spring beans (enabling dependency injection) or
 * simple classes with a no-argument constructor.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * @Component  // Optional - enables DI
 * public class MyModuleSecurityConfig implements IModuleSecurityConfiguration {
 *
 *     @Autowired
 *     private MyService service;  // Can inject dependencies if @Component
 *
 *     @Override
 *     public SessionCreationPolicy sessionCreationPolicy() {
 *         return SessionCreationPolicy.STATELESS;
 *     }
 *
 *     @Override
 *     public boolean disableCsrf() {
 *         return true;
 *     }
 *
 *     @Override
 *     public boolean acceptJwtTokens() {
 *         return true;
 *     }
 * }
 * }</pre>
 *
 * @since 0.3.0
 */
public interface IModuleSecurityConfiguration
{
    /**
     * Whether authentication is required to access this module's endpoints.
     * <p>
     * When true (default), users must be authenticated to access any endpoint
     * in this module. When false, endpoints are publicly accessible unless
     * protected by method-level security annotations.
     * </p>
     *
     * @return true if authentication required (default), false otherwise
     */
    default boolean requireAuthentication() {
        return true;
    }

    /**
     * The session creation policy for this module.
     * <p>
     * Controls how and when HTTP sessions are created for this module's endpoints.
     * </p>
     * <ul>
     *   <li><b>IF_REQUIRED</b> (default): Create session only when needed</li>
     *   <li><b>ALWAYS</b>: Always create a session</li>
     *   <li><b>NEVER</b>: Never create a session, but use existing ones</li>
     *   <li><b>STATELESS</b>: Never create or use sessions (for REST APIs)</li>
     * </ul>
     *
     * @return SessionCreationPolicy enum value (default: IF_REQUIRED)
     */
    default SessionCreationPolicy sessionCreationPolicy() {
        return SessionCreationPolicy.IF_REQUIRED;
    }

    /**
     * Whether CSRF (Cross-Site Request Forgery) protection should be disabled
     * for this module's endpoints.
     * <p>
     * Set to true for stateless APIs or modules with custom authentication
     * mechanisms that don't rely on cookies. For session-based modules, keep
     * this false (default) to maintain CSRF protection.
     * </p>
     *
     * @return true to disable CSRF, false to keep it enabled (default)
     */
    default boolean disableCsrf() {
        return false;
    }

    /**
     * Whether this module accepts OAuth2 JWT bearer tokens for authentication.
     * <p>
     * When enabled, the module's endpoints will accept both:
     * </p>
     * <ul>
     *   <li>JWT bearer tokens in the Authorization header (for external clients/desktop apps)</li>
     *   <li>Session-based authentication (for web application)</li>
     * </ul>
     * <p>
     * JWT tokens are validated against the configured OAuth2 resource server
     * and user permissions are loaded from Forge.
     * </p>
     *
     * @return true to accept JWT tokens, false for session-only (default)
     */
    default boolean acceptJwtTokens() {
        return false;
    }

    /**
     * Custom IP whitelist configuration for this module.
     * <p>
     * If null (default), uses the global IP whitelist from ModuleIpWhitelistConfig.
     * Modules can override to provide module-specific IP restrictions for
     * unauthenticated access.
     * </p>
     * <p>
     * Only applies when {@link #sessionCreationPolicy()} is STATELESS and
     * {@link #disableCsrf()} is true. Authenticated users can access from any IP.
     * </p>
     *
     * @return Set of allowed IP addresses, or null to use global config (default)
     */
    default Set<String> customIpWhitelist() {
        return null;
    }

    /**
     * Whether to allow unauthenticated access from whitelisted IPs.
     * <p>
     * Only applies when {@link #sessionCreationPolicy()} is STATELESS and
     * {@link #disableCsrf()} is true.
     * </p>
     * <ul>
     *   <li>When true (default): Unauthenticated requests from whitelisted IPs are allowed</li>
     *   <li>When false: All requests require authentication, regardless of IP</li>
     * </ul>
     *
     * @return true to allow whitelisted IP access (default), false to require auth
     */
    default boolean allowWhitelistedIpAccess() {
        return true;
    }

    /**
     * Roles that are allowed to access this module.
     * <p>
     * If empty (default), no role-based restrictions are applied beyond
     * {@link #requireAuthentication()}. If specified, users must have at least
     * one of these roles to access the module's endpoints.
     * </p>
     * <p>
     * Example: {@code new String[]{"ADMIN", "MODULE_USER"}}
     * </p>
     *
     * @return Array of allowed role names, or empty for no role restrictions (default)
     */
    default String[] allowedRoles() {
        return new String[0];
    }

    /**
     * Permissions that are allowed to access this module.
     * <p>
     * If empty (default), no permission-based restrictions are applied beyond
     * {@link #requireAuthentication()}. If specified, users must have at least
     * one of these permissions to access the module's endpoints.
     * </p>
     * <p>
     * Example: {@code new String[]{"MODULE_READ", "MODULE_WRITE"}}
     * </p>
     *
     * @return Array of allowed permission names, or empty for no permission restrictions (default)
     */
    default String[] allowedPermissions() {
        return new String[0];
    }

    /**
     * CORS (Cross-Origin Resource Sharing) configuration for this module.
     * <p>
     * If null (default), no CORS configuration is applied and the module
     * follows the global CORS settings. Modules can override to provide
     * module-specific CORS configuration.
     * </p>
     *
     * @return ModuleCorsConfiguration object, or null for no custom CORS (default)
     */
    default ModuleCorsConfiguration getCorsConfiguration() {
        return null;
    }
}
