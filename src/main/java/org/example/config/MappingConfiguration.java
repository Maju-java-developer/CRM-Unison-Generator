package org.example.config;

public class MappingConfiguration {

    private Integer sbpProductId;
    private Integer complaintType;
    private Integer categoryId;
    private Integer turnAroundTime;
    private String adcCode;
    private String escalationStrategyId;

    public Integer getSbpProductId() {
        return sbpProductId;
    }

    public void setSbpProductId(Integer sbpProductId) {
        this.sbpProductId = sbpProductId;
    }

    public Integer getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(Integer complaintType) {
        this.complaintType = complaintType;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(Integer turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public String getAdcCode() {
        return adcCode;
    }

    public void setAdcCode(String adcCode) {
        this.adcCode = adcCode;
    }

    public String getEscalationStrategyId() {
        return escalationStrategyId;
    }

    public void setEscalationStrategyId(String escalationStrategyId) {
        this.escalationStrategyId = escalationStrategyId;
    }

}