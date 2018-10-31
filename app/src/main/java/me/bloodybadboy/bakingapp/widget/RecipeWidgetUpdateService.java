package me.bloodybadboy.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataRepository;
import me.bloodybadboy.bakingapp.inject.Injection;

public class RecipeWidgetUpdateService extends IntentService {

  private Disposable disposable;

  public RecipeWidgetUpdateService() {
    super("RecipeWidgetUpdateService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      RecipesDataRepository recipesDataRepository = Injection.provideRecipesDataRepository();
      disposable = recipesDataRepository.getRecipes().subscribe(recipes -> {

      }, throwable -> {

      });
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }
}
