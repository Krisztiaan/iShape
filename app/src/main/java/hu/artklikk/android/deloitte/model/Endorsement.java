package hu.artklikk.android.deloitte.model;

import hu.artklikk.android.deloitte.util.Util;
import java.io.Serializable;

/**
 * Created by gyozofule on 15. 11. 08..
 */
public class Endorsement implements Serializable{

    public enum Category {
        Charity(0,"Charity"),
        KnowledgeShare(1,"KnowledgeShare"),
        Innovator(2,"Innovator"),
        TeamBuilder(3,"TeamBuilder"),
        BrandBuilder(4,"BrandBuilder"),
        SelfDeployment(5,"SelfDeployment"),
        Special(6,"Special");

        private String code;
        private int id;

        Category (int id, String code) {
            this.code = code;
            this.id = id;
        }

        public int getId(){
            return this.id;
        }

        public String getCode() { return this.code;}

        public static Category getCategoryOfCode(String code){
            for (Category category : Category.values()) {
                if (category.code.equals(code)) {
                    return category;
                }
            }
            return null;
        }

        public static Category getCategoryOfId(int id){
            for (Category category : Category.values()) {
                if (category.id == id) {
                    return category;
                }
            }
            return null;
        }
    }

    private long id;
    private long consigneeId;
    private long consignorId;
    private long time;
    private byte point;
    private String category;
    private String description;
    private boolean acceptedFlag;

    public Endorsement(){

    }

    public Endorsement(long id, long consignorId, long consineeId, long time, byte point, String category, String description, boolean acceptedFlag) {
        this.id = id;
        this.consigneeId = consineeId;
        this.time = time;
        this.point = point;
        this.category = category;
        this.description = description;
        this.acceptedFlag = acceptedFlag;
        this.consignorId = consignorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getConsigneeId() {
        return consigneeId;
    }

    public void setConsigneeId(long consigneeId) {
        this.consigneeId = consigneeId;
    }

    public long getTime() {
        return Util.timeTickConverter(time);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte getPoint() {
        return point;
    }

    public void setPoint(byte point) {
        this.point = point;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAcceptedFlag() {
        return acceptedFlag;
    }

    public void setAcceptedFlag(boolean acceptedFlag) {
        this.acceptedFlag = acceptedFlag;
    }

    public long getConsignorId() {
        return consignorId;
    }

    public void setConsignorId(long consignorId) {
        this.consignorId = consignorId;
    }
}
