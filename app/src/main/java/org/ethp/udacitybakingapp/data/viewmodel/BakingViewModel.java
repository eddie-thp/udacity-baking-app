package org.ethp.udacitybakingapp.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.ethp.udacitybakingapp.data.database.BakingRepository;
import org.ethp.udacitybakingapp.data.database.Ingredient;
import org.ethp.udacitybakingapp.data.database.Recipe;
import org.ethp.udacitybakingapp.data.database.Step;

import java.util.List;

public class BakingViewModel extends AndroidViewModel {

    private BakingRepository mBakingRepository;

    public BakingViewModel(@NonNull Application application) {
        super(application);
        mBakingRepository = new BakingRepository(application);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mBakingRepository.getRecipes();
    }

    public Recipe getSelectedRecipe() {
        return mBakingRepository.getSelectedRecipe();
    }

    public void updateRecipe(Recipe recipe) {
        mBakingRepository.updateRecipe(recipe);
    }

    public LiveData<List<Ingredient>> getIngredients(int recipeId) {
        return mBakingRepository.getIngredients(recipeId);
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        return mBakingRepository.getSteps(recipeId);
    }
}
