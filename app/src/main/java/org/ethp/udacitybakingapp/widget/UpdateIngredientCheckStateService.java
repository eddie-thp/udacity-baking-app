package org.ethp.udacitybakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.BakingRepository;
import org.ethp.udacitybakingapp.data.database.Ingredient;
import org.ethp.udacitybakingapp.data.database.Recipe;

import java.util.List;

public class UpdateIngredientCheckStateService extends IntentService {

    private static final String LOG_TAG = UpdateIngredientCheckStateService.class.getSimpleName();

    public static final String EXTRA_INGREDIENT_IDX = "ingredientIdxExtra";

    public UpdateIngredientCheckStateService() {
        super("UpdateIngredientCheckStateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int ingredientIdx = intent.getIntExtra(EXTRA_INGREDIENT_IDX, -1);

        Log.d(LOG_TAG, "Updating ingredient at " + ingredientIdx + " index.");

        if (ingredientIdx >= 0) {

            Context applicationContext = getApplicationContext();
            BakingRepository bakingRepository = new BakingRepository(applicationContext);
            Recipe recipe = bakingRepository.getSelectedRecipe();

            if (recipe != null) {
                List<Ingredient> ingredients = bakingRepository.getIngredients(recipe.getId());
                if (ingredientIdx < ingredients.size()) {
                    Ingredient ingredient = ingredients.get(ingredientIdx);
                    ingredient.setChecked(!ingredient.isChecked());
                    bakingRepository.updateIngredient(ingredient);

                    // Update widget
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));

                    // Notify that the list of ingredients needs to be updated
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredientsList);

                } else {
                    Log.w(LOG_TAG, "Selected recipe doesn't contain ingredient at " + ingredientIdx + " index.");
                }

            } else {
                Log.w(LOG_TAG, "Selected recipe is NULL.");
            }
        }

    }
}
