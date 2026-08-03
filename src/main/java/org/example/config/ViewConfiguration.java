package org.example.config;

import java.util.List;

public class ViewConfiguration {

    private String viewId;
    private String viewName;

    private List<AttributeConfiguration> attributes;

    public String getViewName() {
        return viewName;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public List<AttributeConfiguration> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeConfiguration> attributes) {
        this.attributes = attributes;
    }
}