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

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * PeriodInstanceImpl
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-12T17:12:54.525+01:00")
public class PeriodInstanceImpl {
  @SerializedName("end")
  private Long end = null;

  @SerializedName("index")
  private Integer index = null;

  @SerializedName("score")
  private Double score = null;

  @SerializedName("start")
  private Long start = null;

  public PeriodInstanceImpl end(Long end) {
    this.end = end;
    return this;
  }

   /**
   * Get end
   * @return end
  **/
  @ApiModelProperty(value = "")
  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
    this.end = end;
  }

  public PeriodInstanceImpl index(Integer index) {
    this.index = index;
    return this;
  }

   /**
   * Get index
   * @return index
  **/
  @ApiModelProperty(value = "")
  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public PeriodInstanceImpl score(Double score) {
    this.score = score;
    return this;
  }

   /**
   * Get score
   * @return score
  **/
  @ApiModelProperty(value = "")
  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public PeriodInstanceImpl start(Long start) {
    this.start = start;
    return this;
  }

   /**
   * Get start
   * @return start
  **/
  @ApiModelProperty(value = "")
  public Long getStart() {
    return start;
  }

  public void setStart(Long start) {
    this.start = start;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PeriodInstanceImpl periodInstanceImpl = (PeriodInstanceImpl) o;
    return Objects.equals(this.end, periodInstanceImpl.end) &&
        Objects.equals(this.index, periodInstanceImpl.index) &&
        Objects.equals(this.score, periodInstanceImpl.score) &&
        Objects.equals(this.start, periodInstanceImpl.start);
  }

  @Override
  public int hashCode() {
    return Objects.hash(end, index, score, start);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PeriodInstanceImpl {\n");
    
    sb.append("    end: ").append(toIndentedString(end)).append("\n");
    sb.append("    index: ").append(toIndentedString(index)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    start: ").append(toIndentedString(start)).append("\n");
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

