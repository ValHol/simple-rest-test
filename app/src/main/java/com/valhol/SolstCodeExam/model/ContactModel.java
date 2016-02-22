package com.valhol.SolstCodeExam.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Valentín on 21-Feb-16.
 */
public class ContactModel {

    String name;
    long employeeId;
    String company;
    String detailsURL;
    String smallImageURL;
    String birthdate;
    PhoneModel phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDetailsURL() {
        return detailsURL;
    }

    public void setDetailsURL(String detailsURL) {
        this.detailsURL = detailsURL;
    }

    public String getSmallImageURL() {
        return smallImageURL;
    }

    public void setSmallImageURL(String smallImageURL) {
        this.smallImageURL = smallImageURL;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public PhoneModel getPhone() {
        return phone;
    }

    public void setPhone(PhoneModel phone) {
        this.phone = phone;
    }
}
