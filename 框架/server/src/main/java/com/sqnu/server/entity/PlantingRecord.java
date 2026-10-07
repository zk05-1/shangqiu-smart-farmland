package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_planting_record")
public class PlantingRecord extends BaseEntity {

    private Long farmlandId;
    private Long cropTypeId;
    private LocalDate plantDate;
    private LocalDate expectHarvestDate;
    private LocalDate actualHarvestDate;
    private BigDecimal plantArea;
    private BigDecimal seedAmount;
    private String status;
    private String description;
    private Farmland farmland;
    private CropType cropType;

    @Column(name = "farmland_id")
    public Long getFarmlandId() {
        return farmlandId;
    }

    public void setFarmlandId(Long farmlandId) {
        this.farmlandId = farmlandId;
    }

    @Column(name = "crop_type_id")
    public Long getCropTypeId() {
        return cropTypeId;
    }

    public void setCropTypeId(Long cropTypeId) {
        this.cropTypeId = cropTypeId;
    }

    @Column(name = "plant_date")
    public LocalDate getPlantDate() {
        return plantDate;
    }

    public void setPlantDate(LocalDate plantDate) {
        this.plantDate = plantDate;
    }

    @Column(name = "expect_harvest_date")
    public LocalDate getExpectHarvestDate() {
        return expectHarvestDate;
    }

    public void setExpectHarvestDate(LocalDate expectHarvestDate) {
        this.expectHarvestDate = expectHarvestDate;
    }

    @Column(name = "actual_harvest_date")
    public LocalDate getActualHarvestDate() {
        return actualHarvestDate;
    }

    public void setActualHarvestDate(LocalDate actualHarvestDate) {
        this.actualHarvestDate = actualHarvestDate;
    }

    @Column(name = "plant_area", precision = 10, scale = 2)
    public BigDecimal getPlantArea() {
        return plantArea;
    }

    public void setPlantArea(BigDecimal plantArea) {
        this.plantArea = plantArea;
    }

    @Column(name = "seed_amount", precision = 10, scale = 2)
    public BigDecimal getSeedAmount() {
        return seedAmount;
    }

    public void setSeedAmount(BigDecimal seedAmount) {
        this.seedAmount = seedAmount;
    }

    @Column(name = "status", length = 20)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Transient
    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }
}