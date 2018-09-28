package org.ethp.udacitybakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.ethp.udacitybakingapp.AppExecutors;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.BakingRepository;
import org.ethp.udacitybakingapp.data.database.Ingredient;
import org.ethp.udacitybakingapp.data.database.Recipe;

import java.util.List;

public class UpdateSelectedRecipeIntentService extends IntentService {

    private static final String LOG_TAG = UpdateSelectedRecipeIntentService.class.getSimpleName();

    public UpdateSelectedRecipeIntentService() {
        super("UpdateSelectedRecipeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BakingRepository bakingRepository = new BakingRepository(getApplicationContext());
        Recipe recipe = bakingRepository.getSelectedRecipe();

        String selectedRecipe = getString(R.string.widget_error_no_recipe_selected);

        if (recipe != null) {
            int recipeId = recipe.getId();
            selectedRecipe = recipe.getName();


//            List<Ingredient> ingredients = bakingRepository.getIngredients(recipeId);
//            if (ingredients != null && ingredients.size() > 0) {
//                Ingredient ingredient = ingredients.get(0);
//            }
        } else {
            Log.d(LOG_TAG, "Selected recipe is NULL.");
        }

        // Update widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));

        IngredientsWidgetProvider.updateWidgetTitle(this, appWidgetManager, appWidgetIds, selectedRecipe);


        // appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredientsList);
    }
}
