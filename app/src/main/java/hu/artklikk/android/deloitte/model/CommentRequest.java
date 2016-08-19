package hu.artklikk.android.deloitte.model;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class CommentRequest {
  public Integer id;
  public String type;
  public String comment;

  public CommentRequest(Integer id, String type, String comment) {
    this.id = id;
    this.type = type;
    this.comment = comment;
  }
}
