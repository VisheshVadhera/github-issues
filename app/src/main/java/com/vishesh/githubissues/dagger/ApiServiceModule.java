package com.vishesh.githubissues.dagger;

import com.vishesh.githubissues.common.GitHubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by vishesh on 28/6/17.
 */
@Module
public class ApiServiceModule {

    @Provides
    @Singleton
    public GitHubService provideGitHubService(Retrofit retrofit) {
        return retrofit.create(GitHubService.class);
    }
}
