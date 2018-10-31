package me.bloodybadboy.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import java.util.ArrayList;
import me.bloodybadboy.bakingapp.data.MockRecipeStore;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.ui.step.StepDetailsActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE_NAME;
import static me.bloodybadboy.bakingapp.Constants.ARG_STEPS;
import static me.bloodybadboy.bakingapp.Constants.ARG_STEP_INDEX;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepDetailsActivityScreenTest {

  private int totalPages = -1;
  @Rule
  public ActivityTestRule<StepDetailsActivity> activityTestRuleActivity =
      new ActivityTestRule<StepDetailsActivity>(StepDetailsActivity.class) {
        @Override protected Intent getActivityIntent() {
          Recipe recipe = MockRecipeStore.getMockRecipe();
          if (recipe == null) {
            return null;
          }

          totalPages = recipe.getSteps().size();

          Intent intent = new Intent();
          intent.putParcelableArrayListExtra(ARG_STEPS,
              (ArrayList<? extends Parcelable>) recipe.getSteps());
          intent.putExtra(ARG_STEP_INDEX, 0);
          intent.putExtra(ARG_RECIPE_NAME, recipe.getName());
          return intent;
        }
      };

  @Test
  public void previousFab_HidesOnFirstPage() {
    onView(withId(R.id.fab_step_details_next)).perform(click());

    onView(withId(R.id.fab_step_details_previous)).check(matches(isDisplayed()));

    onView(withId(R.id.fab_step_details_previous)).perform(click())
        .check(matches(not(isDisplayed())));
  }

  @Test
  public void nextFab_HidesOnLastPage() {

    for (int currentPage = 0; currentPage < totalPages - 1; currentPage++) {
      onView(allOf(withId(R.id.fab_step_details_next), isDisplayed())).perform(click());
    }

    onView(withId(R.id.fab_step_details_next)).check(matches(not(isDisplayed())));

    onView(withId(R.id.fab_step_details_previous)).perform(click());

    onView(withId(R.id.fab_step_details_next)).check(matches(isDisplayed()));
  }
}
