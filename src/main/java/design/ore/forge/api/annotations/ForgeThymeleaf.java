package design.ore.forge.api.annotations;

import design.ore.forge.api.beans.ForgeThymeleafBeans;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ForgeWeb
@Import(ForgeThymeleafBeans.class)
public @interface ForgeThymeleaf
{
}
