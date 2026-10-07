package com.sqnu.server.service;

import com.sqnu.server.common.PageResult;
import com.sqnu.server.entity.WeatherData;
import com.sqnu.server.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 气象数据服务层
 *
 * @author sqnu
 */
@Service
@Transactional
public class WeatherDataService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    /**
     * 分页查询气象数据
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    public PageResult<WeatherData> getWeatherDataList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "recordDate"));
        Page<WeatherData> recordPage = weatherDataRepository.findByDeletedFalse(pageable);

        return new PageResult<>(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getNumber() + 1,
                recordPage.getSize()
        );
    }

    /**
     * 根据ID获取气象数据详情
     *
     * @param id 气象数据ID
     * @return 气象数据详情
     */
    public WeatherData getWeatherDataById(Long id) {
        return weatherDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("气象数据不存在"));
    }

    /**
     * 根据农田ID查询气象数据
     *
     * @param farmlandId 农田ID
     * @return 气象数据列表
     */
    public List<WeatherData> getWeatherDataByFarmlandId(Long farmlandId) {
        return weatherDataRepository.findByFarmlandId(farmlandId);
    }

    /**
     * 创建气象数据
     *
     * @param weatherData 气象数据信息
     * @return 创建后的气象数据
     */
    public WeatherData createWeatherData(WeatherData weatherData) {
        return weatherDataRepository.save(weatherData);
    }

    /**
     * 更新气象数据
     *
     * @param id          气象数据ID
     * @param weatherData 更新信息
     * @return 更新后的气象数据
     */
    public WeatherData updateWeatherData(Long id, WeatherData weatherData) {
        WeatherData existingData = getWeatherDataById(id);

        if (weatherData.getRecordDate() != null) {
            existingData.setRecordDate(weatherData.getRecordDate());
        }
        if (weatherData.getTemperatureMax() != null) {
            existingData.setTemperatureMax(weatherData.getTemperatureMax());
        }
        if (weatherData.getTemperatureMin() != null) {
            existingData.setTemperatureMin(weatherData.getTemperatureMin());
        }
        if (weatherData.getHumidity() != null) {
            existingData.setHumidity(weatherData.getHumidity());
        }
        if (weatherData.getRainfall() != null) {
            existingData.setRainfall(weatherData.getRainfall());
        }
        if (weatherData.getWindSpeed() != null) {
            existingData.setWindSpeed(weatherData.getWindSpeed());
        }
        if (weatherData.getWindDirection() != null) {
            existingData.setWindDirection(weatherData.getWindDirection());
        }
        if (weatherData.getWeatherType() != null) {
            existingData.setWeatherType(weatherData.getWeatherType());
        }
        if (weatherData.getDisasterWarning() != null) {
            existingData.setDisasterWarning(weatherData.getDisasterWarning());
        }

        return weatherDataRepository.save(existingData);
    }

    /**
     * 删除气象数据（逻辑删除）
     *
     * @param id 气象数据ID
     */
    public void deleteWeatherData(Long id) {
        WeatherData weatherData = getWeatherDataById(id);
        weatherData.setDeleted(1);
        weatherDataRepository.save(weatherData);
    }
}