package org.ethp.udacitybakingapp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 *
 * This class is based on: https://github.com/googlecodelabs/android-build-an-app-architecture-components/blob/arch-training-steps/app/src/main/java/com/example/android/sunshine/AppExecutors.java
 */
public class AppExecutors {

    /**
     * Executes Runnables on the UI Thread
     */
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    private static final Object LOCK = new Object();

    private static AppExecutors sInstance;

    private final Executor mDiskExecutor;
    private final Executor mUiThreadExecutor;
    private final Executor mNetworkExecutor;

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new AppExecutors(
                            new MainThreadExecutor(),
                            Executors.newSingleThreadExecutor(),
                            Executors.newFixedThreadPool(3));
                }
            }
        }
        return sInstance;
    }

    private AppExecutors(Executor uiThreadExecutor, Executor diskExecutor, Executor networkExecutor) {
        this.mUiThreadExecutor = uiThreadExecutor;
        this.mDiskExecutor = diskExecutor;
        this.mNetworkExecutor = networkExecutor;
    }

    public Executor getDiskExecutor() {
        return mDiskExecutor;
    }

    public Executor getUiThreadExecutor() {
        return mUiThreadExecutor;
    }

    public Executor getNetworkExecutor() {
        return mNetworkExecutor;
    }

}
