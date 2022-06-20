package br.com.kazuo.shared.observability;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CounterAnnotation {
    String value();
    String[] tags() default {};
}
