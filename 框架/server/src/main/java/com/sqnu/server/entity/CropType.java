package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_crop_type")
public class CropType extends BaseEntity {

    private String cropCode;
    private String cropName;
    private String category;
    private Integer growthPeriod;
    private String suitableSoil;
    private String suitableTemp;
    private String description;
    private String imageUrl;

    @Column(name = "crop_code", length = 50, unique = true)
    public String getCropCode() {
        return cropCode;
    }

    public void setCropCode(String cropCode) {
        this.cropCode = cropCode;
    }

    @Column(name = "crop_name", length = 100)
    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    @Column(name = "category", length = 50)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "growth_period")
    public Integer getGrowthPeriod() {
        return growthPeriod;
    }

    public void setGrowthPeriod(Integer growthPeriod) {
        this.growthPeriod = growthPeriod;
    }

    @Column(name = "suitable_soil", length = 200)
    public String getSuitableSoil() {
        return suitableSoil;
    }

    public void setSuitableSoil(String suitableSoil) {
        this.suitableSoil = suitableSoil;
    }

    @Column(name = "suitable_temp", length = 200)
    public String getSuitableTemp() {
        return suitableTemp;
    }

    public void setSuitableTemp(String suitableTemp) {
        this.suitableTemp = suitableTemp;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "image_url", length = 500)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}