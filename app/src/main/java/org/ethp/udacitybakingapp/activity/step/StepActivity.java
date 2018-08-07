package org.ethp.udacitybakingapp.activity.step;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.ethp.udacitybakingapp.Constants;
import org.ethp.udacitybakingapp.R;
import org.ethp.udacitybakingapp.activity.steps.StepsAdapter;
import org.ethp.udacitybakingapp.data.database.Step;
import org.ethp.udacitybakingapp.data.viewmodel.BakingViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {

    @BindView(R.id.descriptionTextView)
    TextView mDescriptionTextView;

    @BindView(R.id.playerView)
    PlayerView mExoPlayerView;

    SimpleExoPlayer mExoPlayer;

    private BakingViewModel mBakingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        // Bind views
        ButterKnife.bind(this);

        // Setup view model
        mBakingViewModel = ViewModelProviders.of(this).get(BakingViewModel.class);

        int recipeId = getIntent().getIntExtra(Constants.EXTRA_RECIPE_ID, -1);
        if (recipeId >= 0) {
            // TODO current step
            mBakingViewModel.getSteps(recipeId).observe(this, new Observer<List<Step>>() {
                @Override
                public void onChanged(@Nullable List<Step> steps) {
                    Step step = steps.get(0);

                    mDescriptionTextView.setText(step.getDescription());
                    Uri thumbnailUri = Uri.parse(step.getThumbnailURL());
                    initializePlayer(step.getVideoURL());
                }
            });
        }
    }

    public void initializePlayer(String uri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(uri),
                    new DefaultDataSourceFactory(this, userAgent),
                    new DefaultExtractorsFactory(),
                    null, null);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
}
