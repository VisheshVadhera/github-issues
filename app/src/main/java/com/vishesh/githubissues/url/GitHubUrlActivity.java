package com.vishesh.githubissues.url;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.vishesh.githubissues.MainApplication;
import com.vishesh.githubissues.R;
import com.vishesh.githubissues.common.Issue;
import com.vishesh.githubissues.issues.IssuesActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GitHubUrlActivity
        extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;

    @BindView(R.id.edit_issues_url)
    EditText editTextIssuesUrl;
    @BindView(R.id.view_loader)
    SpinKitView viewLoader;
    @BindView(R.id.button_load_issues)
    Button buttonLoadIssues;

    private Unbinder unbinder;

    @Inject
    GitHubUrlViewModel gitHubUrlViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_github_url);
        unbinder = ButterKnife.bind(this);

        ((MainApplication) getApplication()).getInjector().inject(this);

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    @OnClick(R.id.button_load_issues)
    void onLoadIssuesClicked() {
        String repoUrl = editTextIssuesUrl.getText().toString();

        if (TextUtils.isEmpty(repoUrl)) {
            showErrorMessage("Invalid Url. Please enter a valid Repo Url");
        } else {
            compositeDisposable.add(gitHubUrlViewModel
                    .getIssues(repoUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            showLoader();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            hideLoader();
                        }
                    })
                    .subscribeWith(new DisposableSingleObserver<List<Issue>>() {
                        @Override
                        public void onSuccess(@io.reactivex.annotations.NonNull List<Issue> issues) {
                            showIssues(issues);
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            showErrorMessage(e.getMessage());
                        }
                    }));
        }
    }

    private void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void showLoader() {
        viewLoader.setVisibility(View.VISIBLE);
        buttonLoadIssues.setEnabled(false);
        editTextIssuesUrl.setEnabled(false);
    }

    private void hideLoader() {
        viewLoader.setVisibility(View.GONE);
        buttonLoadIssues.setEnabled(true);
        editTextIssuesUrl.setEnabled(true);
    }

    private void showIssues(List<Issue> issues) {
        Intent intent = IssuesActivity.createIntent(this, issues);
        startActivity(intent);
    }
}
