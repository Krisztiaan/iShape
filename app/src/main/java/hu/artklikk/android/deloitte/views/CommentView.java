package hu.artklikk.android.deloitte.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mikhaellopez.circularimageview.CircularImageView;
import hu.artklikk.android.deloitte.Bus;
import hu.artklikk.android.deloitte.Config;
import hu.artklikk.android.deloitte.Events;
import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.CommentResponse;
import hu.artklikk.android.deloitte.util.TimeAgo;
import hu.artklikk.android.deloitte.util.Util;

/**
 * Created by Krisztian Ferencz on 2016. 08. 10..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class CommentView extends LinearLayout {
  @BindView(R.id.imgUser) CircularImageView imgUser;
  @BindView(R.id.txtAuthor) TextView txtAuthor;
  @BindView(R.id.txtSentTime) TextView txtSentTime;
  @BindView(R.id.txtMessage) TextView txtMessage;
  CommentResponse comment;
  private boolean isBlue = true;

  public CommentView(Context context) {
    super(context);
    initCommentView(null);
  }

  public CommentView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initCommentView(attrs);
  }

  public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initCommentView(attrs);
  }

  @TargetApi(21)
  public CommentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initCommentView(attrs);
  }

  public void setIsBlue(boolean isBlue) {
    this.isBlue = isBlue;
    if (isBlue) {
      txtAuthor.setTextColor(getResources().getColor(R.color.light_blue));
      txtMessage.setTextColor(getResources().getColor(R.color.light_blue));
      txtSentTime.setTextColor(getResources().getColor(R.color.light_blue));
    } else {
      txtAuthor.setTextColor(getResources().getColor(R.color.white));
      txtMessage.setTextColor(getResources().getColor(R.color.text_almost_white));
      txtSentTime.setTextColor(getResources().getColor(R.color.text_almost_white));
    }
  }

  private void initCommentView(@Nullable AttributeSet attrs) {
    LayoutInflater inflater = null;
    if (getContext() instanceof Activity) {
      inflater = ((Activity) getContext()).getLayoutInflater();
    } else {
      inflater = LayoutInflater.from(getContext());
    }

    ButterKnife.bind(this, inflater.inflate(R.layout.view_comment, this, true));

    if (attrs != null) {
      int[] styleable = R.styleable.CommentView;
      TypedArray a = getContext().obtainStyledAttributes(attrs, styleable, 0, 0);
      Integer contrast = (a.getInteger(R.styleable.CommentView_comm_color, 0));
      txtMessage.setMaxLines(a.getInteger(R.styleable.CommentView_comm_maxLines, 30));
      a.recycle();
      isBlue = contrast == 0;
    }

    setIsBlue(isBlue);
  }

  public void setComment(CommentResponse comment) {
    this.comment = comment;
    imgUser.setImageBitmap(
        Util.decodeBase64(MainActivity.userMap.get(comment.userID).getProfileImage()));
    txtSentTime.setText(Config.SHOULD_USE_TIME_AGO ? TimeAgo.getPrettyTime(comment.getTime())
        : Util.convertTimestampToDate(comment.getTime(), true));
    txtMessage.setText(comment.comment);
    try {
      txtAuthor.setText(MainActivity.userMap.get(comment.userID).getName());
    } catch (NullPointerException npe) {
      txtAuthor.setText("Admin");
    }
  }

  @OnClick({ R.id.imgUser, R.id.txtAuthor }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.txtAuthor:
      case R.id.imgUser:
        Bus.post(Events.openProfile(comment.userID));
        break;
    }
  }
}
