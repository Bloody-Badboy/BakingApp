package me.bloodybadboy.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.AndroidJUnit4;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.ui.details.RecipeDetailActivity;
import me.bloodybadboy.bakingapp.ui.recipes.RecipesActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE;
import static me.bloodybadboy.bakingapp.RecipesActivityScreenTest.RECIPE_NAME_BROWNIES;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityIntentTest {

  @Rule
  public IntentsTestRule<RecipesActivity> intentsTestRule =
      new IntentsTestRule<>(RecipesActivity.class);

  private IdlingResource idlingResource;

  @Before
  public void stubAllExternalIntents() {
    intending(not(isInternal())).respondWith(
        new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
  }

  @Before
  public void registerIdlingResource() {
    idlingResource = intentsTestRule.getActivity().getIdlingResource();
    IdlingRegistry.getInstance().register(idlingResource);
  }

  @Before
  public void unregisterIdlingResource() {
    if (idlingResource != null) {
      IdlingRegistry.getInstance().unregister(idlingResource);
    }
  }

  @Test
  public void checkIntent_RecipeDetailActivity() {
    onView(withId(R.id.rv_recipes)).perform(scrollToPosition(1));
    onView(
        allOf(withId(R.id.btn_list_item_recipe_show),
            ViewMatchers.hasSibling(
                allOf(withId(R.id.tv_list_item_recipe_name), withText(RECIPE_NAME_BROWNIES))
            ))
    ).perform(click());

    intended(allOf(hasComponent(RecipeDetailActivity.class.getName()),
        hasExtra(is(ARG_RECIPE), any(Recipe.class))));
  }
}
