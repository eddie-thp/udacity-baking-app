package org.ethp.udacitybakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * POJO Room model class representing a recipe's ingredient
 */
@Entity(tableName = "ingredients",
        primaryKeys = {"ingredient", "recipeId"},
        foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = CASCADE))
public class Ingredient {

    private static final String LOG_TAG = Ingredient.class.getSimpleName();

    private static final String JSON_INGREDIENT = "ingredient";
    private static final String JSON_MEASURE = "measure";
    private static final String JSON_QUANTITY = "quantity";

    @NonNull
    String ingredient;
    String measure;
    int quantity;
    int recipeId;
    boolean checked;

    @Ignore
    private Ingredient() {
    }

    public Ingredient(String ingredient, String measure, int quantity, int recipeId) {
        this.ingredient = ingredient;
        this.measure = measure;
        this.quantity = quantity;
        this.recipeId = recipeId;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public boolean isChecked () {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static List<Ingredient> fromJSONArray(Recipe recipe, JSONArray jsonArray) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject ingredientJSON = jsonArray.getJSONObject(i);

                Ingredient ingredient = new Ingredient();
                ingredient.setRecipeId(recipe.getId());
                ingredient.setIngredient(ingredientJSON.getString(JSON_INGREDIENT));
                ingredient.setMeasure(ingredientJSON.getString(JSON_MEASURE));
                ingredient.setQuantity(ingredientJSON.getInt(JSON_QUANTITY));
                ingredient.setChecked(false);

                ingredients.add(ingredient);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to parse step JSON", e);
            }
        }
        return ingredients;
    }
}
