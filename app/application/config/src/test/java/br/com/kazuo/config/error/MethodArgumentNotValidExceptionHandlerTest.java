package br.com.kazuo.config.error;

import br.com.kazuo.config.error.dto.ConstraintViolationMassTestDTO;
import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MethodArgumentNotValidExceptionHandlerTest {
    private MethodArgumentNotValidExceptionHandler handler = new MethodArgumentNotValidExceptionHandler();
    private I18nSupportTest i18nSupportTest = new I18nSupportTest();
    private MethodArgumentNotValidException exception;
    private String msg = "cannot be null";

    private MethodArgumentNotValidException prepareException() throws NoSuchMethodException {
        BindException ex = new BindException("constraintViolationMassTestDTO.testMethodInvalidArgument", "id");
        MethodParameter parameter = new MethodParameter(ConstraintViolationMassTestDTO.class.getDeclaredMethod("testMethodInvalidArgument", Long.class), 0);
        return new MethodArgumentNotValidException(parameter, ex.getBindingResult());
    }

    @Test
    void handle() throws NoSuchMethodException {
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        ResponseError respError = prepareResponseError(respStatus, this.msg);
        ResponseEntity<ResponseWrapper> response = this.handler.handle(prepareException(), i18nSupportTest.messageSource(), i18nSupportTest.locale());
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
    void handleWithErrorsWithoutMessageProperty() throws NoSuchMethodException {
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        ResponseError respError = prepareResponseError(respStatus, this.msg, "Validation.Exception.NotNull");
        this.exception = prepareException();
        this.exception.getBindingResult().addError(getObjectError());
        ResponseEntity<ResponseWrapper> response = this.handler.handle(this.exception, i18nSupportTest.messageSource(), i18nSupportTest.locale());
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
    void handleWithErrorsWithMessageProperty() throws NoSuchMethodException {
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        ResponseError respError = prepareResponseError(respStatus, this.msg);
        this.exception = prepareException();
        this.exception.getBindingResult().addError(getObjectError());
        ResponseEntity<ResponseWrapper> response = this.handler.handle(this.exception, i18nSupportTest.messageSource(), i18nSupportTest.locale());
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
                () -> Assertions.assertEquals(getMessage("Validation.Exception.NotNull"),
                        ((ResponseError)response.getBody().getData()).getMessage(),
                        "Asserts that response data message correspond to response error message")
        );
    }

    private ResponseError prepareResponseError(HttpStatus status, String message) {
        return prepareResponseError(status, message, null);
    }

    private ResponseError prepareResponseError(HttpStatus status, String message, String msgProp) {
        return new ResponseError(
                status.series().value(),
                msgProp == null ? i18nSupportTest.messageSource().getMessage(
                        RestControllerAdvice.getDefaultMessageProperty(status),
                        null,
                        i18nSupportTest.locale()) : getMessage(msgProp),
                message
        );
    }
    private String getMessage(String msgprop) {
        return i18nSupportTest.messageSource().getMessage(
                msgprop,
                null,
                i18nSupportTest.locale());
    }

    private ObjectError getObjectError() {
        String[] codes = new String[1];
        codes[0] = "NotNull";
        Object[] args = new Object[3];
        args[0] = "Qualquercoisa";
        args[1] = "baseProductDTO";
        args[2] = "name";
        return new ObjectError("test", codes, args, "Test error");
    }

}