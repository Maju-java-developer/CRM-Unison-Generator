package org.example.config;

import java.util.List;

public class PickListConfig {

    private String pickListId;
    private String groupId;
    private List<String> values;

    public PickListConfig(String pickListId, String groupId, List<String> values) {
        this.pickListId = pickListId;
        this.groupId = groupId;
        this.values = values;
    }

    // getters & setters

    public String getPickListId() {
        return pickListId;
    }

    public void setPickListId(String pickListId) {
        this.pickListId = pickListId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
