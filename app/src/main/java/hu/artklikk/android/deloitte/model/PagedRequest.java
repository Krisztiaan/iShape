package hu.artklikk.android.deloitte.model;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class PagedRequest {
  public static final String SC_NOMINATION = "SC_NOMINATION";
  public static final String SC_NOTIFICATION = "SC_NOTIFICATION";
@Retention(RetentionPolicy.SOURCE)
@StringDef({SC_NOMINATION, SC_NOTIFICATION})
public @interface FeedType {}

  public int page;
  public int pageSize;
  public @FeedType String type;

  public PagedRequest(int page, int pageSize) {
    this.page = page;
    this.pageSize = pageSize;
  }

  public PagedRequest(int page, int pageSize, @FeedType String type) {
    this.page = page;
    this.pageSize = pageSize;
    this.type = type;
  }
}
