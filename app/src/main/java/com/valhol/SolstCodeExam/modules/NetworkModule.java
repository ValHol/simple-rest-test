package com.valhol.SolstCodeExam.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.valhol.SolstCodeExam.interfaces.ContactsDetailsService;
import com.valhol.SolstCodeExam.interfaces.ContactsService;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Singleton;

/**
 * Created by Valentín on 20-Feb-16.
 */
@Module
public class NetworkModule {
    String mUrl;

    public NetworkModule(String mUrl) {
        this.mUrl = mUrl;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }


    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .baseUrl(mUrl)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    ContactsService provideContactsService(Retrofit retrofit) {
        return retrofit.create(ContactsService.class);
    }

    @Provides
    @Singleton
    ContactsDetailsService provideContactsDetailsService(Retrofit retrofit) {
        return retrofit.create(ContactsDetailsService.class);
    }
}
