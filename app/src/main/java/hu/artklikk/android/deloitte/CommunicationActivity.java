package hu.artklikk.android.deloitte;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.artklikk.android.deloitte.adapter.EndorsementListAdapter;
import hu.artklikk.android.deloitte.client.IShapeClient;
import hu.artklikk.android.deloitte.map.BrowseMapFragment;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.model.communication.LoginResponse;
import hu.artklikk.android.deloitte.util.AnimalProgressDialog;
import hu.artklikk.android.deloitte.util.Util;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by gyozofule on 15. 11. 24..
 */
public class CommunicationActivity extends AppCompatActivity {

  public static final ArrayList<User> userList = new ArrayList<>();
  public static final HashMap<Integer, User> userMap = new HashMap<>();
  static protected Dialog dialog;
  static private Dialog progress;
  public List<Endorsement> newEndorsementList;
  private EditText recPass;

  private EditText newPass;

  private EditText email;
  BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

    @Override public void onReceive(Context context, Intent intent) {
      doLogin();
    }
  };

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override protected void onStop() {
    super.onStop();
    unregisterReceiver(networkStateReceiver);
  }

  private void doLogin() {
    if (isConnected()) {
      String pass =
          Util.getSharedPrefence(CommunicationActivity.this, R.string.preference_key_password);
      if (pass.isEmpty()) {
        showLoginDialog();
      } else {
        new LoginTask().execute(
            Util.getSharedPrefence(CommunicationActivity.this, R.string.preference_key_email), pass,
            "");
      }
    } else {
      showDialog();
      showQuitDialog("No internet access...");
    }
  }

  @Override protected void onStart() {
    super.onStart();
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(networkStateReceiver, filter);
  }

  private void showLoginDialog() {
    if (dialog != null && dialog.isShowing()) return;
    Rect displayRectangle = new Rect();
    Window window = getWindow();
    window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

    dialog = new Dialog(this);

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_fragment_login);

    email = (EditText) dialog.findViewById(R.id.email);
    recPass = (EditText) dialog.findViewById(R.id.password);
    newPass = (EditText) dialog.findViewById(R.id.new_password);

    setEyeClick(newPass);
    setEyeClick(recPass);

    dialog.setCancelable(false);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = (int) (displayRectangle.width() * 0.8f);
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

    dialog.getWindow().setAttributes(lp);
    dialog.getWindow().setDimAmount(0.3f);
    dialog.show();

    dialog.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (email.getText().toString().isEmpty() || recPass.getText().toString().isEmpty()) {
          Toast.makeText(CommunicationActivity.this, "The email and password fields are required!",
              Toast.LENGTH_LONG).show();
        } else {
          dialog.dismiss();
          new LoginTask().execute(email.getText().toString(), recPass.getText().toString(),
              newPass.getText().toString());
        }
      }
    });
  }

  private void setEyeClick(final EditText editText) {
    editText.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_UP) {
          if (event.getRawX() >= (editText.getRight()
              - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
            if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
              editText.setTransformationMethod(null);
              editText.setCompoundDrawablesWithIntrinsicBounds(null, null,
                  getResources().getDrawable(R.drawable.szem_open), null);
            } else {
              editText.setTransformationMethod(new PasswordTransformationMethod());
              editText.setCompoundDrawablesWithIntrinsicBounds(null, null,
                  getResources().getDrawable(R.drawable.szem_closed), null);
            }

            return true;
          }
        }
        return false;
      }
    });
  }

  private void showDialog() {
    if (null == progress) {
      progress = AnimalProgressDialog.getProgressDialog(this);
      progress.show();
    }
  }

  private void dismissDialog() {
    if (null != progress) {
      progress.dismiss();
      progress = null;
    }
  }

  private String getCookieFromConnection(HttpURLConnection connection) {
    String COOKIES_HEADER = "Set-Cookie";
    CookieManager msCookieManager = new CookieManager();

    Map<String, List<String>> headerFields = connection.getHeaderFields();
    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

    if (cookiesHeader != null) {
      for (String cookie : cookiesHeader) {
        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
      }
    }

    for (HttpCookie cookie : msCookieManager.getCookieStore().getCookies()) {
      if (cookie.getName().equals("ASP.NET_SessionId")) return cookie.getValue();
    }

    return null;
  }

  // check network connection
  public boolean isConnected() {
    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected()) {
      return true;
    } else {
      return false;
    }
  }

  private void showQuitDialog(String text) {
    new AlertDialog.Builder(this).setTitle("Ooooops!")
        .setMessage(text)
        .setPositiveButton("exit", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            finish();
          }
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
  }

  private void showEndorsementDialog(Map<Integer, User> userMap,
      List<Endorsement> endorsementList) {
    final Dialog dialog = new Dialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_fragment_endorsement_list);

    ListView listView = (ListView) dialog.findViewById(android.R.id.list);

    listView.setAdapter(new EndorsementListAdapter(endorsementList, userMap, this));

    dialog.setCancelable(false);
    dialog.getWindow().setDimAmount(0.3f);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    dialog.show();

    dialog.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
        sendBroadcast(new Intent(BrowseMapFragment.USER_BROADCAST));
      }
    });
  }

  protected class LoginTask extends AsyncTask<String, Void, LoginResponse> {
    @Override protected LoginResponse doInBackground(String... params) {
      try {
        String uri = Uri.parse(IShapeClient.BASE_URL + "Login")
            .buildUpon()
            .appendQueryParameter("email", params[0])
            .appendQueryParameter("password", params[1])
            .appendQueryParameter("newpassword", params[2])
            .build()
            .toString();
        try {
          URL url = new URL(uri);
          HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
          ObjectMapper mapper = new ObjectMapper();
          LoginResponse loginResponse =
              mapper.readValue(urlConnection.getInputStream(), LoginResponse.class);

          if (null != loginResponse) {
            String cookie = getCookieFromConnection(urlConnection);
            if (cookie == null) {
              return null;
            } else {
              Util.saveSharedPref(CommunicationActivity.this, R.string.preference_key_token,
                  cookie);
            }
          }

          return loginResponse;
        } catch (Exception e) {
          Log.d(getClass().toString(), e.toString());
          return null;
        }
      } catch (Exception e) {
        Log.e("MainActivity", e.getMessage(), e);
      }
      return null;
    }

    @Override protected void onPreExecute() {
      super.onPreExecute();
      showDialog();
    }

    @Override protected void onPostExecute(LoginResponse loginResponse) {
      if (null != loginResponse) {
        if (null != recPass) {
          if (newPass.getText().toString().isEmpty()) {
            Util.saveSharedPref(CommunicationActivity.this, R.string.preference_key_password,
                recPass.getText().toString());
          } else {
            Util.saveSharedPref(CommunicationActivity.this, R.string.preference_key_password,
                newPass.getText().toString());
          }

          Util.saveSharedPref(CommunicationActivity.this, R.string.preference_key_email,
              email.getText().toString());
        }

        userList.clear();
        userMap.clear();
        userList.addAll(loginResponse.getUsers());
        for (User user : userList) {
          userMap.put(user.getId(), user);
        }
        Util.saveSharedPref(CommunicationActivity.this, R.string.preference_key_id,
            loginResponse.getSelfuserId());
        newEndorsementList = loginResponse.getNominations();
        if (0 < newEndorsementList.size()) {
          showEndorsementDialog(userMap, newEndorsementList);
        } else {
          sendBroadcast(new Intent(BrowseMapFragment.USER_BROADCAST));
        }

        if (null != dialog) {
          dialog.dismiss();
        }
        dismissDialog();

        Bus.post(Events.loginResult(true));
      } else {
        Bus.post(Events.loginResult(false));
        if (Util.getSharedPrefence(CommunicationActivity.this, R.string.preference_key_password)
            .isEmpty()) {
          Toast.makeText(CommunicationActivity.this, "Please check password or server connection",
              Toast.LENGTH_LONG).show();
          doLogin();
          dismissDialog();
        } else {
          showQuitDialog("We have some problem with login. Please try again later...");
        }
      }
    }
  }
}
