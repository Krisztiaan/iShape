package hu.artklikk.android.deloitte;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import hu.artklikk.android.deloitte.fragment.ActionsFragment;
import hu.artklikk.android.deloitte.fragment.CalendarFragment;
import hu.artklikk.android.deloitte.fragment.FeedFragment;
import hu.artklikk.android.deloitte.fragment.LegalFragment;
import hu.artklikk.android.deloitte.fragment.MentorFragment;
import hu.artklikk.android.deloitte.fragment.ProfileFragment;
import hu.artklikk.android.deloitte.fragment.RulesListFragment;
import hu.artklikk.android.deloitte.fragment.UserListFragment;
import hu.artklikk.android.deloitte.map.BrowseMapFragment;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import org.greenrobot.eventbus.Subscribe;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_ACTIVITY;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_CALENDAR;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_FEED;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_LEGAL;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_MAP;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_MENTOR;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_PROFILE;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_RULES;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_SEARCH;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DRAWER_TOPLIST;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.DrawerId;
import static hu.artklikk.android.deloitte.NavigationDrawerFragment.NavigationDrawerCallbacks;

public class MainActivity extends CommunicationActivity implements NavigationDrawerCallbacks {

  public TextView title;

  /**
   * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
   */
  private NavigationDrawerFragment mNavigationDrawerFragment;

  private ImageView crown;

  private ImageButton backButton;

  private ImageView icon;

  private RelativeLayout toolbar;

  private Fragment queue;
  private int queueNo;

  @Override public void onDestroy() {
    super.onDestroy();

    int id = android.os.Process.myPid();
    android.os.Process.killProcess(id);
  }

  @Override protected void onResume() {
    super.onResume();
    Bus.register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    Bus.unregister(this);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mNavigationDrawerFragment =
        (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(
            R.id.navigation_drawer);

    crown = (ImageView) findViewById(R.id.crown);
    backButton = (ImageButton) findViewById(R.id.backButton);
    icon = (ImageView) findViewById(R.id.icon);
    title = (TextView) findViewById(R.id.title);
    toolbar = (RelativeLayout) findViewById(R.id.toolbar);

        /*
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_big);

        int blue = bitmap.getPixel(20, 20);
        int yC = bitmap.getHeight() / 50;
        int xC = bitmap.getWidth() / 25;
        for (int i = 0; i < bitmap.getHeight(); i += yC){
            String row = "";
            for (int j = 0; j < bitmap.getWidth(); j += xC){
                if(bitmap.getPixel(j,i) == blue) {
                    row += "," + 0 + "(" + i + "," + j +")";
                } else {
                    row += "," + 1 + "(" + i + "," + j +")";
                }

            }
            Log.d(getClass().toString(), row);
        }
        */

    backButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });

