package org.example.config;

import java.util.ArrayList;
import java.util.List;

public class DocumentConfiguration {

    private String documentName;
    private String documentType;
    private String valueTreeNodeId;
    private MappingConfiguration mapping;
    private List<ViewConfiguration> views = new ArrayList<>();

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public MappingConfiguration getMapping() {
        return mapping;
    }

    public void setMapping(MappingConfiguration mapping) {
        this.mapping = mapping;
    }

    public String getValueTreeNodeId() {
        return valueTreeNodeId;
    }

    public void setValueTreeNodeId(String valueTreeNodeId) {
        this.valueTreeNodeId = valueTreeNodeId;
    }

    public List<ViewConfiguration> getViews() {
        return views;
    }

    public void setViews(List<ViewConfiguration> views) {
        this.views = views;
    }
}
