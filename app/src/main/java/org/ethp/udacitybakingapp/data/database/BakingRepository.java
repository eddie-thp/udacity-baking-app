package org.ethp.udacitybakingapp.data.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

/**
 * Repository class, similarly to what is seen at: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
 */
public class BakingRepository {

    private BakingDao mBakingDao;

    public BakingRepository(Context context) {
        BakingDatabase db = BakingDatabase.getInstance(context);
        mBakingDao = db.getBakingDao();
    }

    public LiveData<List<Recipe>> getRecipesLiveData() {
        return mBakingDao.getRecipesLiveData();
    }

    public Recipe getSelectedRecipe() {
        return mBakingDao.getSelectedRecipe();
    }

    public void updateRecipe(Recipe recipe) {
        mBakingDao.updateRecipe(recipe);
    }

    public LiveData<List<Ingredient>> getIngredientsLiveData(int recipeId) {
        return mBakingDao.getIngredientsLiveData(recipeId);
    }

    public List<Ingredient> getIngredients(int recipeId) {
        return mBakingDao.getIngredients(recipeId);
    }

    public void updateIngredient(Ingredient ingredient) {
        mBakingDao.updateIngredient(ingredient);
    }

    public LiveData<List<Step>> getStepsLiveData(int recipeId) {
        return mBakingDao.getStepsLiveData(recipeId);
    }

}
