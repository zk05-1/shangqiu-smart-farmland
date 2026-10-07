package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.SensorData;
import com.sqnu.server.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public PageResult<SensorData> getSensorDataList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<SensorData> pageResult = sensorDataRepository.findAll(pageable);
        return new PageResult<>(pageResult.getContent(), pageResult.getTotalElements(), page, size);
    }

    public PageResult<SensorData> getSensorDataByFarmlandId(Long farmlandId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<SensorData> pageResult = sensorDataRepository.findByFarmlandId(farmlandId, pageable);
        return new PageResult<>(pageResult.getContent(), pageResult.getTotalElements(), page, size);
    }

    public SensorData getSensorDataById(Long id) {
        return sensorDataRepository.findById(id).orElse(null);
    }

    public List<SensorData> getRecentSensorData(Long farmlandId) {
        return sensorDataRepository.findTop10ByFarmlandIdOrderByCreatedTimeDesc(farmlandId);
    }

    public SensorData createSensorData(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    public SensorData updateSensorData(Long id, SensorData sensorData) {
        SensorData existingData = sensorDataRepository.findById(id).orElse(null);
        if (existingData != null) {
            existingData.setFarmlandId(sensorData.getFarmlandId());
            existingData.setSoilTemperature(sensorData.getSoilTemperature());
            existingData.setSoilMoisture(sensorData.getSoilMoisture());
            existingData.setAirTemperature(sensorData.getAirTemperature());
            existingData.setAirHumidity(sensorData.getAirHumidity());
            existingData.setLightIntensity(sensorData.getLightIntensity());
            existingData.setSoilPh(sensorData.getSoilPh());
            existingData.setNitrogen(sensorData.getNitrogen());
            existingData.setPhosphorus(sensorData.getPhosphorus());
            existingData.setPotassium(sensorData.getPotassium());
            return sensorDataRepository.save(existingData);
        }
        return null;
    }

    public void deleteSensorData(Long id) {
        sensorDataRepository.deleteById(id);
    }

    public void saveAll(List<SensorData> sensorDataList) {
        sensorDataRepository.saveAll(sensorDataList);
    }
}