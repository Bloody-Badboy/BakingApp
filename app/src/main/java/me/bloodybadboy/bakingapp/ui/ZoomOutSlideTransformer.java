package me.bloodybadboy.bakingapp.ui;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {

  private static final float MIN_SCALE = 0.85f;
  private static final float MIN_ALPHA = 0.5f;

  @Override public void transformPage(@NonNull View page, float position) {

    int pageWidth = page.getWidth();
    int pageHeight = page.getHeight();

    if (position < -1) {
      page.setAlpha(0);
    } else if (position <= 1) {

      final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
      final float vertMargin = pageHeight * (1 - scaleFactor) / 2;
      final float horzMargin = pageWidth * (1 - scaleFactor) / 2;

      // Center vertically
      page.setPivotY(0.5f * pageHeight);

      if (position < 0) {
        page.setTranslationX(horzMargin - vertMargin / 2);
      } else {
        page.setTranslationX(-horzMargin + vertMargin / 2);
      }

      // Scale the page down (between MIN_SCALE and 1)
      page.setScaleX(scaleFactor);
      page.setScaleY(scaleFactor);

      // Fade the page relative to its size.
      page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
    } else {
      page.setAlpha(0);
    }
  }
}
