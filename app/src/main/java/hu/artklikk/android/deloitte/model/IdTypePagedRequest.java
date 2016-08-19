package hu.artklikk.android.deloitte.model;

/**
 * Created by Krisztian Ferencz on 2016.08.10..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class IdTypePagedRequest {
  public int id;
  public @PagedRequest.FeedType String type;
  public int page;
  public int pageSize;

  public IdTypePagedRequest(int id, String type, int page, int pageSize) {
    this.id = id;
    this.type = type;
    this.page = page;
    this.pageSize = pageSize;
  }
}
