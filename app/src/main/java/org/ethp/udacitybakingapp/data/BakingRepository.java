package org.ethp.udacitybakingapp.data;

import org.ethp.udacitybakingapp.data.database.BakingDao;
import org.ethp.udacitybakingapp.data.database.Recipe;

import java.util.List;

/**
 * Handles data operations in the Baking application.
 *
 * This class is based on:
 *
 * https://github.com/googlecodelabs/android-build-an-app-architecture-components/blob/arch-training-steps/app/src/main/java/com/example/android/sunshine/data/SunshineRepository.java
 *
 */
public class BakingRepository {

    private static final String LOG_TAG = BakingRepository.class.getSimpleName();

    private static final Object LOCK = new Object();

    private static BakingRepository sInstance;

    private BakingDao bakingDao;

    public synchronized static BakingRepository getInstance(BakingDao bakingDao) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new BakingRepository();
                    sInstance.bakingDao = bakingDao;
                }
            }
        }
        return sInstance;
    }

//    public List<Recipe> getRecipes() {
//        return bakingDao.getRecipes();
//    }

}
