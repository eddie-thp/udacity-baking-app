package org.ethp.udacitybakingapp.data.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Repository class, similarly to what is seen at: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
 */
public class BakingRepository {

    private BakingDao mBakingDao;

    public BakingRepository(Application application) {
        BakingDatabase db = BakingDatabase.getInstance(application);
        mBakingDao = db.getBakingDao();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mBakingDao.getRecipes();
    }

    public LiveData<List<Ingredient>> getIngredients(int recipeId) {
        return mBakingDao.getIngredients(recipeId);
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        return mBakingDao.getSteps(recipeId);
    }

}
