package br.com.kazuo.config.resilience.bulkhead;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.Bulkhead;

@Aspect
@Component
public class BulkHeadProductV1Aop {

  BulkheadRegistry registry;

  public BulkHeadProductV1Aop(BulkheadRegistry registry) {
    this.registry = registry;
  }

  @Around("within(br.com.kazuo.entrypoint.product.v1..*)")
  public Object proceedInternal(ProceedingJoinPoint joinPoint) throws Throwable {
    return process(joinPoint, "v1-produtos");
  }

  private Object process(ProceedingJoinPoint joinPoint, String bulkheadName) throws Throwable {
    Bulkhead bulkhead = this.registry.bulkhead(bulkheadName);
    return bulkhead.executeCheckedSupplier(joinPoint::proceed);
  }
}
