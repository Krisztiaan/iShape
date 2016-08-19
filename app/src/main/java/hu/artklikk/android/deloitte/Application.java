package hu.artklikk.android.deloitte;

import android.content.res.Configuration;
import com.crashlytics.android.Crashlytics;
import hu.artklikk.android.deloitte.util.Util;
import io.fabric.sdk.android.Fabric;
import java.util.Locale;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gyozofule on 15. 12. 08..
 */
public class Application extends android.app.Application {

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    Util.init(this);
    setDefaultFont();

    Locale locale = new Locale("EN");
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext().getResources()
        .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
  }

  private void setDefaultFont() {

    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/OpenSans-Semibold.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build());
  }
}
