package me.bloodybadboy.bakingapp.ui.details;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.IngredientsItem;
import me.bloodybadboy.bakingapp.utils.Utils;
import org.jetbrains.annotations.NotNull;

import static me.bloodybadboy.bakingapp.Constants.ARG_INGREDIENTS;

public class IngredientsFragment extends Fragment {

  private static final int TWO_PANE_GRID_SPAN_COUNT = 2;
  @BindView(R.id.rv_ingredients)
  RecyclerView rvIngredients;
  @BindBool(R.bool.two_pane_layout)
  boolean hasTwoPane;
  private Activity activity;
  private List<IngredientsItem> ingredients;

  public IngredientsFragment() {
    // Required empty public constructor
  }

  public static IngredientsFragment newInstance(List<IngredientsItem> ingredients) {

    Bundle args = new Bundle();
    args.putParcelableArrayList(ARG_INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);

    IngredientsFragment fragment = new IngredientsFragment();
    fragment.setArguments(args);
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

    if (args != null && args.containsKey(ARG_INGREDIENTS)) {
      ingredients = args.getParcelableArrayList(ARG_INGREDIENTS);
    } else {
      Toast.makeText(activity, "Ingredients data not available.", Toast.LENGTH_SHORT).show();
      activity.finish();
    }
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
    ButterKnife.bind(this, rootView);

    initRecycleView();

    return rootView;
  }

  private void initRecycleView() {
    RecyclerView.LayoutManager layoutManager;
    if (hasTwoPane) {
      layoutManager = new GridLayoutManager(activity, TWO_PANE_GRID_SPAN_COUNT);
    } else {
      layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
    }
    rvIngredients.setHasFixedSize(true);
    rvIngredients.setLayoutManager(layoutManager);
    rvIngredients.setAdapter(new IngredientItemRecyclerViewAdapter(ingredients));
  }

  public static class IngredientItemRecyclerViewAdapter
      extends RecyclerView.Adapter<IngredientItemRecyclerViewAdapter.ViewHolder> {

    private final List<IngredientsItem> ingredients;

    IngredientItemRecyclerViewAdapter(List<IngredientsItem> ingredients) {
      this.ingredients = ingredients;
    }

    @NonNull @Override
    public IngredientItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_item_ingredient_layout, parent, false);
      return new IngredientItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientItemRecyclerViewAdapter.ViewHolder holder,
        int position) {
      bindViewHolder(holder, ingredients.get(position));
    }

    private void bindViewHolder(RecyclerView.ViewHolder holder, IngredientsItem ingredient) {
      IngredientItemRecyclerViewAdapter.ViewHolder
          viewHolder = (IngredientItemRecyclerViewAdapter.ViewHolder) holder;
      if (ingredient != null) {
        viewHolder.bind(ingredient);
      }
    }

    @Override
    public int getItemCount() {
      return ingredients != null ? ingredients.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

      @BindView(R.id.tv_list_item_ingredient_name) TextView ingredientName;
      @BindView(R.id.tv_list_item_ingredient_quantity) TextView ingredientQuantity;

      ViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
      }

      void bind(IngredientsItem ingredient) {
        ingredientName.setText(Utils.convertToCamelCase(ingredient.getIngredient()));
        ingredientQuantity.setText(
            String.format("%s %s", ingredient.getQuantity(), ingredient.getMeasure()));

        itemView.setTag(ingredient);
      }
    }
  }
}
