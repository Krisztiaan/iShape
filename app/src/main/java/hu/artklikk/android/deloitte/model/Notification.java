package hu.artklikk.android.deloitte.model;

import hu.artklikk.android.deloitte.util.Util;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "type",
    "fromUserID",
    "toUserID",
    "category",
    "description",
    "animal",
    "time",
    "likeID",
    "likeQuantity",
    "commentQuantity"
})
public class Notification {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("type")
  private String type;
  @JsonProperty("fromUserID")
  private Integer fromUserID;
  @JsonProperty("toUserID")
  private Integer toUserID;
  @JsonProperty("category")
  private String category;
  @JsonProperty("description")
  private String description;
  @JsonProperty("animal")
  private Animal animal;
  @JsonProperty("time")
  private Long time;
  @JsonProperty("likeID")
  private Integer likeID;
  @JsonProperty("likeQuantity")
  private Integer likeQuantity;
  @JsonProperty("commentQuantity")
  private Integer commentQuantity;

  /**
   *
   * @return
   * The id
   */
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  /**
   *
   * @param id
   * The id
   */
  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   *
   * @return
   * The type
   */
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  /**
   *
   * @param type
   * The type
   */
  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  /**
   *
   * @return
   * The fromUserID
   */
  @JsonProperty("fromUserID")
  public Integer getFromUserID() {
    return fromUserID;
  }

  /**
   *
   * @param fromUserID
   * The fromUserID
   */
  @JsonProperty("fromUserID")
  public void setFromUserID(Integer fromUserID) {
    this.fromUserID = fromUserID;
  }

  /**
   *
   * @return
   * The toUserID
   */
  @JsonProperty("toUserID")
  public Integer getToUserID() {
    return toUserID;
  }

  /**
   *
   * @param toUserID
   * The toUserID
   */
  @JsonProperty("toUserID")
  public void setToUserID(Integer toUserID) {
    this.toUserID = toUserID;
  }

  /**
   *
   * @return
   * The category
   */
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }

  /**
   *
   * @param category
   * The category
   */
  @JsonProperty("category")
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   *
   * @return
   * The description
   */
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  /**
   *
   * @param description
   * The description
   */
  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   *
   * @return
   * The animal
   */
  @JsonProperty("animal")
  public Animal getAnimal() {
    return animal;
  }

  /**
   *
   * @param animal
   * The animal
   */
  @JsonProperty("animal")
  public void setAnimal(Animal animal) {
    this.animal = animal;
  }

  /**
   *
   * @return
   * The time
   */
  @JsonProperty("time")
  public Long getTime() {
    return Util.timeTickConverter(time);
  }

  /**
   *
   * @param time
   * The time
   */
  @JsonProperty("time")
  public void setTime(Long time) {
    this.time = time;
  }

  /**
   *
   * @return
   * The likeID
   */
  @JsonProperty("likeID")
  public Integer getLikeID() {
    return likeID;
  }

  /**
   *
   * @param likeID
   * The likeID
   */
  @JsonProperty("likeID")
  public void setLikeID(Integer likeID) {
    this.likeID = likeID;
  }

  /**
   *
   * @return
   * The likeQuantity
   */
  @JsonProperty("likeQuantity")
  public Integer getLikeQuantity() {
    return likeQuantity;
  }

  /**
   *
   * @param likeQuantity
   * The likeQuantity
   */
  @JsonProperty("likeQuantity")
  public void setLikeQuantity(Integer likeQuantity) {
    this.likeQuantity = likeQuantity;
  }

  /**
   *
   * @return
   * The commentQuantity
   */
  @JsonProperty("commentQuantity")
  public Integer getCommentQuantity() {
    return commentQuantity;
  }

  /**
   *
   * @param commentQuantity
   * The commentQuantity
   */
  @JsonProperty("commentQuantity")
  public void setCommentQuantity(Integer commentQuantity) {
    this.commentQuantity = commentQuantity;
  }

}