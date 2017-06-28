package com.vishesh.githubissues.url;

import com.vishesh.githubissues.common.GitHubService;
import com.vishesh.githubissues.common.Issue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by vishesh on 28/6/17.
 */

class GitHubUrlViewModel {

    private GitHubService gitHubService;

    @Inject
    GitHubUrlViewModel(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    Single<List<Issue>> getIssues(final String repoUrl) {
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
    }


    private String[] splitUrl(String repoUrl) {
        return repoUrl.split("/");
    }
}
