package me.bloodybadboy.bakingapp;

import android.app.Application;
import androidx.annotation.NonNull;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class App extends Application {
  private static App sInstance;

  public static App getInstance() {
    return sInstance;
  }

  @Override public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);

    sInstance = this;

    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this);

      Timber.plant(new Timber.DebugTree() {
        @NonNull @Override
        protected String createStackElementTag(@NotNull StackTraceElement element) {
          return "BakingApp+" + super.createStackElementTag(element) + ":" + element.getLineNumber();
        }
      });
    }
  }
}
