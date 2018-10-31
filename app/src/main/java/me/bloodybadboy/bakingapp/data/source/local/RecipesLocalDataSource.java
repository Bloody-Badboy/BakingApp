package me.bloodybadboy.bakingapp.data.source.local;

import io.reactivex.Single;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataSource;
import me.bloodybadboy.bakingapp.data.source.local.db.RecipeDatabase;
import timber.log.Timber;

public class RecipesLocalDataSource implements RecipesDataSource {
  private static volatile RecipesLocalDataSource sInstance = null;
  private RecipeDatabase recipeDatabase;

  private RecipesLocalDataSource(RecipeDatabase recipeDatabase) {
    this.recipeDatabase = recipeDatabase;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + RecipesLocalDataSource.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static RecipesLocalDataSource getInstance(RecipeDatabase recipesStore) {
    if (sInstance == null) {
      synchronized (RecipesLocalDataSource.class) {
        if (sInstance == null) {
          sInstance = new RecipesLocalDataSource(recipesStore);
        }
      }
    }
    return sInstance;
  }

  @Override public Single<List<Recipe>> getRecipes() {
    return recipeDatabase.recipeDao().getAll();
  }

  @Override public Single<Recipe> getRecipeByRecipeId(int recipeId) {
    Timber.d("recipeId: %s", recipeId);
    return recipeDatabase.recipeDao().getRecipeByRecipeId(recipeId);
  }

  @Override public void deleteAndSaveRecipes(List<Recipe> recipes) {
    recipeDatabase.recipeDao().deleteAll();
    recipeDatabase.recipeDao().insertAll(recipes);
  }

  @Override public int getRecipeIdForAppWidget(int appWidgetId) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override public void removeAppWidgetId(int appWidgetId) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override public void setRecipeIdForAppWidget(int recipeId, int appWidgetId) {
    throw new UnsupportedOperationException("Not supported");
  }
}