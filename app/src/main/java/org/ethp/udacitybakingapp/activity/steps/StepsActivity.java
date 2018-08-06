package org.ethp.udacitybakingapp.activity.steps;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.ethp.udacitybakingapp.Constants;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Step;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsActivity extends AppCompatActivity {

    @BindView(R.id.stepsRecyclerView)
    RecyclerView mStepsRecyclerView;

    private StepsAdapter mStepsAdapter;

    private BakingViewModel mBakingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // Bind views
        ButterKnife.bind(this);

        // Setup views
        mStepsAdapter = new StepsAdapter();
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup view model
        mBakingViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);

        int recipeId = getIntent().getIntExtra(Constants.EXTRA_RECIPE_ID, -1);
        if (recipeId >= 0) {
            mBakingViewModel.getSteps(recipeId).observe(this, new Observer<List<Step>>() {
                @Override
                public void onChanged(@Nullable List<Step> steps) {
                    mStepsAdapter.setSteps(steps);
                }
            });
        }

    }
}
