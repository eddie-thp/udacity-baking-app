package org.ethp.udacitybakingapp.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.BakingRepository;
import org.ethp.udacitybakingapp.data.database.Ingredient;
import org.ethp.udacitybakingapp.data.database.Recipe;

import java.util.List;

public class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    BakingRepository mBakingRepository;
    List<Ingredient> mIngredients;

    public IngredientsListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
        mBakingRepository = new BakingRepository(applicationContext);
    }

    @Override
    public void onCreate() {

    }

    // Called when started and when notifyAppWidgetViewDataChange is called
    @Override
    public void onDataSetChanged() {
        Recipe selectedRecipe = mBakingRepository.getSelectedRecipe();
        if (selectedRecipe != null) {
            mIngredients = mBakingRepository.getIngredients(selectedRecipe.getId());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        int count = 0;

        if (mIngredients != null) {
            count = mIngredients.size();
        }

        return count;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = mIngredients.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient_widget_provider);

        // Set checkbox image
        int checkedImageResource = ingredient.isChecked() ? R.drawable.ic_check_box_black_24dp : R.drawable.ic_check_box_outline_blank_black_24dp;
        views.setImageViewResource(R.id.checkedImageView, checkedImageResource);

        // Set ingredient name in text view
        views.setTextViewText(R.id.ingredientNameTextView, ingredient.getIngredient());

        // Create fillInIntent and set OnClick for (image) checkbox and textview
        Intent ingredientFillInIntent = new Intent();
        ingredientFillInIntent.putExtra(UpdateIngredientCheckStateService.EXTRA_INGREDIENT_IDX, position);

        views.setOnClickFillInIntent(R.id.checkedImageView, ingredientFillInIntent);
        views.setOnClickFillInIntent(R.id.ingredientNameTextView, ingredientFillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
