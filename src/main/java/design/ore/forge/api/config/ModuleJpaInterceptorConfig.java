package design.ore.forge.api.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures the OpenEntityManagerInView interceptor for JPA modules.
 * <p>
 * This interceptor keeps the JPA EntityManager open for the entire duration
 * of a web request, allowing lazy loading of entities in controllers and views.
 * </p>
 * <p>
 * Without this, accessing lazy-loaded fields outside of a transaction would
 * throw {@code LazyInitializationException}.
 * </p>
 * <p>
 * This class is automatically configured by {@link ModuleJpaConfiguration}
 * when a module uses {@link design.ore.forge.api.annotations.EnableModuleJpa}.
 * </p>
 *
 * @since 0.3.0
 */
@RequiredArgsConstructor
public class ModuleJpaInterceptorConfig implements WebMvcConfigurer
{
    private final EntityManagerFactory emf;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
        interceptor.setEntityManagerFactory(emf);
        registry.addWebRequestInterceptor(interceptor);
    }
}
