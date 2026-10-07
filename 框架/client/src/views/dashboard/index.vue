<template>
  <div class="dashboard-container">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="dashboard-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
              <el-icon :size="40"><Grid /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ farmlandCount }}</div>
              <div class="stat-label">农田总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="dashboard-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
              <el-icon :size="40"><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ plantingCount }}</div>
              <div class="stat-label">种植记录</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="dashboard-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
              <el-icon :size="40"><Umbrella /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ irrigationCount }}</div>
              <div class="stat-label">灌溉记录</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="dashboard-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
              <el-icon :size="40"><TrendCharts /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ yieldPredictionCount }}</div>
              <div class="stat-label">产量预测</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="8">
        <el-card class="chart-card" shadow="hover">
          <div class="chart-title">
            <h3>农田分布</h3>
          </div>
          <div ref="farmlandChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="chart-card" shadow="hover">
          <div class="chart-title">
            <h3>作物种植统计</h3>
          </div>
          <div ref="cropChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="chart-card" shadow="hover">
          <div class="chart-title">
            <h3>气象趋势</h3>
          </div>
          <div ref="weatherChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新动态 -->
    <el-card class="activity-card" shadow="hover">
      <div class="activity-title">
        <h3>最新动态</h3>
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.time"
          placement="top"
        >
          <el-card>
            <h4>{{ activity.title }}</h4>
            <p>{{ activity.content }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Grid, Document, Umbrella, TrendCharts } from '@element-plus/icons-vue'
import { countFarmland } from '@/api/farmland'

// 统计数据
const farmlandCount = ref(0)
const plantingCount = ref(0)
const irrigationCount = ref(0)
const yieldPredictionCount = ref(0)

// 图表引用
const farmlandChart = ref(null)
const cropChart = ref(null)
const weatherChart = ref(null)

// 最新动态
const recentActivities = ref([
  {
    id: 1,
    title: '新增农田地块',
    content: '梁园区新增50亩农田地块，编号SQ-007',
    time: '2024-06-25 10:30'
  },
  {
    id: 2,
    title: '灌溉记录更新',
    content: '柘城县农田完成滴灌作业，用水量120立方米',
    time: '2024-06-24 15:20'
  },
  {
    id: 3,
    title: '病虫害防治',
    content: '民权县棉花田完成棉铃虫防治，效果良好',
    time: '2024-06-23 08:15'
  }
])

/**
 * 加载统计数据
 */
const loadStatistics = async () => {
  try {
    const response = await countFarmland()
    farmlandCount.value = response.data

    // 模拟其他统计数据
    plantingCount.value = 15
    irrigationCount.value = 23
    yieldPredictionCount.value = 8
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

/**
 * 初始化图表
 */
const initCharts = () => {
  nextTick(() => {
    // 农田分布饼图
    const farmlandChartInstance = echarts.init(farmlandChart.value)
    farmlandChartInstance.setOption({
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '50%',
          data: [
            { value: 3, name: '使用中' },
            { value: 1, name: '休耕' },
            { value: 1, name: '闲置' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    })

    // 作物种植柱状图
    const cropChartInstance = echarts.init(cropChart.value)
    cropChartInstance.setOption({
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['冬小麦', '夏玉米', '棉花', '花生', '大豆']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [150, 120, 80, 50, 40],
          type: 'bar',
          smooth: true,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }
      ]
    })

    // 气象趋势折线图
    const weatherChartInstance = echarts.init(weatherChart.value)
    weatherChartInstance.setOption({
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['最高温度', '最低温度', '降雨量']
      },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '最高温度',
          data: [5, 8, 15, 23, 30, 36],
          type: 'line',
          smooth: true
        },
        {
          name: '最低温度',
          data: [-5, 0, 7, 13, 20, 25],
          type: 'line',
          smooth: true
        },
        {
          name: '降雨量',
          data: [2, 5, 8, 12, 25, 35],
          type: 'line',
          smooth: true
        }
      ]
    })
  })
}

onMounted(() => {
  loadStatistics()
  initCharts()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background: #f5f7fa;
  height: 100%;
  overflow-y: auto;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-content {
  flex: 1;
  padding-left: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 300px;
}

.chart-title {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.chart-title h3 {
  font-size: 16px;
  color: #333;
}

.chart-container {
  height: 220px;
  padding: 10px 0;
}

.activity-card {
  margin-bottom: 20px;
}

.activity-title {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.activity-title h3 {
  font-size: 16px;
  color: #333;
}
</style>