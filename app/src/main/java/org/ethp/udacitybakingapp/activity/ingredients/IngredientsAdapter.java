package org.ethp.udacitybakingapp.activity.ingredients;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.ethp.udacitybakingapp.AppExecutors;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Ingredient;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;
import org.ethp.udacitybakingapp.widget.IngredientsWidgetProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private Context mContext;

    private BakingViewModel mBakingViewModel;

    private List<Ingredient> mIngredients;

    public IngredientsAdapter(Context context, BakingViewModel bakingViewModel) {
        mContext = context;
        mBakingViewModel = bakingViewModel;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredientNameTextView)
        TextView mIngredientNameTextView;

        @BindView(R.id.quantityTextView)
        TextView mQuantityTextView;

        @BindView(R.id.measurementTextView)
        TextView mMeasurementTextView;

        @BindView(R.id.ingredientCheckBox)
        CheckBox mChecked;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View ingredientItemView = inflater.inflate(R.layout.item_ingredient, parent, false);

        IngredientViewHolder viewHolder = new IngredientViewHolder(ingredientItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        final Ingredient ingredient = mIngredients.get(position);
        holder.mIngredientNameTextView.setText(ingredient.getIngredient());
        holder.mQuantityTextView.setText(String.valueOf(ingredient.getQuantity()));
        holder.mMeasurementTextView.setText(ingredient.getMeasure());

        // Remove listener, setChecked and add listener
        holder.mChecked.setOnCheckedChangeListener(null);
        holder.mChecked.setChecked(ingredient.isChecked());
        holder.mChecked.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ingredient.setChecked(isChecked);

                        AppExecutors.getInstance().getDiskExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                mBakingViewModel.updateIngredient(ingredient);

                                // Update widget
                                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, IngredientsWidgetProvider.class));

                                // Notify that the list of ingredients needs to be updated
                                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredientsList);
                            }
                        });
                    }
                }
        );
    }

    void setIngredients(List<Ingredient> ingredients){
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int ingredientCount = 0;
        if (mIngredients != null) {
            ingredientCount = mIngredients.size();
        }
        return ingredientCount;
    }
}
