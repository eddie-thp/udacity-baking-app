package org.ethp.udacitybakingapp.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class BakingDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recipes_database";

    private static BakingDatabase INSTANCE;

    // Use the onOpen callback to populate the database with recipes
    // This seems a hack, but is described on: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#11
    // Also on: https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1
    // MY NOTES: This only works, because loadRecipesData code is executed in a separate thread
    // I personally don't like it, in the future, I'd probably move this to the RecipesActivity onCreate method
    private static final RoomDatabase.Callback DATABASE_CALLBACK = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            BakingDataLoader.loadRecipesData(INSTANCE);
        }

    };

    // Created a separate method, so that I could mock it later on for testing
    private static Builder<BakingDatabase> getRoomDatabaseBuilder(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), BakingDatabase.class, DATABASE_NAME);
    }

    public static BakingDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (BakingDatabase.class) {
                if (INSTANCE == null) {
                    Builder<BakingDatabase> builder = getRoomDatabaseBuilder(context);
                    INSTANCE = builder
                            .addCallback(DATABASE_CALLBACK)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract BakingDao getBakingDao();
}
