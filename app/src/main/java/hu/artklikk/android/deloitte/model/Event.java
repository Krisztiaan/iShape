package hu.artklikk.android.deloitte.model;

import hu.artklikk.android.deloitte.util.Util;

/**
 * Created by gyozofule on 15. 12. 03..
 */
public class Event {
    private int eventId;
    private String title;
    private String description;
    private long startTime;
    private long endTime;

    public Event() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = Util.convertDateTimeTicks(startTime);
    }

    public long getEndTime() {
        return Util.timeTickConverter(endTime);
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
