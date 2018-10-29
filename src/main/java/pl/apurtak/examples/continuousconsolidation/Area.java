package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Area {

  int areaId;
  String areaName;
  int parentId;
  String hierarchyLevel;

  @JsonCreator
  public Area(@JsonProperty("areaId") int areaId,
      @JsonProperty("areaName") String areaName, @JsonProperty("parentId") int parentId,
      @JsonProperty("hierarchyLevel") String hierarchyLevel) {
    this.areaId = areaId;
    this.areaName = areaName;
    this.parentId = parentId;
    this.hierarchyLevel = hierarchyLevel;
  }

  public Area(AreaAgentAssignment areaAgentAssignment) {
    this(areaAgentAssignment.getAreaId(), areaAgentAssignment.getAreaName(),
        areaAgentAssignment.getParentId(), areaAgentAssignment.getHierarchyLevel());
  }
}
