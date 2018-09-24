package org.ethp.udacitybakingapp.activity.recipes;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
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

import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static org.ethp.udacitybakingapp.Constants.EXTRA_RECIPE_ID;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private static final String PLACEHOLDER_SERVICE_URL = "https://loremflickr.com/320/240/recipe";

    private List<Recipe> mRecipes;

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

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
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View recipeItemView = inflater.inflate(R.layout.item_recipe, parent, false);

        recipeItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animation from Udacity Lesson 2.15 (Surfaces)
                // https://github.com/udacity/ud862-samples/blob/master/SimplePaperTransformations/app/src/main/java/com/example/android/papertransformations/MainActivity.java
                int finalRadius = (int)Math.hypot(v.getWidth()/2, v.getHeight()/2);

                TransitionPropagation transitionPropagation = new CircularPropagation();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(v, (int) v.getWidth() / 2, (int) v.getHeight() / 2, 0, finalRadius);

                    v.setBackgroundColor(context.getResources().getColor(R.color.primaryLightColor));

                    anim.start();
                }
                else
                {
                    v.setBackgroundColor(context.getResources().getColor(R.color.primaryLightColor));
                }


            }
        });

        RecipeViewHolder viewHolder = new RecipeViewHolder(recipeItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.mRecipeNameTextView.setText(recipe.getName());
        holder.mServingsTextView.setText(Integer.toString(recipe.getServings()));

        String recipeImage = recipe.getImage();

        final boolean isRecipeImageEmpty = Strings.isNullOrEmpty(recipeImage);
        if (isRecipeImageEmpty)
        {
            recipeImage = PLACEHOLDER_SERVICE_URL;
        }

        RequestBuilder glideReqBuilder = Glide.with(holder.itemView)
                .load(recipeImage);

        if (isRecipeImageEmpty) {
            glideReqBuilder
                    .apply(RequestOptions.signatureOf(new ObjectKey(recipe)))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 2)));
        }

        glideReqBuilder.into(holder.mRecipeImageView);

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
