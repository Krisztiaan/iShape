package hu.artklikk.android.deloitte.model.communication;

import java.io.Serializable;

/**
 * Created by gyozofule on 15. 11. 24..
 */
public class MentorResponse implements Serializable{

    private String description;
    private String profileImage;

    public MentorResponse() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
