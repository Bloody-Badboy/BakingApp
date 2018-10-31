package me.bloodybadboy.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.IngredientsItem;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.inject.Injection;
import me.bloodybadboy.bakingapp.utils.Utils;
import timber.log.Timber;

public class IngredientWidgetService extends RemoteViewsService {

  @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
    int recipeId = -1;
    if (intent != null && intent.hasExtra(IngredientWidgetProvider.EXTRA_RECIPE_ID)) {
      recipeId = intent.getIntExtra(IngredientWidgetProvider.EXTRA_RECIPE_ID, -1);
    }
    return new IngredientListRemoteViewsFactory(Injection.provideApplicationContext(), recipeId);
  }

  public static class IngredientListRemoteViewsFactory
      implements RemoteViewsService.RemoteViewsFactory {

    private Context context;

    private int recipeId;

    private Disposable disposable;

    private Recipe recipe;

    IngredientListRemoteViewsFactory(Context applicationContext, int recipeId) {
      this.context = applicationContext;
      this.recipeId = recipeId;
    }

    @Override public void onCreate() {
    }

    @Override public void onDataSetChanged() {
      disposable = Injection.provideRecipesDataRepository().getRecipeByRecipeId(recipeId)
          .subscribe(
              recipe -> {
                this.recipe = recipe;
              }, Timber::e);
    }

    @Override public void onDestroy() {
      if (disposable != null && !disposable.isDisposed()) {
        disposable.dispose();
      }
    }

    @Override public int getCount() {
      return recipe != null && recipe.getIngredients() != null ? recipe.getIngredients().size() : 0;
    }

    @Override public RemoteViews getViewAt(int position) {
      if (recipe != null && recipe.getIngredients() != null) {

        IngredientsItem ingredient = recipe.getIngredients().get(position);
        if (ingredient != null) {
          RemoteViews remoteViews =
              new RemoteViews(context.getPackageName(),
                  R.layout.widget_list_item_ingredient_layout);
          remoteViews.setTextViewText(R.id.tv_list_item_ingredient_name,
              Utils.convertToCamelCase(ingredient.getIngredient()));
          remoteViews.setTextViewText(R.id.tv_list_item_ingredient_quantity,
              String.format("%s %s", ingredient.getQuantity(), ingredient.getMeasure()));

          return remoteViews;
        }
      }
      return null;
    }

    @Override public RemoteViews getLoadingView() {
      return null;
    }

    @Override public int getViewTypeCount() {
      return 1;
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public boolean hasStableIds() {
      return true;
    }
  }
}
