package hu.artklikk.android.deloitte.model.communication;

import java.io.Serializable;
import java.util.List;

import hu.artklikk.android.deloitte.model.Animal;

/**
 * Created by gyozofule on 15. 11. 24..
 */
public class ProfileResponse implements Serializable{

    private List<Animal> animalList;
    private String profileImage;

    private int pointInYear;

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

    public int getCycleRank() {
        return cycleRank;
    }

    public void setCycleRank(int cycleRank) {
        this.cycleRank = cycleRank;
    }

    public int getYearRank() {
        return yearRank;
    }

    public void setYearRank(int yearRank) {
        this.yearRank = yearRank;
    }

    private int pointInCycle;
    private int cycleRank;
    private int yearRank;

    public ProfileResponse() {
    }


    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
