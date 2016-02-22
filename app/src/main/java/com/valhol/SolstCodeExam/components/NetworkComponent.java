package com.valhol.SolstCodeExam.components;

import com.valhol.SolstCodeExam.ContactDetailFragment;
import com.valhol.SolstCodeExam.ContactsFragment;
import com.valhol.SolstCodeExam.MainActivity;
import com.valhol.SolstCodeExam.modules.ApplicationModule;
import com.valhol.SolstCodeExam.modules.ContentModule;
import com.valhol.SolstCodeExam.modules.NetworkModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules={ApplicationModule.class, NetworkModule.class, ContentModule.class})
public interface NetworkComponent {
    void inject(MainActivity mainActivity);
    void inject(ContactsFragment contactsFragment);
    void inject(ContactDetailFragment contactDetailFragment);
}
