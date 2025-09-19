package design.ore.forge.api.annotations;

import design.ore.forge.api.beans.ForgeWebBeans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableWebMvc
@Configuration
@Import(ForgeWebBeans.class)
public @interface ForgeWeb
{
}
