package me.bloodybadboy.bakingapp.ui.recipes;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.test.espresso.IdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import me.bloodybadboy.bakingapp.IdlingResource.SimpleIdlingResource;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.ViewModelFactory;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataRepository;
import me.bloodybadboy.bakingapp.inject.Injection;
import me.bloodybadboy.bakingapp.ui.GridSpacingItemDecoration;
import me.bloodybadboy.bakingapp.ui.details.RecipeDetailActivity;
import me.bloodybadboy.bakingapp.ui.view.RecipeImageView;
import me.bloodybadboy.bakingapp.utils.NetworkUtil;
import me.bloodybadboy.bakingapp.utils.Utils;
import me.bloodybadboy.bakingapp.utils.ViewUtils;
import me.bloodybadboy.bakingapp.widget.IngredientWidgetProvider;
import timber.log.Timber;

import static me.bloodybadboy.bakingapp.Constants.ARG_RECIPE;

public class RecipesActivity extends AppCompatActivity {

  @BindView(R.id.pb_loading)
  ProgressBar progressBar;

  @BindView(R.id.rv_recipes)
  RecyclerView recyclerView;

  @Nullable
  private SimpleIdlingResource idlingResource;

  private RecipesViewModel viewModel;

  private boolean isWidgetConfigureContext = false;

  private boolean recipesLoaded = false;

  private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

  private RecipesDataRepository dataRepository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    ButterKnife.bind(this);

    dataRepository = Injection.provideRecipesDataRepository();

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    String action = getIntent().getAction();
    Bundle extras = getIntent().getExtras();

