package org.example.config;

import java.util.List;

public class EnhancementConfiguration {
    private String metaEntityId;
    private List<ViewConfiguration> views;

    public String getMetaEntityId() {
        return metaEntityId;
    }

    public void setMetaEntityId(String metaEntityId) {
        this.metaEntityId = metaEntityId;
    }

    public List<ViewConfiguration> getViews() {
        return views;
    }

    public void setViews(List<ViewConfiguration> views) {
        this.views = views;
    }
}
