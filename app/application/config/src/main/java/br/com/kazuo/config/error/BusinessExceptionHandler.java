package br.com.kazuo.config.error;

import br.com.kazuo.domain.entity.exception.BusinessException;
import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public class BusinessExceptionHandler implements CustomExceptionHandler<BusinessException> {
    @Override
    public ResponseEntity<ResponseWrapper> handle(BusinessException exception, MessageSource messageSource, Locale locale) {
        HttpStatus status = getHttpStatus(exception);
        return new ResponseEntity<ResponseWrapper>(
                new ResponseWrapper(
                        new ResponseError(
                                status.series().value(),
                                messageSource.getMessage(RestControllerAdvice.getDefaultMessageProperty(status), null,locale),
                                exception.getMessage()
                        )
                )
                , status
        );
    }

    private HttpStatus getHttpStatus(BusinessException exception) {
        switch (exception.getCategory()) {
            case CLIENT_INVALID_PARAMETER_OR_DATA:
                return HttpStatus.BAD_REQUEST;
            case CLIENT_DATA_NOT_FOUND:
                return HttpStatus.UNPROCESSABLE_ENTITY;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
