package hu.artklikk.android.deloitte;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import hu.artklikk.android.deloitte.model.Notification;
import hu.artklikk.android.deloitte.model.PagedRequest;
import hu.artklikk.android.deloitte.util.Util;
import hu.artklikk.android.deloitte.views.PostView;
import java.util.List;

/**
 * Created by Krisztian Ferencz on 2016. 08. 11..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class NotificationRecyclerAdapter
    extends RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewHolder> {

  private final List<Notification> notifications;

  public NotificationRecyclerAdapter(List<Notification> notifications) {
    this.notifications = notifications;
  }

  @Override public NotificationViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    View v;
    if (viewType == 0) {
      v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
    } else {
      v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_notification, parent, false);
    }
    return new NotificationViewHolder(v);
  }

  @Override
  public void onBindViewHolder(NotificationViewHolder holder, int position) {
    holder.setNotification(notifications.get(position));
  }

  @Override public int getItemViewType(int position) {
    return notifications.get(position).getType().equals(PagedRequest.SC_NOMINATION) ? 0 : 1;
  }

  @Override public int getItemCount() {
    return notifications.size();
  }

  public class NotificationViewHolder extends RecyclerView.ViewHolder {
    private final View view;
    @BindView(R.id.txtAuthor) TextView txtAuthor;
    @BindView(R.id.txtSentTime) TextView txtSentTime;
    @BindView(R.id.txtMessage) TextView txtMessage;

    public NotificationViewHolder(View itemView) {
      super(itemView);
      view = itemView;
      ButterKnife.bind(this, itemView);
    }

    public void setNotification(Notification notification) {
      switch (notification.getType()) {
        case PagedRequest.SC_NOMINATION:
          ((PostView) view).setNotification(notification);
          break;
        case PagedRequest.SC_NOTIFICATION:
        default:
          txtAuthor.setText("Important notification");
          txtMessage.setText(notification.getDescription());
          txtSentTime.setText(Util.convertTimestampToDate(notification.getTime(), true));
          break;
      }
    }
  }
}
