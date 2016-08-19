package hu.artklikk.android.deloitte.util;

import java.util.Calendar;
import java.util.Locale;
import org.ocpsoft.prettytime.PrettyTime;

/**
 * Created by Krisztian Ferencz on 2016. 08. 11..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class TimeAgo {
  private static PrettyTime pT = new PrettyTime();

  public static String getPrettyTime(long time) {
    Calendar cal = Calendar.getInstance(Locale.getDefault());
    cal.setTimeInMillis(time);
    return pT.format(cal);
  }
}