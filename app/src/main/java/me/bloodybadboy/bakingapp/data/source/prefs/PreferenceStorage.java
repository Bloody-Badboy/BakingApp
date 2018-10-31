package me.bloodybadboy.bakingapp.data.source.prefs;

public interface PreferenceStorage {
  void setLocalDataSynced(boolean synced);

  boolean isLocalDataSynced();

  int getRecipeIdForAppWidget(int appWidgetId);

  void setRecipeIdForAppWidget(int recipeId, int appWidgetId);

  void removeAppWidgetId(int appWidgetId);
}
