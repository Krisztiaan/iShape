package hu.artklikk.android.deloitte;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Krisztian Ferencz on 2016. 08. 11..
 * Artklikk Kft.
 * ferencz.krisztian@artklikk.hu
 * krisz011@gmail.com
 */
public class Bus {
  private static final EventBus bus = new EventBus();

  public static void post(Object event) {
    bus.post(event);
  }

  public static void register(Object subscriber) {
    if (!bus.isRegistered(subscriber)) bus.register(subscriber);
  }

  public static void unregister(Object subscriber) {
    if (bus.isRegistered(subscriber)) bus.unregister(subscriber);
  }
}
