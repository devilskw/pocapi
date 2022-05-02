package br.com.kazuo.config.error;

import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Objects;

public class ConstraintViolationExceptionHandler implements CustomExceptionHandler<ConstraintViolationException> {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    @Override
    public ResponseEntity<ResponseWrapper> handle(ConstraintViolationException exception, MessageSource messageSource, Locale locale) {
        return new ResponseEntity<ResponseWrapper>(
                new ResponseWrapper(
                        new ResponseError(
                                HTTP_STATUS.series().value(),
                                this.getMessage(exception, messageSource, locale),
                                exception.getMessage()
                        )
                )
                , HTTP_STATUS
        );
    }


    private String getMessage(final ConstraintViolationException exception, final MessageSource messageSource, final Locale locale) {
        Object[] args = null;
        try {
            ConstraintViolation violation = exception.getConstraintViolations().isEmpty() ? null :
                    (ConstraintViolation)exception.getConstraintViolations().toArray()[0];
            if (Objects.nonNull(violation)) {
                String msgProperty = new StringBuilder()
                        .append(RestControllerAdvice.MSGPREFIX)
                        .append(violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName())
                        .append(".")
                        .append(violation.getPropertyPath().toString())
                        .toString();
                Object argValue = violation.getConstraintDescriptor()
                        .getAttributes().getOrDefault("value", null);
                if (Objects.nonNull(argValue)) {
                    args = new Object[1];
                    args[0] = argValue;
                }
                return this.getMessage(msgProperty, args, messageSource, locale);
            }
            return this.getMessage(HTTP_STATUS, null, messageSource, locale);
        } catch (Exception ex){
            return this.getMessage(HTTP_STATUS, null, messageSource, locale);
        }
    }

    private String getMessage(final String msgProperty, final Object[] args, final MessageSource messageSource, final Locale locale) {
        return messageSource.getMessage(msgProperty, args, locale);
    }

    private String getMessage(final HttpStatus httpStatus, final Object[] args, final MessageSource messageSource, final Locale locale) {
        return messageSource.getMessage(RestControllerAdvice.getDefaultMessageProperty(httpStatus), args,locale);
    }
}
