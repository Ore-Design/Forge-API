package design.ore.forge.api.annotations;

import design.ore.forge.api.config.ModuleJpaConfiguration;
import design.ore.forge.api.config.ModuleJpaConstants;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables JPA/Hibernate support for a Forge module.
 * <p>
 * This annotation configures all necessary JPA infrastructure including:
 * </p>
 * <ul>
 *   <li>DataSource configuration from module properties (jpa.url, jpa.username, jpa.password)</li>
 *   <li>EntityManagerFactory with automatic package scanning</li>
 *   <li>JPA TransactionManager</li>
 *   <li>OpenEntityManagerInView interceptor for lazy loading</li>
 * </ul>
 * <p>
 * For repository support, add {@code @EnableJpaRepositories} alongside this annotation.
 * Use {@link ModuleJpaConstants} to reference the correct bean names.
 * </p>
 *
 * <h2>Basic Usage:</h2>
 * <pre>{@code
 * @ForgeModule(
 *     value = "my-module",
 *     rootPackage = "com.example.mymodule"
 * )
 * @EnableModuleJpa
 * @EnableJpaRepositories(
 *     basePackages = "com.example.mymodule.repositories",
 *     entityManagerFactoryRef = ModuleJpaConstants.ENTITY_MANAGER_FACTORY_BEAN_NAME,
 *     transactionManagerRef = ModuleJpaConstants.TRANSACTION_MANAGER_BEAN_NAME
 * )
 * public class MyModule implements IForgeModule {
 *     // ...
 * }
 * }</pre>
 *
 * <h2>Required Properties:</h2>
 * <p>
 * Modules must define the following properties in their application.yml:
 * </p>
 * <pre>
 * jpa:
 *   url: jdbc:mysql://localhost:3306/mydb
 *   username: dbuser
 *   password: dbpass
 * </pre>
 *
 * <h2>Advanced Configuration:</h2>
 * <pre>{@code
 * @EnableModuleJpa(
 *     entityPackages = {"com.example.shared.entities"},
 *     ddlAuto = "validate",
 *     showSql = true
 * )
 * @EnableJpaRepositories(
 *     basePackages = {"com.example.repos1", "com.example.repos2"},
 *     entityManagerFactoryRef = ModuleJpaConstants.ENTITY_MANAGER_FACTORY_BEAN_NAME,
 *     transactionManagerRef = ModuleJpaConstants.TRANSACTION_MANAGER_BEAN_NAME
 * )
 * }</pre>
 *
 * @see ModuleJpaConstants
 * @since 0.3.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ModuleJpaConfiguration.class)
public @interface EnableModuleJpa
{
    /**
     * Additional packages to scan for JPA entities beyond the module's root package.
     * <p>
     * The module's rootPackage (from {@link ForgeModule}) and "design.ore.api.core"
     * are automatically included. Use this to add extra packages if needed.
     * </p>
     *
     * @return array of additional package names to scan
     */
    String[] entityPackages() default {};

    /**
     * Hibernate DDL auto mode.
     * <p>
     * Controls how Hibernate manages the database schema:
     * </p>
     * <ul>
     *   <li><b>update</b> (default): Update schema if needed, preserve data</li>
     *   <li><b>create</b>: Drop and recreate schema on startup (destroys data!)</li>
     *   <li><b>create-drop</b>: Create on startup, drop on shutdown</li>
     *   <li><b>validate</b>: Validate schema matches entities, don't modify</li>
     *   <li><b>none</b>: No schema management</li>
     * </ul>
     *
     * @return the DDL auto mode
     */
    String ddlAuto() default "update";

    /**
     * Whether to show SQL statements in logs.
     * <p>
     * When true, Hibernate will log all SQL statements. Useful for debugging
     * but should be disabled in production for performance.
     * </p>
     *
     * @return true to show SQL, false otherwise
     */
    boolean showSql() default false;

    /**
     * Whether to format SQL statements in logs.
     * <p>
     * Only applies when {@link #showSql()} is true. Formats SQL for readability.
     * </p>
     *
     * @return true to format SQL, false otherwise
     */
    boolean formatSql() default false;
}
