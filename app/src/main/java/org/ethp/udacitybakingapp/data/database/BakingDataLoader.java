package org.ethp.udacitybakingapp.data.database;

import android.util.Log;

import org.ethp.udacitybakingapp.AppExecutors;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;

/**
 * Helper class to suppo
 */
public abstract class BakingDataLoader {

    private static final String LOG_TAG = BakingDataLoader.class.getSimpleName();

    private static final String BAKING_DATA_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static void loadRecipesData(final BakingDatabase db) {
        Executor networkExecutor = AppExecutors.getInstance().getNetworkExecutor();
        networkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final BakingDao dao = db.getBakingDao();
                if (dao.getRecipeCount() == 0 ) {
                    try {
                        // Retrieve the JSON from the URL
                        URL url = new URL(BAKING_DATA_URL);
                        URLConnection connection = url.openConnection();
                        InputStream is = connection.getInputStream();
                        Scanner s = new Scanner(is).useDelimiter("\\A");
                        String jsonString = "";
                        if (s.hasNext()) {
                            jsonString = s.next();
                        }
                        s.close();

                        final List<Recipe> recipes = Recipe.fromStringJSONArray(jsonString);

                        if (!recipes.isEmpty()) {
                            AppExecutors.getInstance().getDiskExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    dao.insertRecipes(recipes);
                                    for (Recipe recipe : recipes) {
                                        dao.insertIngredients(recipe.getIngredients());
                                        dao.insertSteps(recipe.getSteps());
                                    }
                                }
                            });
                        }
                    } catch (MalformedURLException malformedURLException) {
                        Log.e(LOG_TAG, "Failed to fetch json data", malformedURLException);
                    } catch (IOException ioException) {
                        Log.e(LOG_TAG, "Failed to fetch json data", ioException);
                    } catch (JSONException jsonException) {
                        Log.e(LOG_TAG, "Failed to fetch json data", jsonException);
                    }
                }
            }
        });
    }
}
