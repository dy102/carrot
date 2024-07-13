package app.feedback.auth;

import app.feedback.auth.dto.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static app.feedback.auth.AuthController.AUTHORIZATION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;

@Component
@RequiredArgsConstructor
public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AutoLoginManager autoLoginManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final boolean hasAnnotation = parameter.hasParameterAnnotation(Authorized.class);
        final boolean isValidType = parameter.getParameterType() == Authentication.class;

        return hasAnnotation && isValidType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) {
        Object authentication = webRequest.getAttribute(AUTHORIZATION, SCOPE_SESSION);
        if (authentication != null) {
            return authentication;
        }

        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final Authentication autoLoggedIn = autoLoginManager.getAuthentication(request);

        request.getSession().setAttribute(AUTHORIZATION, autoLoggedIn);
        return autoLoggedIn;
    }
}
