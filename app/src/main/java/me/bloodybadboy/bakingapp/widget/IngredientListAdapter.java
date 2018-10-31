package me.bloodybadboy.bakingapp.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import me.bloodybadboy.bakingapp.R;
import me.bloodybadboy.bakingapp.data.model.IngredientsItem;
import me.bloodybadboy.bakingapp.utils.Utils;

public class IngredientListAdapter extends ArrayAdapter<IngredientsItem> {

  private List<IngredientsItem> ingredients;

  public IngredientListAdapter(@NonNull Context context,
      @NonNull List<IngredientsItem> ingredients) {
    super(context, R.layout.widget_list_item_ingredient_layout, ingredients);
    this.ingredients = ingredients;
  }

  @Override public int getCount() {
    return ingredients == null ? 0 : ingredients.size();
  }

  @Nullable @Override public IngredientsItem getItem(int position) {
    return ingredients == null ? null : ingredients.get(position);
  }

  @NonNull @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.widget_list_item_ingredient_layout, null);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    IngredientsItem ingredient = ingredients.get(position);

    if (ingredient != null) {
      holder.ingredientName.setText(Utils.convertToCamelCase(ingredient.getIngredient()));
      holder.ingredientQuantity.setText(
          String.format("%s %s", ingredient.getQuantity(), ingredient.getMeasure()));
    }

    return convertView;
  }

  class ViewHolder {

    @BindView(R.id.tv_list_item_ingredient_name) TextView ingredientName;
    @BindView(R.id.tv_list_item_ingredient_quantity) TextView ingredientQuantity;

    ViewHolder(View itemView) {
      ButterKnife.bind(this, itemView);
    }
  }
}
