package hu.artklikk.android.deloitte.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import hu.artklikk.android.deloitte.util.Util;
import javax.annotation.Generated;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
@Generated("org.jsonschema2pojo")
public class CommentResponse {

  @SerializedName("id")
  @Expose
  public Integer id;
  @SerializedName("userID")
  @Expose
  public Integer userID;
  @SerializedName("comment")
  @Expose
  public String comment;
  @SerializedName("time")
  @Expose
  private Long time;

  public Long getTime() {
    return Util.timeTickConverter(time);
  }
}