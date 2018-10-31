package me.bloodybadboy.bakingapp.data.source;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.prefs.PreferenceStorage;
import timber.log.Timber;

public class RecipesDataRepository implements RecipesDataSource {
  private static volatile RecipesDataRepository sInstance = null;
  private RecipesDataSource recipesRemoteDataSource;
  private RecipesDataSource recipesLocalDataSource;
  private PreferenceStorage preferenceStorage;

  private RecipesDataRepository(RecipesDataSource recipesRemoteDataSource,
      RecipesDataSource recipesLocalDataSource, PreferenceStorage preferenceStorage) {
    this.recipesRemoteDataSource = recipesRemoteDataSource;
    this.recipesLocalDataSource = recipesLocalDataSource;
    this.preferenceStorage = preferenceStorage;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + RecipesDataRepository.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static RecipesDataRepository getInstance(RecipesDataSource recipesRemoteDataSource,
      RecipesDataSource recipesLocalDataSource, PreferenceStorage preferenceStorage) {
    if (sInstance == null) {
      synchronized (RecipesDataRepository.class) {
        if (sInstance == null) {
          sInstance = new RecipesDataRepository(recipesRemoteDataSource, recipesLocalDataSource,
              preferenceStorage);
        }
      }
    }
    return sInstance;
  }

  @Override public Single<List<Recipe>> getRecipes() {
    if (preferenceStorage.isLocalDataSynced()) {
      return recipesLocalDataSource.getRecipes();
    } else {
      return recipesRemoteDataSource.getRecipes()
          .doOnSuccess(recipes -> Completable.fromAction(() -> deleteAndSaveRecipes(recipes))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(
                  new CompletableObserver() {
                    @Override public void onSubscribe(Disposable d) {

                    }

                    @Override public void onComplete() {
                      preferenceStorage.setLocalDataSynced(true);
                      Timber.d("Recipes synced successfully.");
                    }

                    @Override public void onError(Throwable e) {
                      Timber.e(e);
                    }
                  }));
    }
  }

  @Override public Single<Recipe> getRecipeByRecipeId(int recipeId) {
    return recipesLocalDataSource.getRecipeByRecipeId(recipeId);
  }

  @Override public void deleteAndSaveRecipes(List<Recipe> recipes) {
    recipesLocalDataSource.deleteAndSaveRecipes(recipes);
  }

  @Override public int getRecipeIdForAppWidget(int appWidgetId) {
    return preferenceStorage.getRecipeIdForAppWidget(appWidgetId);
  }

  @Override public void removeAppWidgetId(int appWidgetId) {
    preferenceStorage.removeAppWidgetId(appWidgetId);
  }

  @Override public void setRecipeIdForAppWidget(int recipeId, int appWidgetId) {
    preferenceStorage.setRecipeIdForAppWidget(recipeId, appWidgetId);
  }
}