package com.app.e_library.validation;

import com.app.e_library.service.dto.AddressDto;
import com.app.e_library.service.dto.CityDto;
import com.app.e_library.service.dto.UserDto;
import org.passay.*;
import org.passay.PasswordValidator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    public static boolean isValid(UserDto user) {
        return (user.getFirstname() != null && !user.getFirstname().isEmpty())
                && (user.getLastname() != null && !user.getLastname().isEmpty())
                && (user.getSsn() != null && !user.getSsn().isEmpty())
                && isValidEmail(user.getEmail())
                && isValidPassword(user.getPassword())
                && isValidPhone(user.getPhone())
                && user.getStatus() != null && user.getAddress() != null
                && isValidAddress(user.getAddress());
    }

    private static boolean isValidAddress(AddressDto addressDto) {
        return (addressDto.getStreet() != null && !addressDto.getStreet().isEmpty()) &&
                addressDto.getStreetNumber() != 0 && addressDto.getCity() != null
                && isValidCity(addressDto.getCity());
    }

    private static boolean isValidCity(CityDto cityDto) {
        return cityDto.getCityName() != null && !cityDto.getCityName().isEmpty();
    }

    private static boolean isValidEmail(String email) {
        if (email != null && !email.isEmpty()) {
            String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } else
            return false;
    }

    private static boolean isValidPassword(String password) {
        if (password != null && !password.isEmpty()){
            PasswordValidator validator = new PasswordValidator(Arrays.asList(
                    new LengthRule(8, 30),
                    new UppercaseCharacterRule(1),
                    new DigitCharacterRule(1),
                    new SpecialCharacterRule(1),
                    new NumericalSequenceRule(3,false),
                    new AlphabeticalSequenceRule(3,false),
                    new QwertySequenceRule(3,false),
                    new WhitespaceRule()
            ));

            RuleResult result = validator.validate(new PasswordData(password));
            return result.isValid();
        } else
            return false;
    }

    private static boolean isValidPhone(String phone) {
        final String PHONE_PATTERN = "\\d-\\d{3}-\\d{3}-\\d{4}"; // X-XXX-XXX-XXXX

        return phone != null
                && phone.matches(PHONE_PATTERN)
                && (phone.length() > 8)
                && (phone.length() <= 14);
    }
}
