<template>
  <div class="fertilization-container">
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="农田"><el-select v-model="searchForm.farmlandId" placeholder="请选择农田" clearable style="width: 200px"><el-option v-for="f in farmlandList" :key="f.id" :label="f.farmlandName" :value="f.id" /></el-select></el-form-item>
        <el-form-item label="肥料类型"><el-select v-model="searchForm.type" placeholder="请选择类型" clearable style="width: 120px"><el-option label="有机肥" value="ORGANIC" /><el-option label="化肥" value="CHEMICAL" /><el-option label="复合肥" value="COMPOUND" /></el-select></el-form-item>
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
        <el-table-column prop="fertilizerName" label="肥料名称" width="150" />
        <el-table-column prop="fertilizerType" label="肥料类型" width="100" align="center">
          <template #default="{ row }"><el-tag :type="getFertilizerTypeColor(row.fertilizerType)">{{ getFertilizerTypeText(row.fertilizerType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="applyDate" label="施肥时间" width="180" />
        <el-table-column prop="amount" label="用量(kg)" width="120" align="right" />
        <el-table-column prop="applyMethod" label="施肥方法" width="120" />
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
        <el-form-item label="肥料名称" prop="fertilizerName"><el-input v-model="formData.fertilizerName" /></el-form-item>
        <el-form-item label="肥料类型" prop="fertilizerType"><el-select v-model="formData.fertilizerType" style="width: 100%"><el-option label="有机肥" value="ORGANIC" /><el-option label="化肥" value="CHEMICAL" /><el-option label="复合肥" value="COMPOUND" /></el-select></el-form-item>
        <el-form-item label="施肥时间" prop="applyDate"><el-date-picker v-model="formData.applyDate" type="datetime" placeholder="选择时间" style="width: 100%" /></el-form-item>
        <el-form-item label="用量(kg)" prop="amount"><el-input-number v-model="formData.amount" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="施肥方法"><el-input v-model="formData.applyMethod" /></el-form-item>
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

const searchForm = reactive({ farmlandId: null, type: null })
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增记录')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const farmlandList = ref([])
const formData = reactive({ id: null, farmlandId: null, plantingRecordId: null, fertilizerName: '', fertilizerType: '', applyDate: '', amount: null, applyMethod: '', operator: '', description: '' })

const loadData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, size: pagination.size }
    if (searchForm.farmlandId) params.farmlandId = searchForm.farmlandId
    if (searchForm.type) params.type = searchForm.type
    const response = await request.get('/fertilization/list', { params })
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
const handleAdd = () => { dialogTitle.value = '新增记录'; isEdit.value = false; Object.assign(formData, { id: null, farmlandId: null, plantingRecordId: null, fertilizerName: '', fertilizerType: '', applyDate: '', amount: null, applyMethod: '', operator: '', description: '' }); dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑记录'; isEdit.value = true; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = async (row) => { try { await request.delete(`/fertilization/${row.id}`); ElMessage.success('删除成功'); loadData() } catch (error) { ElMessage.error('删除失败') } }
const handleSubmit = async () => { try { submitLoading.value = true; if (isEdit.value) { await request.put(`/fertilization/${formData.id}`, formData); ElMessage.success('编辑成功') } else { await request.post('/fertilization', formData); ElMessage.success('新增成功') } dialogVisible.value = false; loadData() } catch (error) { ElMessage.error('提交失败') } finally { submitLoading.value = false } }
const handleSizeChange = () => loadData()
const handlePageChange = () => loadData()
const getFertilizerTypeColor = (type) => ({ ORGANIC: 'success', CHEMICAL: 'warning', COMPOUND: 'primary' }[type] || 'info')
const getFertilizerTypeText = (type) => ({ ORGANIC: '有机肥', CHEMICAL: '化肥', COMPOUND: '复合肥' }[type] || type)

onMounted(() => { loadFarmlands(); loadData() })
</script>

<style scoped>
.fertilization-container { padding: 20px; }
.search-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
</style>