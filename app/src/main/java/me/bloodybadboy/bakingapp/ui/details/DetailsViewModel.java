package me.bloodybadboy.bakingapp.ui.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.data.source.RecipesDataSource;
import me.bloodybadboy.bakingapp.domain.Result;
import timber.log.Timber;

public class DetailsViewModel extends ViewModel {

  private final RecipesDataSource recipesRepository;

  private final CompositeDisposable disposables = new CompositeDisposable();
  private final MutableLiveData<Result<Recipe>> recipe = new MutableLiveData<>();
  private final MutableLiveData<Boolean> recipeLoaded = new MutableLiveData<>();

  public DetailsViewModel(@NonNull RecipesDataSource recipesRepository) {
    Timber.d("<init>");
    this.recipesRepository = recipesRepository;
    recipeLoaded.setValue(false);
  }

  public MutableLiveData<Boolean> isRecipeLoadedObservable() {
    return recipeLoaded;
  }

  public MutableLiveData<Result<Recipe>> getObservableRecipe() {
    return recipe;
  }

  void fetchRecipeByRecipeId(int recipeId) {
    disposables.add(recipesRepository.getRecipeByRecipeId(recipeId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> recipe.setValue(Result.Loading()))
        .subscribe(
            recipes -> {
              recipe.setValue(Result.Success(recipes));
              recipeLoaded.setValue(true);
            },
            throwable -> {
              recipe.setValue(Result.Error((Exception) throwable));
              recipeLoaded.setValue(true);
            }
        )
    );
  }

  @Override protected void onCleared() {
    disposables.clear();
  }
}
