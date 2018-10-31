package me.bloodybadboy.bakingapp.domain;

import io.reactivex.Single;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataSource;

public class GetRecipesUseCase implements UseCase<Single<List<Recipe>>> {
  private RecipesDataSource repository;

  public GetRecipesUseCase(RecipesDataSource repository) {
    this.repository = repository;
  }

  @Override
  public Single<List<Recipe>> execute() {
    return repository.getRecipes();
  }
}
