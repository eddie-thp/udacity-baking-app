package org.ethp.udacitybakingapp.activity.recipes;

import android.animation.Animator;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.transition.CircularPropagation;
import android.support.transition.TransitionPropagation;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.common.base.Strings;

import org.ethp.udacitybakingapp.AppExecutors;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Recipe;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;
import org.ethp.udacitybakingapp.widget.IngredientsWidgetProvider;
import org.ethp.udacitybakingapp.widget.UpdateSelectedRecipeIntentService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static org.ethp.udacitybakingapp.Constants.EXTRA_RECIPE_ID;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private static final String PLACEHOLDER_SERVICE_URL = "https://loremflickr.com/320/240/recipe";

    private Activity mActivity;

    private BakingViewModel mBakingViewModel;

    private List<Recipe> mRecipes;

    public RecipesAdapter(Activity activity, BakingViewModel bakingViewModel) {
        this.mActivity = activity;
        this.mBakingViewModel = bakingViewModel;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        Recipe mRecipe;

        @BindView(R.id.recipeNameTextView)
        TextView mRecipeNameTextView;

        @BindView(R.id.servingsTextView)
        TextView mServingsTextView;

        @BindView(R.id.recipeImageView)
        ImageView mRecipeImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);

        View recipeItemView = inflater.inflate(R.layout.item_recipe, parent, false);

        final RecipeViewHolder viewHolder = new RecipeViewHolder(recipeItemView);

        recipeItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AppExecutors.getInstance().getDiskExecutor().execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                Recipe selectedRecipe = mBakingViewModel.getSelectedRecipe();

                                // Unset previously selected recipe
                                if (selectedRecipe != null && selectedRecipe != viewHolder.mRecipe) {
                                    selectedRecipe.setSelected(false);
                                    mBakingViewModel.updateRecipe(selectedRecipe);
                                }

                                // Animate the background change
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                // Animation from Udacity Lesson 2.15 (Surfaces)
                                                // https://github.com/udacity/ud862-samples/blob/master/SimplePaperTransformations/app/src/main/java/com/example/android/papertransformations/MainActivity.java
                                                int finalRadius = (int) Math.hypot(v.getWidth() / 2, v.getHeight() / 2);
                                                Animator anim = ViewAnimationUtils.createCircularReveal(v, (int) v.getWidth() / 2, (int) v.getHeight() / 2, 0, finalRadius);


                                                v.setBackgroundColor(mActivity.getResources().getColor(R.color.primaryLightColor));
                                                anim.start();
                                            }
                                        }
                                    });
                                }

                                // Apply the change to the model
                                viewHolder.mRecipe.setSelected(true);
                                mBakingViewModel.updateRecipe(viewHolder.mRecipe);

                                // Update widget recipe
                                IngredientsWidgetProvider.requestUpdateSelectedRecipe(mActivity);

                            }
                        });
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        holder.mRecipe = recipe;
        holder.mRecipeNameTextView.setText(recipe.getName());
        holder.mServingsTextView.setText(Integer.toString(recipe.getServings()));

        String recipeImage = recipe.getImage();

        Glide.with(holder.itemView)
                .load(recipeImage)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(R.mipmap.recipe_placeholder))
                .into(holder.mRecipeImageView);

        if (recipe.getSelected()) {
            // TODO investigate how can animate the itemView through onBindViewHolder
            // java.lang.IllegalStateException: Cannot start this animator on a detached view!
            // https://stackoverflow.com/questions/34836902/clear-animation-in-viewholders-item-recyclerview
            // NOTE: for now, we are animating when the user clicks on the item - it feels like a hack
            holder.itemView.setBackgroundColor(mActivity.getResources().getColor(R.color.primaryLightColor));
        } else {
            holder.itemView.setBackgroundColor(0);
        }
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
