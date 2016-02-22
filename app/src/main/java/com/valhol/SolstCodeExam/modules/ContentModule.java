package com.valhol.SolstCodeExam.modules;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.valhol.SolstCodeExam.dao.*;
import com.valhol.SolstCodeExam.interfaces.ContactsService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Valentín on 20-Feb-16.
 */

@Module
public class ContentModule {

    public ContentModule() {
    }

    @Provides
    @Singleton
    ContentResolver provideContentResolver(Application application) {
        return application.getContentResolver();
    }

    @Provides
    @Singleton
    DaoMaster provideDaoMaster(Application application) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application,
                "contacts-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        return new DaoMaster(db);
    }

    @Provides
    @Singleton
    DaoSession provideDaoSesion(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    ContactDao provideContactsDao(DaoSession daoSession) {
        return daoSession.getContactDao();
    }

    @Provides
    @Singleton
    PhoneDao providePhoneDao(DaoSession daoSession) {
        return daoSession.getPhoneDao();
    }

    @Provides
    @Singleton
    AddressDao provideAddressDao(DaoSession daoSession) {
        return daoSession.getAddressDao();
    }

    @Provides
    @Singleton
    ContactDetailsDao provideContactDetailsDao(DaoSession daoSession) {
        return daoSession.getContactDetailsDao();
    }

    @Provides
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

}
