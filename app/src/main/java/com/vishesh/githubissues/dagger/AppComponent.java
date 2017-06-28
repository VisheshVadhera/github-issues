package com.vishesh.githubissues.dagger;

import com.vishesh.githubissues.url.GitHubUrlActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by vishesh on 28/6/17.
 */

@Singleton
@Component(modules = {RetrofitModule.class, ApiServiceModule.class})
public interface AppComponent {


    void inject(GitHubUrlActivity activity);

}
