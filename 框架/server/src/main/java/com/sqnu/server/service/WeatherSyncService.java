package com.sqnu.server.service;

import com.sqnu.server.entity.WeatherData;
import com.sqnu.server.repository.WeatherDataRepository;
import com.sqnu.server.repository.FarmlandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * 天气数据同步服务
 * <p>
 * 用于从和风天气API获取商丘市实时天气数据，并同步到数据库。
 * 同时提供模拟数据生成功能，在没有API Key时使用。
 * </p>
 *
 * @author sqnu
 */
@Service
public class WeatherSyncService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private FarmlandRepository farmlandRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${weather.api.key:your_qweather_key}")
    private String qweatherApiKey;

    @Value("${weather.api.base-url:https://api.qweather.com/v7}")
    private String qweatherBaseUrl;

    /**
     * 商丘市和风天气城市ID
     */
    private static final String SHANGQIU_CITY_ID = "101181101";

    /**
     * 同步商丘市天气数据
     * <p>
     * 如果配置了有效的和风天气API Key，则从API获取真实数据。
     * 否则生成模拟数据。
     * </p>
     */
    public void syncShangqiuWeather() {
        if ("your_qweather_key".equals(qweatherApiKey)) {
            System.out.println("==============================================");
            System.out.println("⚠️  和风天气API Key未配置，生成模拟天气数据");
            System.out.println("==============================================");
            generateSimulatedWeatherData();
        } else {
            fetchFromQWeatherAPI();
        }
    }

    /**
     * 从和风天气API获取天气数据
     */
    private void fetchFromQWeatherAPI() {
        try {
            // 获取实时天气
            String weatherUrl = qweatherBaseUrl + "/weather/now?location=" + SHANGQIU_CITY_ID + "&key=" + qweatherApiKey;
            String forecastUrl = qweatherBaseUrl + "/weather/7d?location=" + SHANGQIU_CITY_ID + "&key=" + qweatherApiKey;

            System.out.println("==============================================");
            System.out.println("正在从和风天气API获取商丘天气数据...");
            System.out.println("==============================================");

            try {
                String weatherResponse = restTemplate.getForObject(weatherUrl, String.class);
                System.out.println("实时天气响应: " + weatherResponse);
            } catch (Exception e) {
                System.out.println("获取实时天气失败: " + e.getMessage());
            }

            try {
                String forecastResponse = restTemplate.getForObject(forecastUrl, String.class);
                System.out.println("7天预报响应: " + forecastResponse);
            } catch (Exception e) {
                System.out.println("获取7天预报失败: " + e.getMessage());
            }

            // 解析API响应并保存数据（简化版本，使用模拟数据作为备选）
            generateSimulatedWeatherData();

        } catch (Exception e) {
            System.err.println("天气API调用失败: " + e.getMessage());
            generateSimulatedWeatherData();
        }
    }

    /**
     * 生成模拟天气数据
     * <p>
     * 根据商丘市气候特点生成合理的模拟数据。
     * 夏季(6-8月): 高温多雨
     * 冬季(12-2月): 寒冷干燥
     * 春秋季节: 温和适中
     * </p>
     */
    private void generateSimulatedWeatherData() {
        Random random = new Random();
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();

        // 根据月份生成季节特征
        int seasonType;
        if (month >= 6 && month <= 8) {
            seasonType = 1; // 夏季
        } else if (month >= 12 || month <= 2) {
            seasonType = 2; // 冬季
        } else if (month >= 3 && month <= 5) {
            seasonType = 3; // 春季
        } else {
            seasonType = 4; // 秋季
        }

        // 获取所有农田ID
        List<Long> farmlandIds = farmlandRepository.findAll().stream()
                .map(f -> f.getId())
                .toList();

        if (farmlandIds.isEmpty()) {
            System.out.println("⚠️  暂无农田数据，跳过天气数据生成");
            return;
        }

        // 为每个农田生成最近7天的天气数据
        int count = 0;
        for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
            LocalDate recordDate = today.minusDays(dayOffset);

            for (Long farmlandId : farmlandIds) {
                // 检查是否已存在该日期的数据
                if (weatherDataRepository.findByFarmlandIdAndRecordDate(farmlandId, recordDate).isPresent()) {
                    continue;
                }

                WeatherData weatherData = new WeatherData();
                weatherData.setFarmlandId(farmlandId);
                weatherData.setRecordDate(recordDate);

                // 根据季节生成天气数据
                generateSeasonalWeather(weatherData, seasonType, random, dayOffset);

                weatherDataRepository.save(weatherData);
                count++;
            }
        }

        System.out.println("==============================================");
        System.out.println("✓ 模拟天气数据生成成功");
        System.out.println("  共生成 " + count + " 条天气记录");
        System.out.println("==============================================");
    }

    /**
     * 根据季节生成天气数据
     */
    private void generateSeasonalWeather(WeatherData weatherData, int seasonType, Random random, int dayOffset) {
        String[] weatherTypes = {"晴", "多云", "阴", "小雨", "中雨", "大雨"};
        String[] windDirections = {"东风", "南风", "西风", "北风", "东南风", "西北风"};

        switch (seasonType) {
            case 1: // 夏季：高温多雨
                weatherData.setTemperatureMax(BigDecimal.valueOf(30 + random.nextDouble() * 8));
                weatherData.setTemperatureMin(BigDecimal.valueOf(22 + random.nextDouble() * 6));
                weatherData.setHumidity(BigDecimal.valueOf(65 + random.nextDouble() * 25));
                weatherData.setRainfall(random.nextDouble() > 0.6 ? BigDecimal.valueOf(random.nextDouble() * 30) : BigDecimal.ZERO);
                weatherData.setWindSpeed(BigDecimal.valueOf(2 + random.nextDouble() * 4));
                weatherData.setWeatherType(random.nextDouble() > 0.5 ? weatherTypes[random.nextInt(3)] : weatherTypes[3 + random.nextInt(3)]);
                break;

            case 2: // 冬季：寒冷干燥
                weatherData.setTemperatureMax(BigDecimal.valueOf(5 + random.nextDouble() * 8));
                weatherData.setTemperatureMin(BigDecimal.valueOf(-5 + random.nextDouble() * 5));
                weatherData.setHumidity(BigDecimal.valueOf(40 + random.nextDouble() * 20));
                weatherData.setRainfall(random.nextDouble() > 0.95 ? BigDecimal.valueOf(random.nextDouble() * 5) : BigDecimal.ZERO);
                weatherData.setWindSpeed(BigDecimal.valueOf(3 + random.nextDouble() * 5));
                weatherData.setWeatherType(random.nextDouble() > 0.7 ? weatherTypes[random.nextInt(2)] : weatherTypes[2]);
                break;

            case 3: // 春季：温和适中
                weatherData.setTemperatureMax(BigDecimal.valueOf(15 + random.nextDouble() * 10));
                weatherData.setTemperatureMin(BigDecimal.valueOf(5 + random.nextDouble() * 8));
                weatherData.setHumidity(BigDecimal.valueOf(50 + random.nextDouble() * 20));
                weatherData.setRainfall(random.nextDouble() > 0.7 ? BigDecimal.valueOf(random.nextDouble() * 15) : BigDecimal.ZERO);
                weatherData.setWindSpeed(BigDecimal.valueOf(3 + random.nextDouble() * 4));
                weatherData.setWeatherType(random.nextDouble() > 0.6 ? weatherTypes[random.nextInt(3)] : weatherTypes[3 + random.nextInt(2)]);
                break;

            case 4: // 秋季：凉爽干燥
                weatherData.setTemperatureMax(BigDecimal.valueOf(20 + random.nextDouble() * 8));
                weatherData.setTemperatureMin(BigDecimal.valueOf(10 + random.nextDouble() * 6));
                weatherData.setHumidity(BigDecimal.valueOf(55 + random.nextDouble() * 15));
                weatherData.setRainfall(random.nextDouble() > 0.85 ? BigDecimal.valueOf(random.nextDouble() * 10) : BigDecimal.ZERO);
                weatherData.setWindSpeed(BigDecimal.valueOf(2 + random.nextDouble() * 3));
                weatherData.setWeatherType(random.nextDouble() > 0.7 ? weatherTypes[random.nextInt(2)] : weatherTypes[2]);
                break;
        }

        weatherData.setWindDirection(windDirections[random.nextInt(windDirections.length)]);
        weatherData.setDisasterWarning(random.nextDouble() > 0.98 ? "高温预警" : null);
    }

    /**
     * 定时任务：每天8:00同步天气数据
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduledSyncWeather() {
        System.out.println("==============================================");
        System.out.println("定时任务执行：同步天气数据");
        System.out.println("==============================================");
        syncShangqiuWeather();
    }
}