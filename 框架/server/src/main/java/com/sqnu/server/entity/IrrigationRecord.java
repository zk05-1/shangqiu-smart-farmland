package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_irrigation_record")
public class IrrigationRecord extends BaseEntity {

    private Long farmlandId;
    private Long plantingRecordId;
    private LocalDateTime irrigateDate;
    private BigDecimal waterAmount;
    private String irrigateMethod;
    private Integer irrigateDuration;
    private String operator;
    private String description;
    private Farmland farmland;

    @Column(name = "farmland_id")
    public Long getFarmlandId() {
        return farmlandId;
    }

    public void setFarmlandId(Long farmlandId) {
        this.farmlandId = farmlandId;
    }

    @Column(name = "planting_record_id")
    public Long getPlantingRecordId() {
        return plantingRecordId;
    }

    public void setPlantingRecordId(Long plantingRecordId) {
        this.plantingRecordId = plantingRecordId;
    }

    @Column(name = "irrigate_date")
    public LocalDateTime getIrrigateDate() {
        return irrigateDate;
    }

    public void setIrrigateDate(LocalDateTime irrigateDate) {
        this.irrigateDate = irrigateDate;
    }

    @Column(name = "water_amount", precision = 10, scale = 2)
    public BigDecimal getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(BigDecimal waterAmount) {
        this.waterAmount = waterAmount;
    }

    @Column(name = "irrigate_method", length = 50)
    public String getIrrigateMethod() {
        return irrigateMethod;
    }

    public void setIrrigateMethod(String irrigateMethod) {
        this.irrigateMethod = irrigateMethod;
    }

    @Column(name = "irrigate_duration")
    public Integer getIrrigateDuration() {
        return irrigateDuration;
    }

    public void setIrrigateDuration(Integer irrigateDuration) {
        this.irrigateDuration = irrigateDuration;
    }

    @Column(name = "operator", length = 50)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public Farmland getFarmland() {
        return farmland;
    }

    public void setFarmland(Farmland farmland) {
        this.farmland = farmland;
    }
}