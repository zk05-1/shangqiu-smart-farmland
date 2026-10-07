<template>
  <div class="planting-container">
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="农田"><el-select v-model="searchForm.farmlandId" placeholder="请选择农田" clearable style="width: 200px"><el-option v-for="f in farmlandList" :key="f.id" :label="f.farmlandName" :value="f.id" /></el-select></el-form-item>
        <el-form-item label="状态"><el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px"><el-option label="已播种" value="PLANTING" /><el-option label="生长中" value="GROWING" /><el-option label="已收获" value="HARVESTED" /></el-select></el-form-item>
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
        <el-table-column prop="cropTypeName" label="作物类型" width="120" />
        <el-table-column prop="plantDate" label="播种日期" width="120" />
        <el-table-column prop="expectHarvestDate" label="预计收获" width="120" />
        <el-table-column prop="actualHarvestDate" label="实际收获" width="120" />
        <el-table-column prop="plantArea" label="种植面积(亩)" width="120" align="right" />
        <el-table-column prop="seedAmount" label="种子用量(kg)" width="120" align="right" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }"><el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag></template>
        </el-table-column>
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
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
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
        <el-form-item label="播种日期" prop="plantDate"><el-date-picker v-model="formData.plantDate" type="date" placeholder="选择日期" style="width: 100%" /></el-form-item>
        <el-form-item label="预计收获日期"><el-date-picker v-model="formData.expectHarvestDate" type="date" placeholder="选择日期" style="width: 100%" /></el-form-item>
        <el-form-item label="实际收获日期"><el-date-picker v-model="formData.actualHarvestDate" type="date" placeholder="选择日期" style="width: 100%" /></el-form-item>
        <el-form-item label="种植面积" prop="plantArea"><el-input-number v-model="formData.plantArea" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="种子用量(kg)"><el-input-number v-model="formData.seedAmount" :precision="2" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="状态" prop="status"><el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%"><el-option label="已播种" value="PLANTING" /><el-option label="生长中" value="GROWING" /><el-option label="已收获" value="HARVESTED" /></el-select></el-form-item>
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

const searchForm = reactive({ farmlandId: null, status: null })
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, size: 10, total: 0 })
const dialogVisible = ref(false)
const dialogTitle = ref('新增记录')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const farmlandList = ref([])
const formData = reactive({ id: null, farmlandId: null, cropTypeId: null, plantDate: '', expectHarvestDate: '', actualHarvestDate: '', plantArea: null, seedAmount: null, status: 'PLANTING', description: '' })

const formRules = { farmlandId: [{ required: true, message: '请选择农田', trigger: 'change' }], cropTypeId: [{ required: true, message: '请选择作物类型', trigger: 'change' }], plantDate: [{ required: true, message: '请选择播种日期', trigger: 'change' }], plantArea: [{ required: true, message: '请输入种植面积', trigger: 'blur' }], status: [{ required: true, message: '请选择状态', trigger: 'change' }] }

const cropTypeMap = { 1: '小麦', 2: '蔬菜', 3: '玉米', 4: '经济作物', 5: '花生', 6: '辣椒', 7: '水果', 8: '大豆', 9: '莲藕', 10: '大蒜', 11: '花卉', 12: '西瓜', 13: '烟叶' }

const loadData = async () => {
  loading.value = true
  try {
    const params = { page: pagination.page, size: pagination.size }
    if (searchForm.farmlandId) params.farmlandId = searchForm.farmlandId
    if (searchForm.status) params.status = searchForm.status
    const response = await request.get('/planting/list', { params })
    const records = response.data.list || response.data.records || []
    tableData.value = records.map(r => ({ ...r, cropTypeName: cropTypeMap[r.cropTypeId] || r.cropTypeId }))
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
const handleReset = () => { searchForm.farmlandId = null; searchForm.status = null; loadData() }
const handleAdd = () => { dialogTitle.value = '新增记录'; isEdit.value = false; Object.assign(formData, { id: null, farmlandId: null, cropTypeId: null, plantDate: '', expectHarvestDate: '', actualHarvestDate: '', plantArea: null, seedAmount: null, status: 'PLANTING', description: '' }); dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑记录'; isEdit.value = true; Object.assign(formData, row); dialogVisible.value = true }
const handleDelete = async (row) => { try { await request.delete(`/planting/${row.id}`); ElMessage.success('删除成功'); loadData() } catch (error) { ElMessage.error('删除失败') } }
const handleSubmit = async () => { try { await formRef.value.validate(); submitLoading.value = true; if (isEdit.value) { await request.put(`/planting/${formData.id}`, formData); ElMessage.success('编辑成功') } else { await request.post('/planting', formData); ElMessage.success('新增成功') } dialogVisible.value = false; loadData() } catch (error) { ElMessage.error('提交失败') } finally { submitLoading.value = false } }
const handleSizeChange = () => loadData()
const handlePageChange = () => loadData()
const getStatusType = (status) => ({ PLANTING: 'primary', GROWING: 'success', HARVESTED: 'info' }[status] || 'info')
const getStatusText = (status) => ({ PLANTING: '已播种', GROWING: '生长中', HARVESTED: '已收获' }[status] || status)

onMounted(() => { loadFarmlands(); loadData() })
</script>

<style scoped>
.planting-container { padding: 20px; }
.search-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.toolbar { margin-bottom: 20px; }
</style>