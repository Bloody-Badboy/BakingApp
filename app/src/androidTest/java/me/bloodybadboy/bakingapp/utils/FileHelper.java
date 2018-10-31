package me.bloodybadboy.bakingapp.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHelper {

  public static String readFileAsString(Context context, String filePath) throws Exception {
    final InputStream stream = context.getResources().getAssets().open(filePath);
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    reader.close();
    stream.close();
    return sb.toString();
  }
}
