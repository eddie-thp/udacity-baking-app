package org.ethp.udacitybakingapp;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import org.ethp.udacitybakingapp.data.database.BakingDao;
import org.ethp.udacitybakingapp.data.database.BakingDatabase;
import org.ethp.udacitybakingapp.data.database.BakingDatabaseOnCreateCallback;
import org.ethp.udacitybakingapp.data.database.Ingredient;
import org.ethp.udacitybakingapp.data.database.Recipe;
import org.ethp.udacitybakingapp.data.database.Step;
import org.ethp.udacitybakingapp.helpers.RecipesDataLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@RunWith(AndroidJUnit4.class)
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(BakingDatabase.class)
public class BakingDatabaseCreationTest {

    // Didn't work :(
/*
    private Context mContext;

    private BakingDatabase mBakingDatabase;

    @Before
    public void createDb() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();

        PowerMockito.spy(BakingDatabase.class);
        PowerMockito.doReturn(Room.inMemoryDatabaseBuilder(mContext, BakingDatabase.class)).when(BakingDatabase.class, "getRoomDatabaseBuilder");
        mBakingDatabase = BakingDatabase.getInstance(mContext);
    }

    @After
    public void closeDb() throws IOException {
        mBakingDatabase.close();
    }

    @Test
    public void testDatabaseOnCreateCallback() throws Exception {

        assertTrue(true);
        assertTrue(false);

        final BakingDao dao = mBakingDatabase.getBakingDao();

        LiveData<List<Recipe>> recipesLiveData = dao.getRecipes();
        List<Recipe> recipes = recipesLiveData.getValue();

        // Verify that the database doesn't contain recipes
        assertTrue(recipesLiveData.getValue().isEmpty());

        LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(Mockito.mock(LifecycleOwner.class));

        recipesLiveData.observeForever(new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                assertFalse(recipes.isEmpty());

                for (Recipe recipe : recipes) {
                    System.out.println("ID : " + recipe.getId());
                    System.out.println("NAME : " + recipe.getName());

                    List<Ingredient> ingredients = dao.getIngredients(recipe.getId());
                    for (Ingredient ingredient : ingredients) {
                        System.out.println("INGREDIENT : " + ingredient.getIngredient());
                        assertEquals(ingredient.getRecipeId(), recipe.getId());
                    }

                    List<Step> steps = dao.getSteps(recipe.getId());
                    for (Step step : steps) {
                        System.out.println("STEP : " + step.getDescription());
                        assertEquals(step.getRecipeId(), recipe.getId());
                    }
                }
            }
        });

        RecipesDataLoader.loadRecipesData(mContext);

    }
*/
}
