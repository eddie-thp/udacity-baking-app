package org.ethp.udacitybakingapp.activity.step;

import android.app.NotificationManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.ethp.udacitybakingapp.AppExecutors;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.data.database.Recipe;
import org.ethp.udacitybakingapp.data.database.Step;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepPlayerActivity extends AppCompatActivity implements Player.EventListener {

    private static final String LOG_TAG = StepPlayerActivity.class.getSimpleName();

    public static final String EXTRA_STEP_TO_PLAY = "stepToPlay";

    @BindView(R.id.descriptionTextView)
    TextView mDescriptionTextView;

    @BindView(R.id.playerView)
    PlayerView mExoPlayerView;

    SimpleExoPlayer mExoPlayer;

    private static MediaSessionCompat mMediaSession;

    private PlaybackStateCompat.Builder mStateBuilder;

    private NotificationManager mNotificationManager;

    private BakingViewModel mBakingViewModel;

    private List<Step> mSteps;

    private int mStepToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_player);

        mStepToPlay = getIntent().getIntExtra(EXTRA_STEP_TO_PLAY, 0);

        Log.d(LOG_TAG, "STEP TO PLAY is " + mStepToPlay);

        // Bind views
        ButterKnife.bind(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Setup view model
        mBakingViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);

        AppExecutors.getInstance().getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final Recipe recipe = mBakingViewModel.getSelectedRecipe();

                if (recipe != null) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initializeMediaSession();

                            final LiveData<List<Step>> recipeStepsLiveData = mBakingViewModel.getStepsLiveData(recipe.getId());

                            recipeStepsLiveData.observe(StepPlayerActivity.this, new Observer<List<Step>>() {
                                @Override
                                public void onChanged(@Nullable List<Step> steps) {
                                    // Remove observer after 1st execution, it doesn't matter if the data changes anymore
                                    ((LiveData) recipeStepsLiveData).removeObserver(this);

                                    mSteps = steps;

                                    String userAgent =  Util.getUserAgent(StepPlayerActivity.this, "BakingApp");

                                    ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();

                                    // HACK - storing previous URLs in case I'm loading a bad url - TODO has to be reviewed
                                    String videoURL = "";
                                    for (int i = 0 ; i < mSteps.size(); i++) {
                                        Step step = mSteps.get(i);

                                        if (!step.getVideoURL().isEmpty()) {
                                            videoURL = step.getVideoURL();
                                        } else if (!step.getThumbnailURL().isEmpty()) {
                                            videoURL = step.getThumbnailURL();
                                        }

                                        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                                                new DefaultDataSourceFactory(StepPlayerActivity.this, userAgent),
                                                new DefaultExtractorsFactory(),
                                                null, null);

                                        concatenatingMediaSource.addMediaSource(mediaSource);
                                    }

                                    initializePlayer(concatenatingMediaSource);

                                }
                            });
                        }
                    });
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onSkipToNext() {
            mExoPlayer.seekTo(mSteps.size() - 1);
        }

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }


        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, LOG_TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MediaSessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    public void initializePlayer(MediaSource mediaSource) {
        if (mExoPlayer == null) {
            // TrackSelector trackSelector = new DefaultTrackSelector();

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            mExoPlayer.addListener(this);
            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.prepare(mediaSource);
            // See: https://github.com/google/ExoPlayer/issues/2639, how to seek to a certain track
            mExoPlayer.seekTo(mStepToPlay, 0);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mNotificationManager.cancelAll();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        int index = mExoPlayer.getCurrentPeriodIndex();

        Log.d(LOG_TAG, "TRACK CHANGED - current track: " + index);

        final Step step = mSteps.get(index);
        step.setPlaying(true);

        mDescriptionTextView.setText(step.getDescription());

        AppExecutors.getInstance().getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mBakingViewModel.updateResetPlayingStep();
                mBakingViewModel.updateStep(step);
            }
        });
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
            Log.d(LOG_TAG, "onPlayerStateChanged: PLAYING");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == Player.STATE_READY)){
            Log.d(LOG_TAG, "onPlayerStateChanged: PAUSED");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

        // TODO showNotification(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

}
