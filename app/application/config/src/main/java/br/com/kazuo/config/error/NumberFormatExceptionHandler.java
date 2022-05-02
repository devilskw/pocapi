package br.com.kazuo.config.error;

import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public class NumberFormatExceptionHandler implements CustomExceptionHandler<NumberFormatException> {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    @Override
    public ResponseEntity<ResponseWrapper> handle(NumberFormatException exception, MessageSource messageSource, Locale locale) {
        return new ResponseEntity<ResponseWrapper>(
                new ResponseWrapper(
                        new ResponseError(
                                HTTP_STATUS.series().value(),
                                messageSource.getMessage(RestControllerAdvice.getDefaultMessageProperty(HTTP_STATUS), null, locale),
                                exception.getMessage()
                        )
                )
                , HTTP_STATUS
        );
    }

}
