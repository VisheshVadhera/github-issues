package com.vishesh.githubissues.url;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class GitHubUrlActivity
        extends AppCompatActivity
        implements GitHubUrlPresenter.GitHubUrlView {

    @BindView(R.id.edit_issues_url)
    EditText editTextIssuesUrl;
    @BindView(R.id.view_loader)
    SpinKitView viewLoader;
    @BindView(R.id.button_load_issues)
    Button buttonLoadIssues;

    private Unbinder unbinder;

    @Inject
    GitHubUrlPresenter gitHubUrlPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_github_url);
        unbinder = ButterKnife.bind(this);

        ((MainApplication) getApplication()).getInjector().inject(this);

        gitHubUrlPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        gitHubUrlPresenter.onDestroy();
    }

    @OnClick(R.id.button_load_issues)
    void onLoadIssuesClicked() {
        String repoUrl = editTextIssuesUrl.getText().toString();
        gitHubUrlPresenter.onLoadIssuesClicked(repoUrl);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoader() {
        viewLoader.setVisibility(View.VISIBLE);
        buttonLoadIssues.setEnabled(false);
        editTextIssuesUrl.setEnabled(false);
    }

    @Override
    public void hideLoader() {
        viewLoader.setVisibility(View.GONE);
        buttonLoadIssues.setEnabled(true);
        editTextIssuesUrl.setEnabled(true);
    }

    @Override
    public void showIssues(List<Issue> issues) {
        Intent intent = IssuesActivity.createIntent(this, issues);
        startActivity(intent);
    }
}
