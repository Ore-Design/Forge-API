package design.ore.forge.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import design.ore.forge.api.interfaces.IForgeModule;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * An annotation indicating that the annotated
 * class is to be registered as a module.
 * <p>
 * The annotated class must implement {@link IForgeModule}.
 * <p>
 * Single Java archives can contain more than
 * one of this annotation at a time as long
 * as each module ID is unique.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ForgeModule
{
    /**
     * The unique ID of the module. If two
     * modules of the same ID are found,
     * only the first will be loaded.
     *
     * @return the unique ID of the module
     */
    @AliasFor(annotation = Component.class)
    String value();

    /**
     * The display name of the module.
     * Multiple modules can have the
     * same display name, though this
     * is unadvisable.
     *
     * @return the display name of the module
     */
    String name();

    /**
     * The version of the module. This should
     * be incremented with every update/release.
     *
     * @return the module version
     */
    String version();

    String rootPackage();

    String iconPath() default "";

    /**
     * Optional: Class implementing {@link design.ore.forge.api.interfaces.IModuleSecurityConfiguration}.
     * <p>
     * If provided, security configuration will be loaded from this class
     * instead of the deprecated annotation fields. The class must:
     * </p>
     * <ul>
     *   <li>Implement {@link design.ore.forge.api.interfaces.IModuleSecurityConfiguration}</li>
     *   <li>Have a no-argument constructor OR be a Spring bean (for dependency injection)</li>
     * </ul>
     * <p>
     * This is the <b>recommended</b> approach for configuring module security.
     * </p>
     * <p>
     * Example: {@code securityConfigClass = MyModuleSecurityConfig.class}
     * </p>
     *
     * @return the security configuration class, or IModuleSecurityConfiguration.class if not specified
     * @since 0.3.0
     */
    Class<? extends design.ore.forge.api.interfaces.IModuleSecurityConfiguration> securityConfigClass()
        default design.ore.forge.api.interfaces.IModuleSecurityConfiguration.class;

    /**
     * Whether authentication is required to access this module's endpoints.
     *
     * @return true if authentication required, false otherwise
     * @deprecated Use {@link #securityConfigClass()} instead. This field will be removed in a future version.
     */
    @Deprecated(since = "0.3.0")
    boolean requireAuthentication() default true;

    /**
     * The session creation policy for this module.
     * Options: "ALWAYS", "IF_REQUIRED", "NEVER", "STATELESS"
     * Default: "IF_REQUIRED" (inherits from main security chain)
     *
     * @return the session creation policy
     * @deprecated Use {@link #securityConfigClass()} instead. This field will be removed in a future version.
     */
    @Deprecated(since = "0.3.0")
    String sessionCreationPolicy() default "IF_REQUIRED";

    /**
     * Whether to disable CSRF protection for this module's paths.
     * Set to true for modules that don't need CSRF protection
     * (e.g., stateless APIs, modules with custom authentication).
     *
     * @return true to disable CSRF, false to keep it enabled
     * @deprecated Use {@link #securityConfigClass()} instead. This field will be removed in a future version.
     */
    @Deprecated(since = "0.3.0")
    boolean disableCsrf() default false;

    /**
     * Whether this module accepts OAuth2 JWT bearer tokens for authentication.
     * When enabled, the module's endpoints will accept both:
     * - JWT bearer tokens in the Authorization header (for external clients/desktop apps)
     * - Session-based authentication (for web application)
     *
     * The JWT tokens are validated against the configured OAuth2 resource server
     * (e.g., Microsoft identity platform) and user permissions are loaded from Forge.
     *
     * @return true to accept JWT tokens, false for session-only authentication
     * @deprecated Use {@link #securityConfigClass()} instead. This field will be removed in a future version.
     */
    @Deprecated(since = "0.3.0")
    boolean acceptJwtTokens() default false;
}
