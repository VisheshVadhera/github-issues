package com.vishesh.githubissues.issues;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vishesh.githubissues.R;
import com.vishesh.githubissues.common.Issue;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishesh on 28/6/17.
 */

class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder> {

    private Context context;
    private List<Issue> issues;

    public IssuesAdapter(Context context, List<Issue> issues) {
        this.context = context;
        this.issues = issues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.issue_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Issue issue = issues.get(position);

        holder.textComments.setText(String.format(Locale.ENGLISH, "No. of comments: %d", issue.getComments()));
        holder.textIssueNumber.setText(String.format(Locale.ENGLISH, "Issue id: %d", issue.getNumber()));
        holder.textIssueTitle.setText(String.format(Locale.ENGLISH, "Title: %s", issue.getTitle()));
        holder.textState.setText(String.format(Locale.ENGLISH, "State: %s", issue.getState()));
        holder.textUser.setText(String.format(Locale.ENGLISH, "Opened By: %s", issue.getUser().getLogin()));
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_issue_number)
        TextView textIssueNumber;
        @BindView(R.id.text_issue_title)
        TextView textIssueTitle;
        @BindView(R.id.text_user)
        TextView textUser;
        @BindView(R.id.text_state)
        TextView textState;
        @BindView(R.id.text_comments)
        TextView textComments;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
