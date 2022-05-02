package br.com.kazuo.config.internationalization;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Configuration
public class InternationalizationConfig implements LocaleContextResolver {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        return messageSource;
    }

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        Locale locale = Locale.getDefault();
        List<String> langs = exchange
                .getRequest()
                .getHeaders()
                .get(HttpHeaders.ACCEPT_LANGUAGE);
        if (!isNullOrEmpty(langs) && !isNullOrEmpty(langs.get(0))) {
            try {
                locale = Locale.forLanguageTag(langs.get(0));
            } catch (Exception ex) {
                locale = Locale.getDefault();
            }
        }
        return new SimpleLocaleContext(locale);
    }

    private boolean isNullOrEmpty(List<?> list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    private boolean isNullOrEmpty(String value) {
        return Objects.isNull(value) || value.isEmpty();
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) { }

}
