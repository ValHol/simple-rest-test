package com.valhol.SolstCodeExam.interfaces;

import com.valhol.SolstCodeExam.dao.Contact;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ContactsService {
    @GET("contacts.json")
    Call<List<Contact>> contactsList();
}

