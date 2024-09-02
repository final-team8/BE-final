package com.fastcampus.befinal.common.annotation;

import com.fastcampus.befinal.common.util.UserUpdateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserUpdate {
    String message() default "Invalid user request.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
