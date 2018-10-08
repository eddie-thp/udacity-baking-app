package org.ethp.udacitybakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * POJO Room model class representing a recipe's step
 */
@Entity(tableName = "steps",
        primaryKeys = {"id", "recipeId"},
        foreignKeys = @ForeignKey(entity = Recipe.class,
            parentColumns = "id",
            childColumns = "recipeId",
            onDelete = CASCADE))
public class Step {

    private static final String LOG_TAG = Step.class.getSimpleName();

    private static final String JSON_ID = "id";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_VIDEO_URL = "videoURL";
    private static final String JSON_THUMBNAIL_URL = "thumbnailURL";

    private int id;
    private String description;
    private String videoURL;
    private String thumbnailURL;
    private boolean playing;
    private int recipeId;

    @Ignore
    private Step() {
    }

    public Step(int id, String description, String videoURL, String thumbnailURL, int recipeId) {
        this.id = id;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public static List<Step> fromJSONArray(Recipe recipe, JSONArray jsonArray) {
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject stepJSON = jsonArray.getJSONObject(i);

                Step step = new Step();
                step.setRecipeId(recipe.getId());
                step.setId(stepJSON.getInt(JSON_ID));
                step.setDescription(stepJSON.getString(JSON_DESCRIPTION));
                step.setVideoURL(stepJSON.getString(JSON_VIDEO_URL));
                step.setThumbnailURL(stepJSON.getString(JSON_THUMBNAIL_URL));

                steps.add(step);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to parse step JSON", e);
            }
        }
        return steps;
    }
}
