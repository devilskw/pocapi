package br.com.kazuo.config.error;

import br.com.kazuo.domain.entity.exception.BusinessCategoryExceptionEnum;
import br.com.kazuo.domain.entity.exception.BusinessException;
import br.com.kazuo.entrypoint.dto.ResponseError;
import br.com.kazuo.entrypoint.dto.ResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class BusinessExceptionHandlerTest {

    private BusinessExceptionHandler handler = new BusinessExceptionHandler();
    private I18nSupportTest i18nSupportTest = new I18nSupportTest();

    @Test
    void handleBadRequestTest() {
        BusinessException exception = new BusinessException(
                BusinessCategoryExceptionEnum.CLIENT_INVALID_PARAMETER_OR_DATA, "Test01", null
        );
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

    @Test
    void handleUnprocessableEntityTest() {
        BusinessException exception = new BusinessException(
                BusinessCategoryExceptionEnum.CLIENT_DATA_NOT_FOUND, "Test02", null
        );
        HttpStatus respStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ResponseError respError = prepareResponseError(respStatus, exception.getMessage());
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
                () -> Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode(),
                        "Asserts that response status code is 422"),
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
    void handleInternalServerErrorTest() {
        BusinessException exception = new BusinessException(
                BusinessCategoryExceptionEnum.INTERNAL, "Test03", null
        );
        HttpStatus respStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseError respError = prepareResponseError(respStatus, exception.getMessage());
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
                () -> Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                        "Asserts that response status code is 500"),
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