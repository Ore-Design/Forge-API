package design.ore.forge.api.config;

import com.mysql.cj.jdbc.Driver;
import design.ore.forge.api.annotations.EnableModuleJpa;
import design.ore.forge.api.annotations.ForgeModule;
import design.ore.forge.api.interfaces.IForgeModule;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Automatic JPA configuration for Forge modules.
 * <p>
 * This configuration is imported via {@link EnableModuleJpa} and automatically
 * configures all necessary JPA infrastructure for a module.
 * </p>
 * <p>
 * The configuration creates:
 * </p>
 * <ul>
 *   <li>DataSource from module properties (jpa.url, jpa.username, jpa.password)</li>
 *   <li>EntityManagerFactory with automatic entity scanning</li>
 *   <li>TransactionManager for @Transactional support</li>
 *   <li>OpenEntityManagerInView interceptor for lazy loading</li>
 * </ul>
 * <p>
 * For repository support, add {@code @EnableJpaRepositories} to your module class:
 * </p>
 * <pre>{@code
 * @ForgeModule(...)
 * @EnableModuleJpa
 * @EnableJpaRepositories(
 *     basePackages = "com.example.mymodule.repositories",
 *     entityManagerFactoryRef = "moduleEntityManagerFactory",
 *     transactionManagerRef = "moduleTransactionManager"
 * )
 * public class MyModule implements IForgeModule { }
 * }</pre>
 *
 * @since 0.3.0
 */
@Configuration
@ConditionalOnProperty(name = "jpa.url")
public class ModuleJpaConfiguration implements ImportAware
{
    private AnnotationAttributes enableModuleJpa;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata)
    {
        this.enableModuleJpa = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableModuleJpa.class.getName()));
    }

    /**
     * Creates a DataSource for the module using properties from application.yml.
     * <p>
     * Required properties:
     * </p>
     * <ul>
     *   <li>jpa.url - JDBC connection URL</li>
     *   <li>jpa.username - Database username</li>
     *   <li>jpa.password - Database password</li>
     * </ul>
     */
    @Bean
    public DataSource dataSource(
        @Value("${jpa.url}") String jdbcUrl,
        @Value("${jpa.username}") String username,
        @Value("${jpa.password}") String password)
    {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(Driver.class.getName());
        ds.setUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    /**
     * Creates the EntityManagerFactory with automatic package scanning.
     * <p>
     * Automatically scans:
     * </p>
     * <ul>
     *   <li>The module's root package (from @ForgeModule)</li>
     *   <li>design.ore.api.core (for shared entities)</li>
     *   <li>Any additional packages specified in @EnableModuleJpa</li>
     * </ul>
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean moduleEntityManagerFactory(DataSource dataSource, ApplicationContext applicationContext)
    {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);

        // Automatically determine packages to scan based on module configuration
        String[] packages = determinePackagesToScan(applicationContext);
        emf.setPackagesToScan(packages);

        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Configure Hibernate properties from @EnableModuleJpa
        Map<String, Object> jpaProperties = new HashMap<>();

        String ddlAuto = enableModuleJpa != null ?
            enableModuleJpa.getString("ddlAuto") : "update";
        jpaProperties.put("hibernate.hbm2ddl.auto", ddlAuto);

        if (enableModuleJpa != null)
        {
            boolean showSql = enableModuleJpa.getBoolean("showSql");
            boolean formatSql = enableModuleJpa.getBoolean("formatSql");

            if (showSql)
            {
                jpaProperties.put("hibernate.show_sql", "true");
                if (formatSql)
                {
                    jpaProperties.put("hibernate.format_sql", "true");
                }
            }
        }

        emf.setJpaPropertyMap(jpaProperties);
        return emf;
    }

    /**
     * Creates the JPA transaction manager.
     */
    @Bean
    public PlatformTransactionManager moduleTransactionManager(EntityManagerFactory emf)
    {
        return new JpaTransactionManager(emf);
    }

    /**
     * Creates the JPA interceptor configuration for OpenEntityManagerInView.
     * <p>
     * This enables lazy loading of JPA entities in the view layer (controllers).
     * </p>
     */
    @Bean
    public ModuleJpaInterceptorConfig jpaInterceptorConfig(EntityManagerFactory emf)
    {
        return new ModuleJpaInterceptorConfig(emf);
    }

    /**
     * Automatically determines which packages to scan for JPA entities.
     * <p>
     * Strategy:
     * </p>
     * <ol>
     *   <li>Search the Spring context for the module's @ForgeModule annotation</li>
     *   <li>Extract the rootPackage from @ForgeModule</li>
     *   <li>Add "design.ore.api.core" for shared entities</li>
     *   <li>Add any additional packages from @EnableModuleJpa.entityPackages()</li>
     * </ol>
     *
     * @param ctx The Spring application context
     * @return Array of package names to scan
     */
    private String[] determinePackagesToScan(ApplicationContext ctx)
    {
        List<String> packages = new ArrayList<>();

        // Always include the API core package
        packages.add("design.ore.api.core");

        // Try to find the @ForgeModule annotation to get the module's root package
        String[] beanNames = ctx.getBeanDefinitionNames();
        for (String beanName : beanNames)
        {
            try
            {
                Object bean = ctx.getBean(beanName);
                Class<?> beanClass = bean.getClass();

                // Check if this bean is the module (implements IForgeModule and has @ForgeModule)
                if (IForgeModule.class.isAssignableFrom(beanClass))
                {
                    ForgeModule annotation = beanClass.getAnnotation(ForgeModule.class);
                    if (annotation != null)
                    {
                        // Found the module! Use its root package
                        String rootPackage = annotation.rootPackage();
                        if (rootPackage != null && !rootPackage.isEmpty())
                        {
                            packages.add(rootPackage);
                        }
                        break;
                    }
                }
            }
            catch (Exception ignored)
            {
                // Bean might not be instantiable yet, skip it
            }
        }

        // Add any custom packages from @EnableModuleJpa
        if (enableModuleJpa != null)
        {
            String[] customPackages = enableModuleJpa.getStringArray("entityPackages");
            if (customPackages.length > 0)
            {
                packages.addAll(Arrays.asList(customPackages));
            }
        }

        return packages.toArray(new String[0]);
    }
}
