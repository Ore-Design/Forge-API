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

    boolean requireAuthentication() default true;
}
