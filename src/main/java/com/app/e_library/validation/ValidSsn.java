package com.app.e_library.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = SsnValidator.class)
@Target( {TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidSsn {

    String message() default "Invalid ssn number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
