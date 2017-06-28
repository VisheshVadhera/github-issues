package com.vishesh.githubissues.issues;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vishesh.githubissues.R;
import com.vishesh.githubissues.common.Issue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IssuesActivity extends AppCompatActivity {

    private static final String EXTRA_ISSUES = "EXTRA_ISSUES";

    @BindView(R.id.recycler_view_issues)
    RecyclerView recyclerViewIssues;

    private Unbinder unbinder;

    public static Intent createIntent(Context context, List<Issue> issues) {
        Intent intent = new Intent(context, IssuesActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ISSUES, (ArrayList<? extends Parcelable>) issues);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        unbinder = ButterKnife.bind(this);

        ArrayList<Issue> issues = getIntent().getParcelableArrayListExtra(EXTRA_ISSUES);

        IssuesAdapter issuesAdapter = new IssuesAdapter(this, issues);
        recyclerViewIssues.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIssues.setAdapter(issuesAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
