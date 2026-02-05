package sn.ods.obi.infrastructure.config.utils.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
@RequiredArgsConstructor
public class I18nTranslate {
   private final ResourceBundleMessageSource messageSource;

    public String toTranslate(String msg) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msg, null, locale);
    }
}
