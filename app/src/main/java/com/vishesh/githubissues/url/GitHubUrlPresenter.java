package com.vishesh.githubissues.url;

import android.text.TextUtils;

import com.vishesh.githubissues.common.GitHubService;
import com.vishesh.githubissues.common.Issue;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vishesh on 28/6/17.
 */

class GitHubUrlPresenter {

    private GitHubUrlView gitHubUrlView;

    private GitHubService gitHubService;
    private DisposableSingleObserver<List<Issue>> disposable;

    @Inject
    public GitHubUrlPresenter(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    void setView(GitHubUrlView githubUrlView) {
        this.gitHubUrlView = githubUrlView;
    }

    void onLoadIssuesClicked(String repoUrl) {

        if (TextUtils.isEmpty(repoUrl)) {
            gitHubUrlView.showErrorMessage("Invalid Repo Url. Please enter a valid Url");
        } else {

            String[] splitStrings = splitUrl(repoUrl);
            String owner = splitStrings[0];
            String repo = splitStrings[1];

            gitHubUrlView.showLoader();

            disposable = gitHubService.getIssuesByRepo(owner, repo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Issue>>() {
                        @Override
                        public void onSuccess(@NonNull List<Issue> issues) {
                            gitHubUrlView.hideLoader();
                            gitHubUrlView.showIssues(issues);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            gitHubUrlView.hideLoader();
                            gitHubUrlView.showErrorMessage(e.getMessage());
                        }
                    });
        }
    }

    void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        gitHubUrlView = null;
    }


    private String[] splitUrl(String repoUrl) {
        return repoUrl.split("/");
    }

    /**
     * Created by vishesh on 28/6/17.
     */
    public static interface GitHubUrlView {

        void showErrorMessage(String errorMessage);

        void showLoader();

        void hideLoader();

        void showIssues(List<Issue> issues);
    }
}
