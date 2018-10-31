package me.bloodybadboy.bakingapp.data.source;

import io.reactivex.Single;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;

public interface RecipesDataSource {
  Single<List<Recipe>> getRecipes();

  Single<Recipe> getRecipeByRecipeId(int recipeId);

  void deleteAndSaveRecipes(List<Recipe> recipes);

  int getRecipeIdForAppWidget(int appWidgetId);

  void removeAppWidgetId(int appWidgetId);

  void setRecipeIdForAppWidget(int recipeId, int appWidgetId);
}
