<template>
  <div class="irrigation-container">
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="农田"><el-select v-model="searchForm.farmlandId" placeholder="请选择农田" clearable style="width: 200px"><el-option v-for="f in farmlandList" :key="f.id" :label="f.farmlandName" :value="f.id" /></el-select></el-form-item>
        <el-form-item label="灌溉方式"><el-select v-model="searchForm.method" placeholder="请选择方式" clearable style="width: 120px"><el-option label="滴灌" value="DRIP" /><el-option label="喷灌" value="SPRINKLER" /><el-option label="漫灌" value="FLOOD" /></el-select></el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card" shadow="never">
      <div class="toolbar">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增记录</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="farmlandCode" label="农田编码" width="120" />
        <el-table-column prop="farmlandName" label="农田名称" width="200" />
        <el-table-column prop="irrigateDate" label="灌溉时间" width="180" />
        <el-table-column prop="waterAmount" label="用水量(m³)" width="120" align="right" />
        <el-table-column prop="irrigateMethod" label="灌溉方式" width="100" align="center">
          <template #default="{ row }"><el-tag :type="getMethodType(row.irrigateMethod)">{{ getMethodText(row.irrigateMethod) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="irrigateDuration" label="灌溉时长(分钟)" width="120" align="right" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
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
      <el-form ref="formRef" :model="formData" label-width="120px">
        <el-form-item label="农田" prop="farmlandId">
          <el-select v-model="formData.farmlandId" placeholder="请选择农田" style="width: 100%">
            <el-option v-for="f in farmlandList" :key="f.id" :label="f.farmlandName" :value="f.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="灌溉时间" prop="irrigateDate"><el-date-picker v-model="formData.irrigateDate" type="datetime" placeholder="选择时间" style="width: 100%" /></el-form-item>
        <el-form-item label="用水量(m³)" prop="waterAmount"><el-input-number v-model="formData.waterAmount" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="灌溉方式"><el-select v-model="formData.irrigateMethod" placeholder="请选择" style="width: 100%"><el-option label="滴灌" value="DRIP" /><el-option label="喷灌" value="SPRINKLER" /><el-option label="漫灌" value="FLOOD" /></el-select></el-form-item>
        <el-form-item label="灌溉时长"><el-input-number v-model="formData.irrigateDuration" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="操作人"><el-input v-model="formData.operator" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="formData.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const searchForm = reactive({ farmlandId: null, method: null })
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增记录')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const farmlandList = ref([])
const formData = reactive({ id: null, farmlandId: null, plantingRecordId: null, irrigateDate: '', waterAmount: null, irrigateMethod: '', irrigateDuration: null, operator: '', description: '' })

const loadData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, size: pagination.size }
    if (searchForm.farmlandId) params.farmlandId = searchForm.farmlandId
    if (searchForm.method) params.method = searchForm.method
    const response = await request.get('/irrigation/list', { params })
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
const handleReset = () => { searchForm.farmlandId = null; searchForm.method = null; loadData() }
const handleAdd = () => { dialogTitle.value = '新增记录'; isEdit.value = false; Object.assign(formData, { id: null, farmlandId: null, plantingRecordId: null, irrigateDate: '', waterAmount: null, irrigateMethod: '', irrigateDuration: null, operator: '', description: '' }); dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑记录'; isEdit.value = true; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = async (row) => { try { await request.delete(`/irrigation/${row.id}`); ElMessage.success('删除成功'); loadData() } catch (error) { ElMessage.error('删除失败') } }
const handleSubmit = async () => { try { submitLoading.value = true; if (isEdit.value) { await request.put(`/irrigation/${formData.id}`, formData); ElMessage.success('编辑成功') } else { await request.post('/irrigation', formData); ElMessage.success('新增成功') } dialogVisible.value = false; loadData() } catch (error) { ElMessage.error('提交失败') } finally { submitLoading.value = false } }
const handleSizeChange = () => loadData()
const handlePageChange = () => loadData()
const getMethodType = (method) => ({ DRIP: 'success', SPRINKLER: 'primary', FLOOD: 'warning' }[method] || 'info')
const getMethodText = (method) => ({ DRIP: '滴灌', SPRINKLER: '喷灌', FLOOD: '漫灌' }[method] || method)

onMounted(() => { loadFarmlands(); loadData() })
</script>

<style scoped>
.irrigation-container { padding: 20px; }
.search-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
</style>