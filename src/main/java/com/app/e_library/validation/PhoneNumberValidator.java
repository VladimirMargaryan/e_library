package com.app.e_library.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "\\d-\\d{3}-\\d{3}-\\d{4}"; // X-XXX-XXX-XXXX

    @Override
    public void initialize(ValidPhone contactNumber) {
    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches(PHONE_PATTERN)
                && (contactField.length() > 8) && (contactField.length() <= 14);
    }

}
