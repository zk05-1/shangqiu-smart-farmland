package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_yield_prediction")
public class YieldPrediction extends BaseEntity {

    private Long farmlandId;
    private Long plantingRecordId;
    private Long cropTypeId;
    private Integer predictYear;
    private BigDecimal predictYield;
    private BigDecimal actualYield;
    private String predictModel;
    private BigDecimal confidence;
    private String factors;
    private Farmland farmland;
    private CropType cropType;

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

    @Column(name = "crop_type_id")
    public Long getCropTypeId() {
        return cropTypeId;
    }

    public void setCropTypeId(Long cropTypeId) {
        this.cropTypeId = cropTypeId;
    }

    @Column(name = "predict_year")
    public Integer getPredictYear() {
        return predictYear;
    }

    public void setPredictYear(Integer predictYear) {
        this.predictYear = predictYear;
    }

    @Column(name = "predict_yield", precision = 10, scale = 2)
    public BigDecimal getPredictYield() {
        return predictYield;
    }

    public void setPredictYield(BigDecimal predictYield) {
        this.predictYield = predictYield;
    }

    @Column(name = "actual_yield", precision = 10, scale = 2)
    public BigDecimal getActualYield() {
        return actualYield;
    }

    public void setActualYield(BigDecimal actualYield) {
        this.actualYield = actualYield;
    }

    @Column(name = "predict_model", length = 100)
    public String getPredictModel() {
        return predictModel;
    }

    public void setPredictModel(String predictModel) {
        this.predictModel = predictModel;
    }

    @Column(name = "confidence", precision = 5, scale = 2)
    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }

    @Column(name = "factors", columnDefinition = "TEXT")
    public String getFactors() {
        return factors;
    }

    public void setFactors(String factors) {
        this.factors = factors;
    }

    @Transient
    public Farmland getFarmland() {
        return farmland;
    }

    public void setFarmland(Farmland farmland) {
        this.farmland = farmland;
    }

    @Transient
    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }
}