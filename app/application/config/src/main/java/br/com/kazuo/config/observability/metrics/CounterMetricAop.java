package br.com.kazuo.config.observability.metrics;

import br.com.kazuo.shared.observability.CounterAnnotation;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CounterMetricAop {

    private MeterRegistry registry;

    @Autowired
    public CounterMetricAop(MeterRegistry registry) {
        this.registry = registry;
        Metrics.addRegistry(this.registry);
    }

    @Around(value = "@annotation(br.com.kazuo.shared.observability.CounterAnnotation)")
    public void count(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CounterAnnotation counterAnnotation = signature.getMethod().getAnnotation(CounterAnnotation.class);
        try {
            joinPoint.proceed();
            Metrics.counter(counterAnnotation.value(), counterAnnotation.tags()).increment();
        } catch (Throwable t) {
            Metrics.counter(counterAnnotation.value() + ".error", "exception", t.getClass().getSimpleName()).increment();
        }
    }
}
