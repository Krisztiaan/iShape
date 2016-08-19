package hu.artklikk.android.deloitte.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mikhaellopez.circularimageview.CircularImageView;
import hu.artklikk.android.deloitte.Bus;
import hu.artklikk.android.deloitte.Events;
import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.client.IShapeClient;
import hu.artklikk.android.deloitte.model.IdTypeRequest;
import hu.artklikk.android.deloitte.model.LikeResponse;
import hu.artklikk.android.deloitte.model.Notification;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class PostView extends RelativeLayout {
  public static final int PAGE_SIZE = 5;
  private static final String TAG = "PostView";
  @BindView(R.id.imgAnimal) ImageView imgAnimal;
  @BindView(R.id.txtEndorseDesc) TextView txtEndorseDesc;
  @BindView(R.id.txtFrom) TextView txtFrom;
  @BindView(R.id.imgFrom) CircularImageView imgFrom;
  @BindView(R.id.txtTo) TextView txtTo;
  @BindView(R.id.imgTo) CircularImageView imgTo;
  @BindView(R.id.txtPostText) TextView txtPostText;
  @BindView(R.id.txtLikeCount) TextView txtLikeCount;
  @BindView(R.id.txtCommentCount) TextView txtCommentCount;
  @BindView(R.id.txtPostDate) TextView txtPostDate;
  @BindView(R.id.btnHeart) RelativeLayout btnHeart;
  @BindView(R.id.imgHeart) ImageView imgHeart;
  @BindView(R.id.btnComment) RelativeLayout btnComment;
  @BindView(R.id.imgComment) ImageView imgComment;
  //@BindView(R.id.txtReadNMore) TextView txtReadNMore;
  //@BindView(R.id.linPostComment) LinearLayout linPostComment;
  //@BindView(R.id.commentsList) CommentsListView commentsListView;
  @BindView(R.id.progLike) ProgressBar progLike;
  @BindView(R.id.progComment) ProgressBar progComment;
  //@BindView(R.id.firstComment) CommentView firstComment;
  private Notification notification;

  public PostView(Context context) {
    super(context);
  }

  public PostView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PostView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(21)
  public PostView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  void refreshLiked() {
    txtLikeCount.setText(
        getResources().getQuantityString(R.plurals.no_of_likes, notification.getLikeQuantity(),
            notification.getLikeQuantity()));
    imgHeart.setImageResource(
        notification.getLikeID() == null ? R.drawable.heart_shape_3x : R.drawable.like_act_3x);
  }

  void showLoadingComments(boolean isLoading) {
    progComment.setVisibility(isLoading ? VISIBLE : GONE);
    imgComment.animate().alpha(isLoading ? 0f : 1f).setDuration(100).start();
  }

  void showLoadingLike(boolean isLoading) {
    //progLike.setVisibility(isLoading ? VISIBLE : GONE);
  }

  public Notification getNotification() {
    return notification;
  }

  public void setNotification(Notification notification) {
    Log.i(TAG, "setNotification id: " + notification.getId());
    this.notification = notification;
    //commentsListView.setNoti(notification);
    User from = MainActivity.userMap.get(notification.getFromUserID());
    User to = MainActivity.userMap.get(notification.getToUserID());
    if (from != null) {
      txtFrom.setText(getResources().getString(R.string.from_s, from.getName()));
      imgFrom.setImageBitmap(Util.decodeBase64(from.getProfileImage()));
    } else {
      txtFrom.setText("Admin");
    }
    txtTo.setText(getResources().getString(R.string.to_s, to.getName()));
    imgTo.setImageBitmap(Util.decodeBase64(to.getProfileImage()));
    imgAnimal.setImageResource(
        Util.getAnimalDrawableId(notification.getAnimal().getAnimalTypeEnum()));
    txtEndorseDesc.setText(
        getResources().getString(R.string.n_pts_in_s, notification.getAnimal().getQuantity(),
            notification.getCategory()));
    txtCommentCount.setText(getResources().getQuantityString(R.plurals.no_of_comments,
        notification.getCommentQuantity(), notification.getCommentQuantity()));
    txtPostDate.setText(Util.convertTimestampToDate(notification.getTime(), false));
    txtPostText.setText(notification.getDescription());
    refreshLiked();

      // DO NOT SHOW FIRST COMMENT

    //if (notification.getCommentQuantity() > 0) {
    //  txtReadNMore.setText(
    //      getResources().getQuantityString(R.plurals.read_n_more, notification.getCommentQuantity
    //          (),
    //          notification.getCommentQuantity()));
      //IShapeClient.c.getComments(
      //    new IdTypePagedRequest(notification.getId(), notification.getType(), 1, 1))
      //    .enqueue(new Callback<List<CommentResponse>>() {
      //      @Override public void onResponse(Call<List<CommentResponse>> call,
      //          Response<List<CommentResponse>> response) {
      //        if (response.isSuccessful()) {
      //          if (linPostComment != null) {
      //            firstComment.setComment(response.body().get(0));
      //            linPostComment.setVisibility(VISIBLE);
      //          }
      //        } else {
      //          onFailure(null, new Throwable("Response code: " + response.code()));
      //        }
      //      }
      //
      //      @Override public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
      //        Bus.post(Events.connectionError(t));
      //      }
      //    });
    //} else {
    //  linPostComment.setVisibility(GONE);
    //}
  }

  @OnClick({
      R.id.txtFrom, R.id.imgFrom, R.id.txtTo, R.id.imgTo, R.id.btnHeart, R.id.btnComment,
      /*R.id.txtReadNMore, R.id.linPostComment */
      }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.txtFrom:
      case R.id.imgFrom:
        Bus.post(Events.openProfile(notification.getFromUserID()));
        break;
      case R.id.txtTo:
      case R.id.imgTo:
        Bus.post(Events.openProfile(notification.getToUserID()));
        break;
      case R.id.btnHeart:
        showLoadingLike(true);
        Call<LikeResponse> call;
        if (notification.getLikeID() == null) {
          call =
              IShapeClient.c.like(new IdTypeRequest(notification.getId(), notification.getType()));
        } else {
          call = IShapeClient.c.unlike(
              new IdTypeRequest(notification.getLikeID(), notification.getType()));
        }

        call.enqueue(new Callback<LikeResponse>() {
          @Override
          public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
            if (response.isSuccessful()) {
              if (txtFrom != null) {
                notification.setLikeID(response.body().likeId);
                notification.setLikeQuantity(response.body().likeQuantity);
                setNotification(notification);
                showLoadingLike(false);
              }
            } else {
              onFailure(null, new Throwable("Response code: " + response.code()));
            }
          }

          @Override public void onFailure(Call<LikeResponse> call, Throwable t) {
            Bus.post(Events.connectionError(t));
            showLoadingLike(false);
          }
        });
        break;
      case R.id.btnComment:
      //case R.id.linPostComment:
      //case R.id.txtReadNMore:
        Bus.post(Events.openComment(notification.getType(), notification));
        break;
    }
  }
}
