package hu.artklikk.android.deloitte.views;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import hu.artklikk.android.deloitte.Bus;
import hu.artklikk.android.deloitte.CommentRecyclerAdapter;
import hu.artklikk.android.deloitte.Events;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.client.IShapeClient;
import hu.artklikk.android.deloitte.model.CommentRequest;
import hu.artklikk.android.deloitte.model.CommentResponse;
import hu.artklikk.android.deloitte.model.IdTypePagedRequest;
import hu.artklikk.android.deloitte.model.Notification;
import io.codetail.widget.RevealFrameLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Krisztian Ferencz on 2016. 08. 11..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class CommentsListView extends RevealFrameLayout {
  public static final int PAGE_SIZE = 10;
  private final List<CommentResponse> comments = new ArrayList<>();
  @BindView(R.id.recComments) RecyclerView recComments;
  @BindView(R.id.editMessage) EditText editMessage;
  @BindView(R.id.frameInput) FrameLayout frameInput;
  @BindView(R.id.progMoreComments) ProgressBar progMoreComments;
  @BindView(R.id.linCoRoot) LinearLayout linCoRoot;
  LinearLayoutManager lm;
  private CommentRecyclerAdapter recyclerAdapter;
  private Notification noti = null;
  private View revParent;

  public CommentsListView(Context context) {
    super(context);
    init(null);
  }

  public CommentsListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public CommentsListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @OnEditorAction(R.id.editMessage) protected boolean onMessageSend(final TextView v) {
    String message = v.getText().toString();
    if (message.length() == 0) {
      v.setError("Can't send empty message");
      return false;
    }
    if (message.length() > 300) {
      v.setError("The message is longer than 300 characters");
      return false;
    }

    v.clearFocus();
    InputMethodManager imm =
        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    v.animate().alpha(.6f).start();
    v.setText("Sending...");
    IShapeClient.c.sendComment(new CommentRequest(noti.getId(), noti.getType(), message))
        .enqueue(new Callback<CommentResponse>() {
          @Override
          public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
            if (response.isSuccessful()) {
              v.animate().alpha(1).start();
              v.setHint("Message sent for approval");
              v.setText("");
            } else {
              onFailure(null, new Throwable("Response code: " + response.code()));
            }
          }

          @Override public void onFailure(Call<CommentResponse> call, Throwable t) {
            v.animate().alpha(1).start();
            v.setHint("Unable to send message");
            v.setText("");
          }
        });
    return true;
  }

  private void setLoadingMore(boolean isLoading) {
    if (progMoreComments != null) {
      progMoreComments.setVisibility(isLoading ? VISIBLE : GONE);
    }
  }

  private void init(@Nullable AttributeSet attrs) {
    ButterKnife.bind(this, inflate(getContext(), R.layout.view_comments_opened, this));
    lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
      @Override
      public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
          super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
          Log.e("probe", "meet a IOOBE in RecyclerView");
        }
      }
    };
    lm.setReverseLayout(true);
    lm.setStackFromEnd(true);
    recComments.setLayoutManager(lm);

    recyclerAdapter = new CommentRecyclerAdapter(comments, recComments);
    recComments.setAdapter(recyclerAdapter);
    recyclerAdapter.setOnLoadMoreListener(new CommentRecyclerAdapter.OnLoadMoreListener() {
      @Override public void onLoadMore() {
        comments.add(null);
        recyclerAdapter.notifyItemInserted(comments.size() - 1);
        if (((comments.size() - 1) / PAGE_SIZE) == 0) return; //don't load on empty, need first q
        IShapeClient.c.getComments(new IdTypePagedRequest(noti.getId(), noti.getType(),
            (int) (((comments.size() - 1) / PAGE_SIZE) + 1), PAGE_SIZE))
            .enqueue(new Callback<List<CommentResponse>>() {
              @Override public void onResponse(Call<List<CommentResponse>> call,
                  Response<List<CommentResponse>> response) {
                if (response.isSuccessful()) {
                  comments.remove(comments.size() - 1);
                  recyclerAdapter.notifyItemRemoved(comments.size());
                  addComments(response.body());
                  recyclerAdapter.setLoaded();
                } else {
                  onFailure(null, new Throwable("Response code: " + response.code()));
                }
              }

              @Override public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
                comments.remove(comments.size() - 1);
                recyclerAdapter.notifyItemRemoved(comments.size());
                recyclerAdapter.setLoaded();
                Bus.post(Events.connectionError(t));
              }
            });
      }
    });
  }

  public void setNoti(Notification noti) {
    this.noti = noti;
    comments.clear();
    recyclerAdapter.notifyItemRangeRemoved(0, comments.size());
    lm.scrollToPosition(0);
    setLoadingMore(true);
    IShapeClient.c.getComments(new IdTypePagedRequest(noti.getId(), noti.getType(), 1, PAGE_SIZE))
        .enqueue(new Callback<List<CommentResponse>>() {
          @Override public void onResponse(Call<List<CommentResponse>> call,
              Response<List<CommentResponse>> response) {
            if (response.isSuccessful()) {
              if (recComments != null) {
                comments.clear();
                recyclerAdapter.notifyItemRangeRemoved(0, comments.size());
                addComments(response.body());
                lm.scrollToPosition(0);
                setLoadingMore(false);
              }
            } else {
              onFailure(null, new Throwable("Response code: " + response.code()));
            }
          }

          @Override public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
            Bus.post(Events.connectionError(t));
            setLoadingMore(false);
          }
        });
  }

  public void addComments(Collection<CommentResponse> commentResponses) {
    //filterContained(commentResponses);
    int insertPos = comments.size();
    comments.addAll(commentResponses);
    //sortCommentsByTime();
    recyclerAdapter.notifyItemRangeInserted(insertPos, commentResponses.size());
  }

  private void filterContained(Collection<CommentResponse> commentResponses) {
    for (CommentResponse comment : comments) {
      for (CommentResponse pluscomment : commentResponses) {
        if (pluscomment.id.equals(comment.id)) {
          commentResponses.remove(
              pluscomment); // FIXME: 2016. 08. 11. may introduce ConcurrentModificationException
          break;
        }
      }
    }
  }

  private void sortCommentsByTime() {
    Collections.sort(comments, new Comparator<CommentResponse>() {
      @Override public int compare(CommentResponse commentResponse, CommentResponse t1) {
        return commentResponse.getTime().compareTo(t1.getTime());
      }
    });
  }

  public void clearComments() {
    int c = comments.size();
    comments.clear();
    recyclerAdapter.notifyItemRangeRemoved(0, c);
  }

  @OnClick(R.id.imgX) public void hide() {
    editMessage.clearFocus();
    InputMethodManager imm =
        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(editMessage.getWindowToken(), 0);

    // get the center for the clipping circle
    int cx = getRight();
    int cy = getBottom();

    // get the final radius for the clipping circle
    int dx = Math.max(cx, getWidth());
    int dy = Math.max(cy, getHeight());
    float finalRadius = (float) Math.hypot(dx, dy);
    Animator animator =
        io.codetail.animation.ViewAnimationUtils.createCircularReveal(linCoRoot, cx, cy,
            finalRadius, 0);
    animator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {

      }

      @Override public void onAnimationEnd(Animator animator) {
        setVisibility(GONE);
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.setDuration(300);
    animator.start();
  }

  @OnFocusChange(R.id.editMessage) protected void onFocusChange(TextView v, boolean isFocused) {
    if (isFocused) {
      v.setHint(R.string.hint_new_comment);
    }
  }

  public void revealInParent(View v) {
    revParent = v;
    // get the center for the clipping circle
    int cx = v.getRight();
    int cy = v.getBottom();

    // get the final radius for the clipping circle
    int dx = Math.max(cx, v.getWidth());
    int dy = Math.max(cy, v.getHeight());
    float finalRadius = (float) Math.hypot(dx, dy);
    Animator animator =
        io.codetail.animation.ViewAnimationUtils.createCircularReveal(linCoRoot, cx, cy, 0,
            finalRadius);
    animator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {
        setVisibility(VISIBLE);
      }

      @Override public void onAnimationEnd(Animator animator) {

      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.setDuration(300);
    animator.start();
  }
}
