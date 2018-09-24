package org.ethp.udacitybakingapp.activity.recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Recipe;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity {

    @BindView(R.id.recipesRecyclerView)
    RecyclerView mRecipesRecyclerView;

    @BindView(R.id.recipeMenuFAB)
    FloatingActionButton mRecipeMenuFAB;

    @BindView(R.id.stepsMenuFAB)
    FloatingActionButton mStepsMenuFAB;

    @BindView(R.id.stepsMenuTextView)
    TextView mStepsMenuTextView;

    @BindView(R.id.ingredientsMenuFAB)
    FloatingActionButton mIngredientsMenuFAB;

    @BindView(R.id.ingredientsMenuTextView)
    TextView mIngredientsMenuTextView;

    private RecipesAdapter mRecipesAdapter;

    private BakingViewModel mBakingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // Bind views
        ButterKnife.bind(this);

        // Setup views
        setupFABMenu();
        setupRecipesRecyclerView();

    }

    void setupFABMenu() {
        mRecipeMenuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO check if recipe is selected
                final int visibility = (mStepsMenuFAB.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                mStepsMenuFAB.setVisibility(visibility);
                mStepsMenuTextView.setVisibility(visibility);
                mIngredientsMenuFAB.setVisibility(visibility);
                mIngredientsMenuTextView.setVisibility(visibility);
            }
        });
    }

    void setupRecipesRecyclerView()
    {
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
