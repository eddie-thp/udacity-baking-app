package org.ethp.udacitybakingapp.activity.recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Recipe;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity {

    @BindView(R.id.recipesRecyclerView)
    RecyclerView mRecipesRecyclerView;

    private RecipesAdapter mRecipesAdapter;

    private BakingViewModel mBakingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // Bind views
        ButterKnife.bind(this);

        // Setup views
        mRecipesAdapter = new RecipesAdapter();
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);
        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup view model
        mBakingViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);
        mBakingViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mRecipesAdapter.setRecipes(recipes);
            }
        });
    }
}
