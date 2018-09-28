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

    public LiveData<List<Recipe>> getRecipesLiveData() {
        return mBakingRepository.getRecipesLiveData();
    }

    public Recipe getSelectedRecipe() {
        return mBakingRepository.getSelectedRecipe();
    }

    public void updateRecipe(Recipe recipe) {
        mBakingRepository.updateRecipe(recipe);
    }

    public LiveData<List<Ingredient>> getIngredientsLiveData(int recipeId) {
        return mBakingRepository.getIngredientsLiveData(recipeId);
    }

    public void updateIngredient(Ingredient ingredient) {
        mBakingRepository.updateIngredient(ingredient);
    }

    public LiveData<List<Step>> getStepsLiveData(int recipeId) {
        return mBakingRepository.getStepsLiveData(recipeId);
    }
}
