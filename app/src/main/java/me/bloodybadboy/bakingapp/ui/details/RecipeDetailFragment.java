package me.bloodybadboy.bakingapp.ui.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.ui.ZoomOutSlideTransformer;
import timber.log.Timber;

import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE;

public class RecipeDetailFragment extends Fragment {

  // @BindView(R.id.tabs) TabLayout tabLayout;
  @BindView(R.id.pager) ViewPager viewPager;

  @BindBool(R.bool.two_pane_layout) boolean hasTwoPane;

  private TabLayout tabLayout;
  private Activity activity;
  private Recipe recipe;

  public RecipeDetailFragment() {
  }

  public static RecipeDetailFragment newInstance(Recipe recipe) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(ARG_RECIPE, recipe);

    RecipeDetailFragment fragment = new RecipeDetailFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getArguments();

    activity = getActivity();
    if (activity == null) {
      throw new IllegalStateException("Fragment must be associated with a activity.");
    }

    if (args != null && args.containsKey(ARG_RECIPE)) {
      recipe = args.getParcelable(ARG_RECIPE);
    } else {
      Toast.makeText(activity, "Recipe data not available.", Toast.LENGTH_SHORT).show();
      activity.finish();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    ButterKnife.bind(this, rootView);

    tabLayout = activity.findViewById(R.id.tabs);

    updateUI(recipe);

    return rootView;
  }

  private void updateUI(Recipe recipe) {
    CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.collapsing_toolbar_layout);
    if (appBarLayout != null) {
      appBarLayout.setTitle(recipe.getName());
    }

    ImageView recipeThumb = activity.findViewById(R.id.riv_details_recipe_thumb);
    if (recipeThumb != null) {
      Glide.with(activity).load(recipe.getImage()).into(recipeThumb);
    }

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
    sectionsPagerAdapter.addFragment(IngredientsFragment.newInstance(recipe.getIngredients()),
        getString(R.string.tab_title_ingredients));
    sectionsPagerAdapter.addFragment(StepsFragment.newInstance(recipe.getSteps(), recipe.getName()),
        getString(R.string.tab_title_steps));

    viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
    viewPager.setAdapter(sectionsPagerAdapter);

    if (tabLayout != null) {
      tabLayout.setupWithViewPager(viewPager);
    } else {
      Timber.w("TabLayout == null");
    }
  }

  private static class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitleList = new ArrayList<>();

    SectionsPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override public Fragment getItem(int position) {
      return fragmentList.get(position);
    }

    @Nullable @Override public CharSequence getPageTitle(int position) {
      return fragmentTitleList.get(position);
    }

    void addFragment(Fragment fragment, String title) {
      fragmentList.add(fragment);
      fragmentTitleList.add(title);
    }

    @Override public int getCount() {
      return fragmentList.size();
    }
  }
}
