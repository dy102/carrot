package app.feedback.common.config;

import app.feedback.auth.AuthorizationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    private final AuthorizationArgumentResolver resolver;

    public WebConfigurer(final AuthorizationArgumentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(resolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userDir = System.getProperty("user.dir");
        if (userDir.contains("/build/libs")) {
            userDir = userDir.replace("/build/libs", "");
        } else if (userDir.contains("\\build\\libs")) {
            userDir = userDir.replace("\\build\\libs", "");
        }
        String imagePath = "file:///" + userDir + "/image/";

        registry.addResourceHandler("/image/**")
                .addResourceLocations(imagePath);
    }
}
