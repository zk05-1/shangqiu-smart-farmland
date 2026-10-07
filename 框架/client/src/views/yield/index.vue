<template>
  <div class="yield-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>产量预测趋势</span></template>
          <div ref="yieldChartRef" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>预测模型分布</span></template>
          <div ref="modelChartRef" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top: 20px">
      <template #header><span>产量预测列表</span></template>
      <div class="toolbar"><el-button type="primary" icon="Plus" @click="handleAdd">新增预测</el-button></div>
      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="farmlandCode" label="农田编码" width="120" />
        <el-table-column prop="farmlandName" label="农田名称" width="200" />
        <el-table-column prop="cropTypeName" label="作物类型" width="120" />
        <el-table-column prop="predictYear" label="预测年份" width="120" />
        <el-table-column prop="predictYield" label="预测产量(kg/亩)" width="140" align="right" />
        <el-table-column prop="actualYield" label="实际产量(kg/亩)" width="140" align="right">
          <template #default="{ row }">{{ row.actualYield || '-' }}</template>
        </el-table-column>
        <el-table-column prop="predictModel" label="预测模型" width="180" />
        <el-table-column prop="confidence" label="置信度" width="100" align="center">
          <template #default="{ row }"><el-tag :type="getConfidenceType(row.confidence)">{{ (row.confidence * 100).toFixed(0) }}%</el-tag></template>
        </el-table-column>
        <el-table-column prop="factors" label="影响因素" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" align="center">
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
        <el-form-item label="农田" prop="farmlandId">
          <el-select v-model="formData.farmlandId" placeholder="请选择农田" style="width: 100%">
            <el-option v-for="f in farmlandList" :key="f.id" :label="f.farmlandName" :value="f.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="作物类型" prop="cropTypeId">
          <el-select v-model="formData.cropTypeId" placeholder="请选择作物类型" style="width: 100%">
            <el-option label="小麦" :value="1" />
            <el-option label="蔬菜" :value="2" />
            <el-option label="玉米" :value="3" />
            <el-option label="经济作物" :value="4" />
            <el-option label="花生" :value="5" />
            <el-option label="辣椒" :value="6" />
            <el-option label="水果" :value="7" />
            <el-option label="大豆" :value="8" />
            <el-option label="莲藕" :value="9" />
            <el-option label="大蒜" :value="10" />
            <el-option label="花卉" :value="11" />
            <el-option label="西瓜" :value="12" />
            <el-option label="烟叶" :value="13" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测年份" prop="predictYear"><el-input-number v-model="formData.predictYear" :min="2000" :max="2100" style="width: 100%" /></el-form-item>
        <el-form-item label="预测产量(kg/亩)" prop="predictYield"><el-input-number v-model="formData.predictYield" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="实际产量(kg/亩)"><el-input-number v-model="formData.actualYield" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="预测模型"><el-input v-model="formData.predictModel" /></el-form-item>
        <el-form-item label="置信度"><el-input-number v-model="formData.confidence" :precision="2" :min="0" :max="1" style="width: 100%" /></el-form-item>
        <el-form-item label="影响因素"><el-input v-model="formData.factors" type="textarea" :rows="3" /></el-form-item>
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

const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增预测')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const farmlandList = ref([])
const yieldChartRef = ref(null)
const modelChartRef = ref(null)
const yieldChartInstance = ref(null)
const modelChartInstance = ref(null)
const formData = reactive({ id: null, farmlandId: null, plantingRecordId: null, cropTypeId: null, predictYear: new Date().getFullYear(), predictYield: null, actualYield: null, predictModel: '', confidence: null, factors: '' })

const cropTypeMap = { 1: '小麦', 2: '蔬菜', 3: '玉米', 4: '经济作物', 5: '花生', 6: '辣椒', 7: '水果', 8: '大豆', 9: '莲藕', 10: '大蒜', 11: '花卉', 12: '西瓜', 13: '烟叶' }

