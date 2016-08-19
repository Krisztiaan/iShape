package hu.artklikk.android.deloitte.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
@Generated("org.jsonschema2pojo")
public class LikeResponse {

  @SerializedName("success")
  @Expose
  public String success;
  @SerializedName("likeId")
  @Expose
  public Integer likeId;
  @SerializedName("likeQuantity")
  @Expose
  public Integer likeQuantity;

}