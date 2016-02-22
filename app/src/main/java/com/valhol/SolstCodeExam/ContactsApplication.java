package com.valhol.SolstCodeExam;

import android.app.Application;
import com.valhol.SolstCodeExam.components.DaggerNetworkComponent;
import com.valhol.SolstCodeExam.components.NetworkComponent;
import com.valhol.SolstCodeExam.modules.ApplicationModule;
import com.valhol.SolstCodeExam.modules.ContentModule;
import com.valhol.SolstCodeExam.modules.NetworkModule;

public class ContactsApplication extends Application {

    private NetworkComponent mNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkComponent = DaggerNetworkComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule("https://solstice.applauncher.com/external/"))
                .contentModule(new ContentModule())
                .build();

    }

    public NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }
}
