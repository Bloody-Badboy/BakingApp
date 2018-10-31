package me.bloodybadboy.bakingapp.ui.step;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import java.util.Objects;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.StepsItem;
import timber.log.Timber;

import static me.bloodybadboy.bakingapp.Constants.ARG_STEP;

public class StepDetailsPageFragment extends Fragment {

  @BindView(R.id.exo_player_view) PlayerView playerView;
  @BindView(R.id.step_description) TextView stepDescription;
  @BindView(R.id.main_media_frame) FrameLayout mainMediaFrame;

  private long startPosition = C.INDEX_UNSET;
  private int currentWindow = C.INDEX_UNSET;
  private boolean playerFullScreen = false;

  private static final String PLAYER_FULLSCREEN = "player_fullscreen";
  private static final String CURRENT_WINDOW_INDEX = "current_window_index";
  private static final String PLAYBACK_POSITION = "playback_position";

  private Activity activity;
  private Unbinder unbinder;
  private StepsItem step;
  private SimpleExoPlayer exoPlayer;
  private Dialog fullScreenDialog;

  public StepDetailsPageFragment() {
    // Required empty public constructor
  }

  static StepDetailsPageFragment newInstance(StepsItem step) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(ARG_STEP, step);

    StepDetailsPageFragment stepDetailsPageFragment = new StepDetailsPageFragment();
    stepDetailsPageFragment.setArguments(bundle);

    return stepDetailsPageFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getArguments();

    activity = getActivity();
    if (activity == null) {
      throw new IllegalStateException("Fragment must be associated with a activity.");
    }

    if (args != null && args.containsKey(ARG_STEP)) {
      step = args.getParcelable(ARG_STEP);
    } else {
      Toast.makeText(activity, "Step data not available.", Toast.LENGTH_SHORT).show();
      activity.finish();
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_step_details_page, container, false);
    unbinder = ButterKnife.bind(this, rootView);

    stepDescription.setText(step.getDescription());

    if (savedInstanceState != null) {
      startPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
      currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
      playerFullScreen = savedInstanceState.getBoolean(PLAYER_FULLSCREEN, true);
    }

    String videoUrl = step.getVideoURL();
    if (TextUtils.isEmpty(videoUrl)) {
      mainMediaFrame.setVisibility(View.GONE);
    } else {
      initializePlayer(Uri.parse(videoUrl));
    }

    initFullscreenDialog();
    initFullscreenButton();

    return rootView;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (playerFullScreen) {
      openFullscreenDialog();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    releasePlayer();
  }

  @Override public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    Timber.d("onSaveInstanceState() [%s]", step.getStepId());
    Timber.d("startPosition: %s", startPosition);

    outState.putLong(PLAYBACK_POSITION, startPosition);
    outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
    outState.putBoolean(PLAYER_FULLSCREEN, playerFullScreen);
  }


  private void initFullscreenDialog() {

    fullScreenDialog = new Dialog(Objects.requireNonNull(getActivity()),
        android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
      public void onBackPressed() {
        if (playerFullScreen) {
          closeFullscreenDialog();
        }
        super.onBackPressed();
      }
    };
  }

  private void openFullscreenDialog() {

    mainMediaFrame.removeView(playerView);
    fullScreenDialog.addContentView(playerView,
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    playerFullScreen = true;
    fullScreenDialog.show();
  }

  private void closeFullscreenDialog() {
    ((ViewGroup) playerView.getParent()).removeView(playerView);
    mainMediaFrame.addView(playerView);
    playerFullScreen = false;
    fullScreenDialog.dismiss();
  }

  private void initFullscreenButton() {
    PlayerControlView controlView = playerView.findViewById(R.id.exo_controller);
    FrameLayout fullScreenToggleButton = controlView.findViewById(R.id.exo_fullscreen_button);
    fullScreenToggleButton.setOnClickListener(v -> {
      if (!playerFullScreen) {
        openFullscreenDialog();
      } else {
        closeFullscreenDialog();
      }
    });
  }

  private void initializePlayer(Uri videoUri) {
    Timber.d("initializePlayer() [%s]", step.getStepId());
    Timber.d("startPosition: %s", startPosition);
    if (exoPlayer == null) {

      Timber.d("init new ExoPlayer instance");

      exoPlayer = ExoPlayerFactory.newSimpleInstance(
          new DefaultRenderersFactory(activity),
          new DefaultTrackSelector(),
          new DefaultLoadControl());

      playerView.setPlayer(exoPlayer);

      exoPlayer.setPlayWhenReady(true);

      boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
      if (haveStartPosition) {
        exoPlayer.seekTo(currentWindow, startPosition);
      }

      MediaSource mediaSource = buildMediaSource(videoUri);
      exoPlayer.prepare(mediaSource, !haveStartPosition, false);
    }
  }

  private void releasePlayer() {
    Timber.d("releasePlayer() [" + step.getStepId() + "]");
    if (exoPlayer != null) {
      exoPlayer.release();
      startPosition = Math.max(0, exoPlayer.getContentPosition());
      currentWindow = exoPlayer.getCurrentWindowIndex();
      exoPlayer = null;
    }
  }

  private MediaSource buildMediaSource(Uri uri) {
    return new ExtractorMediaSource.Factory(
        new DefaultHttpDataSourceFactory("BakingApp"))
        .createMediaSource(uri);
  }

  @SuppressLint("InlinedApi")
  private void hideSystemUi() {
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  public static String bundleToString(Bundle bundle) {
    if (bundle != null) {
      StringBuilder stringBuilder = new StringBuilder();
      for (String key : bundle.keySet()) {
        Object value = bundle.get(key);
        if (value != null) {
          stringBuilder.append("[");
          stringBuilder.append(
              String.format("%s : %s (%s)", key, value.toString(), value.getClass().getName()));
          stringBuilder.append("] ");
        }
      }
      return stringBuilder.toString();
    }
    return null;
  }
}
