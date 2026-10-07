package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_sensor_data")
public class SensorData extends BaseEntity {

    private Long farmlandId;
    private BigDecimal soilTemperature;
    private BigDecimal soilMoisture;
    private BigDecimal airTemperature;
    private BigDecimal airHumidity;
    private BigDecimal lightIntensity;
    private BigDecimal soilPh;
    private BigDecimal nitrogen;
    private BigDecimal phosphorus;
    private BigDecimal potassium;

    @Column(name = "farmland_id")
    public Long getFarmlandId() {
        return farmlandId;
    }

    public void setFarmlandId(Long farmlandId) {
        this.farmlandId = farmlandId;
    }

    @Column(name = "soil_temperature", precision = 6, scale = 2)
    public BigDecimal getSoilTemperature() {
        return soilTemperature;
    }

    public void setSoilTemperature(BigDecimal soilTemperature) {
        this.soilTemperature = soilTemperature;
    }

    @Column(name = "soil_moisture", precision = 6, scale = 2)
    public BigDecimal getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(BigDecimal soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    @Column(name = "air_temperature", precision = 6, scale = 2)
    public BigDecimal getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(BigDecimal airTemperature) {
        this.airTemperature = airTemperature;
    }

    @Column(name = "air_humidity", precision = 6, scale = 2)
    public BigDecimal getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(BigDecimal airHumidity) {
        this.airHumidity = airHumidity;
    }

    @Column(name = "light_intensity", precision = 10, scale = 2)
    public BigDecimal getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(BigDecimal lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    @Column(name = "soil_ph", precision = 4, scale = 2)
    public BigDecimal getSoilPh() {
        return soilPh;
    }

    public void setSoilPh(BigDecimal soilPh) {
        this.soilPh = soilPh;
    }

    @Column(name = "nitrogen", precision = 6, scale = 2)
    public BigDecimal getNitrogen() {
        return nitrogen;
    }

    public void setNitrogen(BigDecimal nitrogen) {
        this.nitrogen = nitrogen;
    }

    @Column(name = "phosphorus", precision = 6, scale = 2)
    public BigDecimal getPhosphorus() {
        return phosphorus;
    }

    public void setPhosphorus(BigDecimal phosphorus) {
        this.phosphorus = phosphorus;
    }

    @Column(name = "potassium", precision = 6, scale = 2)
    public BigDecimal getPotassium() {
        return potassium;
    }

    public void setPotassium(BigDecimal potassium) {
        this.potassium = potassium;
    }
}