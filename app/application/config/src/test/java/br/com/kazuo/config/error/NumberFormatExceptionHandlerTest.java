package br.com.kazuo.config.error;

import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class NumberFormatExceptionHandlerTest {
    private NumberFormatExceptionHandler handler = new NumberFormatExceptionHandler();
    private I18nSupportTest i18nSupportTest = new I18nSupportTest();

    @Test
    void handleNumberFormatExceptionBadRequestTest() {
        NumberFormatException exception = new NumberFormatException("test");
        HttpStatus respStatus = HttpStatus.BAD_REQUEST;
        ResponseError respError =prepareResponseError(respStatus, exception.getMessage());
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
}