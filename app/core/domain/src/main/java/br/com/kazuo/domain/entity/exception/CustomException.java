package br.com.kazuo.domain.entity.exception;

import java.util.HashMap;
import java.util.Map;

public class CustomException extends RuntimeException {
  private final CustomExceptionCategoryEnum category;
  private final Map<String, String> args;

  private CustomException(CustomExceptionBuilder builder) {
    super(builder.message, builder.cause);
    this.category = builder.category;
    this.args = builder.args;
  }

  public CustomExceptionCategoryEnum getCategory() {
    return this.category;
  }

  public Map<String, String> getArgs() {
    return this.args;
  }

  public static class CustomExceptionBuilder {
    private final CustomExceptionCategoryEnum category;
    private final String message;
    private Map<String, String> args;
    private Throwable cause;

    public CustomExceptionBuilder(String message, CustomExceptionCategoryEnum category, Throwable cause) {
      this.message = message;
      this.category = category;
      this.cause = cause == null ? new Throwable(message) : cause;
    }

    public CustomExceptionBuilder setArgs(Map<String, String> args) {
      this.args = args;
      return this;
    }

    public CustomExceptionBuilder addArg(String key, String value) {
      if (this.args == null) {
        this.args = new HashMap<>();
      }
      this.args.put(key, value);
      return this;
    }

    public CustomExceptionBuilder setCause(Throwable cause) {
      this.cause = cause;
      return this;
    }

    public CustomException build() {
      return new CustomException(this);
    }

  }

}
