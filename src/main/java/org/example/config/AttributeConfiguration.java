package org.example.config;

public class AttributeConfiguration {

    private String systemName;
    private String attributeName;
    private String attributeType;
    private String tableColumn;
    private boolean isMandatory;

    private String pickListId = null;

    public String getSystemName() {
        return systemName;
    }

    public String getPickListId() {
        return pickListId;
    }

    public void setPickListId(String pickListId) {
        this.pickListId = pickListId;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }
}