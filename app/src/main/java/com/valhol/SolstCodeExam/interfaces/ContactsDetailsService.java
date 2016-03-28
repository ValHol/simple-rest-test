package com.valhol.SolstCodeExam.interfaces;

import com.valhol.SolstCodeExam.dao.ContactDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ContactsDetailsService {
    @GET("Contacts/id/{employeeid}.json")
    Call<ContactDetails> contactsDetails(@Path("employeeid") long employeeId);
}