const loadData = async () => {
  loading.value = true
  try {
    const response = await request.get('/yield-prediction/list', { params: { page: pagination.page, size: pagination.size } })
    const records = response.data.list || response.data.records || []
    tableData.value = records.map(r => ({ ...r, cropTypeName: cropTypeMap[r.cropTypeId] || r.cropTypeId }))
    pagination.total = response.data.total
    nextTick(() => { initCharts() })
  } catch (error) { ElMessage.error('加载数据失败') } finally { loading.value = false }
}

const initCharts = () => {
  initYieldChart()
  initModelChart()
}

const initYieldChart = () => {
  if (!yieldChartRef.value) return
  if (yieldChartInstance.value) yieldChartInstance.value.dispose()
  yieldChartInstance.value = echarts.init(yieldChartRef.value)

  const farmlandNames = []
  const predictData = []
  const actualData = []

  tableData.value.forEach(item => {
    farmlandNames.push(item.farmlandName?.substring(0, 8) || '')
    predictData.push(item.predictYield)
    actualData.push(item.actualYield || null)
  })

  yieldChartInstance.value.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['预测产量', '实际产量'] },
    xAxis: { type: 'category', data: farmlandNames, axisLabel: { rotate: 30 } },
    yAxis: { type: 'value', name: '产量(kg/亩)' },
    series: [
      { name: '预测产量', data: predictData, type: 'bar', itemStyle: { color: '#3498db' } },
      { name: '实际产量', data: actualData, type: 'bar', itemStyle: { color: '#2ecc71' } }
    ]
  })
}

const initModelChart = () => {
  if (!modelChartRef.value) return
  if (modelChartInstance.value) modelChartInstance.value.dispose()
  modelChartInstance.value = echarts.init(modelChartRef.value)

  const modelCounts = {}
  tableData.value.forEach(item => {
    modelCounts[item.predictModel] = (modelCounts[item.predictModel] || 0) + 1
  })

  const modelNames = Object.keys(modelCounts)
  const modelValues = Object.values(modelCounts)

  modelChartInstance.value.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{ name: '预测模型', type: 'pie', radius: ['40%', '70%'], avoidLabelOverlap: false, label: { show: false, position: 'center' }, emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } }, labelLine: { show: false }, data: modelNames.map((name, idx) => ({ value: modelValues[idx], name })) }]
  })
}

const loadFarmlands = async () => {
  try {
    const response = await request.get('/farmland/list', { params: { page: 1, size: 100 } })
    farmlandList.value = response.data.list || response.data.records || []
  } catch (error) { console.error('加载农田列表失败:', error) }
}

const handleAdd = () => { dialogTitle.value = '新增预测'; isEdit.value = false; Object.assign(formData, { id: null, farmlandId: null, plantingRecordId: null, cropTypeId: null, predictYear: new Date().getFullYear(), predictYield: null, actualYield: null, predictModel: '', confidence: null, factors: '' }); dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑预测'; isEdit.value = true; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = async (row) => { try { await request.delete(`/yield-prediction/${row.id}`); ElMessage.success('删除成功'); loadData() } catch (error) { ElMessage.error('删除失败') } }
const handleSubmit = async () => { try { submitLoading.value = true; if (isEdit.value) { await request.put(`/yield-prediction/${formData.id}`, formData); ElMessage.success('编辑成功') } else { await request.post('/yield-prediction', formData); ElMessage.success('新增成功') } dialogVisible.value = false; loadData() } catch (error) { ElMessage.error('提交失败') } finally { submitLoading.value = false } }
const handleSizeChange = () => loadData()
const handlePageChange = () => loadData()
const getConfidenceType = (confidence) => { const c = confidence || 0; return c >= 0.9 ? 'success' : c >= 0.8 ? 'primary' : c >= 0.7 ? 'warning' : 'danger' }

onMounted(() => { loadFarmlands(); loadData() })
</script>

<style scoped>
.yield-container { padding: 20px; }
.toolbar { margin-bottom: 20px; }
</style>