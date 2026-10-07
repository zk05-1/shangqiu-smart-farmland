package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_fertilization_record")
public class FertilizationRecord extends BaseEntity {

    private Long farmlandId;
    private Long plantingRecordId;
    private String fertilizerName;
    private String fertilizerType;
    private LocalDateTime applyDate;
    private BigDecimal amount;
    private String applyMethod;
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

    @Column(name = "fertilizer_name", length = 100)
    public String getFertilizerName() {
        return fertilizerName;
    }

    public void setFertilizerName(String fertilizerName) {
        this.fertilizerName = fertilizerName;
    }

    @Column(name = "fertilizer_type", length = 50)
    public String getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(String fertilizerType) {
        this.fertilizerType = fertilizerType;
    }

    @Column(name = "apply_date")
    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    @Column(name = "amount", precision = 10, scale = 2)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "apply_method", length = 100)
    public String getApplyMethod() {
        return applyMethod;
    }

    public void setApplyMethod(String applyMethod) {
        this.applyMethod = applyMethod;
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