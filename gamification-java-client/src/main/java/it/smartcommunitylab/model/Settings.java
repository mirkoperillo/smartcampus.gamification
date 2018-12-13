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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * Settings
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-12T17:12:54.525+01:00")
public class Settings {
  @SerializedName("statisticsConfig")
  private Map<String, String> statisticsConfig = null;

  public Settings statisticsConfig(Map<String, String> statisticsConfig) {
    this.statisticsConfig = statisticsConfig;
    return this;
  }

  public Settings putStatisticsConfigItem(String key, String statisticsConfigItem) {
    if (this.statisticsConfig == null) {
      this.statisticsConfig = new HashMap<String, String>();
    }
    this.statisticsConfig.put(key, statisticsConfigItem);
    return this;
  }

   /**
   * Get statisticsConfig
   * @return statisticsConfig
  **/
  @ApiModelProperty(value = "")
  public Map<String, String> getStatisticsConfig() {
    return statisticsConfig;
  }

  public void setStatisticsConfig(Map<String, String> statisticsConfig) {
    this.statisticsConfig = statisticsConfig;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Settings settings = (Settings) o;
    return Objects.equals(this.statisticsConfig, settings.statisticsConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statisticsConfig);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Settings {\n");
    
    sb.append("    statisticsConfig: ").append(toIndentedString(statisticsConfig)).append("\n");
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
