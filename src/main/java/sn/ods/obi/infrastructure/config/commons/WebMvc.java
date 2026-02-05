package sn.ods.obi.infrastructure.config.commons;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;


@Configuration
public class WebMvc extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/images/")
                .addResourceLocations("file:/opt/boutik221-fm/images/");
    }
}
