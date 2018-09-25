package org.ethp.udacitybakingapp.activity.recipes;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.ethp.udacitybakingapp.AppExecutors;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.activity.ingredients.IngredientsActivity;
import org.ethp.udacitybakingapp.activity.steps.StepsActivity;
import org.ethp.udacitybakingapp.data.database.Recipe;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.ethp.udacitybakingapp.Constants.EXTRA_RECIPE_ID;

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
            public void onClick(final View v) {
                AppExecutors.getInstance().getDiskExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Recipe selectedRecipe = mBakingViewModel.getSelectedRecipe();

                        if (selectedRecipe == null) {
                            // TODO in the future we may want to move the FAB above, as the snackbar shows up: https://stackoverflow.com/questions/33709953/make-snackbar-push-view-upwards
                            // TODO also check: https://lab.getbase.com/introduction-to-coordinator-layout-on-android/
                            // I've tried the above, but couldn't make it work :(
                            Snackbar.make(v, R.string.error_no_recipe_selected, Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        final int visibility = (mStepsMenuFAB.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setRecipesFABSubMenuVisibility(visibility);
                            }
                        });
                    }
                });
            }
        });

        mStepsMenuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecipeActivity(StepsActivity.class);
            }
        });

        mIngredientsMenuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecipeActivity(IngredientsActivity.class);
            }
        });
    }

    void setRecipesFABSubMenuVisibility(final int visibility) {
        mStepsMenuFAB.setVisibility(visibility);
        mStepsMenuTextView.setVisibility(visibility);
        mIngredientsMenuFAB.setVisibility(visibility);
        mIngredientsMenuTextView.setVisibility(visibility);
    }

    void setupRecipesRecyclerView()
    {
        // Setup view model
        mBakingViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);

        mRecipesAdapter = new RecipesAdapter(this, mBakingViewModel);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);
        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observer view model changes
        mBakingViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mRecipesAdapter.setRecipes(recipes);
            }
        });
    }

    private void startRecipeActivity(final Class<?> activityClass) {
        setRecipesFABSubMenuVisibility(View.INVISIBLE);
        AppExecutors.getInstance().getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Recipe selectedRecipe = mBakingViewModel.getSelectedRecipe();
                Intent intent = new Intent(RecipesActivity.this, activityClass);
                intent.putExtra(EXTRA_RECIPE_ID,selectedRecipe.getId());
                RecipesActivity.this.startActivity(intent);
            }
        });
    }
}
