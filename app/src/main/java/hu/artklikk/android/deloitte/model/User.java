package hu.artklikk.android.deloitte.model;

import java.io.Serializable;

import hu.artklikk.android.deloitte.map.model.MapObjectModel;

/**
 * Created by gyozofule on 15. 11. 01..
 */
public class User implements Serializable{
    private int id;
    private String name;
    private String job;
    private int[] poi;
    private String profileImage;
    private byte usedYears;
    private boolean isMenthor;
    private int pointInYear;
    private int pointInCycle;
    private int starCategroy;

    private int cycleRank;
    private int yearRank;

    public int getYearRank() {
        return yearRank;
    }

    public void setYearRank(int yearRank) {
        this.yearRank = yearRank;
    }

    public void setMenthor(boolean isMenthor) {
        this.isMenthor = isMenthor;
    }

    public int getCycleRank() {
        return cycleRank;
    }

    public void setCycleRank(int cycleRank) {
        this.cycleRank = cycleRank;
    }

    private MapObjectModel.ROLE role;

    public User(){

    }

    public User(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.job = user.getJob();
        this.poi = user.getPoi();
        this.profileImage = user.getProfileImage();
        this.usedYears = user.getUsedYears();
        this.isMenthor = user.isMenthor();
        this.pointInYear = user.pointInYear;
        this.pointInCycle = user.pointInCycle;
        this.starCategroy = user.starCategroy;
        this.cycleRank = user.cycleRank;
        this.yearRank = user.yearRank;

        if (isMenthor) {
            this.role = MapObjectModel.ROLE.MENTOR;
        } else {
            this.role = MapObjectModel.ROLE.OTHER;
        }
    }


    public User(int id, String name, String job, int[] poi, byte usedYears,
                boolean isMenthor, int pointInYear, int pointInCycle, int starCategroy, String profileImage, int cycleRank, int yearRank) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.poi = poi;
        this.profileImage = profileImage;
        this.usedYears = usedYears;
        this.isMenthor = isMenthor;
        this.pointInYear = pointInYear;
        this.pointInCycle = pointInCycle;
        this.starCategroy = starCategroy;
        this.cycleRank = cycleRank;
        this.yearRank = yearRank;

        if (isMenthor) {
            this.role = MapObjectModel.ROLE.MENTOR;
        } else {
            this.role = MapObjectModel.ROLE.OTHER;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int[] getPoi() {
        return poi;
    }

    public void setPoi(int[] poi) {
        this.poi = poi;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public byte getUsedYears() {
        return usedYears;
    }

    public void setUsedYears(byte usedYears) {
        this.usedYears = usedYears;
    }

    public boolean isMenthor() {
        return isMenthor;
    }

    public void setIsMenthor(boolean isMenthor) {
        this.isMenthor = isMenthor;
    }

    public int getPointInYear() {
        return pointInYear;
    }

    public void setPointInYear(int pointInYear) {
        this.pointInYear = pointInYear;
    }

    public int getPointInCycle() {
        return pointInCycle;
    }

    public void setPointInCycle(int pointInCycle) {
        this.pointInCycle = pointInCycle;
    }

    public int getStarCategroy() {
        return this.starCategroy;
    }

    public void setStarCategroy(int starCategroy) {
        this.starCategroy = starCategroy - 1;
    }

    public MapObjectModel.ROLE getRole() {
        return role;
    }

    public void setRole(MapObjectModel.ROLE role) {
        this.role = role;
    }



    @Override
    public String toString(){
        return name;
    }

    //public static String BASEIMAGE = "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";
}
