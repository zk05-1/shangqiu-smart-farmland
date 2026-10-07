<template>
  <div class="weather-container">
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
      <el-col :span="24"><el-card shadow="hover"><template #header><span>温度与湿度趋势</span></template><div ref="chartRef" style="height: 300px"></div></el-card></el-col>
    </el-row>

    <el-card class="table-card" shadow="never" style="margin-top: 20px">
      <div class="toolbar"><el-button type="primary" icon="Plus" @click="handleAdd">新增数据</el-button></div>
      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="farmlandId" label="农田ID" width="100" />
        <el-table-column prop="recordDate" label="记录日期" width="120" />
        <el-table-column prop="temperatureMax" label="最高温度(℃)" width="120" align="right" />
        <el-table-column prop="temperatureMin" label="最低温度(℃)" width="120" align="right" />
        <el-table-column prop="humidity" label="湿度(%)" width="100" align="right" />
        <el-table-column prop="rainfall" label="降雨量(mm)" width="100" align="right" />
        <el-table-column prop="weatherType" label="天气类型" width="100" align="center">
          <template #default="{ row }"><el-tag>{{ row.weatherType }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="disasterWarning" label="灾害预警" width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button link type="primary" icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.size" :page-sizes="[10, 20, 50]" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="formData" label-width="130px">
        <el-form-item label="农田ID" prop="farmlandId"><el-input-number v-model="formData.farmlandId" :min="1" style="width: 100%" /></el-form-item>
        <el-form-item label="记录日期" prop="recordDate"><el-date-picker v-model="formData.recordDate" type="date" style="width: 100%" /></el-form-item>
        <el-form-item label="最高温度(℃)"><el-input-number v-model="formData.temperatureMax" :precision="1" style="width: 100%" /></el-form-item>
        <el-form-item label="最低温度(℃)"><el-input-number v-model="formData.temperatureMin" :precision="1" style="width: 100%" /></el-form-item>
        <el-form-item label="湿度(%)"><el-input-number v-model="formData.humidity" :precision="1" :min="0" :max="100" style="width: 100%" /></el-form-item>
        <el-form-item label="降雨量(mm)"><el-input-number v-model="formData.rainfall" :precision="1" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="风速(m/s)"><el-input-number v-model="formData.windSpeed" :precision="1" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="风向"><el-input v-model="formData.windDirection" /></el-form-item>
        <el-form-item label="天气类型"><el-input v-model="formData.weatherType" /></el-form-item>
        <el-form-item label="灾害预警"><el-input v-model="formData.disasterWarning" /></el-form-item>
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
const chartRef = ref(null)
const formData = reactive({ id: null, farmlandId: null, recordDate: '', temperatureMax: null, temperatureMin: null, humidity: null, rainfall: null, windSpeed: null, windDirection: '', weatherType: '', disasterWarning: '' })

const chartInstance = ref(null)

const loadData = async () => {
  loading.value = true
  try {
    const response = await request.get('/weather/list', { params: { page: pagination.page, size: pagination.size } })
    tableData.value = response.data.records || response.data.list || []
    pagination.total = response.data.total
    nextTick(() => { initChart() })
  } catch (error) { ElMessage.error('加载数据失败') } finally { loading.value = false }
}

const initChart = () => {
  if (!chartRef.value) return
  
  if (chartInstance.value) {
    chartInstance.value.dispose()
  }
  
  chartInstance.value = echarts.init(chartRef.value)
  
  const dates = []
  const tempMaxData = []
  const tempMinData = []
  const humidityData = []
  
  if (tableData.value && tableData.value.length > 0) {
    const sortedData = [...tableData.value].sort((a, b) => new Date(a.recordDate) - new Date(b.recordDate))
    sortedData.forEach(item => {
      dates.push(item.recordDate)
      tempMaxData.push(item.temperatureMax)
      tempMinData.push(item.temperatureMin)
      humidityData.push(item.humidity)
    })
  } else {
    dates.push('')
    tempMaxData.push(0)
    tempMinData.push(0)
    humidityData.push(0)
  }

  chartInstance.value.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['最高温度', '最低温度', '湿度'] },
    xAxis: { type: 'category', data: dates },
    yAxis: [{ type: 'value', name: '温度(℃)' }, { type: 'value', name: '湿度(%)', max: 100 }],
    series: [
      { name: '最高温度', data: tempMaxData, type: 'line', smooth: true, itemStyle: { color: '#e74c3c' } },
      { name: '最低温度', data: tempMinData, type: 'line', smooth: true, itemStyle: { color: '#3498db' } },
      { name: '湿度', data: humidityData, type: 'line', smooth: true, yAxisIndex: 1, itemStyle: { color: '#2ecc71' } }
    ]
  })
}

const handleSearch = () => { pagination.page = 1; loadData() }
const handleReset = () => { searchForm.farmlandId = null; loadData() }
const handleAdd = () => { dialogTitle.value = '新增数据'; isEdit.value = false; Object.assign(formData, { id: null, farmlandId: null, recordDate: '', temperatureMax: null, temperatureMin: null, humidity: null, rainfall: null, windSpeed: null, windDirection: '', weatherType: '', disasterWarning: '' }); dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑数据'; isEdit.value = true; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = async (row) => { try { await request.delete(`/weather/${row.id}`); ElMessage.success('删除成功'); loadData() } catch (error) { ElMessage.error('删除失败') } }
const handleSubmit = async () => { try { submitLoading.value = true; if (isEdit.value) { await request.put(`/weather/${formData.id}`, formData); ElMessage.success('编辑成功') } else { await request.post('/weather', formData); ElMessage.success('新增成功') } dialogVisible.value = false; loadData() } catch (error) { ElMessage.error('提交失败') } finally { submitLoading.value = false } }
const handleSizeChange = () => loadData()
const handlePageChange = () => loadData()

onMounted(() => { loadData() })
</script>

<style scoped>
.weather-container { padding: 20px; }
.search-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
</style>