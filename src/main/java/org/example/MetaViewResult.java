package org.example;

public class MetaViewResult {

    private final String metaViewId;

    private final String sql;

    public MetaViewResult(
            String metaViewId,
            String sql
    ) {

        this.metaViewId = metaViewId;

        this.sql = sql;
    }

    public String getMetaViewId() {

        return metaViewId;
    }

    public String getSql() {

        return sql;
    }
}