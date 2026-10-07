package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pest_monitor")
public class PestMonitor extends BaseEntity {

    private Long farmlandId;
    private Long plantingRecordId;
    private String pestName;
    private String pestType;
    private String severity;
    private LocalDate foundDate;
    private BigDecimal affectedArea;
    private String imageUrl;
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

    @Column(name = "pest_name", length = 100)
    public String getPestName() {
        return pestName;
    }

    public void setPestName(String pestName) {
        this.pestName = pestName;
    }

    @Column(name = "pest_type", length = 50)
    public String getPestType() {
        return pestType;
    }

    public void setPestType(String pestType) {
        this.pestType = pestType;
    }

    @Column(name = "severity", length = 20)
    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Column(name = "found_date")
    public LocalDate getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(LocalDate foundDate) {
        this.foundDate = foundDate;
    }

    @Column(name = "affected_area", precision = 10, scale = 2)
    public BigDecimal getAffectedArea() {
        return affectedArea;
    }

    public void setAffectedArea(BigDecimal affectedArea) {
        this.affectedArea = affectedArea;
    }

    @Column(name = "image_url", length = 500)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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