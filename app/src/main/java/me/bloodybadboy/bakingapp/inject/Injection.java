package me.bloodybadboy.bakingapp.inject;

import android.content.Context;
import com.squareup.moshi.Moshi;
import me.bloodybadboy.bakingapp.App;
import me.bloodybadboy.bakingapp.data.source.RecipesDataRepository;
import me.bloodybadboy.bakingapp.data.source.RecipesDataSource;
import me.bloodybadboy.bakingapp.data.source.local.RecipesLocalDataSource;
import me.bloodybadboy.bakingapp.data.source.local.db.RecipeDatabase;
import me.bloodybadboy.bakingapp.data.source.prefs.PreferenceStorage;
import me.bloodybadboy.bakingapp.data.source.prefs.SharedPreferenceStorage;
import me.bloodybadboy.bakingapp.data.source.remote.RecipeDataService;
import me.bloodybadboy.bakingapp.data.source.remote.RecipesRemoteDataSource;
import okhttp3.OkHttpClient;

@SuppressWarnings("WeakerAccess") public class Injection {

  private static volatile OkHttpClient sOkHttpClientInstance = null;

  public static Context provideApplicationContext() {
    return App.getInstance();
  }

  private static OkHttpClient provideOkHttpClient() {
    if (sOkHttpClientInstance == null) {
      synchronized (Injection.class) {
        if (sOkHttpClientInstance == null) {
          sOkHttpClientInstance = new OkHttpClient.Builder().build();
        }
      }
    }
    return sOkHttpClientInstance;
  }

  public static Moshi provideMoshi() {
    return new Moshi.Builder().build();
  }

  public static PreferenceStorage providePreferenceStorage() {
    return SharedPreferenceStorage.getInstance(Injection.provideApplicationContext(),
        Injection.provideMoshi());
  }

  public static RecipeDatabase provideRecipeDatabase() {
    return RecipeDatabase.getInstance(Injection.provideApplicationContext());
  }

  public static RecipeDataService provideRemoteRecipeDataService() {
    return RecipeDataService.getInstance(Injection.provideOkHttpClient(), Injection.provideMoshi());
  }

  public static RecipesDataSource provideRecipesRemoteDataSource() {
    return RecipesRemoteDataSource.getInstance(Injection.provideRemoteRecipeDataService());
  }

  public static RecipesDataSource provideRecipesLocalDataSource() {
    return RecipesLocalDataSource.getInstance(Injection.provideRecipeDatabase());
  }

  public static RecipesDataRepository provideRecipesDataRepository() {
    return RecipesDataRepository.getInstance(
        Injection.provideRecipesRemoteDataSource(),
        Injection.provideRecipesLocalDataSource(),
        Injection.providePreferenceStorage()
    );
  }
}
