package org.ethp.udacitybakingapp.activity.recipes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.activity.ingredients.IngredientsActivity;
import org.ethp.udacitybakingapp.activity.step.StepActivity;
import org.ethp.udacitybakingapp.data.database.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.ethp.udacitybakingapp.Constants.EXTRA_RECIPE_ID;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipeNameTextView)
        TextView mRecipeNameTextView;

        @BindView(R.id.ingredientsButton)
        View mIngredientsButton;

        @BindView(R.id.stepsButton)
        View mStepsButton;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mIngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startRecipeActivity(view, IngredientsActivity.class);
                }
            });

            mStepsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startRecipeActivity(view, StepActivity.class);
                }
            });
        }

        private void startRecipeActivity(View view, Class<?> activityClass) {
            int position = getAdapterPosition();
            Intent intent = new Intent(view.getContext(), activityClass);
            intent.putExtra(EXTRA_RECIPE_ID, mRecipes.get(position).getId());
            view.getContext().startActivity(intent);
        }

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeItemView = inflater.inflate(R.layout.item_recipe, parent, false);

        RecipeViewHolder viewHolder = new RecipeViewHolder(recipeItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.mRecipeNameTextView.setText(recipe.getName());
//        recipeItemView.setBackground();


    }

    void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int recipeCount = 0;
        if (mRecipes != null) {
            recipeCount = mRecipes.size();
        }
        return recipeCount;
    }
}
