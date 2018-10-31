package me.bloodybadboy.bakingapp.data.source.local.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import me.bloodybadboy.bakingapp.data.model.Recipe;

@Database(entities =
    {
        Recipe.class,
    },
    version = 1,
    exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

  private static volatile RecipeDatabase sInstance = null;

  public abstract RecipesDao recipeDao();

  public static RecipeDatabase getInstance(Context context) {
    if (sInstance == null) {
      synchronized (RecipeDatabase.class) {
        if (sInstance == null) {
          sInstance = Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class,
              "recipe-database")
              // allow queries on the main thread.
              // Don't do this on a real app! See PersistenceBasicSample for an example.
              // .allowMainThreadQueries()
              .build();
        }
      }
    }
    return sInstance;
  }
}
