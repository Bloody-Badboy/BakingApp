package me.bloodybadboy.bakingapp.ui;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

  private int spanCount;
  private int spacing;

  public GridSpacingItemDecoration(int spanCount, int spacing) {
    this.spanCount = spanCount;
    this.spacing = spacing;
  }

  @Override
  public void getItemOffsets(@NotNull Rect outRect, @NotNull View view,
      @NotNull RecyclerView parent,
      @NotNull RecyclerView.State state) {
    int position = parent.getChildAdapterPosition(view);

    int column = position % spanCount;

    outRect.left = spacing - column * spacing / spanCount;
    outRect.right = (column + 1) * spacing / spanCount;

    if (position < spanCount) {
      outRect.top = spacing;
    }
    outRect.bottom = spacing;
  }
}

