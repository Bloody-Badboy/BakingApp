package me.bloodybadboy.bakingapp.ui.details;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.ViewModelFactory;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.utils.Utils;

import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE;

public class RecipeDetailActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.appbar_layout)
  AppBarLayout appBarLayout;

  private DetailsViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      CollapsingToolbarLayout.LayoutParams layoutParams =
          (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
      layoutParams.setMargins(0, Utils.getStatusBarHeight(this), 0, 0);
      toolbar.setLayoutParams(layoutParams);
    }

    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    viewModel = obtainViewModel(this);
    if (savedInstanceState == null) {

      Recipe recipe = getIntent().getParcelableExtra(ARG_RECIPE);

      RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(recipe);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.recipe_detail_container, fragment)
          .commit();
    }
  }

  public static DetailsViewModel obtainViewModel(FragmentActivity activity) {
    ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
    return ViewModelProviders.of(activity, factory).get(DetailsViewModel.class);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
