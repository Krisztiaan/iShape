package hu.artklikk.android.deloitte.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import hu.artklikk.android.deloitte.Application;
import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.adapter.AutocompleteAdapter;
import hu.artklikk.android.deloitte.map.model.MapObjectModel;
import hu.artklikk.android.deloitte.model.Animal;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.model.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by gyozofule on 15. 11. 08..
 */
public class Util {
  private static long autocompleteId;

  public static Bitmap decodeBase64(String base64) {
    byte[] data = Base64.decode(base64, Base64.DEFAULT);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(data, 0, data.length);
    return decodedByte;
  }

  public static String[] convertUserNamesToArray(List<User> userList) {
    String[] userNameArray = new String[userList.size()];
    int counter = 0;
    for (User user : userList) {
      userNameArray[counter] = user.getName();
      ++counter;
    }

    return userNameArray;
  }

  public static Drawable generatePinToMap(User user, Resources resources) {
    return generatePinToMap(user, resources, null);
  }

  public static long timeTickConverter(long ticks) {
    return (ticks - 621355968000000000L) / 10000;
  }

  public static Drawable generatePinToMap(User user, Resources resources, String base64) {
    Bitmap pinBitmap;
    if (MapObjectModel.ROLE.SELF.equals(user.getRole())) {
      if (user.isMenthor()) {
        pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi4c);
      } else {
        pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi4);
      }
    } else {
      if (user.isMenthor()) {
        switch (user.getUsedYears()) {
          case 0:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi1c);
            break;
          case 1:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi2c);
            break;
          case 2:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi3c);
            break;
          default:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi3c);
            break;
        }
      } else {
        switch (user.getUsedYears()) {
          case 0:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi1);
            break;
          case 1:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi2);
            break;
          case 2:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi3);
            break;
          default:
            pinBitmap = BitmapFactory.decodeResource(resources, R.drawable.poi3);
            break;
        }
      }
    }

    Bitmap bitmap = pinBitmap.copy(Bitmap.Config.ARGB_8888, true);

    Canvas c = new Canvas(bitmap);
    Bitmap imageBitmap;
    if (null == base64) {
      imageBitmap = getCroppedBitmap(
          Bitmap.createScaledBitmap(Util.decodeBase64(user.getProfileImage()),
              (int) (pinBitmap.getWidth() / 1.45), (int) (pinBitmap.getWidth() / 1.45), false));
    } else {
      imageBitmap = getCroppedBitmap(
          Bitmap.createScaledBitmap(Util.decodeBase64(base64), (int) (pinBitmap.getWidth() / 1.45),
              (int) (pinBitmap.getWidth() / 1.45), false));
    }

    if (user.isMenthor()) {
      c.drawBitmap(imageBitmap, (int) (pinBitmap.getWidth() / 6.2),
          (int) (pinBitmap.getWidth() / 1.55), null);
    } else {
      c.drawBitmap(imageBitmap, (int) (pinBitmap.getWidth() / 6.2),
          (int) (pinBitmap.getWidth() / 6.2), null);
    }

    imageBitmap.recycle();
    pinBitmap.recycle();

    return new BitmapDrawable(resources, bitmap);
  }

  public static Drawable generatePinToLists(User user, Resources resources, int resId) {
    Bitmap pinBitmap = BitmapFactory.decodeResource(resources, resId);

    Bitmap bitmap = pinBitmap.copy(Bitmap.Config.ARGB_8888, true);

    Canvas c = new Canvas(bitmap);
    Bitmap imageBitmap;
    if (null == user) {
      return new BitmapDrawable(resources, pinBitmap);
    } else {
      imageBitmap = getCroppedBitmap(Util.decodeBase64(user.getProfileImage()));
    }

    Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, (int) (pinBitmap.getWidth() / 1.45),
        (int) (pinBitmap.getWidth() / 1.45), false);

    c.drawBitmap(scaled, (int) (pinBitmap.getWidth() / 6.2), (int) (pinBitmap.getWidth() / 6.2),
        null);

    imageBitmap.recycle();
    pinBitmap.recycle();

    return new BitmapDrawable(resources, bitmap);
  }

  public static String generateHTMLContent(String description) {
    return generateHTMLContent(description, false);
  }

  public static String generateHTMLContent(String description, boolean isTransparemnt) {

    String background = isTransparemnt ? "rgba(255,255,255,0.5)" : "#fff";

    return "<html><head>" + "<style type=\"text/css\">body{color: " +
        "#00A4DA;" +
        " background-color: " + background +
        ";}" + "</style></head>" + "<body>" + description + "</body></html>";
  }

  public static long convertDateTimeTicks(long ticks) {
    return (ticks - 621355968000000000L) / 10000;
  }

  public static Dialog buildEndorsementDialog(final Activity activity, Handler handler) {
    return buildEndorsementDialog(activity, null, handler);
  }

  public static Dialog buildEndorsementDialog(final Activity activity, final User user,
      final Handler handler) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    View view =
        activity.getLayoutInflater().inflate(R.layout.dialog_fragment_new_endorsement, null);

    final long dateNow = System.currentTimeMillis();
    autocompleteId = -1;

    final int selfId = Util.getSharedPrefenceId(activity, R.string.preference_key_id);

    ArrayList<User> listWithoutSelf = new ArrayList<>();
        listWithoutSelf.addAll(MainActivity.userList);
    listWithoutSelf.remove(MainActivity.userMap.get(selfId));

    final AutoCompleteTextView searchText =
        (AutoCompleteTextView) view.findViewById(R.id.searchText);
    AutocompleteAdapter nameAdapter =
        new AutocompleteAdapter(activity, android.R.layout.simple_list_item_1, listWithoutSelf);
    searchText.setAdapter(nameAdapter);

    searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        autocompleteId = id;
      }
    });

    TextView title = (TextView) view.findViewById(R.id.title);
    TextView endorse = (TextView) view.findViewById(R.id.endorse);
    final TextView point = (TextView) view.findViewById(R.id.point);
    final TextView reason = (TextView) view.findViewById(R.id.reason);
    final Spinner category = (Spinner) view.findViewById(R.id.spinnerCategory);
    category.setAdapter(new ArrayAdapter<>(activity, R.layout.spinner_text,
        activity.getResources().getStringArray(R.array.category_selector_array)));

    final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

    view.findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!reason.getText().toString().isEmpty() && (autocompleteId != -1 || null != user)) {
          String categoryText =
              Endorsement.Category.getCategoryOfId(category.getSelectedItemPosition()).getCode();
          Endorsement endorsement;
          if (null != user) {
            endorsement =
                new Endorsement(0, selfId, user.getId(), dateNow, (byte) ratingBar.getRating(),
                    categoryText, reason.getText().toString(), false);
          } else {
            endorsement =
                new Endorsement(0, selfId, autocompleteId, dateNow, (byte) ratingBar.getRating(),
                    categoryText, reason.getText().toString(), false);
          }

          Message message = new Message();
          Bundle bundle = new Bundle();
          bundle.putSerializable("data", endorsement);
          message.setData(bundle);
          handler.sendMessage(message);
        } else {
          Toast.makeText(activity, "Fill up all fields correctly!", Toast.LENGTH_LONG).show();
        }
      }
    });

    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
      @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        point.setText(String.format(activity.getResources().
            getString(R.string.points), String.valueOf((int) rating)));
      }
    });

    ratingBar.setRating(1);

    if (null != user) {
      view.findViewById(R.id.toLayout).setVisibility(View.GONE);
      title.setText(R.string.i_would_like_to);
      view.findViewById(R.id.toTitleLayout).setVisibility(View.VISIBLE);
      endorse.setText(user.getName());
    }

    final TextView date = (TextView) view.findViewById(R.id.date);
    date.setText(convertTimestampToDate(dateNow, true));

    builder.setView(view);
    Dialog dialog = builder.create();
    dialog.getWindow().setDimAmount(0.3f);

    return dialog;
  }

  private static Bitmap getCroppedBitmap(Bitmap bitmap) {
    Bitmap output =
        Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(output);

    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);

    return output;
  }

  public static ImageView resolveAnimalDrawable(Animal.AnimalType animalType, Context context,
      boolean isMini) {
    int resourceId;

    switch (animalType) {
      case Rabbit:
        resourceId = R.drawable.nyul;
        break;
      case Panda:
        resourceId = R.drawable.panda;
        break;
      case Fox:
        resourceId = R.drawable.roka;
        break;
      case Owl:
        resourceId = R.drawable.bagoly;
        break;
      case Beaver:
        resourceId = R.drawable.hod;
        break;
      case Monkey:
        resourceId = R.drawable.majom;
        break;
      case Bee:
        resourceId = R.drawable.meh;
        break;
      case Dog:
        resourceId = R.drawable.kutya;
        break;
      case Butterfly:
        resourceId = R.drawable.pillango;
        break;
      case Parrot:
        resourceId = R.drawable.papagaj;
        break;
      case Squirrel:
        resourceId = R.drawable.mokus;
        break;
      case Racoon:
        resourceId = R.drawable.mosomedve;
        break;
      case Lion:
        resourceId = R.drawable.oroszlan;
        break;
      case Zebra:
        resourceId = R.drawable.zebra;
        break;
      case Unicorn:
        resourceId = R.drawable.unikornis;
        break;
      case Sloth:
        resourceId = R.drawable.lajhar;
        break;
      case Chameleon:
        resourceId = R.drawable.kameleon;
        break;
      case Buffalo:
        resourceId = R.drawable.bivaly;
        break;
      case Tiger:
        resourceId = R.drawable.tigris;
        break;
      default:
        resourceId = R.drawable.nyul;
        break;
    }

    ImageView imageView = new ImageView(context);
    imageView.setImageResource(resourceId);
    imageView.setPadding(10, 10, 10, 10);

    if (isMini) {
      int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
          context.getResources().getDisplayMetrics());
      int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
          context.getResources().getDisplayMetrics());

      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
      imageView.setLayoutParams(layoutParams);
    }
    return imageView;
  }

  public static @DrawableRes int getAnimalDrawableId(Animal.AnimalType type) {
    switch (type) {
      case Rabbit:
        return R.drawable.nyul;

      case Panda:
        return R.drawable.panda;

      case Fox:
        return R.drawable.roka;

      case Owl:
        return R.drawable.bagoly;

      case Beaver:
        return R.drawable.hod;

      case Monkey:
        return R.drawable.majom;

      case Bee:
        return R.drawable.meh;

      case Dog:
        return R.drawable.kutya;

      case Butterfly:
        return R.drawable.pillango;

      case Parrot:
        return R.drawable.papagaj;

      case Squirrel:
        return R.drawable.mokus;

      case Racoon:
        return R.drawable.mosomedve;

      case Lion:
        return R.drawable.oroszlan;

      case Zebra:
        return R.drawable.zebra;

      case Unicorn:
        return R.drawable.unikornis;

      case Sloth:
        return R.drawable.lajhar;

      case Chameleon:
        return R.drawable.kameleon;

      case Buffalo:
        return R.drawable.bivaly;

      case Tiger:
        return R.drawable.tigris;

      default:
        return R.drawable.nyul;
    }
  }

  public static String convertTimestampToDate(long time, boolean hasTime) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    cal.setTimeInMillis(time);
    return DateFormat.format(hasTime ? "yyyy.MM.dd. HH:mm" : "yyyy.MM.dd.", cal).toString();
  }

  public static String convertTimestampToHour(long time) {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    cal.setTimeInMillis(time);
    String hour = DateFormat.format("HH", cal).toString();
    String min = DateFormat.format("mm", cal).toString();
    return hour + "h " + min + "m";
  }

  public static void init(Application application) {
    app = application;
  }

  private static Application app;

  public static String getSharedPrefence(Context context, final int key) {
    if (context == null) {
      context = app;
    }
    SharedPreferences sharedPref = context.getSharedPreferences(Util.PREFS, Context.MODE_PRIVATE);
    return sharedPref.getString(context.getString(key), "");
  }

  public static final String PREFS = "ishape_prefs";

  public static int getSharedPrefenceId(final Activity activity, final int key) {
    SharedPreferences sharedPref = activity.getSharedPreferences(Util.PREFS, Context.MODE_PRIVATE);
    return sharedPref.getInt(activity.getString(key), -1);
  }

  public static void saveSharedPref(final Activity activity, final int key, final String value) {
    SharedPreferences sharedPref = activity.getSharedPreferences(Util.PREFS, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(activity.getString(key), value);
    editor.commit();
  }

  public static void saveSharedPref(final Activity activity, final int key, final int value) {
    SharedPreferences sharedPref = activity.getSharedPreferences(Util.PREFS, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(activity.getString(key), value);
    editor.commit();
  }

  public static Bitmap takeScreenShot(Activity activity) {
    View view = activity.getWindow().getDecorView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap b1 = view.getDrawingCache();
    Rect frame = new Rect();
    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    int statusBarHeight = frame.top;
    int width = activity.getWindowManager().getDefaultDisplay().getWidth();
    int height = activity.getWindowManager().getDefaultDisplay().getHeight();
    Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
    view.destroyDrawingCache();
    return b;
  }

  public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
    Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
    if (radius < 1) {
      return (null);
    }
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();
    int[] pix = new int[w * h];
    Log.e("pix", w + " " + h + " " + pix.length);
    bitmap.getPixels(pix, 0, w, 0, 0, w, h);
    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;
    int r[] = new int[wh];
    int g[] = new int[wh];
    int b[] = new int[wh];
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
    int vmin[] = new int[Math.max(w, h)];
    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    int dv[] = new int[256 * divsum];
    for (i = 0; i < 256 * divsum; i++) {
      dv[i] = (i / divsum);
    }
    yw = yi = 0;
    int[][] stack = new int[div][3];
    int stackpointer;
    int stackstart;
    int[] sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;
    for (y = 0; y < h; y++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      for (i = -radius; i <= radius; i++) {
        p = pix[yi + Math.min(wm, Math.max(i, 0))];
        sir = stack[i + radius];
        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);
        rbs = r1 - Math.abs(i);
        rsum += sir[0] * rbs;
        gsum += sir[1] * rbs;
        bsum += sir[2] * rbs;
        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }
      }
      stackpointer = radius;
      for (x = 0; x < w; x++) {
        r[yi] = dv[rsum];
        g[yi] = dv[gsum];
        b[yi] = dv[bsum];
        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;
        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];
        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];
        if (y == 0) {
          vmin[x] = Math.min(x + radius + 1, wm);
        }
        p = pix[yw + vmin[x]];
        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);
        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];
        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;
        stackpointer = (stackpointer + 1) % div;
        sir = stack[(stackpointer) % div];
        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];
        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];
        yi++;
      }
      yw += w;
    }
    for (x = 0; x < w; x++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      yp = -radius * w;
      for (i = -radius; i <= radius; i++) {
        yi = Math.max(0, yp) + x;
        sir = stack[i + radius];
        sir[0] = r[yi];
        sir[1] = g[yi];
        sir[2] = b[yi];
        rbs = r1 - Math.abs(i);
        rsum += r[yi] * rbs;
        gsum += g[yi] * rbs;
        bsum += b[yi] * rbs;
        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }
        if (i < hm) {
          yp += w;
        }
      }
      yi = x;
      stackpointer = radius;
      for (y = 0; y < h; y++) {
        // Preserve alpha channel: ( 0xff000000 & pix[yi] )
        pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;
        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];
        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];
        if (x == 0) {
          vmin[y] = Math.min(y + r1, hm) * w;
        }
        p = x + vmin[y];
        sir[0] = r[p];
        sir[1] = g[p];
        sir[2] = b[p];
        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];
        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;
        stackpointer = (stackpointer + 1) % div;
        sir = stack[stackpointer];
        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];
        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];
        yi += w;
      }
    }
    Log.e("pix", w + " " + h + " " + pix.length);
    bitmap.setPixels(pix, 0, w, 0, 0, w, h);
    return (bitmap);
  }
}
