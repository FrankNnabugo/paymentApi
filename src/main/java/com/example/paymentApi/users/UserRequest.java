package com.example.paymentApi.users;

public class UserRequest{

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    private String password;

    private String acceptedTerms;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(String acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }
}
