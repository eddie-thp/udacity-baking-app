package org.ethp.udacitybakingapp.activity.ingredients;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.ethp.udacitybakingapp.Constants;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Ingredient;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {

    @BindView(R.id.ingredientsRecyclerView)
    RecyclerView mIngredientsRecyclerView;

    private IngredientsAdapter mIngredientsAdapter;

    private BakingViewModel mBakingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        // Bind views
        ButterKnife.bind(this);

        // Setup views
        mIngredientsAdapter = new IngredientsAdapter();
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup view model
        mBakingViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);

        int recipeId = getIntent().getIntExtra(Constants.EXTRA_RECIPE_ID, -1);
        if (recipeId >= 0) {
            mBakingViewModel.getIngredients(recipeId).observe(this, new Observer<List<Ingredient>>() {
                @Override
                public void onChanged(@Nullable List<Ingredient> ingredients) {
                    mIngredientsAdapter.setIngredients(ingredients);
                }
            });
        }
    }
}
