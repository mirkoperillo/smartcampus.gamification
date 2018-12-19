/**
 * Copyright 2018-2019 SmartCommunity Lab(FBK-ICT).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

import java.util.Objects;

import org.threeten.bp.OffsetDateTime;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * ArchivedConcept
 */

public class ArchivedConcept {
  @SerializedName("archivingDate")
  private OffsetDateTime archivingDate = null;

  @SerializedName("challenge")
  private ChallengeConcept challenge = null;

  @SerializedName("gameId")
  private String gameId = null;

  @SerializedName("groupChallenge")
  private GroupChallenge groupChallenge = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("playerId")
  private String playerId = null;

  public ArchivedConcept archivingDate(OffsetDateTime archivingDate) {
    this.archivingDate = archivingDate;
    return this;
  }

   /**
   * Get archivingDate
   * @return archivingDate
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getArchivingDate() {
    return archivingDate;
  }

  public void setArchivingDate(OffsetDateTime archivingDate) {
    this.archivingDate = archivingDate;
  }

  public ArchivedConcept challenge(ChallengeConcept challenge) {
    this.challenge = challenge;
    return this;
  }

   /**
   * Get challenge
   * @return challenge
  **/
  @ApiModelProperty(value = "")
  public ChallengeConcept getChallenge() {
    return challenge;
  }

  public void setChallenge(ChallengeConcept challenge) {
    this.challenge = challenge;
  }

  public ArchivedConcept gameId(String gameId) {
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

  public ArchivedConcept groupChallenge(GroupChallenge groupChallenge) {
    this.groupChallenge = groupChallenge;
    return this;
  }

   /**
   * Get groupChallenge
   * @return groupChallenge
  **/
  @ApiModelProperty(value = "")
  public GroupChallenge getGroupChallenge() {
    return groupChallenge;
  }

  public void setGroupChallenge(GroupChallenge groupChallenge) {
    this.groupChallenge = groupChallenge;
  }

  public ArchivedConcept id(String id) {
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

  public ArchivedConcept playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

   /**
   * Get playerId
   * @return playerId
  **/
  @ApiModelProperty(value = "")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArchivedConcept archivedConcept = (ArchivedConcept) o;
    return Objects.equals(this.archivingDate, archivedConcept.archivingDate) &&
        Objects.equals(this.challenge, archivedConcept.challenge) &&
        Objects.equals(this.gameId, archivedConcept.gameId) &&
        Objects.equals(this.groupChallenge, archivedConcept.groupChallenge) &&
        Objects.equals(this.id, archivedConcept.id) &&
        Objects.equals(this.playerId, archivedConcept.playerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(archivingDate, challenge, gameId, groupChallenge, id, playerId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArchivedConcept {\n");
    
    sb.append("    archivingDate: ").append(toIndentedString(archivingDate)).append("\n");
    sb.append("    challenge: ").append(toIndentedString(challenge)).append("\n");
    sb.append("    gameId: ").append(toIndentedString(gameId)).append("\n");
    sb.append("    groupChallenge: ").append(toIndentedString(groupChallenge)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
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

