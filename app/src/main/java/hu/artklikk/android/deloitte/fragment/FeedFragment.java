package hu.artklikk.android.deloitte.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.astuetz.PagerSlidingTabStrip;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.PagedRequest;

public class FeedFragment extends Fragment {

  public static String ENDORSEMENT = "endorsement";
  @BindView(R.id.tabs) PagerSlidingTabStrip tabs;
  @BindView(R.id.viewPager) ViewPager viewPager;

  public FeedFragment() {
  }

  public static FeedFragment newInstance() {
    FeedFragment fragment = new FeedFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, final ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_feed, null);

    ButterKnife.bind(this, view);

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      viewPager.setAdapter(new ScreenSlidePagerAdapter(getFragmentManager()));
    } else {
      viewPager.setAdapter(new ScreenSlidePagerAdapter(getChildFragmentManager()));
    }

    tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
    //tabs.setActivateTextColor(getResources().getColor(android.R.color.white));
    //tabs.setDeactivateTextColor(getResources().getColor(R.color.light_blue));
    tabs.setViewPager(viewPager);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position) {
          case 0:
            currentPage = null;
            break;
          case 1:
            currentPage = PagedRequest.SC_NOMINATION;
            break;
          case 2:
            currentPage = PagedRequest.SC_NOTIFICATION;
            break;
          default:
            currentPage = null;
        }
      }

      @Override public void onPageSelected(int position) {

      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });

    return view;
  }



  public static String currentPage = null;

  private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public ScreenSlidePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return FeedWallFragment.getInstance(null);
        case 1:
          return FeedWallFragment.getInstance(PagedRequest.SC_NOMINATION);
        case 2:
          return FeedWallFragment.getInstance(PagedRequest.SC_NOTIFICATION);
        default:
          return FeedWallFragment.getInstance(null);
      }
    }

    @Override public int getCount() {
      return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "All";
        case 1:
          return "Endorsements";
        case 2:
          return "Messages";
        default:
          return "";
      }
    }
  }
}
