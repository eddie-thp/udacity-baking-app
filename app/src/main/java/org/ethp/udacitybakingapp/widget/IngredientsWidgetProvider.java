package org.ethp.udacitybakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.activity.recipes.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = IngredientsWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        // Request update to the recipe name
        requestUpdateSelectedRecipe(context);

        // Request update to the list of ingredients
        requestIngredientsUpdate(context, views);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private void requestIngredientsUpdate(Context context, RemoteViews remoteViews) {
        Intent intent = new Intent(context, IngredientsListRemoteViewsService.class);
        remoteViews.setRemoteAdapter(R.id.ingredientsList, intent);

        // Start the pending intent template to start the service that updates the ingredient check state
        Intent updateIngredientCheckStateIntent = new Intent(context, UpdateIngredientCheckStateService.class);
        PendingIntent updateIngredientCheckStatePendingIntent = PendingIntent.getService(context, 0, updateIngredientCheckStateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.ingredientsList, updateIngredientCheckStatePendingIntent);
    }

    public static void requestUpdateSelectedRecipe(Context context) {
        Intent intent = new Intent(context, UpdateSelectedRecipeIntentService.class);
        context.startService(intent);
    }

    public static void updateWidgetTitle(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String title) {
        for (int appWidgetId : appWidgetIds) {
            updateWidgetTitle(context, appWidgetManager, appWidgetId, title);
        }
    }

    public static void updateWidgetTitle(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String title) {
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        views.setTextViewText(R.id.selectedRecipe, title);

        Intent intent = new Intent(context, RecipesActivity.class);
        PendingIntent recipesActivityPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.selectedRecipe, recipesActivityPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
