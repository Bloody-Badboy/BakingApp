package me.bloodybadboy.bakingapp.data.source.local.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Single;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipesDao {
  @Query("SELECT * FROM recipes")
  Single<List<Recipe>> getAll();

  @Query("SELECT * FROM recipes WHERE recipe_id = :recipeId")
  Single<Recipe> getRecipeByRecipeId(int recipeId);

  @Insert(onConflict = REPLACE)
  void insertAll(List<Recipe> recipes);

  @Query("DELETE FROM recipes")
  void deleteAll();
}
