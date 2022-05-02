package br.com.kazuo.config.error;

import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface CustomExceptionHandler <T extends Exception> {
    ResponseEntity<ResponseWrapper> handle(T exception, MessageSource messageSource, Locale locale);
}
