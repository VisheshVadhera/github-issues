package com.vishesh.githubissues.common;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by vishesh on 28/6/17.
 */

public interface GitHubService {

    /*@GET("repos/{owner}/{repo}/issues")
    Single<List<Issue>> getIssuesByRepo(@Path("owner") String owner,
                                        @Path("repo") String repo);*/

    @GET("repos/{owner}/{repo}/issues")
    Observable<List<Issue>> getIssuesByRepo(@Path("owner") String owner,
                                            @Path("repo") String repo);
}
