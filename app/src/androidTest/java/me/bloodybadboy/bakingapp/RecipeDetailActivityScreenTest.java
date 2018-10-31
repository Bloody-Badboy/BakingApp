package me.bloodybadboy.bakingapp;

import android.content.Intent;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import me.bloodybadboy.bakingapp.data.MockRecipeStore;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.ui.details.RecipeDetailActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE;
import static me.bloodybadboy.bakingapp.RecipesActivityScreenTest.RECIPE_NAME_BROWNIES;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityScreenTest {

  @Rule
  public ActivityTestRule<RecipeDetailActivity> activityTestRuleActivity =
      new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
        @Override protected Intent getActivityIntent() {
          Recipe recipe = MockRecipeStore.getMockRecipe();
          if (recipe == null) {
            return null;
          }
          Intent intent = new Intent();
          intent.putExtra(ARG_RECIPE, recipe);
          return intent;
        }
      };

  @Test
  public void checkToolbar_ShowsRecipeName() {
    onView(allOf(instanceOf(CollapsingToolbarLayout.class),
        withContentDescription(RECIPE_NAME_BROWNIES))).check(
        matches(isDisplayed()));
  }

  @Test
  public void clickIngredientsTab_ShowsIngredientsList() {
    collapseAndSelectTabWithText(R.string.tab_title_ingredients);

    onView(withId(R.id.rv_ingredients)).perform(scrollToPosition(4))
        .check(matches(isDisplayed()));
    onView(allOf(instanceOf(AppCompatTextView.class),
        withText("Large Eggs"))).check(matches(isDisplayed()));

    onView(withId(R.id.rv_ingredients)).perform(scrollToPosition(7))
        .check(matches(isDisplayed()));
    onView(allOf(instanceOf(AppCompatTextView.class),
        withText("Salt"))).check(matches(isDisplayed()));
  }

  @Test
  public void clickStepsTab_ShowsStepsList() {
    collapseAndSelectTabWithText(R.string.tab_title_steps);

    onView(withId(R.id.rv_steps)).perform(scrollToPosition(4))
        .check(matches(isDisplayed()));
    onView(allOf(instanceOf(AppCompatTextView.class),
        withText("Step 4"))).check(matches(isDisplayed()));
    onView(allOf(instanceOf(AppCompatTextView.class),
        withText("Mix together dry ingredients."))).check(matches(isDisplayed()));
  }

  @Test
  public void clickStepItem_OpensStepDetails() {
    collapseAndSelectTabWithText(R.string.tab_title_steps);

    onView(withId(R.id.rv_steps)).perform(actionOnItemAtPosition(4, click()));

    onView(withId(R.id.fab_step_details_previous)).check(matches(isDisplayed()));
    onView(withId(R.id.fab_step_details_next)).check(matches(isDisplayed()));
  }

  private void collapseAndSelectTabWithText(@StringRes int resourceId) {
    onView(withId(R.id.appbar_layout)).perform(click(), swipeUp());

    onView(withId(R.id.tabs)).check(matches(isCompletelyDisplayed()));

    onView(allOf(
        withText(resourceId),
        isDescendantOfA(withId(R.id.tabs))
    )).perform(click());
  }
}
