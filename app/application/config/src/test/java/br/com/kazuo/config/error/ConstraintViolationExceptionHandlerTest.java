package br.com.kazuo.config.error;

import br.com.kazuo.config.error.dto.ConstraintViolationMassTestDTO;
import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;

class ConstraintViolationExceptionHandlerTest {

    ConstraintViolationExceptionHandler handler = new ConstraintViolationExceptionHandler();
    private I18nSupportTest i18nSupportTest = new I18nSupportTest();

    @Test
    void handleConstraintViolationExceptionBadRequestWithoutMessagePropertyTest() {
        String message = "Teste01";
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        ConstraintViolationMassTestDTO testDTO = new ConstraintViolationMassTestDTO(1L, -1L);
        ConstraintViolationException exception = new ConstraintViolationException(validator.validate(testDTO));
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        ResponseError respError = this.prepareResponseError(respStatus,exception.getMessage());
        ResponseEntity<ResponseWrapper> response = handler.handle(
                exception,
                i18nSupportTest.messageSource(),
                i18nSupportTest.locale()
        );
        Assertions.assertAll(
                () -> Assertions.assertNotNull(response,
                        "Asserts that response is not null"),
                () -> Assertions.assertNotNull(response.getBody(),
                        "Asserts that response body is not null"),
                () -> Assertions.assertNotNull(response.getStatusCode(),
                        "Asserts that response status code is not null"),
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                        "Asserts that response status code is 400"),
                () -> Assertions.assertNotNull(response.getBody().getData(),
                        "Asserts that response body data is not null"),
                () -> Assertions.assertTrue((response.getBody().getData() instanceof ResponseError),
                        "Asserts that response body data is ResponseError type"),
                () -> Assertions.assertEquals(respError.getCode(),
                        ((ResponseError)response.getBody().getData()).getCode(),
                        "Asserts that response data code correspond to response error code"),
                () -> Assertions.assertEquals(respError.getMessage(),
                        ((ResponseError)response.getBody().getData()).getMessage(),
                        "Asserts that response data message correspond to response error message")
        );
    }

    @Test
    void handleConstraintViolationExceptionBadRequestDefinedMessageTest() {
        String message = "Teste01";
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        ConstraintViolationMassTestDTO testDTO = new ConstraintViolationMassTestDTO(0L, 0L);
        ConstraintViolationException exception = new ConstraintViolationException(validator.validate(testDTO));
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        Object[] args = new Object[1];
        args[0] = "1";
        ResponseError respError =this.prepareResponseError(respStatus,"Validation.Exception.Min.test",  args, exception.getMessage());
        ResponseEntity<ResponseWrapper> response = handler.handle(
                exception,
                i18nSupportTest.messageSource(),
                i18nSupportTest.locale()
        );
        Assertions.assertAll(
                () -> Assertions.assertNotNull(response,
                        "Asserts that response is not null"),
                () -> Assertions.assertNotNull(response.getBody(),
                        "Asserts that response body is not null"),
                () -> Assertions.assertNotNull(response.getStatusCode(),
                        "Asserts that response status code is not null"),
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                        "Asserts that response status code is 400"),
                () -> Assertions.assertNotNull(response.getBody().getData(),
                        "Asserts that response body data is not null"),
                () -> Assertions.assertTrue((response.getBody().getData() instanceof ResponseError),
                        "Asserts that response body data is ResponseError type"),
                () -> Assertions.assertEquals(respError.getCode(),
                        ((ResponseError)response.getBody().getData()).getCode(),
                        "Asserts that response data code correspond to response error code"),
                () -> Assertions.assertEquals(respError.getMessage(),
                        ((ResponseError)response.getBody().getData()).getMessage(),
                        "Asserts that response data message correspond to response error message")
        );
    }

    @Test
    void handleConstraintViolationExceptionBadRequestDefinedMessageWithoutArgsTest() {
        String message = "Teste01";
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        ConstraintViolationMassTestDTO testDTO = new ConstraintViolationMassTestDTO(0L, 0L);
        ConstraintViolationException exception = new ConstraintViolationException(validator.validate(testDTO));
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        Object[] args = null;
        ResponseError respError =this.prepareResponseError(respStatus,"Validation.Exception.Min.test",  args, exception.getMessage());
        ResponseEntity<ResponseWrapper> response = handler.handle(
                exception,
                i18nSupportTest.messageSource(),
                i18nSupportTest.locale()
        );
        Assertions.assertAll(
                () -> Assertions.assertNotNull(response,
                        "Asserts that response is not null"),
                () -> Assertions.assertNotNull(response.getBody(),
                        "Asserts that response body is not null"),
                () -> Assertions.assertNotNull(response.getStatusCode(),
                        "Asserts that response status code is not null"),
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                        "Asserts that response status code is 400"),
                () -> Assertions.assertNotNull(response.getBody().getData(),
                        "Asserts that response body data is not null"),
                () -> Assertions.assertTrue((response.getBody().getData() instanceof ResponseError),
                        "Asserts that response body data is ResponseError type"),
                () -> Assertions.assertEquals(respError.getCode(),
                        ((ResponseError)response.getBody().getData()).getCode(),
                        "Asserts that response data code correspond to response error code"),
                () -> Assertions.assertEquals(respError.getMessage(),
                        ((ResponseError)response.getBody().getData()).getMessage(),
                        "Asserts that response data message correspond to response error message")
        );
    }

    @Test
    void handleConstraintViolationExceptionBadRequestGenericMessageTest() {
        String message = "Teste01";
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        ConstraintViolationMassTestDTO testDTO = new ConstraintViolationMassTestDTO(10L, 0L);
        ConstraintViolationException exception = new ConstraintViolationException(validator.validate(testDTO));
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        Object[] args = new Object[1];
        args[0] = "1";
        ResponseError respError =this.prepareResponseError(respStatus, exception.getMessage());
        ResponseEntity<ResponseWrapper> response = handler.handle(
                exception,
                i18nSupportTest.messageSource(),
                i18nSupportTest.locale()
        );
        Assertions.assertAll(
                () -> Assertions.assertNotNull(response,
                        "Asserts that response is not null"),
                () -> Assertions.assertNotNull(response.getBody(),
                        "Asserts that response body is not null"),
                () -> Assertions.assertNotNull(response.getStatusCode(),
                        "Asserts that response status code is not null"),
                () -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                        "Asserts that response status code is 400"),
                () -> Assertions.assertNotNull(response.getBody().getData(),
                        "Asserts that response body data is not null"),
                () -> Assertions.assertTrue((response.getBody().getData() instanceof ResponseError),
                        "Asserts that response body data is ResponseError type"),
                () -> Assertions.assertEquals(respError.getCode(),
                        ((ResponseError)response.getBody().getData()).getCode(),
                        "Asserts that response data code correspond to response error code"),
                () -> Assertions.assertEquals(respError.getMessage(),
                        ((ResponseError)response.getBody().getData()).getMessage(),
                        "Asserts that response data message correspond to response error message")
        );
    }

    private ResponseError prepareResponseError(HttpStatus status, String message) {
        return new ResponseError(
                status.series().value(),
                i18nSupportTest.messageSource().getMessage(
                        RestControllerAdvice.getDefaultMessageProperty(status),
                        null,
                        i18nSupportTest.locale()),
                message
        );
    }


    private ResponseError prepareResponseError(HttpStatus status, String msgProperty, Object[] args, String message) {
        return new ResponseError(
                status.series().value(),
                i18nSupportTest.messageSource().getMessage(
                        msgProperty,
                        args,
                        i18nSupportTest.locale()),
                message
        );
    }

    private String getMessage(final String msgProperty, final Object[] args, final MessageSource messageSource, final Locale locale) {
        return messageSource.getMessage(msgProperty, args, locale);
    }

    private String getMessage(final HttpStatus httpStatus, final Object[] args, final MessageSource messageSource, final Locale locale) {
        return messageSource.getMessage(RestControllerAdvice.getDefaultMessageProperty(httpStatus), args,locale);
    }
}