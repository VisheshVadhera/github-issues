package com.vishesh.githubissues;

import android.app.Application;

import com.vishesh.githubissues.dagger.ApiServiceModule;
import com.vishesh.githubissues.dagger.AppComponent;
import com.vishesh.githubissues.dagger.DaggerAppComponent;
import com.vishesh.githubissues.dagger.RetrofitModule;

/**
 * Created by vishesh on 28/6/17.
 */

public class MainApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setupDagger();
    }

    private void setupDagger() {
        appComponent = DaggerAppComponent.builder()
                .apiServiceModule(new ApiServiceModule())
                .retrofitModule(new RetrofitModule(getString(R.string.base_url)))
                .build();
    }

    public AppComponent getInjector() {
        return appComponent;
    }
}
