package hu.artklikk.android.deloitte.model;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class IdTypeRequest {
  public int id;
  public @PagedRequest.FeedType String type;
  public int page;
  public int pageSize;

  public IdTypeRequest(int id, String type) {
    this.id = id;
    this.type = type;
  }
}
