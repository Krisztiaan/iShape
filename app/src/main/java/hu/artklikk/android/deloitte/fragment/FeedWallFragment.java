package hu.artklikk.android.deloitte.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import hu.artklikk.android.deloitte.Bus;
import hu.artklikk.android.deloitte.EndlessRecyclerViewScrollListener;
import hu.artklikk.android.deloitte.Events;
import hu.artklikk.android.deloitte.NotificationRecyclerAdapter;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.SpaceItemDecoration;
import hu.artklikk.android.deloitte.client.IShapeClient;
import hu.artklikk.android.deloitte.model.Notification;
import hu.artklikk.android.deloitte.model.PagedRequest;
import hu.artklikk.android.deloitte.views.CommentsListView;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class FeedWallFragment extends Fragment {

  public static final String WALL_TYPE = "wall_type";
  public static final int PAGE_SIZE = 10;

  private final List<Notification> notificationList = new ArrayList<>();
  private final NotificationRecyclerAdapter adapter =
      new NotificationRecyclerAdapter(notificationList);
  @BindView(R.id.recFeedWall) RecyclerView recFeedWall;
  @BindView(R.id.progWall) ProgressBar progWall;
  @BindView(R.id.commentsView) CommentsListView commentsListView;

  public static FeedWallFragment getInstance(@Nullable @PagedRequest.FeedType String wallType) {
    FeedWallFragment fragment = new FeedWallFragment();
    Bundle args = new Bundle();
    args.putString(WALL_TYPE, wallType);
    fragment.setArguments(args);
    return fragment;
  }

  void showLoading(boolean isLoading) {
    progWall.setVisibility(isLoading ? View.VISIBLE : View.GONE);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_feed_wall, null);
    ButterKnife.bind(this, view);
    recFeedWall.setAdapter(adapter);
    LinearLayoutManager lm =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
          @Override
          public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
              super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
              Log.e("probe", "meet a IOOBE in RecyclerView");
            }
          }
        };
    recFeedWall.setLayoutManager(lm);
    recFeedWall.addItemDecoration(
        new SpaceItemDecoration(getActivity(), R.dimen.activity_vertical_margin, true, true));
    recFeedWall.addOnScrollListener(new EndlessRecyclerViewScrollListener(lm) {
      @Override public void onLoadMore(int page, int totalItemsCount) {
        //noinspection WrongConstant
        IShapeClient.c.getActivityFeed(
            new PagedRequest(page + 1, PAGE_SIZE, getArguments().getString(WALL_TYPE, null)))
            .enqueue(new Callback<List<Notification>>() {
              @Override public void onResponse(Call<List<Notification>> call,
                  Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                  notificationList.addAll(response.body());
                  adapter.notifyItemRangeInserted(0, notificationList.size());
                  showLoading(false);
                } else {
                  onFailure(null, new Throwable("Response code: " + response.code()));
                }
              }

              @Override public void onFailure(Call<List<Notification>> call, Throwable t) {
                Bus.post(Events.connectionError(t));
                showLoading(false);
              }
            });
      }
    });
    //noinspection WrongConstant
    IShapeClient.c.getActivityFeed(
        new PagedRequest(1, PAGE_SIZE, getArguments().getString(WALL_TYPE, null)))
        .enqueue(new Callback<List<Notification>>() {
          @Override public void onResponse(Call<List<Notification>> call,
              Response<List<Notification>> response) {
            if (response.isSuccessful()) {
              notificationList.addAll(response.body());
              adapter.notifyItemRangeInserted(0, notificationList.size());
              showLoading(false);
            } else {
              onFailure(null, new Throwable("Response code: " + response.code()));
            }
          }

          @Override public void onFailure(Call<List<Notification>> call, Throwable t) {
            Bus.post(Events.connectionError(t));
            showLoading(false);
          }
        });
    return view;
  }

  @Override public void onResume() {
    super.onResume();
    Bus.register(this);
  }

  @Override public void onPause() {
    super.onPause();
    Bus.unregister(this);
  }

  @Subscribe protected void onOpenComment(Events.OpenComment event) {
    if ((getArguments().getString(WALL_TYPE, null) == null && FeedFragment.currentPage == null) || (
        (getArguments().getString(WALL_TYPE, null) != null && FeedFragment.currentPage != null)
            && getArguments().getString(WALL_TYPE, null).equals(FeedFragment.currentPage))) {
      if (event.isOpen) {
        commentsListView.setNoti(event.notification);
        commentsListView.revealInParent(recFeedWall);
      } else {
        commentsListView.hide();
      }
    }
  }
}
