package com.app.e_library.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SsnValidator implements ConstraintValidator<ValidSsn, String> {

    private static final String SSN_PATTERN = "\\d{3}-\\d{2}-\\d{4}"; // XXX-XX-XXXX

    @Override
    public void initialize(ValidSsn constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.matches(SSN_PATTERN)
                && s.length() == 11;
    }
}