    if (action != null) {
      isWidgetConfigureContext =
          action.equalsIgnoreCase(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);

      if (extras != null && isWidgetConfigureContext) {
        appWidgetId = extras.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID);

        Timber.d("AppWidget ID: %s", appWidgetId);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
          finish();
        }
      }
    }

    viewModel = obtainViewModel(this);
    viewModel.getObservableRecipeList().observe(this, result -> {
      if (result.loading()) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        if (idlingResource != null) {
          idlingResource.setIdleState(false);
        }
      } else {
        progressBar.setVisibility(View.INVISIBLE);
        if (result.succeeded()) {
          recyclerView.setVisibility(View.VISIBLE);
          setupRecyclerView(recyclerView, result.data);

          if (idlingResource != null) {
            idlingResource.setIdleState(true);
          }
        } else {
          Toast.makeText(this, result.exception.getMessage(), Toast.LENGTH_LONG).show();
        }
      }
    });

    viewModel.isRecipesLoadedObservable().observe(this, loaded -> {
      recipesLoaded = loaded;
      if (!recipesLoaded) {
        if (NetworkUtil.isOnline()) {
          viewModel.fetchRecipeList();
        } else {
          showNoInternetSnackBar();
        }
      }
    });

    initRecycleView();
  }

  public static RecipesViewModel obtainViewModel(FragmentActivity activity) {
    ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
    return ViewModelProviders.of(activity, factory).get(RecipesViewModel.class);
  }

  private void initRecycleView() {
    int gridItemSpanCount = Utils.calculateNoOfColumns(this);

    SnapHelper snapHelperTop = new GravityPagerSnapHelper(Gravity.TOP);
    RecyclerView.LayoutManager layoutManager;

    if (gridItemSpanCount > 1) {
      layoutManager = new GridLayoutManager(this, gridItemSpanCount);
    } else {
      layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    }
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(gridItemSpanCount,
        (int) getResources().getDimension(R.dimen.recipe_grid_item_spacing)));
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
    if (!(layoutManager instanceof GridLayoutManager)) {
      snapHelperTop.attachToRecyclerView(recyclerView);
    }
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Recipe> recipes) {
    recyclerView.setAdapter(
        new RecipeItemRecyclerViewAdapter(this, recipes, Injection.provideRecipesDataRepository()));
  }

  protected void showNoInternetSnackBar() {
    progressBar.setVisibility(View.GONE);
    Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content),
        getString(R.string.no_internet),
        Snackbar.LENGTH_INDEFINITE);

    snackBar.setAction(getString(R.string.retry), __ -> {
      if (NetworkUtil.isOnline()) {
        if (!recipesLoaded) {
          viewModel.fetchRecipeList();
        }
      } else {
        showNoInternetSnackBar();
      }
    });
    snackBar.setActionTextColor(getResources().getColor(android.R.color.white));

    View sbView = snackBar.getView();
    TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    snackBar.show();
  }

  @VisibleForTesting
  @NonNull
  public IdlingResource getIdlingResource() {
    if (idlingResource == null) {
      idlingResource = new SimpleIdlingResource();
    }
    return idlingResource;
  }

  public static class RecipeItemRecyclerViewAdapter
      extends RecyclerView.Adapter<RecipeItemRecyclerViewAdapter.ViewHolder> {

    private final RecipesActivity activity;
    private final List<Recipe> recipes;
    private Context context;
    private RecipesDataRepository dataRepository;

    RecipeItemRecyclerViewAdapter(RecipesActivity parent, List<Recipe> recipes,
        RecipesDataRepository dataRepository) {
      this.recipes = recipes;
      activity = parent;
      this.dataRepository = dataRepository;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      context = parent.getContext();
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.list_item_recipe_layout, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

      ViewUtils.doOnPreDraw(holder.itemView, () -> {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(holder.itemView, "translationY",
                holder.itemView.getMeasuredHeight() >> 1, 0),
            ObjectAnimator.ofFloat(holder.itemView, "alpha", 0, 1)
        );

        animatorSet.setDuration(400L);
        animatorSet.start();
      });

      bindViewHolder(holder, recipes.get(position));
    }

    @Override public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
      super.onViewDetachedFromWindow(holder);
      holder.itemView.clearAnimation();
    }

    private void bindViewHolder(RecyclerView.ViewHolder holder, Recipe recipe) {
      ViewHolder viewHolder = (ViewHolder) holder;
      if (recipe != null) {
        viewHolder.bind(recipe);
      }
    }

    @Override
    public int getItemCount() {
      return recipes != null ? recipes.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

      @BindView(R.id.iv_list_item_recipe_thumb) RecipeImageView recipeThumb;
      @BindView(R.id.tv_list_item_recipe_name) TextView recipeName;
      @BindView(R.id.tv_list_item_recipe_servings) TextView recipeServings;
      @BindView(R.id.btn_list_item_recipe_show) Button showRecipeBtn;

      ViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
      }

      @OnClick(R.id.btn_list_item_recipe_show)
      void onShowRecipeClick() {
        if (activity.isWidgetConfigureContext) {

          dataRepository.setRecipeIdForAppWidget(((Recipe) itemView.getTag()).getRecipeId(),
              activity.appWidgetId);

          AppWidgetManager appWidgetManager =
              AppWidgetManager.getInstance(activity.getApplicationContext());

          IngredientWidgetProvider.updateAppWidget(activity.getApplicationContext(),
              appWidgetManager, activity.appWidgetId);

          Intent resultValue = new Intent();
          resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, activity.appWidgetId);
          activity.setResult(RESULT_OK, resultValue);
          activity.finish();
        } else {
          Recipe recipe = (Recipe) itemView.getTag();
          Intent intent = new Intent(context, RecipeDetailActivity.class);
          intent.putExtra(ARG_RECIPE, recipe);
          context.startActivity(intent);
        }
      }

      void bind(Recipe recipe) {

        Glide.with(itemView).load(recipe.getImage())
            .apply(
                RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
            )
            .into(recipeThumb);

        recipeName.setText(recipe.getName());
        recipeServings.setText(
            context.getString(R.string.servings, String.valueOf(recipe.getServings())));
        if (activity.isWidgetConfigureContext) {
          showRecipeBtn.setText(context.getString(R.string.add_recipe));
        }

        itemView.setTag(recipe);
      }
    }
  }
}
