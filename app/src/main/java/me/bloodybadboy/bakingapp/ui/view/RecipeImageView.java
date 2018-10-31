package me.bloodybadboy.bakingapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class RecipeImageView extends AppCompatImageView {

  private static final float DEFAULT_ASPECT_RATIO = 1.5f; // 3:2

  public RecipeImageView(Context context) {
    super(context);
  }

  public RecipeImageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public RecipeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @SuppressWarnings("SuspiciousNameCombination")
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int newWidth = getMeasuredWidth();
    int newHeight = (int) (newWidth / DEFAULT_ASPECT_RATIO);
    setMeasuredDimension(newWidth, newHeight);
  }
}
