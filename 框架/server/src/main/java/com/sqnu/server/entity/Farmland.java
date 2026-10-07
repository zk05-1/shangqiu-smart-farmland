package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_farmland")
public class Farmland extends BaseEntity {

    private String farmlandCode;
    private String farmlandName;
    private BigDecimal area;
    private String location;
    private String longitude;
    private String latitude;
    private String soilType;
    private BigDecimal soilPh;
    private String ownerName;
    private String ownerPhone;
    private String status;
    private String imageUrl;
    private String description;

    @Column(name = "farmland_code", length = 50, unique = true)
    public String getFarmlandCode() {
        return farmlandCode;
    }

    public void setFarmlandCode(String farmlandCode) {
        this.farmlandCode = farmlandCode;
    }

    @Column(name = "farmland_name", length = 100)
    public String getFarmlandName() {
        return farmlandName;
    }

    public void setFarmlandName(String farmlandName) {
        this.farmlandName = farmlandName;
    }

    @Column(name = "area", precision = 10, scale = 2)
    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    @Column(name = "location", length = 255)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "longitude", length = 20)
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name = "latitude", length = 20)
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name = "soil_type", length = 50)
    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    @Column(name = "soil_ph", precision = 4, scale = 2)
    public BigDecimal getSoilPh() {
        return soilPh;
    }

    public void setSoilPh(BigDecimal soilPh) {
        this.soilPh = soilPh;
    }

    @Column(name = "owner_name", length = 50)
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Column(name = "owner_phone", length = 20)
    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    @Column(name = "status", length = 20)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "image_url", length = 255)
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
}