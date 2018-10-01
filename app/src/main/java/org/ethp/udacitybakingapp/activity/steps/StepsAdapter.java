package org.ethp.udacitybakingapp.activity.steps;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.activity.step.StepPlayerActivity;
import org.ethp.udacitybakingapp.data.database.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private List<Step> mSteps;

    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.playImageView)
        ImageView mPlayImageView;

        @BindView(R.id.descriptionTextView)
        TextView mDescriptionTextView;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stepItemView = inflater.inflate(R.layout.item_step, parent, false);

        StepViewHolder viewHolder = new StepViewHolder(stepItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = mSteps.get(position);

        int visibility = (position == 0 ? View.VISIBLE : View.INVISIBLE);
        holder.mPlayImageView.setVisibility(visibility);

        holder.mDescriptionTextView.setText(step.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StepPlayerActivity.class);
                view.getContext().startActivity(intent);
            }
        });


    }

    void setSteps(List<Step> steps){
        mSteps = steps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int stepsCount = 0;
        if (mSteps != null) {
            stepsCount = mSteps.size();
        }
        return stepsCount;
    }
}
