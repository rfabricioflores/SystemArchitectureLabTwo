package se.fabricioflores.systemarchitecturelabtwo.interceptor;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

@InterceptorBinding
@Target({ TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}
