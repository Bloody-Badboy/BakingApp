package me.bloodybadboy.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataRepository;
import me.bloodybadboy.bakingapp.inject.Injection;
import timber.log.Timber;

public class IngredientWidgetProvider extends AppWidgetProvider {

  public static final String EXTRA_RECIPE_ID = "recipe_id";
  private static RecipesDataRepository dataRepository;

  public IngredientWidgetProvider() {
    dataRepository = Injection.provideRecipesDataRepository();
  }

  public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {

    int recipeId = dataRepository.getRecipeIdForAppWidget(appWidgetId);
    if (recipeId != -1) {
      dataRepository.getRecipeByRecipeId(recipeId)
          .subscribeOn(Schedulers.io())
          .subscribe(new SingleObserver<Recipe>() {
            @Override public void onSubscribe(Disposable d) {

            }

            @Override public void onSuccess(Recipe recipe) {
              RemoteViews views =
                  new RemoteViews(context.getPackageName(), R.layout.recipe_app_ingredient_widget);
              views.setTextViewText(R.id.tv_widget_recipe_name, recipe.getName());

              Intent intent = new Intent(context, IngredientWidgetService.class);
              intent.putExtra(EXTRA_RECIPE_ID, recipe.getRecipeId());
              views.setRemoteAdapter(R.id.list_widget_recipe_ingredients, intent);

              // Instruct the widget manager to update the widget
              appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override public void onError(Throwable e) {
              Timber.d(e);
            }
          });
    }
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override public void onDeleted(Context context, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      dataRepository.removeAppWidgetId(appWidgetId);
    }
  }
}

