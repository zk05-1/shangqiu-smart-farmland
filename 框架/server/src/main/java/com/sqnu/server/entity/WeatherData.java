package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_weather_data")
public class WeatherData extends BaseEntity {

    private Long farmlandId;
    private LocalDate recordDate;
    private BigDecimal temperatureMax;
    private BigDecimal temperatureMin;
    private BigDecimal humidity;
    private BigDecimal rainfall;
    private BigDecimal windSpeed;
    private String windDirection;
    private String weatherType;
    private String disasterWarning;

    @Column(name = "farmland_id")
    public Long getFarmlandId() {
        return farmlandId;
    }

    public void setFarmlandId(Long farmlandId) {
        this.farmlandId = farmlandId;
    }

    @Column(name = "record_date")
    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Column(name = "temperature_max", precision = 5, scale = 2)
    public BigDecimal getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(BigDecimal temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    @Column(name = "temperature_min", precision = 5, scale = 2)
    public BigDecimal getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(BigDecimal temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    @Column(name = "humidity", precision = 5, scale = 2)
    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    @Column(name = "rainfall", precision = 10, scale = 2)
    public BigDecimal getRainfall() {
        return rainfall;
    }

    public void setRainfall(BigDecimal rainfall) {
        this.rainfall = rainfall;
    }

    @Column(name = "wind_speed", precision = 5, scale = 2)
    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(BigDecimal windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Column(name = "wind_direction", length = 20)
    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    @Column(name = "weather_type", length = 50)
    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    @Column(name = "disaster_warning", length = 100)
    public String getDisasterWarning() {
        return disasterWarning;
    }

    public void setDisasterWarning(String disasterWarning) {
        this.disasterWarning = disasterWarning;
    }
}