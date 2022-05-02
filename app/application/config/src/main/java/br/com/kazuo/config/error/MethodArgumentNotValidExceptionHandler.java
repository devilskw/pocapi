package br.com.kazuo.config.error;

import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Locale;

public class MethodArgumentNotValidExceptionHandler implements CustomExceptionHandler<MethodArgumentNotValidException> {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    @Override
    public ResponseEntity<ResponseWrapper> handle(MethodArgumentNotValidException exception, MessageSource messageSource, Locale locale) {
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

    public String getMessage(MethodArgumentNotValidException exception, MessageSource messageSource, Locale locale) {
        // exception.getBindingResult().getAllErrors().isEmpty() ? null : exception.getBindingResult().getAllErrors()
        //"name" exception.getBindingResult().getAllErrors().get(0).getArguments().get(0) - args
        //
        //"baseProductDTO" exception.getBindingResult().getAllErrors().get(0).getObjectName()
        //
        // exception.getBindingResult().getAllErrors().get(0).getArguments()[0].getCodes() // 0 = baseProductDTO.name / 1 = name
        // exception.getBindingResult().getAllErrors().get(0).getArguments()[0].defaultMessage // "name"
        //
        // exception.getBindingResult().getAllErrors().get(0).getCodes()[0] // 0 = NotNull.baseProductDTO.name
        Object[] args = null;
        try {
            if (!exception.getBindingResult().getAllErrors().isEmpty() &&
                    exception.getBindingResult().getAllErrors().get(0).getCodes().length > 0) {
                String msgProperty = new StringBuilder()
                        .append(RestControllerAdvice.MSGPREFIX)
                        .append(exception.getBindingResult().getAllErrors().get(0).getCodes()[0])
                        .toString();
                args = exception.getBindingResult().getAllErrors().get(0).getArguments();
                if (args.length > 1) {
                    args = Arrays.copyOfRange(args, 1 , args.length);
                }
                return messageSource.getMessage(msgProperty, args, locale);
            }
            return this.getMessage(HTTP_STATUS, null, messageSource, locale);
        } catch (Exception ex) {
            return this.getMessage(HTTP_STATUS, null, messageSource, locale);
        }

    }

    private String getMessage(final HttpStatus httpStatus, final Object[] args, final MessageSource messageSource, final Locale locale) {
        return messageSource.getMessage(RestControllerAdvice.getDefaultMessageProperty(httpStatus), args,locale);
    }
}
