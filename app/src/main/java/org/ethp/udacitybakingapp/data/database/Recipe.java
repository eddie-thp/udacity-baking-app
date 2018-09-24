package org.ethp.udacitybakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * POJO Room model class representing a recipe
 */
@Entity(tableName = "recipes")
public class Recipe {

    private static final String LOG_TAG = Recipe.class.getSimpleName();

    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";
    private static final String JSON_SERVINGS = "servings";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_INGREDIENTS = "ingredients";
    private static final String JSON_STEPS = "steps";

    @PrimaryKey
    private int id;
    private String name;
    private int servings;
    private String image;
    private boolean selected;

    @Ignore
    private List<Ingredient> ingredients;

    @Ignore
    private List<Step> steps;

    @Ignore
    private Recipe() {
        this.selected = false;
    }

    public Recipe(int id, String name, int servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(this.ingredients);
    }

    public void setIngredients(List<Ingredient> ingredients)
    {
        this.ingredients = new ArrayList<>(ingredients);
    }

    public List<Step> getSteps() {
        return Collections.unmodifiableList(this.steps);
    }

    public void setSteps(List<Step> steps)
    {
        this.steps = new ArrayList<>(steps);
    }

    public static List<Recipe> fromStringJSONArray(String stringJSONArray) throws JSONException{
        List<Recipe> recipes = new ArrayList<>();
        JSONArray recipesJSONArray = new JSONArray(stringJSONArray);

        for (int i = 0; i < recipesJSONArray.length(); i++) {
            try {
                JSONObject recipeJSON = recipesJSONArray.getJSONObject(i);
                Recipe recipe = fromJSONObject(recipeJSON);
                recipes.add(recipe);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to parse recipe json", e);
            }
        }

        return recipes;
    }

    private static Recipe fromJSONObject(JSONObject recipeJSON) throws JSONException {
        Recipe recipe = new Recipe();
        recipe.setId(recipeJSON.getInt(JSON_ID));
        recipe.setName(recipeJSON.getString(JSON_NAME));
        recipe.setServings(recipeJSON.getInt(JSON_SERVINGS));
        recipe.setImage(recipeJSON.getString(JSON_IMAGE));
        recipe.ingredients = Ingredient.fromJSONArray(recipe, recipeJSON.getJSONArray(JSON_INGREDIENTS));
        recipe.steps = Step.fromJSONArray(recipe, recipeJSON.getJSONArray(JSON_STEPS));
        return recipe;
    }
}
