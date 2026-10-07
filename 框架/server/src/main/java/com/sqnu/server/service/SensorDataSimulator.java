package com.sqnu.server.service;

import com.sqnu.server.entity.Farmland;
import com.sqnu.server.entity.SensorData;
import com.sqnu.server.repository.FarmlandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SensorDataSimulator {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private FarmlandRepository farmlandRepository;

    private final Random random = new Random();

    @Scheduled(cron = "0 */30 * * * ?")
    public void simulateSensorData() {
        System.out.println("==============================================");
        System.out.println("定时任务执行：模拟传感器数据");
        System.out.println("==============================================");
        
        generateSensorDataForAllFarmlands();
    }

    public void generateSensorDataForAllFarmlands() {
        List<Farmland> farmlands = farmlandRepository.findAll();
        List<SensorData> sensorDataList = new ArrayList<>();

        for (Farmland farmland : farmlands) {
            SensorData sensorData = generateSensorData(farmland.getId(), farmland.getSoilType());
            sensorDataList.add(sensorData);
        }

        if (!sensorDataList.isEmpty()) {
            sensorDataService.saveAll(sensorDataList);
            System.out.println("✓ 传感器数据生成成功，共生成 " + sensorDataList.size() + " 条记录");
        }
    }

    private SensorData generateSensorData(Long farmlandId, String soilType) {
        SensorData sensorData = new SensorData();
        sensorData.setFarmlandId(farmlandId);

        int hour = LocalTime.now().getHour();
        boolean isDaytime = hour >= 6 && hour <= 18;

        sensorData.setAirTemperature(generateAirTemperature(hour));
        sensorData.setAirHumidity(generateAirHumidity(hour));
        sensorData.setSoilTemperature(generateSoilTemperature(sensorData.getAirTemperature()));
        sensorData.setSoilMoisture(generateSoilMoisture(soilType));
        sensorData.setLightIntensity(generateLightIntensity(isDaytime, hour));
        sensorData.setSoilPh(generateSoilPh(soilType));
        sensorData.setNitrogen(generateNutrient(60, 150));
        sensorData.setPhosphorus(generateNutrient(20, 80));
        sensorData.setPotassium(generateNutrient(80, 200));

        return sensorData;
    }

    private BigDecimal generateAirTemperature(int hour) {
        double baseTemp;
        if (hour >= 14 && hour <= 16) {
            baseTemp = 30 + random.nextDouble() * 5;
        } else if (hour >= 4 && hour <= 6) {
            baseTemp = 22 + random.nextDouble() * 4;
        } else {
            baseTemp = 25 + random.nextDouble() * 6;
        }
        return BigDecimal.valueOf(baseTemp).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal generateAirHumidity(int hour) {
        double baseHumidity;
        if (hour >= 5 && hour <= 8) {
            baseHumidity = 75 + random.nextDouble() * 15;
        } else if (hour >= 13 && hour <= 15) {
            baseHumidity = 55 + random.nextDouble() * 10;
        } else {
            baseHumidity = 65 + random.nextDouble() * 10;
        }
        return BigDecimal.valueOf(baseHumidity).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal generateSoilTemperature(BigDecimal airTemp) {
        double airTempValue = airTemp.doubleValue();
        double soilTemp = airTempValue - 2 + random.nextDouble() * 3;
        return BigDecimal.valueOf(soilTemp).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal generateSoilMoisture(String soilType) {
        double baseMoisture;
        if ("黑土".equals(soilType) || "壤土".equals(soilType)) {
            baseMoisture = 45 + random.nextDouble() * 15;
        } else if ("沙土".equals(soilType)) {
            baseMoisture = 30 + random.nextDouble() * 10;
        } else if ("粘土".equals(soilType)) {
            baseMoisture = 55 + random.nextDouble() * 15;
        } else {
            baseMoisture = 40 + random.nextDouble() * 15;
        }
        return BigDecimal.valueOf(baseMoisture).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal generateLightIntensity(boolean isDaytime, int hour) {
        if (!isDaytime) {
            return BigDecimal.valueOf(0);
        }

        double intensity;
        if (hour >= 10 && hour <= 14) {
            intensity = 80000 + random.nextDouble() * 30000;
        } else if (hour == 6 || hour == 18) {
            intensity = 10000 + random.nextDouble() * 10000;
        } else {
            intensity = 30000 + random.nextDouble() * 40000;
        }
        return BigDecimal.valueOf(intensity).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal generateSoilPh(String soilType) {
        double basePh;
        if ("黑土".equals(soilType)) {
            basePh = 6.5 + random.nextDouble() * 0.8;
        } else if ("壤土".equals(soilType)) {
            basePh = 6.2 + random.nextDouble() * 0.8;
        } else if ("沙土".equals(soilType)) {
            basePh = 6.8 + random.nextDouble() * 0.6;
        } else if ("粘土".equals(soilType)) {
            basePh = 6.0 + random.nextDouble() * 0.8;
        } else {
            basePh = 6.5 + random.nextDouble() * 0.5;
        }
        return BigDecimal.valueOf(basePh).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal generateNutrient(double min, double max) {
        double value = min + random.nextDouble() * (max - min);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}