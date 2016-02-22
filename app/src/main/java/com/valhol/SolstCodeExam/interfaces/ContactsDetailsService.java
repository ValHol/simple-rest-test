package com.valhol.SolstCodeExam.interfaces;

import com.valhol.SolstCodeExam.model.ContactDetailModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface ContactsDetailsService {
    @GET("Contacts/id/{employeeid}.json")
    Call<ContactDetailModel> contactsDetails(@Path("employeeid") long employeeId);
}
