package design.ore.forge.api.authentication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for injecting the current authenticated user into module controller methods.
 * <p>
 * Usage in module controllers:
 * <pre>
 * {@code
 *     @GetMapping("/example")
 *     public String exampleEndpoint(@ModuleUser Optional<ForgeUserInfo> user)
 *     {
 *         if (user.isPresent())
 *         {
 *             // User is authenticated
 *             return "Hello " + user.get().getFullName();
 *         }
 *         return "Hello Guest";
 *     }
 * }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleUser
{
}