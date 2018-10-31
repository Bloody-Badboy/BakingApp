package me.bloodybadboy.bakingapp.data.source.remote;

import io.reactivex.Single;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataSource;

public class RecipesRemoteDataSource implements RecipesDataSource {
  private static volatile RecipesRemoteDataSource sInstance = null;
  private RecipeDataService dataProvider;

  private RecipesRemoteDataSource(RecipeDataService dataProvider) {
    this.dataProvider = dataProvider;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + RecipesRemoteDataSource.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static RecipesRemoteDataSource getInstance(RecipeDataService dataService) {
    if (sInstance == null) {
      synchronized (RecipesRemoteDataSource.class) {
        if (sInstance == null) {
          sInstance = new RecipesRemoteDataSource(dataService);
        }
      }
    }
    return sInstance;
  }

  @Override public Single<List<Recipe>> getRecipes() {
    return dataProvider.getRecipes();
  }

  @Override public Single<Recipe> getRecipeByRecipeId(int recipeId) {
    throw new UnsupportedOperationException("Not supported");
  }

  @Override public void deleteAndSaveRecipes(List<Recipe> recipes) {
    throw new UnsupportedOperationException("Not supported");
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