package me.bloodybadboy.bakingapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

public class StepsItem implements Parcelable {

  public static final Creator<StepsItem> CREATOR = new Creator<StepsItem>() {
    @Override
    public StepsItem createFromParcel(Parcel in) {
      return new StepsItem(in);
    }

    @Override
    public StepsItem[] newArray(int size) {
      return new StepsItem[size];
    }
  };

  @Json(name = "id")
  private int stepId;

  @Json(name = "videoURL")
  private String videoURL;

  @Json(name = "description")
  private String description;

  @Json(name = "shortDescription")
  private String shortDescription;

  @Json(name = "thumbnailURL")
  private String thumbnailURL;

  @SuppressWarnings("WeakerAccess") protected StepsItem(Parcel in) {
    videoURL = in.readString();
    description = in.readString();
    stepId = in.readInt();
    shortDescription = in.readString();
    thumbnailURL = in.readString();
  }

  public String getVideoURL() {
    return videoURL;
  }

  public void setVideoURL(String videoURL) {
    this.videoURL = videoURL;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getStepId() {
    return stepId;
  }

  public void setStepId(int stepId) {
    this.stepId = stepId;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

  public void setThumbnailURL(String thumbnailURL) {
    this.thumbnailURL = thumbnailURL;
  }

  @Override
  public String toString() {
    return
        "StepsItem{" +
            "videoURL = '" + videoURL + '\'' +
            ",description = '" + description + '\'' +
            ",stepId = '" + stepId + '\'' +
            ",shortDescription = '" + shortDescription + '\'' +
            ",thumbnailURL = '" + thumbnailURL + '\'' +
            "}";
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(videoURL);
    parcel.writeString(description);
    parcel.writeInt(stepId);
    parcel.writeString(shortDescription);
    parcel.writeString(thumbnailURL);
  }
}