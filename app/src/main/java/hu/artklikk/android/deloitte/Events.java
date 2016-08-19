package hu.artklikk.android.deloitte;

import hu.artklikk.android.deloitte.model.Notification;

/**
 * Created by Krisztian Ferencz on 2016. 08. 11..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class Events {
  public static OpenProfile openProfile(int uid) {
    return new OpenProfile(uid);
  }

  public static OpenComment openComment(String fromWallType, Notification notification) {
    return new OpenComment(fromWallType, notification);
  }

  public static OpenComment closeComment(String fromWallType) {
    return new OpenComment(fromWallType, null, false);
  }

  public static ConnectionError connectionError(Throwable t) {
    return new ConnectionError(t);
  }

  public static LoginResult loginResult(boolean isSuccess) {
    return new LoginResult(isSuccess);
  }

  public static class OpenComment {
    public final String fromWallType;
    public final Notification notification;
    public boolean isOpen = true;

    public OpenComment(String fromWallType, Notification notification) {
      this.fromWallType = fromWallType;
      this.notification = notification;
    }

    public OpenComment(String fromWallType, Notification notification, boolean isOpen) {
      this.fromWallType = fromWallType;
      this.notification = notification;
      this.isOpen = isOpen;
    }
  }

  public static class OpenProfile {
    public final int userId;

    public OpenProfile(int uid) {
      userId = uid;
    }
  }

  public static class ConnectionError {
    public final Throwable t;

    public ConnectionError(Throwable t) {
      this.t = t;
    }
  }

  public static class LoginResult {
    public final boolean isSuccess;

    public LoginResult(boolean isSuccess) {
      this.isSuccess = isSuccess;
    }
  }
}
