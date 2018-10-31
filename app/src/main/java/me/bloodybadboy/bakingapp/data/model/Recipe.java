package me.bloodybadboy.bakingapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.squareup.moshi.Json;
import java.util.List;
import me.bloodybadboy.bakingapp.utils.RecipesTypeConverters;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

  public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
    @Override
    public Recipe createFromParcel(Parcel in) {
      return new Recipe(in);
    }

    @Override
    public Recipe[] newArray(int size) {
      return new Recipe[size];
    }
  };


  @Json(name = "id")
  @ColumnInfo(name = "recipe_id")
  @PrimaryKey
  private int recipeId;

  @Json(name = "name")
  @ColumnInfo(name = "name")
  private String name;

  @Json(name = "image")
  @ColumnInfo(name = "image")
  private String image;

  @Json(name = "servings")
  @ColumnInfo(name = "servings")
  private int servings;

  @Json(name = "ingredients")
  @TypeConverters(RecipesTypeConverters.class)
  private List<IngredientsItem> ingredients;

  @Json(name = "steps")
  @TypeConverters(RecipesTypeConverters.class)
  private List<StepsItem> steps;

  public Recipe(){

  }
  @Ignore
  @SuppressWarnings("WeakerAccess") protected Recipe(Parcel in) {
    image = in.readString();
    servings = in.readInt();
    name = in.readString();
    ingredients = in.createTypedArrayList(IngredientsItem.CREATOR);
    recipeId = in.readInt();
    steps = in.createTypedArrayList(StepsItem.CREATOR);
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public int getServings() {
    return servings;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<IngredientsItem> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<IngredientsItem> ingredients) {
    this.ingredients = ingredients;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public List<StepsItem> getSteps() {
    return steps;
  }

  public void setSteps(List<StepsItem> steps) {
    this.steps = steps;
  }

  @Override
  public String toString() {
    return
        "Recipe{" +
            "image = '" + image + '\'' +
            ",servings = '" + servings + '\'' +
            ",name = '" + name + '\'' +
            ",ingredients = '" + ingredients + '\'' +
            ",recipeId = '" + recipeId + '\'' +
            ",steps = '" + steps + '\'' +
            "}";
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(image);
    parcel.writeInt(servings);
    parcel.writeString(name);
    parcel.writeTypedList(ingredients);
    parcel.writeInt(recipeId);
    parcel.writeTypedList(steps);
  }
}