<template>
  <div class="sensor-container">
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="农田ID"><el-input-number v-model="searchForm.farmlandId" :min="1" style="width: 150px" /></el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="12"><el-card shadow="hover"><template #header><span>土壤温湿度趋势</span></template><div ref="soilChartRef" style="height: 300px"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="hover"><template #header><span>空气温湿度趋势</span></template><div ref="airChartRef" style="height: 300px"></div></el-card></el-col>
    </el-row>

    <el-card class="table-card" shadow="never" style="margin-top: 20px">
      <div class="toolbar"><el-button type="primary" icon="Plus" @click="handleAdd">新增数据</el-button></div>
      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="farmlandId" label="农田ID" width="100" />
        <el-table-column prop="createdTime" label="采集时间" width="180" />
        <el-table-column prop="soilTemperature" label="土壤温度(℃)" width="120" align="right" />
        <el-table-column prop="soilMoisture" label="土壤湿度(%)" width="120" align="right" />
        <el-table-column prop="airTemperature" label="空气温度(℃)" width="120" align="right" />
        <el-table-column prop="airHumidity" label="空气湿度(%)" width="120" align="right" />
        <el-table-column prop="lightIntensity" label="光照强度(lx)" width="130" align="right" />
        <el-table-column prop="soilPh" label="土壤pH" width="100" align="right" />
        <el-table-column prop="nitrogen" label="氮含量" width="100" align="right" />
        <el-table-column prop="phosphorus" label="磷含量" width="100" align="right" />
        <el-table-column prop="potassium" label="钾含量" width="100" align="right" />
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button link type="primary" icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.size" :page-sizes="[10, 20, 50]" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form ref="formRef" :model="formData" label-width="130px">
        <el-form-item label="农田ID" prop="farmlandId"><el-input-number v-model="formData.farmlandId" :min="1" style="width: 100%" /></el-form-item>
        <el-form-item label="土壤温度(℃)"><el-input-number v-model="formData.soilTemperature" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="土壤湿度(%)"><el-input-number v-model="formData.soilMoisture" :precision="2" :min="0" :max="100" style="width: 100%" /></el-form-item>
        <el-form-item label="空气温度(℃)"><el-input-number v-model="formData.airTemperature" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="空气湿度(%)"><el-input-number v-model="formData.airHumidity" :precision="2" :min="0" :max="100" style="width: 100%" /></el-form-item>
        <el-form-item label="光照强度(lx)"><el-input-number v-model="formData.lightIntensity" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="土壤pH"><el-input-number v-model="formData.soilPh" :precision="2" :min="0" :max="14" style="width: 100%" /></el-form-item>
        <el-form-item label="氮含量"><el-input-number v-model="formData.nitrogen" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="磷含量"><el-input-number v-model="formData.phosphorus" :precision="2" style="width: 100%" /></el-form-item>
        <el-form-item label="钾含量"><el-input-number v-model="formData.potassium" :precision="2" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import request from '@/utils/request'

const searchForm = reactive({ farmlandId: null })
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增数据')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const soilChartRef = ref(null)
const airChartRef = ref(null)
const soilChartInstance = ref(null)
const airChartInstance = ref(null)
const formData = reactive({ id: null, farmlandId: null, soilTemperature: null, soilMoisture: null, airTemperature: null, airHumidity: null, lightIntensity: null, soilPh: null, nitrogen: null, phosphorus: null, potassium: null })

const loadData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, size: pagination.size }
    if (searchForm.farmlandId) {
      const response = await request.get(`/sensor/farmland/${searchForm.farmlandId}`, { params })
      tableData.value = response.data.records || response.data.list || []
      pagination.total = response.data.total
    } else {
      const response = await request.get('/sensor/list', { params })
      tableData.value = response.data.records || response.data.list || []
      pagination.total = response.data.total
    }
    nextTick(() => { initCharts() })
  } catch (error) { ElMessage.error('加载数据失败') } finally { loading.value = false }
}

const initCharts = () => {
  initSoilChart()
  initAirChart()
}

const initSoilChart = () => {
  if (!soilChartRef.value) return
  if (soilChartInstance.value) soilChartInstance.value.dispose()
  soilChartInstance.value = echarts.init(soilChartRef.value)

  const dates = []
  const soilTempData = []
  const soilMoistData = []

  if (tableData.value && tableData.value.length > 0) {
    const sortedData = [...tableData.value].sort((a, b) => new Date(a.createdTime) - new Date(b.createdTime))
    sortedData.forEach(item => {
      dates.push(item.createdTime?.substring(11, 16) || '')
      soilTempData.push(item.soilTemperature)
      soilMoistData.push(item.soilMoisture)
    })
  }

  soilChartInstance.value.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['土壤温度', '土壤湿度'] },
    xAxis: { type: 'category', data: dates },
    yAxis: [{ type: 'value', name: '温度(℃)' }, { type: 'value', name: '湿度(%)', max: 100 }],
    series: [
      { name: '土壤温度', data: soilTempData, type: 'line', smooth: true, itemStyle: { color: '#e74c3c' } },
      { name: '土壤湿度', data: soilMoistData, type: 'line', smooth: true, yAxisIndex: 1, itemStyle: { color: '#3498db' } }
    ]
  })
}

const initAirChart = () => {
  if (!airChartRef.value) return
  if (airChartInstance.value) airChartInstance.value.dispose()
  airChartInstance.value = echarts.init(airChartRef.value)

  const dates = []
  const airTempData = []
  const airMoistData = []

  if (tableData.value && tableData.value.length > 0) {
    const sortedData = [...tableData.value].sort((a, b) => new Date(a.createdTime) - new Date(b.createdTime))
    sortedData.forEach(item => {
      dates.push(item.createdTime?.substring(11, 16) || '')
      airTempData.push(item.airTemperature)
      airMoistData.push(item.airHumidity)
    })
  }

  airChartInstance.value.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['空气温度', '空气湿度'] },
    xAxis: { type: 'category', data: dates },
    yAxis: [{ type: 'value', name: '温度(℃)' }, { type: 'value', name: '湿度(%)', max: 100 }],
    series: [
      { name: '空气温度', data: airTempData, type: 'line', smooth: true, itemStyle: { color: '#f39c12' } },
      { name: '空气湿度', data: airMoistData, type: 'line', smooth: true, yAxisIndex: 1, itemStyle: { color: '#2ecc71' } }
    ]
  })
}

const handleSearch = () => { pagination.page = 1; loadData() }
const handleReset = () => { searchForm.farmlandId = null; loadData() }
const handleAdd = () => { dialogTitle.value = '新增数据'; isEdit.value = false; Object.assign(formData, { id: null, farmlandId: null, soilTemperature: null, soilMoisture: null, airTemperature: null, airHumidity: null, lightIntensity: null, soilPh: null, nitrogen: null, phosphorus: null, potassium: null }); dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑数据'; isEdit.value = true; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = async (row) => { try { await request.delete(`/sensor/${row.id}`); ElMessage.success('删除成功'); loadData() } catch (error) { ElMessage.error('删除失败') } }
const handleSubmit = async () => { try { submitLoading.value = true; if (isEdit.value) { await request.put(`/sensor/${formData.id}`, formData); ElMessage.success('编辑成功') } else { await request.post('/sensor', formData); ElMessage.success('新增成功') } dialogVisible.value = false; loadData() } catch (error) { ElMessage.error('提交失败') } finally { submitLoading.value = false } }
const handleSizeChange = () => loadData()
const handlePageChange = () => loadData()

onMounted(() => { loadData() })
</script>

<style scoped>
.sensor-container { padding: 20px; }
.search-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
</style>