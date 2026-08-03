package org.example;

public class MetaEntityResult {
    private String metaEntityId;

    private String metaEntityAndDetailViewSQL;
    private String processAllocationSQL;

    public String getMetaEntityId() {
        return metaEntityId;
    }

    public void setMetaEntityId(String metaEntityId) {
        this.metaEntityId = metaEntityId;
    }

    public String getMetaEntityAndDetailViewSQL() {
        return metaEntityAndDetailViewSQL;
    }

    public void setMetaEntityAndDetailViewSQL(String metaEntityAndDetailViewSQL) {
        this.metaEntityAndDetailViewSQL = metaEntityAndDetailViewSQL;
    }

    public String getProcessAllocationSQL() {
        return processAllocationSQL;
    }

    public void setProcessAllocationSQL(String processAllocationSQL) {
        this.processAllocationSQL = processAllocationSQL;
    }
}
