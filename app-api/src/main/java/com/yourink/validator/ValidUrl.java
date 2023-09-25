package com.yourink.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UrlValidator.class) // 검사를 수행할 Validator 클래스 지정
@Target({ElementType.FIELD}) // 어노테이션을 사용할 수 있는 대상 지정
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUrl {
    String message() default "URL 형식을 확인해주세요";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
