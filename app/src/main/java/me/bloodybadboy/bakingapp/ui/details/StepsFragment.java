package me.bloodybadboy.bakingapp.ui.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.StepsItem;
import me.bloodybadboy.bakingapp.ui.step.StepDetailsActivity;
import me.bloodybadboy.bakingapp.ui.step.StepDetailsFragment;

import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE_NAME;
import static me.bloodybadboy.bakingapp.Constants.ARG_STEP_INDEX;

public class StepsFragment extends Fragment {

  private static final String ARG_STEPS = "steps";

  @BindView(R.id.rv_steps)
  RecyclerView rvSteps;

  private Activity activity;
  private List<StepsItem> steps;
  private String recipeName;

  public StepsFragment() {
    // Required empty public constructor
  }

  public static Fragment newInstance(List<StepsItem> steps, String recipeName) {
    Bundle bundle = new Bundle();
    bundle.putParcelableArrayList(ARG_STEPS, (ArrayList<? extends Parcelable>) steps);
    bundle.putString(ARG_RECIPE_NAME, recipeName);

    StepsFragment fragment = new StepsFragment();
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

    if (args != null && args.containsKey(ARG_STEPS) && args.containsKey(ARG_RECIPE_NAME)) {
      steps = args.getParcelableArrayList(ARG_STEPS);
      recipeName = args.getString(ARG_RECIPE_NAME);
    } else {
      Toast.makeText(activity, "steps data not available.", Toast.LENGTH_SHORT).show();
      activity.finish();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
    ButterKnife.bind(this, rootView);

    initRecycleView();

    return rootView;
  }

  private void initRecycleView() {
    LinearLayoutManager layoutManager =
        new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
    rvSteps.setHasFixedSize(true);
    rvSteps.setLayoutManager(layoutManager);
    rvSteps.setAdapter(
        new StepsItemRecyclerViewAdapter(getChildFragmentManager(), steps, recipeName));
  }

  public static class StepsItemRecyclerViewAdapter
      extends RecyclerView.Adapter<StepsItemRecyclerViewAdapter.ViewHolder> {

    private final List<StepsItem> steps;
    private FragmentManager fragmentManager;
    private String recipeName;

    StepsItemRecyclerViewAdapter(FragmentManager fragmentManager, List<StepsItem> Steps,
        String recipeName) {
      this.fragmentManager = fragmentManager;
      this.steps = Steps;
      this.recipeName = recipeName;
    }

    @NonNull @Override
    public StepsItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_item_steps_layout, parent, false);
      return new StepsItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
        @NonNull final StepsItemRecyclerViewAdapter.ViewHolder holder,
        int position) {
      bindViewHolder(holder, steps.get(position));
    }

    private void bindViewHolder(RecyclerView.ViewHolder holder, StepsItem step) {
      StepsItemRecyclerViewAdapter.ViewHolder
          viewHolder = (StepsItemRecyclerViewAdapter.ViewHolder) holder;
      if (step != null) {
        viewHolder.bind(step);
      }
    }

    @Override
    public int getItemCount() {
      return steps != null ? steps.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

      @BindView(R.id.tv_list_item_step) TextView stepCount;
      @BindView(R.id.tv_list_item_step_description) TextView stepShortDescription;
      @BindBool(R.bool.two_pane_layout) boolean hasTwoPane;

      ViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(view1 -> {
          if (hasTwoPane) {

            StepDetailsFragment fragment =
                StepDetailsFragment.newInstance(steps, getAdapterPosition());

            fragmentManager.beginTransaction()
                .replace(R.id.step_detail_page_container, fragment)
                .commit();
          } else {
            Intent intent = new Intent(itemView.getContext(), StepDetailsActivity.class);
            intent.putParcelableArrayListExtra(ARG_STEPS, (ArrayList<? extends Parcelable>) steps);
            intent.putExtra(ARG_STEP_INDEX, getAdapterPosition());
            intent.putExtra(ARG_RECIPE_NAME, recipeName);
            itemView.getContext().startActivity(intent);
          }
        });
      }

      void bind(StepsItem step) {
        if (step.getStepId() == 0) { // Recipe Introduction
          stepCount.setText(step.getShortDescription());
          stepShortDescription.setVisibility(View.GONE);
        } else {
          stepCount.setText(String.format(Locale.getDefault(), "Step %d", step.getStepId()));
          stepShortDescription.setText(step.getShortDescription());
        }
        itemView.setTag(step);
      }
    }
  }
}
