package hu.artklikk.android.deloitte;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import hu.artklikk.android.deloitte.model.CommentResponse;
import hu.artklikk.android.deloitte.views.CommentView;
import java.util.List;

/**
 * Created by Krisztian Ferencz on 2016. 08. 11..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class CommentRecyclerAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int VIEW_ITEM = 1;
  private final int VIEW_PROG = 0;

  private final List<CommentResponse> comments;
  // The minimum amount of items to have below your current scroll position before loading more.
  private int visibleThreshold = 2;
  private int lastVisibleItem, totalItemCount;
  private boolean loading;
  private OnLoadMoreListener onLoadMoreListener;

  public CommentRecyclerAdapter(@NonNull List<CommentResponse> comments, RecyclerView recyclerView) {
    this.comments = comments;

    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

      final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          totalItemCount = linearLayoutManager.getItemCount();
          lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
          if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            if (onLoadMoreListener != null) {
              onLoadMoreListener.onLoadMore();
            }
            loading = true;
          }
        }
      });
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder vh;
    if (viewType == VIEW_ITEM) {
      vh = new CommentViewHolder((CommentView) LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_view_comment, parent, false));
    } else {
      View v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.view_progress_item, parent, false);

      vh = new ProgressViewHolder(v);
    }

    return vh;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof CommentViewHolder) {
      ((CommentViewHolder) holder).setComment(comments.get(position));
    } else {
      ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }
  }

  public void setLoaded() {
    loading = false;
  }

  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    this.onLoadMoreListener = onLoadMoreListener;
  }

  public interface OnLoadMoreListener {
    void onLoadMore();
  }

  public static class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public ProgressViewHolder(View v) {
      super(v);
      progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }
  }

  @Override public int getItemViewType(int position) {
    return comments.get(position) != null ? VIEW_ITEM : VIEW_PROG;
  }

  @Override public int getItemCount() {
    return comments.size();
  }

  public class CommentViewHolder extends RecyclerView.ViewHolder {

    private final CommentView commentView;

    public CommentViewHolder(CommentView itemView) {
      super(itemView);
      itemView.setIsBlue(false);
      commentView = itemView;
    }

    public void setComment(CommentResponse comment) {
      commentView.setComment(comment);
    }
  }
}
