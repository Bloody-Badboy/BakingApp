package me.bloodybadboy.bakingapp.ui.step;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.StepsItem;

import static me.bloodybadboy.bakingapp.Constants.ARG_STEPS;
import static me.bloodybadboy.bakingapp.Constants.ARG_STEP_INDEX;

public class StepDetailsFragment extends Fragment {

  @BindView(R.id.pager_step_details_steps) ViewPager viewPager;
  @BindView(R.id.fab_step_details_previous) FloatingActionButton fabPrevious;
  @BindView(R.id.fab_step_details_next) FloatingActionButton fabNext;

  private List<StepsItem> steps;
  private int stepIndex = -1;
  private int totalSteps = -1;
  private int currentStepIndex = -1;

  public StepDetailsFragment() {
    // Required empty public constructor
  }

  public static StepDetailsFragment newInstance(List<StepsItem> steps, int stepIndex) {
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(ARG_STEPS, (ArrayList<? extends Parcelable>) steps);
    bundle.putInt(ARG_STEP_INDEX, stepIndex);

    StepDetailsFragment fragment = new StepDetailsFragment();
    fragment.setArguments(bundle);

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle args = getArguments();
    Activity activity = getActivity();
    if (activity == null) {
      throw new IllegalStateException("Fragment must be associated with a activity.");
    }

    if (args != null &&
        args.containsKey(ARG_STEPS) &&
        args.containsKey(ARG_STEP_INDEX)) {
      steps = args.getParcelableArrayList(ARG_STEPS);
      currentStepIndex = stepIndex = args.getInt(ARG_STEP_INDEX);
      totalSteps = steps.size();
    } else {
      Toast.makeText(activity, "Steps data not available.", Toast.LENGTH_SHORT).show();
      activity.finish();
    }
  }

  @SuppressLint("ClickableViewAccessibility") @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
    ButterKnife.bind(this, rootView);

    SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
    for (StepsItem step : steps) {
      StepDetailsPageFragment pageFragment = StepDetailsPageFragment.newInstance(step);
      pagerAdapter.addFragment(pageFragment, String.valueOf(step.getStepId()));
    }
    // block scrolling
    viewPager.setOnTouchListener((view, motionEvent) -> true);
    viewPager.setAdapter(pagerAdapter);
    viewPager.setCurrentItem(stepIndex);

    if (currentStepIndex >= totalSteps - 1) {
      fabNext.hide();
    }

    if (currentStepIndex <= 0) {
      fabPrevious.hide();
    }

    return rootView;
  }

  @OnClick(R.id.fab_step_details_previous)
  void onClickPreviousPage() {
    if (currentStepIndex > 0) {
      currentStepIndex--;
      viewPager.setCurrentItem(currentStepIndex);
    }
    if (currentStepIndex <= 0) {
      fabPrevious.hide();
    } else if (currentStepIndex < totalSteps - 1) {
      fabNext.show();
    }
  }

  @OnClick(R.id.fab_step_details_next)
  void onClickNextPage() {
    if (currentStepIndex < totalSteps - 1) {
      currentStepIndex++;
      viewPager.setCurrentItem(currentStepIndex);
    }

    if (currentStepIndex >= totalSteps - 1) {
      fabNext.hide();
    } else if (currentStepIndex >= 0) {
      fabPrevious.show();
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
