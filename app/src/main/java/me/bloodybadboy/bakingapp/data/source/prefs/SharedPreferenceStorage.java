package me.bloodybadboy.bakingapp.data.source.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SharedPreferenceStorage implements PreferenceStorage {

  private static final String RECIPE_SYNCED = "recipes_synced";
  private static final String APP_WIDGET_ID_RECIPE_ID_MAP = "widget_id_recipe_id_map";

  private static volatile SharedPreferenceStorage sInstance = null;
  private final SharedPreferences sharedPreferences;
  private final Moshi moshi;

  public JsonAdapter<Map<Integer, Integer>> getMapJsonAdapter() {
    return mapJsonAdapter;
  }

  private JsonAdapter<Map<Integer, Integer>> mapJsonAdapter;

  private SharedPreferenceStorage(Context context, Moshi moshi) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    this.moshi = moshi;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + SharedPreferenceStorage.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static SharedPreferenceStorage getInstance(Context context, Moshi moshi) {
    if (sInstance == null) {
      synchronized (SharedPreferenceStorage.class) {
        if (sInstance == null) {
          sInstance = new SharedPreferenceStorage(context, moshi);
        }
      }
    }
    return sInstance;
  }

  @Override public void setLocalDataSynced(boolean synced) {
    sharedPreferences.edit().putBoolean(RECIPE_SYNCED, synced).apply();
  }

  @Override public boolean isLocalDataSynced() {
    return sharedPreferences.getBoolean(RECIPE_SYNCED, false);
  }

  @Override public int getRecipeIdForAppWidget(int appWidgetId) {
    Map<Integer, Integer> integerMap = getAllAppWidgetIdRecipeIds();
    if (integerMap != null) {
      for (Map.Entry<Integer, Integer> entry : integerMap.entrySet()) {
        if (entry.getKey() == appWidgetId) {
          return entry.getValue();
        }
      }
    }
    return -1;
  }

  @Override public void setRecipeIdForAppWidget(int recipeId, int appWidgetId) {

    if (mapJsonAdapter == null) {
      mapJsonAdapter =
          moshi.adapter(Types.newParameterizedType(Map.class, Integer.class, Integer.class));
    }

    Map<Integer, Integer> integerMap = getAllAppWidgetIdRecipeIds();
    integerMap.put(appWidgetId, recipeId);

    sharedPreferences.edit()
        .putString(APP_WIDGET_ID_RECIPE_ID_MAP, mapJsonAdapter.toJson(integerMap))
        .apply();
  }

  @SuppressLint("UseSparseArrays")
  @Override public void removeAppWidgetId(int appWidgetId) {
    if (mapJsonAdapter == null) {
      mapJsonAdapter =
          moshi.adapter(Types.newParameterizedType(Map.class, Integer.class, Integer.class));
    }

    Map<Integer, Integer> map = getAllAppWidgetIdRecipeIds();
    Map<Integer, Integer> newMap = new HashMap<>();

    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
      if (entry.getKey() != appWidgetId) {
        map.put(entry.getKey(), entry.getValue());
      }
    }

    sharedPreferences.edit()
        .putString(APP_WIDGET_ID_RECIPE_ID_MAP, mapJsonAdapter.toJson(newMap))
        .apply();
  }

  @SuppressLint("UseSparseArrays")
  private Map<Integer, Integer> getAllAppWidgetIdRecipeIds() {

    if (mapJsonAdapter == null) {
      mapJsonAdapter =
          moshi.adapter(Types.newParameterizedType(Map.class, Integer.class, Integer.class));
    }

    String json = sharedPreferences.getString(APP_WIDGET_ID_RECIPE_ID_MAP, null);

    Map<Integer, Integer> map = new HashMap<>();

    if (json != null && !TextUtils.isEmpty(json)) {
      try {
        Map<Integer, Integer> integerMap = mapJsonAdapter.fromJson(json);
        if (integerMap != null) {
          for (Map.Entry<Integer, Integer> entry : integerMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return map;
  }
}