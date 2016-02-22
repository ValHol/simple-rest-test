package com.valhol.SolstCodeExam.interfaces;

import com.valhol.SolstCodeExam.model.ContactDetailModel;
import com.valhol.SolstCodeExam.model.ContactModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface ContactsService {
    @GET("contacts.json")
    Call<List<ContactModel>> contactsList();
}

