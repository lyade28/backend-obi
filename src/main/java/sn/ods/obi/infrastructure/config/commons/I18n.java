package sn.ods.obi.infrastructure.config.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@Configuration
public class I18n extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    final List<Locale> locales = Arrays.asList(
            Locale.ENGLISH,
            Locale.FRANCE,
            Locale.FRENCH);
    @Override
    public @NotNull Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), locales);
    }
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("i18n/messages");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }
}
