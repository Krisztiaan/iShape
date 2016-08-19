package hu.artklikk.android.deloitte.model.communication;

import java.util.ArrayList;
import java.util.List;

import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.model.User;

/**
 * Created by gyozofule on 15. 11. 24..
 */
public class LoginResponse {
    private int selfuserId;
    private ArrayList<User> users;
    private List<Endorsement> nominations;

    public LoginResponse(){}

    public int getSelfuserId() {
        return selfuserId;
    }

    public void setSelfuserId(int selfuserId) {
        this.selfuserId = selfuserId;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public List<Endorsement> getNominations() {
        return nominations;
    }

    public void setNominations(List<Endorsement> endorsements) {
        this.nominations = endorsements;
    }
}
