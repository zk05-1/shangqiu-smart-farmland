package com.sqnu.server.config;

import com.sqnu.server.entity.*;
import com.sqnu.server.repository.*;
import com.sqnu.server.service.WeatherSyncService;
import com.sqnu.server.service.SensorDataSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FarmlandRepository farmlandRepository;

    @Autowired
    private PlantingRecordRepository plantingRecordRepository;

    @Autowired
    private IrrigationRecordRepository irrigationRecordRepository;

    @Autowired
    private FertilizationRecordRepository fertilizationRecordRepository;

    @Autowired
    private PestMonitorRepository pestMonitorRepository;

    @Autowired
    private PestControlRepository pestControlRepository;

    @Autowired
    private YieldPredictionRepository yieldPredictionRepository;

    @Autowired
    private WeatherSyncService weatherSyncService;

    @Autowired
    private SensorDataSimulator sensorDataSimulator;

    @Override
    public void run(String... args) {
        initAdminUser();
        initNormalUser();
        initFarmlandData();
        initPlantingRecords();
        initIrrigationRecords();
        initFertilizationRecords();
        initPestMonitorData();
        initYieldPredictionData();
        weatherSyncService.syncShangqiuWeather();
        sensorDataSimulator.generateSensorDataForAllFarmlands();
    }

    private void initAdminUser() {
        Optional<SysUser> existingAdmin = userRepository.findByUsername("admin");
        if (existingAdmin.isEmpty()) {
            SysUser admin = new SysUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRealName("系统管理员");
            admin.setAvatar("https://api.dicebear.com/7.x/initials/svg?seed=admin");
            admin.setPhone("13837001001");
            admin.setEmail("admin@shangqiu.gov.cn");
            admin.setGender(1);
            admin.setStatus(1);
            userRepository.save(admin);
            System.out.println("==============================================");
            System.out.println("✓ 管理员用户创建成功");
            System.out.println("==============================================");
        } else {
            SysUser admin = existingAdmin.get();
            if (!admin.getPassword().startsWith("$2a$")) {
                admin.setPassword(passwordEncoder.encode("123456"));
                userRepository.save(admin);
            }
        }
    }

    private void initNormalUser() {
        Optional<SysUser> existingUser = userRepository.findByUsername("user");
        if (existingUser.isEmpty()) {
            SysUser user = new SysUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRealName("普通用户");
            user.setAvatar("https://api.dicebear.com/7.x/initials/svg?seed=user");
            user.setPhone("13837001002");
            user.setEmail("user@shangqiu.gov.cn");
            user.setGender(2);
            user.setStatus(1);
            userRepository.save(user);
            System.out.println("==============================================");
            System.out.println("✓ 普通用户创建成功");
            System.out.println("==============================================");
        }
    }

    private void initFarmlandData() {
        if (farmlandRepository.count() > 0) {
            System.out.println("==============================================");
            System.out.println("✓ 农田数据已存在，跳过初始化");
            System.out.println("==============================================");
            return;
        }

        createFarmland("SQ-LY-001", "梁园区城郊农田", 50.0, "梁园区城郊乡", 115.62, 34.42, "壤土", 7.2, "张主任", "13837001001", "USE");
        createFarmland("SQ-LY-002", "梁园区万亩良田", 1200.0, "梁园区谢集镇", 115.58, 34.45, "沙壤土", 7.0, "李主任", "13837001002", "USE");
        createFarmland("SQ-LY-003", "梁园区蔬菜基地", 80.0, "梁园区水池铺乡", 115.65, 34.38, "壤土", 6.8, "王主任", "13837001003", "USE");

        createFarmland("SQ-SY-001", "睢阳区古城农田", 60.0, "睢阳区古宋街道", 115.60, 34.40, "壤土", 7.1, "赵主任", "13837002001", "USE");
        createFarmland("SQ-SY-002", "睢阳区现代农业示范园", 500.0, "睢阳区李口镇", 115.55, 34.35, "沙壤土", 6.9, "孙主任", "13837002002", "USE");
        createFarmland("SQ-SY-003", "睢阳区花生种植基地", 200.0, "睢阳区坞墙镇", 115.70, 34.32, "沙土", 6.5, "周主任", "13837002003", "USE");

        createFarmland("SQ-MQ-001", "民权县小麦基地", 800.0, "民权县程庄镇", 115.15, 34.60, "壤土", 7.3, "吴主任", "13837003001", "USE");
        createFarmland("SQ-MQ-002", "民权县辣椒种植基地", 150.0, "民权县孙六镇", 115.30, 34.55, "沙壤土", 6.8, "郑主任", "13837003002", "USE");
        createFarmland("SQ-MQ-003", "民权县果园", 100.0, "民权县双塔镇", 115.05, 34.58, "壤土", 7.0, "冯主任", "13837003003", "USE");

        createFarmland("SQ-SX-001", "睢县玉米种植基地", 600.0, "睢县蓼堤镇", 115.25, 34.48, "壤土", 7.2, "陈主任", "13837004001", "USE");
        createFarmland("SQ-SX-002", "睢县莲藕基地", 50.0, "睢县城郊乡", 115.35, 34.42, "黏土", 7.5, "韩主任", "13837004002", "USE");
        createFarmland("SQ-SX-003", "睢县大豆种植区", 300.0, "睢县后台乡", 115.45, 34.38, "沙壤土", 6.9, "杨主任", "13837004003", "USE");

        createFarmland("SQ-NL-001", "宁陵县酥梨基地", 500.0, "宁陵县石桥镇", 115.35, 34.52, "壤土", 7.1, "许主任", "13837005001", "USE");
        createFarmland("SQ-NL-002", "宁陵县小麦示范区", 400.0, "宁陵县柳河镇", 115.45, 34.55, "沙壤土", 6.8, "何主任", "13837005002", "USE");
        createFarmland("SQ-NL-003", "宁陵县蔬菜大棚", 60.0, "宁陵县华堡镇", 115.50, 34.48, "壤土", 7.0, "吕主任", "13837005003", "USE");

        createFarmland("SQ-ZC-001", "柘城县辣椒之乡", 2000.0, "柘城县张桥乡", 115.20, 34.35, "沙壤土", 6.6, "施主任", "13837006001", "USE");
        createFarmland("SQ-ZC-002", "柘城县大蒜基地", 1500.0, "柘城县胡襄镇", 115.35, 34.30, "壤土", 7.0, "孔主任", "13837006002", "USE");
        createFarmland("SQ-ZC-003", "柘城县玉米种植区", 800.0, "柘城县慈圣镇", 115.15, 34.32, "沙土", 6.8, "曹主任", "13837006003", "USE");

        createFarmland("SQ-YC-001", "虞城县苹果基地", 300.0, "虞城县张集镇", 115.85, 34.55, "壤土", 7.2, "陶主任", "13837007001", "USE");
        createFarmland("SQ-YC-002", "虞城县小麦高产田", 1000.0, "虞城县稍岗镇", 115.75, 34.48, "沙壤土", 7.0, "姜主任", "13837007002", "USE");
        createFarmland("SQ-YC-003", "虞城县花卉基地", 40.0, "虞城县刘店乡", 115.70, 34.45, "壤土", 6.9, "谢主任", "13837007003", "USE");

        createFarmland("SQ-XY-001", "夏邑县西瓜之乡", 800.0, "夏邑县李集镇", 116.00, 34.38, "沙土", 6.5, "宋主任", "13837008001", "USE");
        createFarmland("SQ-XY-002", "夏邑县花生基地", 600.0, "夏邑县车站镇", 115.90, 34.42, "沙壤土", 6.8, "董主任", "13837008002", "USE");
        createFarmland("SQ-XY-003", "夏邑县玉米种植区", 500.0, "夏邑县会亭镇", 116.05, 34.35, "壤土", 7.0, "梁主任", "13837008003", "USE");

        createFarmland("SQ-YC-004", "永城市小麦基地", 2000.0, "永城市芒山镇", 116.30, 34.25, "壤土", 7.1, "杜主任", "13837009001", "USE");
        createFarmland("SQ-YC-005", "永城市蔬菜基地", 100.0, "永城市城厢乡", 116.35, 34.30, "沙壤土", 6.9, "魏主任", "13837009002", "USE");
        createFarmland("SQ-YC-006", "永城市烟叶种植区", 300.0, "永城市酂城镇", 116.20, 34.35, "壤土", 7.0, "贾主任", "13837009003", "FALLOW");

        System.out.println("==============================================");
        System.out.println("✓ 商丘市农田数据初始化成功 (" + farmlandRepository.count() + "块)");
        System.out.println("==============================================");
    }

    private void initPlantingRecords() {
        if (plantingRecordRepository.count() > 0) {
            System.out.println("==============================================");
            System.out.println("✓ 种植记录已存在，跳过初始化");
            System.out.println("==============================================");
            return;
        }

        createPlantingRecord(1L, 1L, LocalDate.of(2026, 10, 15), LocalDate.of(2027, 6, 15), LocalDate.of(2027, 6, 20), BigDecimal.valueOf(50), BigDecimal.valueOf(30), "HARVESTED", "小麦冬播");
        createPlantingRecord(2L, 1L, LocalDate.of(2026, 10, 20), LocalDate.of(2027, 6, 20), LocalDate.of(2027, 6, 25), BigDecimal.valueOf(1200), BigDecimal.valueOf(720), "HARVESTED", "万亩良田小麦");
        createPlantingRecord(3L, 2L, LocalDate.of(2027, 2, 10), LocalDate.of(2027, 5, 15), null, BigDecimal.valueOf(80), BigDecimal.valueOf(160), "GROWING", "春季蔬菜");
        createPlantingRecord(4L, 3L, LocalDate.of(2027, 3, 1), LocalDate.of(2027, 7, 1), null, BigDecimal.valueOf(60), BigDecimal.valueOf(60), "GROWING", "玉米种植");
        createPlantingRecord(5L, 4L, LocalDate.of(2027, 4, 1), LocalDate.of(2027, 9, 1), null, BigDecimal.valueOf(500), BigDecimal.valueOf(150), "PLANTING", "现代农业示范园");
        createPlantingRecord(6L, 5L, LocalDate.of(2027, 4, 15), LocalDate.of(2027, 9, 15), null, BigDecimal.valueOf(200), BigDecimal.valueOf(400), "PLANTING", "花生种植");
        createPlantingRecord(7L, 1L, LocalDate.of(2026, 10, 25), LocalDate.of(2027, 6, 25), LocalDate.of(2027, 6, 28), BigDecimal.valueOf(800), BigDecimal.valueOf(480), "HARVESTED", "小麦基地");
        createPlantingRecord(8L, 6L, LocalDate.of(2027, 3, 10), LocalDate.of(2027, 8, 10), null, BigDecimal.valueOf(150), BigDecimal.valueOf(300), "GROWING", "辣椒种植");
        createPlantingRecord(9L, 7L, LocalDate.of(2027, 4, 1), LocalDate.of(2027, 10, 1), null, BigDecimal.valueOf(100), BigDecimal.valueOf(200), "PLANTING", "果园");

        createPlantingRecord(10L, 8L, LocalDate.of(2027, 3, 15), LocalDate.of(2027, 9, 15), null, BigDecimal.valueOf(600), BigDecimal.valueOf(180), "GROWING", "玉米基地");
        createPlantingRecord(11L, 9L, LocalDate.of(2027, 5, 1), LocalDate.of(2027, 10, 1), null, BigDecimal.valueOf(50), BigDecimal.valueOf(100), "PLANTING", "莲藕基地");
        createPlantingRecord(12L, 10L, LocalDate.of(2027, 4, 20), LocalDate.of(2027, 9, 20), null, BigDecimal.valueOf(300), BigDecimal.valueOf(600), "PLANTING", "大豆种植");
        createPlantingRecord(13L, 7L, LocalDate.of(2027, 4, 5), LocalDate.of(2027, 10, 15), null, BigDecimal.valueOf(500), BigDecimal.valueOf(1000), "PLANTING", "酥梨基地");
        createPlantingRecord(14L, 1L, LocalDate.of(2026, 10, 18), LocalDate.of(2027, 6, 18), LocalDate.of(2027, 6, 22), BigDecimal.valueOf(400), BigDecimal.valueOf(240), "HARVESTED", "小麦示范区");
        createPlantingRecord(15L, 2L, LocalDate.of(2027, 2, 15), LocalDate.of(2027, 5, 20), null, BigDecimal.valueOf(60), BigDecimal.valueOf(120), "GROWING", "蔬菜大棚");
        createPlantingRecord(16L, 6L, LocalDate.of(2027, 3, 20), LocalDate.of(2027, 8, 20), null, BigDecimal.valueOf(2000), BigDecimal.valueOf(4000), "GROWING", "辣椒之乡");
        createPlantingRecord(17L, 11L, LocalDate.of(2027, 9, 1), LocalDate.of(2028, 5, 1), null, BigDecimal.valueOf(1500), BigDecimal.valueOf(3000), "PLANTING", "大蒜基地");
        createPlantingRecord(18L, 8L, LocalDate.of(2027, 3, 25), LocalDate.of(2027, 9, 25), null, BigDecimal.valueOf(800), BigDecimal.valueOf(240), "GROWING", "玉米种植区");

        createPlantingRecord(19L, 7L, LocalDate.of(2027, 4, 10), LocalDate.of(2027, 10, 20), null, BigDecimal.valueOf(300), BigDecimal.valueOf(600), "PLANTING", "苹果基地");
        createPlantingRecord(20L, 1L, LocalDate.of(2026, 10, 22), LocalDate.of(2027, 6, 22), LocalDate.of(2027, 6, 26), BigDecimal.valueOf(1000), BigDecimal.valueOf(600), "HARVESTED", "小麦高产田");
        createPlantingRecord(21L, 12L, LocalDate.of(2027, 5, 15), LocalDate.of(2027, 10, 15), null, BigDecimal.valueOf(40), BigDecimal.valueOf(40), "PLANTING", "花卉基地");
        createPlantingRecord(22L, 13L, LocalDate.of(2027, 4, 25), LocalDate.of(2027, 8, 25), null, BigDecimal.valueOf(800), BigDecimal.valueOf(800), "GROWING", "西瓜之乡");
        createPlantingRecord(23L, 5L, LocalDate.of(2027, 4, 30), LocalDate.of(2027, 9, 30), null, BigDecimal.valueOf(600), BigDecimal.valueOf(1200), "PLANTING", "花生基地");
        createPlantingRecord(24L, 8L, LocalDate.of(2027, 3, 18), LocalDate.of(2027, 9, 18), null, BigDecimal.valueOf(500), BigDecimal.valueOf(150), "GROWING", "玉米种植区");
        createPlantingRecord(25L, 1L, LocalDate.of(2026, 10, 28), LocalDate.of(2027, 6, 28), LocalDate.of(2027, 7, 2), BigDecimal.valueOf(2000), BigDecimal.valueOf(1200), "HARVESTED", "小麦基地");
        createPlantingRecord(26L, 2L, LocalDate.of(2027, 2, 20), LocalDate.of(2027, 5, 25), null, BigDecimal.valueOf(100), BigDecimal.valueOf(200), "GROWING", "蔬菜基地");
        createPlantingRecord(27L, 14L, LocalDate.of(2027, 4, 15), LocalDate.of(2027, 9, 15), null, BigDecimal.valueOf(300), BigDecimal.valueOf(600), "PLANTING", "烟叶种植");

        System.out.println("==============================================");
        System.out.println("✓ 种植记录初始化成功 (" + plantingRecordRepository.count() + "条)");
        System.out.println("==============================================");
    }

    private void initIrrigationRecords() {
        if (irrigationRecordRepository.count() > 0) {
            System.out.println("==============================================");
            System.out.println("✓ 灌溉记录已存在，跳过初始化");
            System.out.println("==============================================");
            return;
        }

        createIrrigationRecord(1L, 1L, LocalDateTime.of(2027, 3, 15, 8, 0), BigDecimal.valueOf(50), "SPRINKLER", 60, "张主任", "春季灌溉");
        createIrrigationRecord(2L, 2L, LocalDateTime.of(2027, 4, 10, 9, 0), BigDecimal.valueOf(800), "DRIP", 120, "李主任", "小麦抽穗期灌溉");
        createIrrigationRecord(3L, 3L, LocalDateTime.of(2027, 3, 20, 7, 30), BigDecimal.valueOf(30), "SPRINKLER", 45, "王主任", "蔬菜苗期灌溉");
        createIrrigationRecord(4L, 4L, LocalDateTime.of(2027, 4, 5, 8, 0), BigDecimal.valueOf(60), "FLOOD", 90, "赵主任", "玉米播种后灌溉");
        createIrrigationRecord(5L, 5L, LocalDateTime.of(2027, 5, 1, 6, 30), BigDecimal.valueOf(200), "DRIP", 180, "孙主任", "示范园灌溉");
        createIrrigationRecord(6L, 6L, LocalDateTime.of(2027, 5, 15, 7, 0), BigDecimal.valueOf(80), "SPRINKLER", 75, "周主任", "花生灌溉");
        createIrrigationRecord(7L, 7L, LocalDateTime.of(2027, 3, 25, 8, 30), BigDecimal.valueOf(500), "DRIP", 150, "吴主任", "小麦基地灌溉");
        createIrrigationRecord(8L, 8L, LocalDateTime.of(2027, 4, 20, 9, 0), BigDecimal.valueOf(60), "SPRINKLER", 60, "郑主任", "辣椒灌溉");
        createIrrigationRecord(9L, 9L, LocalDateTime.of(2027, 5, 20, 7, 30), BigDecimal.valueOf(40), "DRIP", 45, "冯主任", "果园灌溉");

        System.out.println("==============================================");
        System.out.println("✓ 灌溉记录初始化成功 (" + irrigationRecordRepository.count() + "条)");
        System.out.println("==============================================");
    }

    private void initFertilizationRecords() {
        if (fertilizationRecordRepository.count() > 0) {
            System.out.println("==============================================");
            System.out.println("✓ 施肥记录已存在，跳过初始化");
            System.out.println("==============================================");
            return;
        }

        createFertilizationRecord(1L, 1L, "尿素", "CHEMICAL", LocalDateTime.of(2027, 3, 10, 8, 0), BigDecimal.valueOf(100), "撒施", "张主任", "追肥");
        createFertilizationRecord(2L, 2L, "复合肥", "COMPOUND", LocalDateTime.of(2027, 4, 5, 7, 30), BigDecimal.valueOf(500), "条施", "李主任", "小麦拔节期施肥");
        createFertilizationRecord(3L, 3L, "有机肥", "ORGANIC", LocalDateTime.of(2027, 2, 25, 9, 0), BigDecimal.valueOf(200), "撒施", "王主任", "蔬菜基肥");
        createFertilizationRecord(4L, 4L, "复合肥", "COMPOUND", LocalDateTime.of(2027, 3, 18, 8, 0), BigDecimal.valueOf(150), "穴施", "赵主任", "玉米基肥");
        createFertilizationRecord(5L, 5L, "有机肥", "ORGANIC", LocalDateTime.of(2027, 3, 25, 7, 0), BigDecimal.valueOf(800), "撒施", "孙主任", "示范园基肥");
        createFertilizationRecord(6L, 6L, "复合肥", "COMPOUND", LocalDateTime.of(2027, 4, 20, 8, 30), BigDecimal.valueOf(100), "条施", "周主任", "花生追肥");
        createFertilizationRecord(7L, 7L, "尿素", "CHEMICAL", LocalDateTime.of(2027, 3, 12, 6, 30), BigDecimal.valueOf(300), "撒施", "吴主任", "小麦追肥");
        createFertilizationRecord(8L, 8L, "复合肥", "COMPOUND", LocalDateTime.of(2027, 4, 15, 9, 0), BigDecimal.valueOf(80), "穴施", "郑主任", "辣椒追肥");
        createFertilizationRecord(9L, 9L, "有机肥", "ORGANIC", LocalDateTime.of(2027, 4, 10, 7, 30), BigDecimal.valueOf(150), "环状施", "冯主任", "果园基肥");

        System.out.println("==============================================");
        System.out.println("✓ 施肥记录初始化成功 (" + fertilizationRecordRepository.count() + "条)");
        System.out.println("==============================================");
    }

    private void initPestMonitorData() {
        if (pestMonitorRepository.count() > 0) {
            System.out.println("==============================================");
            System.out.println("✓ 病虫害监测数据已存在，跳过初始化");
            System.out.println("==============================================");
            return;
        }

        createPestMonitor(3L, 3L, "蚜虫", "INSECT", "LIGHT", LocalDate.of(2027, 4, 10), BigDecimal.valueOf(5), "", "蔬菜蚜虫轻度发生");
        createPestMonitor(8L, 8L, "棉铃虫", "INSECT", "MODERATE", LocalDate.of(2027, 5, 5), BigDecimal.valueOf(20), "", "辣椒棉铃虫中度发生");
        createPestMonitor(9L, 9L, "白粉病", "DISEASE", "LIGHT", LocalDate.of(2027, 5, 8), BigDecimal.valueOf(10), "", "果园白粉病轻度发生");
        createPestMonitor(16L, 16L, "烟青虫", "INSECT", "SEVERE", LocalDate.of(2027, 5, 12), BigDecimal.valueOf(150), "", "辣椒烟青虫重度发生");

        createPestControl(1L, "生物防治", "天敌昆虫", BigDecimal.valueOf(500), LocalDateTime.of(2027, 4, 12, 8, 0), "GOOD", "王主任", BigDecimal.valueOf(500), "释放瓢虫");
        createPestControl(2L, "化学防治", "氯虫苯甲酰胺", BigDecimal.valueOf(20), LocalDateTime.of(2027, 5, 7, 7, 30), "EXCELLENT", "郑主任", BigDecimal.valueOf(800), "喷雾防治");
        createPestControl(3L, "化学防治", "粉锈宁", BigDecimal.valueOf(15), LocalDateTime.of(2027, 5, 10, 9, 0), "GOOD", "冯主任", BigDecimal.valueOf(600), "喷雾防治");
        createPestControl(4L, "化学防治", "高效氯氰菊酯", BigDecimal.valueOf(50), LocalDateTime.of(2027, 5, 15, 6, 30), "EXCELLENT", "施主任", BigDecimal.valueOf(2000), "大面积喷雾");

        System.out.println("==============================================");
        System.out.println("✓ 病虫害监测数据初始化成功 (" + pestMonitorRepository.count() + "条监测, " + pestControlRepository.count() + "条防治)");
        System.out.println("==============================================");
    }

    private void initYieldPredictionData() {
        if (yieldPredictionRepository.count() > 0) {
            System.out.println("==============================================");
            System.out.println("✓ 产量预测数据已存在，跳过初始化");
            System.out.println("==============================================");
            return;
        }

        createYieldPrediction(1L, 1L, 1L, 2027, BigDecimal.valueOf(550), BigDecimal.valueOf(562), "MLP神经网络", BigDecimal.valueOf(0.92), "气候适宜，土壤肥力充足");
        createYieldPrediction(2L, 2L, 1L, 2027, BigDecimal.valueOf(520), BigDecimal.valueOf(535), "随机森林", BigDecimal.valueOf(0.88), "万亩良田示范区");
        createYieldPrediction(7L, 7L, 1L, 2027, BigDecimal.valueOf(530), BigDecimal.valueOf(548), "支持向量机", BigDecimal.valueOf(0.90), "小麦基地");
        createYieldPrediction(16L, 16L, 6L, 2027, BigDecimal.valueOf(450), null, "集成学习模型", BigDecimal.valueOf(0.85), "辣椒产量预测");
        createYieldPrediction(13L, 13L, 7L, 2027, BigDecimal.valueOf(2500), null, "回归分析", BigDecimal.valueOf(0.87), "酥梨产量预测");
        createYieldPrediction(22L, 22L, 13L, 2027, BigDecimal.valueOf(3000), null, "时间序列模型", BigDecimal.valueOf(0.86), "西瓜产量预测");

        System.out.println("==============================================");
        System.out.println("✓ 产量预测数据初始化成功 (" + yieldPredictionRepository.count() + "条)");
        System.out.println("==============================================");
    }

    private void createFarmland(String code, String name, Double area, String location, Double longitude, Double latitude, String soilType, Double soilPh, String ownerName, String ownerPhone, String status) {
        Farmland farmland = new Farmland();
        farmland.setFarmlandCode(code);
        farmland.setFarmlandName(name);
        farmland.setArea(BigDecimal.valueOf(area));
        farmland.setLocation(location);
        farmland.setLongitude(String.valueOf(longitude));
        farmland.setLatitude(String.valueOf(latitude));
        farmland.setSoilType(soilType);
        farmland.setSoilPh(BigDecimal.valueOf(soilPh));
        farmland.setOwnerName(ownerName);
        farmland.setOwnerPhone(ownerPhone);
        farmland.setStatus(status);
        farmland.setDescription("商丘市" + location + "农田地块");
        farmlandRepository.save(farmland);
    }

    private void createPlantingRecord(Long farmlandId, Long cropTypeId, LocalDate plantDate, LocalDate expectHarvestDate, LocalDate actualHarvestDate, BigDecimal plantArea, BigDecimal seedAmount, String status, String description) {
        PlantingRecord record = new PlantingRecord();
        record.setFarmlandId(farmlandId);
        record.setCropTypeId(cropTypeId);
        record.setPlantDate(plantDate);
        record.setExpectHarvestDate(expectHarvestDate);
        record.setActualHarvestDate(actualHarvestDate);
        record.setPlantArea(plantArea);
        record.setSeedAmount(seedAmount);
        record.setStatus(status);
        record.setDescription(description);
        plantingRecordRepository.save(record);
    }

    private void createIrrigationRecord(Long farmlandId, Long plantingRecordId, LocalDateTime irrigateDate, BigDecimal waterAmount, String irrigateMethod, Integer irrigateDuration, String operator, String description) {
        IrrigationRecord record = new IrrigationRecord();
        record.setFarmlandId(farmlandId);
        record.setPlantingRecordId(plantingRecordId);
        record.setIrrigateDate(irrigateDate);
        record.setWaterAmount(waterAmount);
        record.setIrrigateMethod(irrigateMethod);
        record.setIrrigateDuration(irrigateDuration);
        record.setOperator(operator);
        record.setDescription(description);
        irrigationRecordRepository.save(record);
    }

    private void createFertilizationRecord(Long farmlandId, Long plantingRecordId, String fertilizerName, String fertilizerType, LocalDateTime applyDate, BigDecimal amount, String applyMethod, String operator, String description) {
        FertilizationRecord record = new FertilizationRecord();
        record.setFarmlandId(farmlandId);
        record.setPlantingRecordId(plantingRecordId);
        record.setFertilizerName(fertilizerName);
        record.setFertilizerType(fertilizerType);
        record.setApplyDate(applyDate);
        record.setAmount(amount);
        record.setApplyMethod(applyMethod);
        record.setOperator(operator);
        record.setDescription(description);
        fertilizationRecordRepository.save(record);
    }

    private void createPestMonitor(Long farmlandId, Long plantingRecordId, String pestName, String pestType, String severity, LocalDate foundDate, BigDecimal affectedArea, String imageUrl, String description) {
        PestMonitor monitor = new PestMonitor();
        monitor.setFarmlandId(farmlandId);
        monitor.setPlantingRecordId(plantingRecordId);
        monitor.setPestName(pestName);
        monitor.setPestType(pestType);
        monitor.setSeverity(severity);
        monitor.setFoundDate(foundDate);
        monitor.setAffectedArea(affectedArea);
        monitor.setImageUrl(imageUrl);
        monitor.setDescription(description);
        pestMonitorRepository.save(monitor);
    }

    private void createPestControl(Long pestMonitorId, String controlMethod, String pesticideName, BigDecimal dosage, LocalDateTime controlDate, String effect, String operator, BigDecimal cost, String description) {
        PestControl control = new PestControl();
        control.setPestMonitorId(pestMonitorId);
        control.setControlMethod(controlMethod);
        control.setPesticideName(pesticideName);
        control.setDosage(dosage);
        control.setControlDate(controlDate);
        control.setEffect(effect);
        control.setOperator(operator);
        control.setCost(cost);
        control.setDescription(description);
        pestControlRepository.save(control);
    }

    private void createYieldPrediction(Long farmlandId, Long plantingRecordId, Long cropTypeId, Integer predictYear, BigDecimal predictYield, BigDecimal actualYield, String predictModel, BigDecimal confidence, String factors) {
        YieldPrediction prediction = new YieldPrediction();
        prediction.setFarmlandId(farmlandId);
        prediction.setPlantingRecordId(plantingRecordId);
        prediction.setCropTypeId(cropTypeId);
        prediction.setPredictYear(predictYear);
        prediction.setPredictYield(predictYield);
        prediction.setActualYield(actualYield);
        prediction.setPredictModel(predictModel);
        prediction.setConfidence(confidence);
        prediction.setFactors(factors);
        yieldPredictionRepository.save(prediction);
    }
}