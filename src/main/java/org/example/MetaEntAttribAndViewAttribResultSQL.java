package org.example;

public class MetaEntAttribAndViewAttribResultSQL {
    private String attributeSQl;
    private String revertAttributeSQl;

    public MetaEntAttribAndViewAttribResultSQL() {
    }

    public MetaEntAttribAndViewAttribResultSQL(String attributeSQl, String revertAttributeSQl) {
        this.attributeSQl = attributeSQl;
        this.revertAttributeSQl = revertAttributeSQl;
    }

    public String getAttributeSQl() {
        return attributeSQl;
    }

    public String getRevertAttributeSQl() {
        return revertAttributeSQl;
    }
}
