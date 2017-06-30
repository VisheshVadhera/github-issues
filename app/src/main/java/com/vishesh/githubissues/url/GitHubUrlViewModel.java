package com.vishesh.githubissues.url;

import com.vishesh.githubissues.common.GitHubService;
import com.vishesh.githubissues.common.Issue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import retrofit2.HttpException;

/**
 * Created by vishesh on 28/6/17.
 */

class GitHubUrlViewModel {

    private static final String TAG = "GitHubUrlViewModel";
    private GitHubService gitHubService;
    private PublishSubject<CharSequence> editTextSubject;


    @Inject
    GitHubUrlViewModel(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    PublishSubject<CharSequence> textChangeSubject() {
        return editTextSubject;
    }

    Observable<List<Issue>> getIssues(Observable<CharSequence> charSequenceObservable) {

        return charSequenceObservable
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> charSequence.toString().contains("/"))
                .map(repoUrl -> repoUrl.toString().split("/"))
                .filter(params -> params.length > 1)
                .flatMap(params -> gitHubService.getIssuesByRepo(params[0], params[1])
                        .onErrorResumeNext(throwable -> {
                            if (throwable instanceof HttpException) {
                                return Observable.error(throwable);
                            } else {
                                return Observable.empty();
                            }
                        }));
    }

    Observable<List<Issue>> toObservable() {


        /**
         * 1. Trying to access array's second element.
         * 2. Recover on Thread Interrupted exception.
         * 3. Give feedback to the users if an http exception occurs.
         * 4. Resubscribe to the stream if an error occurs.
         */

        editTextSubject = PublishSubject.create();
        return editTextSubject
                .toFlowable(BackpressureStrategy.LATEST)
                .toObservable()
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(charSequence -> charSequence.toString().contains("/"))
                .map(repoUrl -> repoUrl.toString().split("/"))
                .flatMap(strings -> gitHubService.getIssuesByRepo(strings[0], strings[1]));

        /*editTextSubject = PublishSubject.create();
        return editTextSubject
                .toFlowable(BackpressureStrategy.LATEST)
                .toObservable()
                .doOnNext(charSequence -> {
                    Log.i(TAG, "Received charsequence1: " + charSequence);
                })
                .debounce(300, TimeUnit.MILLISECONDS)
                .doOnNext(charSequence -> {
                    Log.i(TAG, "Received charsequence2: " + charSequence);
                })
                .filter(charSequence -> charSequence.toString().contains("/"))
                .doOnNext(charSequence -> {
                    Log.i(TAG, "Received charsequence3: " + charSequence);
                })
                .map(repoUrl -> repoUrl.toString().split("/"))
                .filter(strings -> strings.length > 1)
                .flatMap(strings -> gitHubService.getIssuesByRepo(strings[0], strings[1])
                        .onErrorResumeNext(throwable -> {
                            Observable.empty();
                        }));*//*
    }*/

    /*Single<List<Issue>> getIssues(final String repoUrl) {
        return Single.just(repoUrl)
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull String s) throws Exception {
                        String[] splitStrings = splitUrl(repoUrl);
                        List<String> strings = new ArrayList<String>();
                        strings.add(0, splitStrings[0]);
                        strings.add(1, splitStrings[1]);
                        return strings;
                    }
                })
                .flatMap(new Function<List<String>, SingleSource<List<Issue>>>() {
                    @Override
                    public SingleSource<List<Issue>> apply(@NonNull List<String> strings) throws Exception {
                        return gitHubService.getIssuesByRepo(strings.get(0), strings.get(1));
                    }
                });
    }*/
    }

    private String[] splitUrl(String repoUrl) {
        return repoUrl.split("/");
    }
}

