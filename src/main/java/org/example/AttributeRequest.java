package org.example;

public class AttributeRequest {

    private String metaEntityId;

    private String metaViewId;

    private String systemName;

    private String attributeName;

    private String attributeType;

    private String tableColumn;

    private String pickList;

    private int displayOrder;

    private Boolean isMandatory;

    public AttributeRequest(String metaEntityId, String metaViewId, String systemName, String attributeName, String attributeType, String tableColumn, String pickList, int displayOrder, boolean isMandatory) {
        this.metaEntityId = metaEntityId;
        this.metaViewId = metaViewId;
        this.systemName = systemName;
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.tableColumn = tableColumn;
        this.pickList = pickList;
        if (attributeType.equalsIgnoreCase("String")) {
            this.pickList = null;
        }
        this.displayOrder = displayOrder;
        this.isMandatory = isMandatory;
    }
    public AttributeRequest(String metaEntityId, String metaViewId, String systemName, String attributeName, String attributeType, String tableColumn, int displayOrder, boolean isMandatory) {
        this.metaEntityId = metaEntityId;
        this.metaViewId = metaViewId;
        this.systemName = systemName;
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.tableColumn = tableColumn;
        if (attributeType.equalsIgnoreCase("String")) {
            this.pickList = null;
        }
        this.displayOrder = displayOrder;
        this.isMandatory = isMandatory;
    }

    public String getMetaEntityId() {
        return metaEntityId;
    }

    public void setMetaEntityId(String metaEntityId) {
        this.metaEntityId = metaEntityId;
    }

    public String getMetaViewId() {
        return metaViewId;
    }

    public void setMetaViewId(String metaViewId) {
        this.metaViewId = metaViewId;
    }

    public String getSystemName() {
        return systemName;
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

    public String getPickList() {
        return pickList;
    }

    public void setPickList(String pickList) {
        this.pickList = pickList;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getMandatory() {
        return isMandatory;
    }

    public void setMandatory(Boolean mandatory) {
        isMandatory = mandatory;
    }
}