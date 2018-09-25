package org.ethp.udacitybakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BakingDao {

    @Insert
    void insertRecipes(List<Recipe> recipe);

    @Insert
    void insertIngredients(List<Ingredient> ingredients);

    @Insert
    void insertSteps(List<Step> steps);

    @Update
    void updateRecipe(Recipe recipe);

    @Update
    void updateIngredient(Ingredient ingredient);

    @Query("SELECT * FROM recipes ORDER BY id")
    LiveData<List<Recipe>> getRecipes();

    @Query("SELECT COUNT(*) FROM recipes")
    int getRecipeCount();

    @Query("SELECT * from recipes WHERE selected = 1")
    Recipe getSelectedRecipe();

    @Query("SELECT * FROM ingredients WHERE recipeId=:recipeId")
    LiveData<List<Ingredient>> getIngredients(final int recipeId);

    @Query("SELECT * FROM steps WHERE recipeId=:recipeId")
    LiveData<List<Step>> getSteps(final int recipeId);

}
