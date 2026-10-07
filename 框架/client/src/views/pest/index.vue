<template>
  <div class="pest-container">
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="农田"><el-select v-model="searchForm.farmlandId" placeholder="请选择农田" clearable style="width: 200px"><el-option v-for="f in farmlandList" :key="f.id" :label="f.farmlandName" :value="f.id" /></el-select></el-form-item>
        <el-form-item label="类型"><el-select v-model="searchForm.type" placeholder="请选择类型" clearable style="width: 100px"><el-option label="虫害" value="INSECT" /><el-option label="病害" value="DISEASE" /><el-option label="草害" value="WEED" /></el-select></el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="never">
      <div class="toolbar">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增监测</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="farmlandCode" label="农田编码" width="120" />
        <el-table-column prop="farmlandName" label="农田名称" width="200" />
        <el-table-column prop="pestName" label="病虫害名称" width="150" />
        <el-table-column prop="pestType" label="类型" width="100" align="center">
          <template #default="{ row }"><el-tag :type="getPestTypeColor(row.pestType)">{{ getPestTypeText(row.pestType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="severity" label="严重程度" width="100" align="center">
          <template #default="{ row }"><el-tag :type="getSeverityColor(row.severity)">{{ getSeverityText(row.severity) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="foundDate" label="发现日期" width="120" />
        <el-table-column prop="affectedArea" label="受影响面积(亩)" width="120" align="right" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button link type="primary" icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="success" icon="Check" @click="handleControl(row)">防治</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.size" :page-sizes="[10, 20, 50]" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handlePageChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="formData" label-width="120px">
        <el-form-item label="农田" prop="farmlandId">
          <el-select v-model="formData.farmlandId" placeholder="请选择农田" style="width: 100%">
            <el-option v-for="f in farmlandList" :key="f.id" :label="f.farmlandName" :value="f.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="病虫害名称" prop="pestName"><el-input v-model="formData.pestName" /></el-form-item>
        <el-form-item label="类型" prop="pestType"><el-select v-model="formData.pestType" style="width: 100%"><el-option label="虫害" value="INSECT" /><el-option label="病害" value="DISEASE" /><el-option label="草害" value="WEED" /></el-select></el-form-item>
        <el-form-item label="严重程度"><el-select v-model="formData.severity" style="width: 100%"><el-option label="轻度" value="LIGHT" /><el-option label="中度" value="MODERATE" /><el-option label="重度" value="SEVERE" /></el-select></el-form-item>
        <el-form-item label="发现日期" prop="foundDate"><el-date-picker v-model="formData.foundDate" type="date" style="width: 100%" /></el-form-item>
        <el-form-item label="受影响面积"><el-input-number v-model="formData.affectedArea" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="图片URL"><el-input v-model="formData.imageUrl" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="formData.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="controlDialogVisible" title="病虫害防治" width="600px">
      <el-form ref="controlFormRef" :model="controlForm" label-width="120px">
        <el-form-item label="防治方法"><el-input v-model="controlForm.controlMethod" /></el-form-item>
        <el-form-item label="农药名称"><el-input v-model="controlForm.pesticideName" /></el-form-item>
        <el-form-item label="用量"><el-input-number v-model="controlForm.dosage" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="防治时间"><el-date-picker v-model="controlForm.controlDate" type="datetime" style="width: 100%" /></el-form-item>
        <el-form-item label="效果"><el-select v-model="controlForm.effect" style="width: 100%"><el-option label="极好" value="EXCELLENT" /><el-option label="好" value="GOOD" /><el-option label="一般" value="FAIR" /><el-option label="差" value="POOR" /></el-select></el-form-item>
        <el-form-item label="操作人"><el-input v-model="controlForm.operator" /></el-form-item>
        <el-form-item label="防治成本"><el-input-number v-model="controlForm.cost" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="controlForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="controlDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="controlSubmitLoading" @click="handleControlSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const searchForm = reactive({ farmlandId: null, type: null })
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增监测')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const farmlandList = ref([])
const formData = reactive({ id: null, farmlandId: null, plantingRecordId: null, pestName: '', pestType: '', severity: '', foundDate: '', affectedArea: null, imageUrl: '', description: '' })

const controlDialogVisible = ref(false)
const controlSubmitLoading = ref(false)
const controlFormRef = ref(null)
const controlForm = reactive({ pestMonitorId: null, controlMethod: '', pesticideName: '', dosage: null, controlDate: '', effect: '', operator: '', cost: null, description: '' })

const loadData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, size: pagination.size }
    if (searchForm.farmlandId) params.farmlandId = searchForm.farmlandId
    if (searchForm.type) params.type = searchForm.type
    const response = await request.get('/pest-monitor/list', { params })
    tableData.value = response.data.list || response.data.records || []
    pagination.total = response.data.total
  } catch (error) { ElMessage.error('加载数据失败') } finally { loading.value = false }
}

const loadFarmlands = async () => {
  try {
    const response = await request.get('/farmland/list', { params: { page: 1, size: 100 } })
    farmlandList.value = response.data.list || response.data.records || []
  } catch (error) { console.error('加载农田列表失败:', error) }
}

const handleSearch = () => { pagination.page = 1; loadData() }
const handleReset = () => { searchForm.farmlandId = null; searchForm.type = null; loadData() }
const handleAdd = () => { dialogTitle.value = '新增监测'; isEdit.value = false; Object.assign(formData, { id: null, farmlandId: null, plantingRecordId: null, pestName: '', pestType: '', severity: '', foundDate: '', affectedArea: null, imageUrl: '', description: '' }); dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑监测'; isEdit.value = true; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = async (row) => { try { await request.delete(`/pest-monitor/${row.id}`); ElMessage.success('删除成功'); loadData() } catch (error) { ElMessage.error('删除失败') } }
const handleControl = (row) => { Object.assign(controlForm, { pestMonitorId: row.id, controlMethod: '', pesticideName: '', dosage: null, controlDate: '', effect: '', operator: '', cost: null, description: '' }); controlDialogVisible.value = true }
const handleSubmit = async () => { try { submitLoading.value = true; if (isEdit.value) { await request.put(`/pest-monitor/${formData.id}`, formData); ElMessage.success('编辑成功') } else { await request.post('/pest-monitor', formData); ElMessage.success('新增成功') } dialogVisible.value = false; loadData() } catch (error) { ElMessage.error('提交失败') } finally { submitLoading.value = false } }
const handleControlSubmit = async () => { try { controlSubmitLoading.value = true; await request.post('/pest-control', controlForm); ElMessage.success('防治记录已添加'); controlDialogVisible.value = false } catch (error) { ElMessage.error('提交失败') } finally { controlSubmitLoading.value = false } }
const handleSizeChange = () => loadData()
const handlePageChange = () => loadData()
const getPestTypeColor = (type) => ({ INSECT: 'warning', DISEASE: 'danger', WEED: 'info' }[type] || 'info')
const getPestTypeText = (type) => ({ INSECT: '虫害', DISEASE: '病害', WEED: '草害' }[type] || type)
const getSeverityColor = (severity) => ({ LIGHT: 'success', MODERATE: 'warning', SEVERE: 'danger' }[severity] || 'info')
const getSeverityText = (severity) => ({ LIGHT: '轻度', MODERATE: '中度', SEVERE: '重度' }[severity] || severity)

onMounted(() => { loadFarmlands(); loadData() })
</script>

<style scoped>
.pest-container { padding: 20px; }
.search-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
</style>