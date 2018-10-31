package me.bloodybadboy.bakingapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import me.bloodybadboy.bakingapp.ui.recipes.RecipesActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipesActivityScreenTest {

  static final String RECIPE_NAME_NUTELLA_PIE = "Nutella Pie";
  static final String RECIPE_NAME_BROWNIES = "Brownies";
  static final String RECIPE_NAME_YELLOW_CAKE = "Yellow Cake";
  static final String RECIPE_NAME_CHEESECAKE = "Cheesecake";
  @Rule
  public ActivityTestRule<RecipesActivity> activityTestRuleActivity =
      new ActivityTestRule<>(RecipesActivity.class);
  private IdlingResource idlingResource;

  @Before
  public void registerIdlingResource() {
    idlingResource = activityTestRuleActivity.getActivity().getIdlingResource();
    IdlingRegistry.getInstance().register(idlingResource);
  }

  @Before
  public void unregisterIdlingResource() {
    if (idlingResource != null) {
      IdlingRegistry.getInstance().unregister(idlingResource);
    }
  }

  @Test
  public void checkText_RecipeActivity() {
    onView(withId(R.id.rv_recipes)).perform(scrollToPosition(0)).check(matches(isDisplayed()));
    onView(withText(RECIPE_NAME_NUTELLA_PIE)).check(matches(isDisplayed()));

    onView(withId(R.id.rv_recipes)).perform(scrollToPosition(1)).check(matches(isDisplayed()));
    onView(withText(RECIPE_NAME_BROWNIES)).check(matches(isDisplayed()));

    onView(withId(R.id.rv_recipes)).perform(scrollToPosition(2)).check(matches(isDisplayed()));
    onView(withText(RECIPE_NAME_YELLOW_CAKE)).check(matches(isDisplayed()));

    onView(withId(R.id.rv_recipes)).perform(scrollToPosition(3)).check(matches(isDisplayed()));
    onView(withText(RECIPE_NAME_CHEESECAKE)).check(matches(isDisplayed()));
  }

  @Test
  public void clickRecipeListItem_OpensRecipeDetailsActivity() {
    onView(withId(R.id.rv_recipes)).perform(scrollToPosition(1));
    onView(
        allOf(withId(R.id.btn_list_item_recipe_show),
            ViewMatchers.hasSibling(
                allOf(withId(R.id.tv_list_item_recipe_name), withText(RECIPE_NAME_BROWNIES))
            ))
    ).perform(click());

    onView(allOf(instanceOf(CollapsingToolbarLayout.class),
        withContentDescription(RECIPE_NAME_BROWNIES))).check(
        matches(isDisplayed()));
  }
}
