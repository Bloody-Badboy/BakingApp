package me.bloodybadboy.bakingapp.utils;

import androidx.room.TypeConverter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.IngredientsItem;
import me.bloodybadboy.bakingapp.data.model.StepsItem;
import timber.log.Timber;

public class RecipesTypeConverters {
  private static final Moshi moshi = new Moshi.Builder().build();
  private static final JsonAdapter<List<IngredientsItem>> ingredientsJsonAdapter =
      moshi.adapter(Types.newParameterizedType(List.class, IngredientsItem.class));
  private static final JsonAdapter<List<StepsItem>> stepsJsonAdapter =
      moshi.adapter(Types.newParameterizedType(List.class, StepsItem.class));

  @TypeConverter
  public static List<IngredientsItem> toIngredientsList(String data) {
    if (data != null) {
      try {
        return ingredientsJsonAdapter.fromJson(data);
      } catch (IOException e) {
        Timber.e(e);
      }
    }
    return Collections.emptyList();
  }

  @TypeConverter
  public static String fromIngredientsList(List<IngredientsItem> data) {
    if (data != null) {
      return ingredientsJsonAdapter.toJson(data);
    }
    return "";
  }

  @TypeConverter
  public static List<StepsItem> toStepsList(String data) {
    if (data != null) {
      try {
        return stepsJsonAdapter.fromJson(data);
      } catch (IOException e) {
        Timber.e(e);
      }
    }
    return Collections.emptyList();
  }

  @TypeConverter
  public static String fromStepsList(List<StepsItem> data) {
    if (data != null) {
      return stepsJsonAdapter.toJson(data);
    }
    return "";
  }
}
