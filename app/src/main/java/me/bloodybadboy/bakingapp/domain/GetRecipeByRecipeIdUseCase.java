package me.bloodybadboy.bakingapp.domain;

import io.reactivex.Single;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataSource;

public class GetRecipeByRecipeIdUseCase implements UseCase<Single<Recipe>> {
  private RecipesDataSource repository;
  private int recipeId;

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public GetRecipeByRecipeIdUseCase(RecipesDataSource repository) {
    this.repository = repository;
  }

  @Override public Single<Recipe> execute() {
    return repository.getRecipeByRecipeId(recipeId);
  }
}
