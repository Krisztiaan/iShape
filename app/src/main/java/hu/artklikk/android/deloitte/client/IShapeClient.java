package hu.artklikk.android.deloitte.client;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.util.Util;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Krisztian Ferencz on 2016.08.09..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class IShapeClient {
  private static final String SERVER_IP = "78.131.88.250";
  public static final String BASE_URL = "http://" + SERVER_IP + ":8090/Api/";

  public static final IShapeApi c;

  private static Cookie createNonPersistentCookie() {
    return new Cookie.Builder()
        .domain(SERVER_IP)
        .path("/")
        .name("ASP.NET_SessionId")
        .value(Util.getSharedPrefence(null, R.string.preference_key_token))
        .httpOnly()
        .build();
  }

  static {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient.Builder()
        .cookieJar(new CookieJar() {
          @Override
          public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
          }

          @Override
          public List<Cookie> loadForRequest(HttpUrl url) {
            final ArrayList<Cookie> oneCookie = new ArrayList<>(1);
            oneCookie.add(createNonPersistentCookie());
            return oneCookie;
          }
        })
        .addInterceptor(interceptor)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    c = retrofit.create(IShapeApi.class);
  }
}
