package design.ore.forge.api.authentication;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Utility class for accessing the authenticated user in module code.
 * <p>
 * This provides an alternative to using the @ModuleUser annotation when
 * programmatic access to the user is needed.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * Optional<ForgeUserInfo> user = ForgeUserUtils.getCurrentUser(request);
 * if (user.isPresent()) {
 *     String email = user.get().getEmail();
 * }
 * }
 * </pre>
 */
public class ForgeUserUtils
{
    public static final String FORGE_USER_ATTRIBUTE = "forge.user";

    /**
     * Retrieves the authenticated user from the request.
     *
     * @param request the HTTP servlet request
     * @return an Optional containing the ForgeUserInfo if a user is authenticated, empty otherwise
     */
    public static Optional<ForgeUserInfo> getCurrentUser(HttpServletRequest request)
    {
        if (request == null) return Optional.empty();

        Object userAttribute = request.getAttribute(FORGE_USER_ATTRIBUTE);

        if (userAttribute instanceof ForgeUserInfo userInfo) return Optional.of(userInfo);

        return Optional.empty();
    }

    /**
     * Checks if a user is authenticated in the current request.
     *
     * @param request the HTTP servlet request
     * @return true if a user is authenticated, false otherwise
     */
    public static boolean isAuthenticated(HttpServletRequest request)
    {
        return getCurrentUser(request).isPresent();
    }
}