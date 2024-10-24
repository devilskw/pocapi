package br.com.kazuo.config.resilience.retry;

import java.util.function.Predicate;

import br.com.kazuo.domain.entity.exception.CustomException;

// Based on:
// https://reflectoring.io/retry-with-springboot-resilience4j/
public class ConditionalRetryPredicate implements Predicate<CustomException> {
  @Override
  public boolean test(CustomException exception) {
    switch (exception.getCategory()) {
    case OPERATION_FAILED:
    case INTERNAL:
      return true;
    default:
      return false;
    }
  }
}