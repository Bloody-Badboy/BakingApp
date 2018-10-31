package me.bloodybadboy.bakingapp.ui.recipes;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.domain.GetRecipesUseCase;
import me.bloodybadboy.bakingapp.domain.Result;
import timber.log.Timber;

public class RecipesViewModel extends ViewModel {

  private final GetRecipesUseCase recipesUseCase;

  private final CompositeDisposable disposables = new CompositeDisposable();
  private final MutableLiveData<Result<List<Recipe>>> recipesList = new MutableLiveData<>();
  private final MutableLiveData<Boolean> recipesLoaded = new MutableLiveData<>();

  public RecipesViewModel(@NonNull GetRecipesUseCase recipesUseCase) {
    Timber.d("<init>");
    this.recipesUseCase = recipesUseCase;
    recipesLoaded.setValue(false);
  }

  public MutableLiveData<Boolean> isRecipesLoadedObservable() {
    return recipesLoaded;
  }

  public MutableLiveData<Result<List<Recipe>>> getObservableRecipeList() {
    return recipesList;
  }

  @Override
  protected void onCleared() {
    Timber.d("onCleared()");
    disposables.clear();
  }

  void fetchRecipeList() {
    disposables.add(recipesUseCase.execute()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> recipesList.setValue(Result.Loading()))
        .subscribe(
            recipes -> {
              recipesList.setValue(Result.Success(recipes));
              recipesLoaded.setValue(true);
            },
            throwable -> {
              recipesList.setValue(Result.Error((Exception) throwable));
              recipesLoaded.setValue(true);
            }
        )
    );
  }
}
