package br.com.kazuo.domain.entity.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import br.com.kazuo.domain.entity.exception.CustomException.CustomExceptionBuilder;

public class CustomExceptionUnitTest {

  private static final String ArgsKeyPrefix = "chave_";
  private static final String ArgsValuePrefix = "valor_";
  private static final String ArgsStringFormat = "%3d";

  private String getArgKey(Integer keyDynamicPartValue) {
    return ArgsKeyPrefix.concat(String.format(ArgsStringFormat, keyDynamicPartValue));
  }

  private String getArgValue(Integer kevalueDynamicPartValue, final boolean nullValue) {
    return nullValue ? null : ArgsValuePrefix.concat(String.format(ArgsStringFormat, kevalueDynamicPartValue));
  }

  private Map<String, String> prepareFakeArgs(int qtde, boolean nullValue) {
    Map<String, String> args = new HashMap<>();
    int contCreations = qtde;
    while (contCreations > 0) {
      args.put(getArgKey(contCreations), getArgValue(contCreations, nullValue));
      contCreations--;
    }
    return args;
  }

  private List<Executable> prepareAssertionExecutables(CustomException exception, String message, int qtde,
      boolean nullArgValue) {
    List<Executable> executables = new ArrayList<>();
    Executable assertExceptionIsDataNotFoundCustomExceptionClass = () -> assertInstanceOf(CustomException.class,
        exception, "Exception object should be a instance of DataNotFoundCustomException class");
    executables.add(assertExceptionIsDataNotFoundCustomExceptionClass);
    Executable assertExceptionInheritesFromCustomExceptionClass = () -> assertInstanceOf(CustomException.class,
        exception, "Exception object should inherit from CustomException superclass");
    executables.add(assertExceptionInheritesFromCustomExceptionClass);
    Executable assertMessage = null;
    if (message == null) {
      assertMessage = () -> assertNull(exception.getMessage(),
          "Asserts that message from DataNotFoundCustomException is null");
    } else {
      assertMessage = () -> assertEquals(message, exception.getMessage(),
          "Asserts that message from DataNotFoundCustomException is equals expected message");
    }
    executables.add(assertMessage);
    Executable assertExceptionArgumentsQtde = () -> assertEquals(qtde < 0 ? 0 : qtde, exception.getArgs().size());
    executables.add(assertExceptionArgumentsQtde);

    for (int argIndexKeyControl = qtde; argIndexKeyControl > 0; argIndexKeyControl--) {
      String argKeyExpected = getArgKey(argIndexKeyControl);
      String argValueExpected = getArgValue(argIndexKeyControl, nullArgValue);
      Executable assertExistsKeyArgument = () -> assertTrue(exception.getArgs().containsKey(argKeyExpected),
          "Asserts that key argument exists");
      executables.add(assertExistsKeyArgument);
      String foundValue = exception.getArgs().getOrDefault(argKeyExpected, "*****Not Found*****");
      Executable assertsCorrectValueArgument = () -> assertEquals(argValueExpected, foundValue);
      executables.add(assertsCorrectValueArgument);
    }
    return executables;
  }

  @Test
  public void givenMessageAndArgs_whenCreatingDataNotFoundCustomException() throws Exception {
    final String errorMessage = "Teste de erro Data Not Found Exception";
    final int qtde = 100;
    CustomException exception = new CustomExceptionBuilder(errorMessage,
        CustomExceptionCategoryEnum.CLIENT_DATA_NOT_FOUND, null).setArgs(prepareFakeArgs(qtde, false)).build();
    Assertions.assertAll(prepareAssertionExecutables(exception, errorMessage, qtde, false));
  }

  @Test
  public void givenMessageAndNullArgs_whenCreatingDataNotFoundCustomException() throws Exception {
    final String errorMessage = "Teste de erro Data Not Found Exception";
    final int qtde = 0;
    CustomException exception = new CustomExceptionBuilder(errorMessage,
        CustomExceptionCategoryEnum.CLIENT_DATA_NOT_FOUND, null).setArgs(prepareFakeArgs(qtde, true)).build();
    Assertions.assertAll(prepareAssertionExecutables(exception, errorMessage, qtde, true));
  }

}
