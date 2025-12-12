package design.ore.forge.api.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * Argument resolver for injecting ForgeUserInfo into module controller methods.
 * This resolver extracts the user information that was set by the ModuleNamespaceFilter.
 */
public class ModuleUserArgumentResolver implements HandlerMethodArgumentResolver
{
    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        return parameter.hasParameterAnnotation(ModuleUser.class)
                && parameter.getParameterType().equals(Optional.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
    {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null)
        {
            return Optional.empty();
        }

        Object userAttribute = request.getAttribute(ForgeUserUtils.FORGE_USER_ATTRIBUTE);

        if (userAttribute instanceof ForgeUserInfo userInfo) return Optional.of(userInfo);

        return Optional.empty();
    }
}