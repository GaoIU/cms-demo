package com.gaoc.core.config;

import com.gaoc.core.properties.BaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@RequiredArgsConstructor
@SpringBootConfiguration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    private static final String[] STATIC_RESOURCE = {"classpath:/META-INF/resources/",
            "classpath:/resources/", "classpath:/static/", "classpath:/public/"};

    private final BaseProperties baseProperties;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(STATIC_RESOURCE);
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        if (baseProperties.getEnableCors()) {
            registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*")
                    .allowCredentials(true).maxAge(3600);
        }
        super.addCorsMappings(registry);
    }

}
