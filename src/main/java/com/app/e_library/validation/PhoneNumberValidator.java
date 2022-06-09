package com.app.e_library.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "\\d-\\d{3}-\\d{3}-\\d{4}"; // X-XXX-XXX-XXXX

    @Override
    public void initialize(ValidPhone contactNumber) {
    }

    @Override
    public boolean isValid(String phoneNumber,
                           ConstraintValidatorContext cxt) {
        return phoneNumber != null && phoneNumber.matches(PHONE_PATTERN)
                && (phoneNumber.length() > 8) && (phoneNumber.length() <= 14);
    }

}
