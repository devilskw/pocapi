package br.com.kazuo.config.error;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import io.github.resilience4j.bulkhead.BulkheadFullException;

public class BulkheadFullExceptionHandler {

	public ResponseEntity<ResponseWrapper<ResponseError>> handle(BulkheadFullException exception,
			MessageSource messageSource, Locale locale) {
		HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
		return new ResponseEntity<>(new ResponseWrapper<>(new ResponseError(status.series().value(),
				messageSource.getMessage(RestControllerAdvice.getDefaultMessageProperty(status), null, locale),
				exception.getMessage())), status);
	}

}
