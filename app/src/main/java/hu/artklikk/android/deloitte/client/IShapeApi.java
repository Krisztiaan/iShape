package hu.artklikk.android.deloitte.client;

import hu.artklikk.android.deloitte.model.CommentRequest;
import hu.artklikk.android.deloitte.model.CommentResponse;
import hu.artklikk.android.deloitte.model.IdTypePagedRequest;
import hu.artklikk.android.deloitte.model.IdTypeRequest;
import hu.artklikk.android.deloitte.model.LikeResponse;
import hu.artklikk.android.deloitte.model.Notification;
import hu.artklikk.android.deloitte.model.PagedRequest;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public interface IShapeApi {
  @POST("ActivityFeed")
  Call<List<Notification>> getActivityFeed(@Body PagedRequest request);

  @POST("Like")
  Call<LikeResponse> like(@Body IdTypeRequest request);

  @POST("UnLike")
  Call<LikeResponse> unlike(@Body IdTypeRequest request);

  @POST("GetLikers")
  Call<List<Integer>> getLikers(@Body IdTypeRequest request);

  @POST("SendComment")
  Call<CommentResponse> sendComment(@Body CommentRequest request);

  @POST("GetComments")
  Call<List<CommentResponse>> getComments(@Body IdTypePagedRequest request);
}
