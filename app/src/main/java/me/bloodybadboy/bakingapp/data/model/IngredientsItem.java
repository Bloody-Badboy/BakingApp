package me.bloodybadboy.bakingapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import com.squareup.moshi.Json;

public class IngredientsItem implements Parcelable {

  public static final Creator<IngredientsItem> CREATOR = new Creator<IngredientsItem>() {
    @Override
    public IngredientsItem createFromParcel(Parcel in) {
      return new IngredientsItem(in);
    }

    @Override
    public IngredientsItem[] newArray(int size) {
      return new IngredientsItem[size];
    }
  };

  @Json(name = "quantity")
  private float quantity;

  @Json(name = "measure")
  private String measure;

  @Json(name = "ingredient")
  private String ingredient;

  @SuppressWarnings("WeakerAccess") protected IngredientsItem(Parcel in) {
    quantity = in.readFloat();
    measure = in.readString();
    ingredient = in.readString();
  }

  public float getQuantity() {
    return quantity;
  }

  public void setQuantity(float quantity) {
    this.quantity = quantity;
  }

  public String getMeasure() {
    return measure;
  }

  public void setMeasure(String measure) {
    this.measure = measure;
  }

  public String getIngredient() {
    return ingredient;
  }

  public void setIngredient(String ingredient) {
    this.ingredient = ingredient;
  }

  @Override
  public String toString() {
    return
        "IngredientsItem{" +
            "quantity = '" + quantity + '\'' +
            ",measure = '" + measure + '\'' +
            ",ingredient = '" + ingredient + '\'' +
            "}";
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeFloat(quantity);
    parcel.writeString(measure);
    parcel.writeString(ingredient);
  }
}