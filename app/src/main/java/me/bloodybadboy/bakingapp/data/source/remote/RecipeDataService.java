package me.bloodybadboy.bakingapp.data.source.remote;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import io.reactivex.Single;
import java.util.List;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.ByteString;

public class RecipeDataService {

  private static final String SERVICE_END_POINT =
      "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

  private static final String IMAGE_URLS[] = {
      "https://res.cloudinary.com/arpan/image/upload/v1533013635/baking-app/nutella_pie.png",
      "https://res.cloudinary.com/arpan/image/upload/v1533013635/baking-app/brownies.png",
      "https://res.cloudinary.com/arpan/image/upload/v1533013635/baking-app/yellow_cake.png",
      "https://res.cloudinary.com/arpan/image/upload/v1533013635/baking-app/cheese_cake.png"
  };

  private static final ByteString UTF8_BOM = ByteString.decodeHex("EFBBBF");

  private static volatile RecipeDataService sInstance = null;
  private OkHttpClient okHttpClient;
  private final Moshi moshi;

  private RecipeDataService(OkHttpClient okHttpClient, Moshi moshi) {
    this.okHttpClient = okHttpClient;
    this.moshi = moshi;
    if (sInstance != null) {
      throw new AssertionError(
          "Another instance of "
              + RecipeDataService.class.getName()
              + " class already exists, Can't create a new instance.");
    }
  }

  public static RecipeDataService getInstance(OkHttpClient okHttpClient, Moshi moshi) {
    if (sInstance == null) {
      synchronized (RecipeDataService.class) {
        if (sInstance == null) {
          sInstance = new RecipeDataService(okHttpClient, moshi);
        }
      }
    }
    return sInstance;
  }

  Single<List<Recipe>> getRecipes() {

    Request request = new Request.Builder()
        .url(SERVICE_END_POINT)
        .build();

    final Call call = okHttpClient.newCall(request);

    return Single.create(emitter -> {
      Response response = call.execute();

      ResponseBody responseBody = response.body();

      BufferedSource source;
      if (responseBody != null) {
        source = responseBody.source();

        if (source.rangeEquals(0, UTF8_BOM)) {
          source.skip(UTF8_BOM.size());
        }
        JsonReader reader = JsonReader.of(source);

        JsonAdapter<List<Recipe>> recipesJsonAdapter =
            moshi.adapter(Types.newParameterizedType(List.class, Recipe.class));

        List<Recipe> result = recipesJsonAdapter.fromJson(reader);
        if (reader.peek() != JsonReader.Token.END_DOCUMENT) {
          throw new JsonDataException("JSON document was not fully consumed.");
        }

        // No image urls are provides so, manually adding some images urls
        if (result != null) {
          result.get(0).setImage(IMAGE_URLS[0]);
          result.get(1).setImage(IMAGE_URLS[1]);
          result.get(2).setImage(IMAGE_URLS[2]);
          result.get(3).setImage(IMAGE_URLS[3]);
        }

        emitter.onSuccess(result);
      } else {
        throw new NullPointerException("responseBody == null");
      }
    });
  }
}
