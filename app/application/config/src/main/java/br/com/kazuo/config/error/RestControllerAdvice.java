package br.com.kazuo.config.error;

import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import io.github.resilience4j.bulkhead.BulkheadFullException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Locale;

@ControllerAdvice
public class RestControllerAdvice {
    public static final String MSGPREFIX = "Validation.Exception.";
    private static final String MSG_PREFIX_STATUSERROR = "HttpStatus.Default.Exception.";
    protected final MessageSource messageSource;
    private final ConstraintViolationExceptionHandler constraintViolationExceptionHandler;
    private final NumberFormatExceptionHandler numberFormatExceptionHandler;
    private final MethodArgumentNotValidExceptionHandler methodArgumentNotValidExceptionHandler;
    private final BulkheadFullExceptionHandler bulkheadFullExceptionHandler;

    public RestControllerAdvice(@Autowired MessageSource messageSource) {
        this.messageSource = messageSource;
        this.constraintViolationExceptionHandler = new ConstraintViolationExceptionHandler();
        this.numberFormatExceptionHandler = new NumberFormatExceptionHandler();
        this.methodArgumentNotValidExceptionHandler = new MethodArgumentNotValidExceptionHandler();
        this.bulkheadFullExceptionHandler = new BulkheadFullExceptionHandler();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseWrapper<ResponseError>> handleConstraintViolationExceptions(
            final ConstraintViolationException exception, final Locale locale) {
        return this.constraintViolationExceptionHandler.handle(exception, this.messageSource, locale);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ResponseWrapper<ResponseError>> handleNumberFormatExceptions(
            final NumberFormatException exception, final Locale locale) {
        return this.numberFormatExceptionHandler.handle(exception, this.messageSource, locale);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<ResponseError>> handleMethodArgumentNotValidExceptions(
            final MethodArgumentNotValidException exception, final Locale locale) {
        return this.methodArgumentNotValidExceptionHandler.handle(exception, this.messageSource, locale);
    }

    @ExceptionHandler(BulkheadFullException.class)
    public ResponseEntity<ResponseWrapper<ResponseError>> handleBulkheadFullException(
            final BulkheadFullException exception, final Locale locale) {
        return this.bulkheadFullExceptionHandler.handle(exception, this.messageSource, locale);
    }

    public static String getDefaultMessageProperty(HttpStatus status) {
        return new StringBuilder().append(MSG_PREFIX_STATUSERROR).append(status.getReasonPhrase().replace(" ", "_"))
                .toString();
    }

}
