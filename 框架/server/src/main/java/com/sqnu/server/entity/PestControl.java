package com.sqnu.server.entity;

import com.sqnu.server.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pest_control")
public class PestControl extends BaseEntity {

    private Long pestMonitorId;
    private String controlMethod;
    private String pesticideName;
    private BigDecimal dosage;
    private LocalDateTime controlDate;
    private String effect;
    private String operator;
    private BigDecimal cost;
    private String description;
    private PestMonitor pestMonitor;

    @Column(name = "pest_monitor_id")
    public Long getPestMonitorId() {
        return pestMonitorId;
    }

    public void setPestMonitorId(Long pestMonitorId) {
        this.pestMonitorId = pestMonitorId;
    }

    @Column(name = "control_method", length = 100)
    public String getControlMethod() {
        return controlMethod;
    }

    public void setControlMethod(String controlMethod) {
        this.controlMethod = controlMethod;
    }

    @Column(name = "pesticide_name", length = 100)
    public String getPesticideName() {
        return pesticideName;
    }

    public void setPesticideName(String pesticideName) {
        this.pesticideName = pesticideName;
    }

    @Column(name = "dosage", precision = 10, scale = 2)
    public BigDecimal getDosage() {
        return dosage;
    }

    public void setDosage(BigDecimal dosage) {
        this.dosage = dosage;
    }

    @Column(name = "control_date")
    public LocalDateTime getControlDate() {
        return controlDate;
    }

    public void setControlDate(LocalDateTime controlDate) {
        this.controlDate = controlDate;
    }

    @Column(name = "effect", length = 50)
    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Column(name = "operator", length = 50)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "cost", precision = 10, scale = 2)
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public PestMonitor getPestMonitor() {
        return pestMonitor;
    }

    public void setPestMonitor(PestMonitor pestMonitor) {
        this.pestMonitor = pestMonitor;
    }
}