package design.ore.forge.api.config;

/**
 * Constants for module JPA configuration.
 * <p>
 * Use these constants when configuring {@code @EnableJpaRepositories} to avoid
 * hardcoding bean names and ensure consistency across version updates.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * @EnableModuleJpa
 * @EnableJpaRepositories(
 *     basePackages = "com.example.mymodule.repositories",
 *     entityManagerFactoryRef = ModuleJpaConstants.ENTITY_MANAGER_FACTORY_BEAN_NAME,
 *     transactionManagerRef = ModuleJpaConstants.TRANSACTION_MANAGER_BEAN_NAME
 * )
 * public class MyModule implements IForgeModule { }
 * }</pre>
 *
 * @since 0.3.0
 */
public final class ModuleJpaConstants
{
    /**
     * Bean name for the module's EntityManagerFactory.
     * <p>
     * Use this constant in {@code @EnableJpaRepositories(entityManagerFactoryRef = ...)}
     * </p>
     */
    public static final String ENTITY_MANAGER_FACTORY_BEAN_NAME = "moduleEntityManagerFactory";

    /**
     * Bean name for the module's PlatformTransactionManager.
     * <p>
     * Use this constant in {@code @EnableJpaRepositories(transactionManagerRef = ...)}
     * </p>
     */
    public static final String TRANSACTION_MANAGER_BEAN_NAME = "moduleTransactionManager";

    /**
     * Bean name for the module's DataSource.
     * <p>
     * Primarily for internal use, but available if modules need to reference it.
     * </p>
     */
    public static final String DATA_SOURCE_BEAN_NAME = "dataSource";

    // Private constructor to prevent instantiation
    private ModuleJpaConstants()
    {
        throw new AssertionError("ModuleJpaConstants is a utility class and should not be instantiated");
    }
}
