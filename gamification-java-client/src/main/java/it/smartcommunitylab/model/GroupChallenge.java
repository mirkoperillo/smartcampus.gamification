/*
 * Gamification Engine API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package it.smartcommunitylab.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.threeten.bp.OffsetDateTime;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.swagger.annotations.ApiModelProperty;

/**
 * GroupChallenge
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-12T17:12:54.525+01:00")
public class GroupChallenge {
  @SerializedName("attendees")
  private List<Attendee> attendees = null;

  @SerializedName("challengeModel")
  private String challengeModel = null;

  @SerializedName("challengePointConcept")
  private PointConceptRef challengePointConcept = null;

  @SerializedName("challengeTarget")
  private Double challengeTarget = null;

  @SerializedName("end")
  private OffsetDateTime end = null;

  @SerializedName("gameId")
  private String gameId = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("instanceName")
  private String instanceName = null;

  @SerializedName("origin")
  private String origin = null;

  @SerializedName("priority")
  private Integer priority = null;

  @SerializedName("reward")
  private Reward reward = null;

  @SerializedName("start")
  private OffsetDateTime start = null;

  /**
   * Gets or Sets state
   */
  @JsonAdapter(StateEnum.Adapter.class)
  public enum StateEnum {
    PROPOSED("PROPOSED"),
    
    ASSIGNED("ASSIGNED"),
    
    ACTIVE("ACTIVE"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED"),
    
    REFUSED("REFUSED"),
    
    AUTO_DISCARDED("AUTO_DISCARDED");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StateEnum fromValue(String text) {
      for (StateEnum b : StateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<StateEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StateEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StateEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return StateEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("state")
  private StateEnum state = null;

  @SerializedName("stateDate")
  private Map<String, OffsetDateTime> stateDate = null;

  public GroupChallenge attendees(List<Attendee> attendees) {
    this.attendees = attendees;
    return this;
  }

  public GroupChallenge addAttendeesItem(Attendee attendeesItem) {
    if (this.attendees == null) {
      this.attendees = new ArrayList<Attendee>();
    }
    this.attendees.add(attendeesItem);
    return this;
  }

   /**
   * Get attendees
   * @return attendees
  **/
  @ApiModelProperty(value = "")
  public List<Attendee> getAttendees() {
    return attendees;
  }

  public void setAttendees(List<Attendee> attendees) {
    this.attendees = attendees;
  }

  public GroupChallenge challengeModel(String challengeModel) {
    this.challengeModel = challengeModel;
    return this;
  }

   /**
   * Get challengeModel
   * @return challengeModel
  **/
  @ApiModelProperty(value = "")
  public String getChallengeModel() {
    return challengeModel;
  }

  public void setChallengeModel(String challengeModel) {
    this.challengeModel = challengeModel;
  }

  public GroupChallenge challengePointConcept(PointConceptRef challengePointConcept) {
    this.challengePointConcept = challengePointConcept;
    return this;
  }

   /**
   * Get challengePointConcept
   * @return challengePointConcept
  **/
  @ApiModelProperty(value = "")
  public PointConceptRef getChallengePointConcept() {
    return challengePointConcept;
  }

  public void setChallengePointConcept(PointConceptRef challengePointConcept) {
    this.challengePointConcept = challengePointConcept;
  }

  public GroupChallenge challengeTarget(Double challengeTarget) {
    this.challengeTarget = challengeTarget;
    return this;
  }

   /**
   * Get challengeTarget
   * @return challengeTarget
  **/
  @ApiModelProperty(value = "")
  public Double getChallengeTarget() {
    return challengeTarget;
  }

  public void setChallengeTarget(Double challengeTarget) {
    this.challengeTarget = challengeTarget;
  }

  public GroupChallenge end(OffsetDateTime end) {
    this.end = end;
    return this;
  }

   /**
   * Get end
   * @return end
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getEnd() {
    return end;
  }

  public void setEnd(OffsetDateTime end) {
    this.end = end;
  }

  public GroupChallenge gameId(String gameId) {
    this.gameId = gameId;
    return this;
  }

   /**
   * Get gameId
   * @return gameId
  **/
  @ApiModelProperty(value = "")
  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public GroupChallenge id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GroupChallenge instanceName(String instanceName) {
    this.instanceName = instanceName;
    return this;
  }

   /**
   * Get instanceName
   * @return instanceName
  **/
  @ApiModelProperty(value = "")
  public String getInstanceName() {
    return instanceName;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }

  public GroupChallenge origin(String origin) {
    this.origin = origin;
    return this;
  }

   /**
   * Get origin
   * @return origin
  **/
  @ApiModelProperty(value = "")
  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public GroupChallenge priority(Integer priority) {
    this.priority = priority;
    return this;
  }

   /**
   * Get priority
   * @return priority
  **/
  @ApiModelProperty(value = "")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public GroupChallenge reward(Reward reward) {
    this.reward = reward;
    return this;
  }

   /**
   * Get reward
   * @return reward
  **/
  @ApiModelProperty(value = "")
  public Reward getReward() {
    return reward;
  }

  public void setReward(Reward reward) {
    this.reward = reward;
  }

  public GroupChallenge start(OffsetDateTime start) {
    this.start = start;
    return this;
  }

   /**
   * Get start
   * @return start
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getStart() {
    return start;
  }

  public void setStart(OffsetDateTime start) {
    this.start = start;
  }

  public GroupChallenge state(StateEnum state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public GroupChallenge stateDate(Map<String, OffsetDateTime> stateDate) {
    this.stateDate = stateDate;
    return this;
  }

  public GroupChallenge putStateDateItem(String key, OffsetDateTime stateDateItem) {
    if (this.stateDate == null) {
      this.stateDate = new HashMap<String, OffsetDateTime>();
    }
    this.stateDate.put(key, stateDateItem);
    return this;
  }

   /**
   * Get stateDate
   * @return stateDate
  **/
  @ApiModelProperty(value = "")
  public Map<String, OffsetDateTime> getStateDate() {
    return stateDate;
  }

  public void setStateDate(Map<String, OffsetDateTime> stateDate) {
    this.stateDate = stateDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupChallenge groupChallenge = (GroupChallenge) o;
    return Objects.equals(this.attendees, groupChallenge.attendees) &&
        Objects.equals(this.challengeModel, groupChallenge.challengeModel) &&
        Objects.equals(this.challengePointConcept, groupChallenge.challengePointConcept) &&
        Objects.equals(this.challengeTarget, groupChallenge.challengeTarget) &&
        Objects.equals(this.end, groupChallenge.end) &&
        Objects.equals(this.gameId, groupChallenge.gameId) &&
        Objects.equals(this.id, groupChallenge.id) &&
        Objects.equals(this.instanceName, groupChallenge.instanceName) &&
        Objects.equals(this.origin, groupChallenge.origin) &&
        Objects.equals(this.priority, groupChallenge.priority) &&
        Objects.equals(this.reward, groupChallenge.reward) &&
        Objects.equals(this.start, groupChallenge.start) &&
        Objects.equals(this.state, groupChallenge.state) &&
        Objects.equals(this.stateDate, groupChallenge.stateDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attendees, challengeModel, challengePointConcept, challengeTarget, end, gameId, id, instanceName, origin, priority, reward, start, state, stateDate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupChallenge {\n");
    
    sb.append("    attendees: ").append(toIndentedString(attendees)).append("\n");
    sb.append("    challengeModel: ").append(toIndentedString(challengeModel)).append("\n");
    sb.append("    challengePointConcept: ").append(toIndentedString(challengePointConcept)).append("\n");
    sb.append("    challengeTarget: ").append(toIndentedString(challengeTarget)).append("\n");
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    gameId: ").append(toIndentedString(gameId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    instanceName: ").append(toIndentedString(instanceName)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    reward: ").append(toIndentedString(reward)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    stateDate: ").append(toIndentedString(stateDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

