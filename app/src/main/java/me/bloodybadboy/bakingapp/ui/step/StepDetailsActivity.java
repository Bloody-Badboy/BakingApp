package me.bloodybadboy.bakingapp.ui.step;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.StepsItem;
import timber.log.Timber;

import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE_NAME;
import static me.bloodybadboy.bakingapp.Constants.ARG_STEPS;
import static me.bloodybadboy.bakingapp.Constants.ARG_STEP_INDEX;

public class StepDetailsActivity extends AppCompatActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;

  private List<StepsItem> steps;
  private int stepIndex = -1;
  private String recipeName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_step_details);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    Intent intent = getIntent();

    if (intent.hasExtra(ARG_STEPS) &&
        intent.hasExtra(ARG_STEP_INDEX) &&
        intent.hasExtra(ARG_RECIPE_NAME)
    ) {
      steps = intent.getParcelableArrayListExtra(ARG_STEPS);
      stepIndex = intent.getIntExtra(ARG_STEP_INDEX, -1);
      recipeName = intent.getStringExtra(ARG_RECIPE_NAME);
    } else {
      Toast.makeText(this, "Steps data not available.", Toast.LENGTH_SHORT).show();
      finish();
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(recipeName);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    Timber.d(ARG_STEP_INDEX
        + ": "
        + stepIndex
        + ", "
        + ARG_RECIPE_NAME
        + ": "
        + recipeName);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, StepDetailsFragment.newInstance(steps, stepIndex))
          .commitNow();
    }
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
