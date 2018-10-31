package me.bloodybadboy.bakingapp.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

public final class AnimationUtils {
  private AnimationUtils() {
    throw new AssertionError("Can't create instance of a utility class.");
  }

  public static void runSlideInUp(View view, long duration) {
    ViewGroup parent = (ViewGroup) view.getParent();
    int distance = parent.getHeight() - view.getTop();

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(
        ObjectAnimator.ofFloat(view, "alpha", 0, 1),
        ObjectAnimator.ofFloat(view, "translationY", distance, 0)
    );

    animatorSet.setDuration(duration);
    animatorSet.start();
  }
}
