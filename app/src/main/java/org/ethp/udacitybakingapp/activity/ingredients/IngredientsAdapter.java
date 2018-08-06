package org.ethp.udacitybakingapp.activity.ingredients;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredients;

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredientNameTextView)
        TextView mIngredientNameTextView;

        @BindView(R.id.quantityTextView)
        TextView mQuantityTextView;

        @BindView(R.id.measurementTextView)
        TextView mMeasurementTextView;

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
        Ingredient ingredient = mIngredients.get(position);
        holder.mIngredientNameTextView.setText(ingredient.getIngredient());
        holder.mQuantityTextView.setText(String.valueOf(ingredient.getQuantity()));
        holder.mMeasurementTextView.setText(ingredient.getMeasure());
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
