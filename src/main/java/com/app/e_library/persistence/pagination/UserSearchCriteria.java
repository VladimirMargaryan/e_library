package com.app.e_library.persistence.pagination;

import lombok.Setter;

@Setter
public class UserSearchCriteria extends PageRequest {
    private String firstname;
    private String lastname;
    private String ssn;
    private String email;
    private String phone;
    private String street;
    private Integer streetNumber;
    private String city;


    public String getFirstname() {
        return firstname = firstname == null ? "" : firstname.replaceAll("\\s+", "").toLowerCase();
    }

    public String getLastname() {
        return lastname = lastname == null ? "" : lastname.replaceAll("\\s+", "").toLowerCase();
    }

    public String getSsn() {
        return ssn = ssn == null ? "" : ssn.replaceAll("[^0-9]", "").toLowerCase();
    }

    public String getEmail() {
        return email = email == null ? "" : email.replaceAll("\\s+", "").toLowerCase();
    }

    public String getPhone() {
        return phone = phone == null ? "" : phone.replaceAll("[^0-9]", "").toLowerCase();
    }

    public String getStreet() {
        return street = street == null ? "" : street.replaceAll("\\s+", "").toLowerCase();
    }

    public Integer getStreetNumber() {
        return streetNumber = streetNumber == null ? new Integer(0) : streetNumber;
    }

    public String getCity() {
        return city = city == null ? "" : city.replaceAll("\\s+", "").toLowerCase();
    }
}
