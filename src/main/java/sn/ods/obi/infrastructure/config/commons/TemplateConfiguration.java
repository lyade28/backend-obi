package sn.ods.obi.infrastructure.config.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateConfiguration {

    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.addTemplateResolver(emailTemplateResolver());
        return springTemplateEngine;
    }

    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver configurer = new ClassLoaderTemplateResolver();
        configurer.setPrefix("/templates/");
        configurer.setSuffix(".html");
        configurer.setTemplateMode(TemplateMode.HTML);
        configurer.setCharacterEncoding("UTF-8");
        configurer.setCacheable(false);
        configurer.setOrder(0);
        configurer.setCheckExistence(true);
        return configurer;
    }
}