    // Set up the drawer.
    mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout),
        (ImageButton) findViewById(R.id.imageButtonDrawler));
  }

  @Override public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putSerializable("userMap", userMap);
    savedInstanceState.putSerializable("userList", userList);
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    userMap.clear();
    userList.clear();
    userMap.putAll((HashMap<Integer, User>) savedInstanceState.getSerializable("userMap"));
    userList.addAll((ArrayList<User>) savedInstanceState.getSerializable("userList"));
  }

  @Override public void onNavigationDrawerItemSelected(@DrawerId int position) {
    FragmentManager fragmentManager = getFragmentManager();
    Fragment fragment = BrowseMapFragment.newInstance();

    if (null != crown) {
      crown.setVisibility(View.GONE);
    }

    switch (position) {
      case DRAWER_MAP:
        fragment = BrowseMapFragment.newInstance();
        break;
      case DRAWER_SEARCH:
        fragment = UserListFragment.newInstance(false);
        break;
      case DRAWER_TOPLIST:
        fragment = UserListFragment.newInstance(true);
        break;
      case DRAWER_PROFILE:
        fragment =
            ProfileFragment.newInstance(Util.getSharedPrefenceId(this, R.string.preference_key_id));
        break;
      case DRAWER_ACTIVITY:
        fragment = ActionsFragment.newInstance();
        break;
      case DRAWER_RULES:
        fragment = RulesListFragment.newInstance();
        break;
      case DRAWER_MENTOR:
        for (User user : userList) {
          if (user.isMenthor()) {
            fragment = MentorFragment.newInstance(user.getId());
            crown.setVisibility(View.VISIBLE);
          }
        }
        break;
      case DRAWER_CALENDAR:
        fragment = CalendarFragment.newInstance();
        break;
      case DRAWER_LEGAL:
        fragment = LegalFragment.newInstance();
        break;

      case DRAWER_FEED:
        fragment = FeedFragment.newInstance();
        break;
    }
    if (userList.isEmpty()) {
      queue = fragment;
      queueNo = position;
      return;
    }

    fragmentManager.beginTransaction()
        .replace(R.id.container, fragment)
        .addToBackStack(onSectionAttached(position))
        .commit();
  }

  @Subscribe protected void onLoginEvent(Events.LoginResult event) {
    if (event.isSuccess && queue != null) {
      getFragmentManager().beginTransaction()
          .replace(R.id.container, queue)
          .addToBackStack(onSectionAttached(queueNo))
          .commit();
    }
  }

  @Subscribe protected void onOpenProfileEvent(Events.OpenProfile event) {
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.container, ProfileFragment.newInstance(event.userId))
        .addToBackStack(onSectionAttached(DRAWER_PROFILE))
        .commit();
  }

  @Subscribe void onConnectionError(Events.ConnectionError event) {
    Toast.makeText(MainActivity.this, "Communication error: " + event.t.getMessage(),
        Toast.LENGTH_SHORT).show();
    event.t.printStackTrace();
  }

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  private boolean wantsToExit=false;

  @Override public void onBackPressed() {
    int backstackCount = getFragmentManager().getBackStackEntryCount();
    if (mNavigationDrawerFragment.isDrawlerOpened()) {
      //close the drawler
    } else if (0 < backstackCount && !wantsToExit) {
      if(backstackCount<=2) {
        Toast.makeText(MainActivity.this, "Press again to exit.", Toast.LENGTH_SHORT).show();
        wantsToExit = true;
        toolbar.postDelayed(new Runnable() {
          @Override public void run() {
            wantsToExit = false;
          }
        }, 3000);
      }
      int fragmentId =
          Integer.valueOf(getFragmentManager().getBackStackEntryAt(backstackCount - 1).getName());
      if (fragmentId == DRAWER_FEED) {
        Bus.post(Events.closeComment(null));
      } else {
        getFragmentManager().popBackStack();
      }
      if (null != this.title) {
        //noinspection WrongConstant
        onSectionAttached(fragmentId);
        mNavigationDrawerFragment.mDrawerListView.setItemChecked(fragmentId, true);
      }
      crown.setVisibility(fragmentId == 6 ? View.VISIBLE : View.GONE);
    } else {
      super.onBackPressed();
      super.onBackPressed();
    }
  }

  public String onSectionAttached(@DrawerId int number) {
    String title = "title";

    switch (number) {
      case DRAWER_FEED:
        title = "";
        break;
      case DRAWER_SEARCH:
        title = getString(R.string.drawler_search);
        break;
      case DRAWER_TOPLIST:
        title = getString(R.string.drawler_toplist);
        break;
      case DRAWER_PROFILE:
        title = getString(R.string.drawler_profile);
        break;
      case DRAWER_ACTIVITY:
        title = getString(R.string.drawler_activity);
        break;
      case DRAWER_RULES:
        title = getString(R.string.drawler_rules);
        break;
      case DRAWER_MENTOR:
        title = getString(R.string.drawler_mentor);
        break;
      case DRAWER_CALENDAR:
        title = getString(R.string.drawler_calendar);
        break;
      case DRAWER_LEGAL:
        title = getString(R.string.drawler_legal);
        break;
      case DRAWER_MAP:
        title = "";
        break;
    }

    if (null != this.title) {
      this.title.setText(title);

      if (number != DRAWER_MAP && number != DRAWER_FEED) {
        icon.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
      } else {
        icon.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
      }

      if (number == DRAWER_ACTIVITY || number == DRAWER_FEED) {
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
      } else {
        toolbar.setBackground(getResources().getDrawable(R.drawable.bg_with_blue_bottom_border));
      }

      if (number == DRAWER_FEED) {

      }
    }

    return String.valueOf(number);
  }
}
