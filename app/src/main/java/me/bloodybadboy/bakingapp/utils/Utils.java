package me.bloodybadboy.bakingapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import me.bloodybadboy.bakingapp.R;

public final class Utils {

  public static int calculateNoOfColumns(Context context) {
    DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
    float itemWidth = context.getResources().getDimension(R.dimen.recipe_grid_item_min_width);
    int col = (int) (mDisplayMetrics.widthPixels / itemWidth) < 0 ? context.getResources()
        .getInteger(R.integer.recipe_grid_min_cols)
        : (int) (mDisplayMetrics.widthPixels / itemWidth);
    int maxCols = context.getResources().getInteger(R.integer.recipe_grid_max_cols);
    return col > maxCols ? maxCols : col;
  }

  public static int getStatusBarHeight(@NonNull Context context) {
    int result = 0;
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = context.getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  public static String convertToCamelCase(String str) {
    if (TextUtils.isEmpty(str)) {
      return str;
    }
    str = str.toLowerCase();
    final char[] buffer = str.toCharArray();
    boolean capitalizeNext = true;
    for (int i = 0; i < buffer.length; i++) {
      final char ch = buffer[i];
      if (Character.isWhitespace(ch) || ch == '(') {
        capitalizeNext = true;
      } else if (capitalizeNext) {
        buffer[i] = Character.toTitleCase(ch);
        capitalizeNext = false;
      }
    }
    return new String(buffer);
  }
}
