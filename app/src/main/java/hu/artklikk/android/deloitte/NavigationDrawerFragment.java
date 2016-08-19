package hu.artklikk.android.deloitte;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import hu.artklikk.android.deloitte.util.Util;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

  /**
   * Remember the position of the selected item.
   */
  private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

  /**
   * Per the design guidelines, you should show the drawer on launch until the user manually
   * expands it. This shared preference tracks this.
   */
  private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
  /**
   * Helper component that ties the action bar to the navigation drawer.
   */
  public ActionBarDrawerToggle mDrawerToggle;
  public ListView mDrawerListView;
  /**
   * A pointer to the current callbacks instance (the Activity).
   */
  private NavigationDrawerCallbacks mCallbacks;
  private DrawerLayout mDrawerLayout;
  private View mFragmentContainerView;

  private @DrawerId int mCurrentSelectedPosition = DRAWER_FEED;
  private boolean mUserLearnedDrawer;

  public NavigationDrawerFragment() {

  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Read in the flag indicating whether or not the user has demonstrated awareness of the
    // drawer. See PREF_USER_LEARNED_DRAWER for details.
    SharedPreferences sp = getContext().getSharedPreferences(Util.PREFS, Context.MODE_PRIVATE);
    mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

    if (savedInstanceState != null) {
      //noinspection WrongConstant
      mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
    }

    // Select either the default item (0) or the last selected item.
    selectItem(mCurrentSelectedPosition);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Indicate that this fragment would like to influence the set of actions in the action bar.
    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mDrawerListView =
        (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
      }
    });

    mDrawerListView.setAdapter(
        new ArrayAdapter<>(getActionBar().getThemedContext(), R.layout.list_item_menu,
            R.id.textMenuItem, new String[] {
            getString(R.string.drawer_feed),
            getString(R.string.drawler_map),
            getString(R.string.drawler_search),
            getString(R.string.drawler_toplist),
            getString(R.string.drawler_activity),
            getString(R.string.drawler_mentor),
            getString(R.string.drawler_calendar),
            getString(R.string.drawler_profile),
            getString(R.string.drawler_rules),
            getString(R.string.drawler_legal)
        }));
    mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
    return mDrawerListView;
  }

  public static final int DRAWER_FEED = 0;
  public static final int DRAWER_MAP = 1;
  public static final int DRAWER_SEARCH = 2;
  public static final int DRAWER_TOPLIST = 3;
  public static final int DRAWER_ACTIVITY = 4;
  public static final int DRAWER_MENTOR = 5;
  public static final int DRAWER_CALENDAR = 6;
  public static final int DRAWER_PROFILE = 7;
  public static final int DRAWER_RULES = 8;
  public static final int DRAWER_LEGAL = 9;

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({DRAWER_ACTIVITY, DRAWER_CALENDAR, DRAWER_FEED, DRAWER_LEGAL, DRAWER_MAP,
      DRAWER_MENTOR, DRAWER_PROFILE, DRAWER_RULES, DRAWER_SEARCH, DRAWER_TOPLIST})
  @interface DrawerId {}

  /**
   * Users of this fragment must call this method to set up the navigation drawer interactions.
   *
   * @param fragmentId The android:id of this fragment in its activity's layout.
   * @param drawerLayout The DrawerLayout containing this fragment's UI.
   */
  public void setUp(int fragmentId, DrawerLayout drawerLayout, ImageView toggle) {
    mFragmentContainerView = getActivity().findViewById(fragmentId);
    mDrawerLayout = drawerLayout;

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // set up the drawer's list view with items and click listener
    getActionBar().hide();

    toggle.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        openCloseDrawler();
      }
    });

        /*
        ActionBar actionBar =  getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);
        //TODO set icon!!!
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.deloitte_icon));
        */

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the navigation drawer and the action bar app icon.
    mDrawerToggle = new ActionBarDrawerToggle(getActivity(),                    /* host Activity */
        mDrawerLayout,                    /* DrawerLayout object */
        R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
        R.string.navigation_drawer_close  /* "close drawer" description for accessibility */) {

      @Override public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, 0);
      }

      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (!isAdded()) {
          return;
        }

        getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
      }

      @Override public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (!isAdded()) {
          return;
        }

        if (!mUserLearnedDrawer) {
          // The user manually opened the drawer; store this flag to prevent auto-showing
          // the navigation drawer automatically in the future.
          mUserLearnedDrawer = true;
          SharedPreferences sp = getContext().getSharedPreferences(Util.PREFS, Context.MODE_PRIVATE);
          sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }

        getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
      }
    };

    // Defer code dependent on restoration of previous instance state.
    mDrawerLayout.post(new Runnable() {
      @Override public void run() {
        mDrawerToggle.syncState();
      }
    });

    mDrawerLayout.addDrawerListener(mDrawerToggle);
  }

  private void selectItem(@DrawerId int position) {
    mCurrentSelectedPosition = position;
    if (mDrawerListView != null) {
      mDrawerListView.setItemChecked(position, true);
    }
    if (mDrawerLayout != null) {
      mDrawerLayout.closeDrawer(mFragmentContainerView);
    }
    if (mCallbacks != null) {
      mCallbacks.onNavigationDrawerItemSelected(position);
    }
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mCallbacks = (NavigationDrawerCallbacks) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
  }

  private void openCloseDrawler() {
    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
      mDrawerLayout.closeDrawer(Gravity.RIGHT);
    } else {
      mDrawerLayout.openDrawer(Gravity.RIGHT);
    }
  }

  public boolean isDrawlerOpened() {
    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
      mDrawerLayout.closeDrawer(Gravity.RIGHT);
      return true;
    } else {
      return false;
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item != null && item.getItemId() == android.R.id.home) {
      openCloseDrawler();
    }
    return false;
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Forward the new configuration the drawer toggle component.
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  private ActionBar getActionBar() {
    return ((AppCompatActivity) getActivity()).getSupportActionBar();
  }

  /**
   * Callbacks interface that all activities using this fragment must implement.
   */
  public interface NavigationDrawerCallbacks {
    /**
     * Called when an item in the navigation drawer is selected.
     */
    void onNavigationDrawerItemSelected(@DrawerId int position);
  }
}
