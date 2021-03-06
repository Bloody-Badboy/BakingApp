package me.bloodybadboy.bakingapp.data;

import androidx.test.InstrumentationRegistry;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import me.bloodybadboy.bakingapp.data.model.Recipe;
import me.bloodybadboy.bakingapp.utils.FileHelper;

public class MockRecipeStore {
  private static final String RECIPE_JSON =
      "{\"id\":2,\"name\":\"Brownies\",\"ingredients\":[{\"quantity\":350,\"measure\":\"G\",\"ingredient\":\"Bittersweet chocolate (60-70% cacao)\"},{\"quantity\":226,\"measure\":\"G\",\"ingredient\":\"unsalted butter\"},{\"quantity\":300,\"measure\":\"G\",\"ingredient\":\"granulated sugar\"},{\"quantity\":100,\"measure\":\"G\",\"ingredient\":\"light brown sugar\"},{\"quantity\":5,\"measure\":\"UNIT\",\"ingredient\":\"large eggs\"},{\"quantity\":1,\"measure\":\"TBLSP\",\"ingredient\":\"vanilla extract\"},{\"quantity\":140,\"measure\":\"G\",\"ingredient\":\"all purpose flour\"},{\"quantity\":40,\"measure\":\"G\",\"ingredient\":\"cocoa powder\"},{\"quantity\":1.5,\"measure\":\"TSP\",\"ingredient\":\"salt\"},{\"quantity\":350,\"measure\":\"G\",\"ingredient\":\"semisweet chocolate chips\"}],\"steps\":[{\"id\":0,\"shortDescription\":\"Recipe Introduction\",\"description\":\"Recipe Introduction\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc33_-intro-brownies/-intro-brownies.mp4\",\"thumbnailURL\":\"\"},{\"id\":1,\"shortDescription\":\"Starting prep\",\"description\":\"1. Preheat the oven to 350�F. Butter the bottom and sides of a 9\\\"x13\\\" pan.\",\"videoURL\":\"\",\"thumbnailURL\":\"\"},{\"id\":2,\"shortDescription\":\"Melt butter and bittersweet chocolate.\",\"description\":\"2. Melt the butter and bittersweet chocolate together in a microwave or a double boiler. If microwaving, heat for 30 seconds at a time, removing bowl and stirring ingredients in between.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc43_1-melt-choclate-chips-and-butter-brownies/1-melt-choclate-chips-and-butter-brownies.mp4\",\"thumbnailURL\":\"\"},{\"id\":3,\"shortDescription\":\"Add sugars to wet mixture.\",\"description\":\"3. Mix both sugars into the melted chocolate in a large mixing bowl until mixture is smooth and uniform.\",\"videoURL\":\"\",\"thumbnailURL\":\"\"},{\"id\":4,\"shortDescription\":\"Mix together dry ingredients.\",\"description\":\"4. Sift together the flour, cocoa, and salt in a small bowl and whisk until mixture is uniform and no clumps remain. \",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc9e_4-sift-flower-add-coco-powder-salt-brownies/4-sift-flower-add-coco-powder-salt-brownies.mp4\",\"thumbnailURL\":\"\"},{\"id\":5,\"shortDescription\":\"Add eggs.\",\"description\":\"5. Crack 3 eggs into the chocolate mixture and carefully fold them in. Crack the other 2 eggs in and carefully fold them in. Fold in the vanilla.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc62_2-mix-egss-with-choclate-butter-brownies/2-mix-egss-with-choclate-butter-brownies.mp4\",\"thumbnailURL\":\"\"},{\"id\":6,\"shortDescription\":\"Add dry mixture to wet mixture.\",\"description\":\"6. Dump half of flour mixture into chocolate mixture and carefully fold in, just until no streaks remain. Repeat with the rest of the flour mixture. Fold in the chocolate chips.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdcc8_5-mix-wet-and-cry-batter-together-brownies/5-mix-wet-and-cry-batter-together-brownies.mp4\",\"thumbnailURL\":\"\"},{\"id\":7,\"shortDescription\":\"Add batter to pan.\",\"description\":\"7. Pour the batter into the prepared pan and bake for 30 minutes.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdcf4_8-put-brownies-in-oven-to-bake-brownies/8-put-brownies-in-oven-to-bake-brownies.mp4\",\"thumbnailURL\":\"\"},{\"id\":8,\"shortDescription\":\"Remove pan from oven.\",\"description\":\"8. Remove the pan from the oven and let cool until room temperature. If you want to speed this up, you can feel free to put the pan in a freezer for a bit.\",\"videoURL\":\"\",\"thumbnailURL\":\"\"},{\"id\":9,\"shortDescription\":\"Cut and serve.\",\"description\":\"9. Cut and serve.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdcf9_9-final-product-brownies/9-final-product-brownies.mp4\",\"thumbnailURL\":\"\"}],\"servings\":8,\"image\":\"\"}";

  public static Recipe getMockRecipe() {

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Recipe> recipeJsonAdapter =
        moshi.adapter(Recipe.class);

    try {
      return recipeJsonAdapter.fromJson(
          FileHelper.readFileAsString(InstrumentationRegistry.getContext(),
              "brownies_recipe.json"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
