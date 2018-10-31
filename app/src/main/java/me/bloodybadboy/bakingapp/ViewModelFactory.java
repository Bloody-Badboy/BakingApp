package me.bloodybadboy.bakingapp;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import me.bloodybadboy.bakingapp.data.source.RecipesDataRepository;
import me.bloodybadboy.bakingapp.data.source.RecipesDataSource;
import me.bloodybadboy.bakingapp.domain.GetRecipesUseCase;
import me.bloodybadboy.bakingapp.inject.Injection;
import me.bloodybadboy.bakingapp.ui.details.DetailsViewModel;
import me.bloodybadboy.bakingapp.ui.recipes.RecipesViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

  private static volatile ViewModelFactory INSTANCE;

  private final Application application;

  private final RecipesDataSource recipesRepository;

  private ViewModelFactory(Application application, RecipesDataSource repository) {
    this.application = application;
    recipesRepository = repository;
  }

  public static ViewModelFactory getInstance(Application application) {

    if (INSTANCE == null) {
      synchronized (ViewModelFactory.class) {
        if (INSTANCE == null) {
          INSTANCE = new ViewModelFactory(application, Injection.provideRecipesDataRepository());
        }
      }
    }
    return INSTANCE;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(RecipesViewModel.class)) {
      //noinspection unchecked
      return (T) new RecipesViewModel(
          new GetRecipesUseCase(recipesRepository)); //todo: inject use case using injector
    } else if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
      //noinspection unchecked
      return (T) new DetailsViewModel(recipesRepository);
    }
    throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
  }
}
