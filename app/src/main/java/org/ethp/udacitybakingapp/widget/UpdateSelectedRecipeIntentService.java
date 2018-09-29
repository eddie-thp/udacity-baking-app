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
import org.ethp.udacitybakingapp.data.database.Recipe;

public class UpdateSelectedRecipeIntentService extends IntentService {

    private static final String LOG_TAG = UpdateSelectedRecipeIntentService.class.getSimpleName();

    public UpdateSelectedRecipeIntentService() {
        super("UpdateSelectedRecipeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context applicationContext = getApplicationContext();
        BakingRepository bakingRepository = new BakingRepository(applicationContext);
        Recipe recipe = bakingRepository.getSelectedRecipe();

        String selectedRecipe = getString(R.string.widget_error_no_recipe_selected);

        if (recipe != null) {
            selectedRecipe = recipe.getName();
            Log.d(LOG_TAG, "Selected recipe " + selectedRecipe);
        } else {
            Log.d(LOG_TAG, "Selected recipe is NULL.");
        }

        // Update widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));

        IngredientsWidgetProvider.updateWidgetTitle(applicationContext, appWidgetManager, appWidgetIds, selectedRecipe);

        // Notify that the list of ingredients needs to be updated
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredientsList);
    }
}
