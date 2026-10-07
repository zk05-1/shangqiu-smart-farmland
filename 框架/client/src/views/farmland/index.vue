<template>
  <div class="farmland-container">
    <!-- 顶部搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="关键字">
          <el-input
            v-model="searchForm.keyword"
            placeholder="搜索地块编号/名称/位置"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="使用中" value="USE" />
            <el-option label="休耕" value="FALLOW" />
            <el-option label="闲置" value="IDLE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
          <el-button type="success" icon="Map" @click="showMapView">地图视图</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="toolbar">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增地块</el-button>
        <el-button type="danger" icon="Delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">
          批量删除
        </el-button>
      </div>

      <el-table
        ref="tableRef"
        :data="farmlandList"
        v-loading="loading"
        border
        stripe
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="farmlandCode" label="地块编号" width="120" />
        <el-table-column prop="farmlandName" label="地块名称" width="150" />
        <el-table-column prop="area" label="面积(亩)" width="100" align="right">
          <template #default="{ row }">
            {{ row.area ? row.area.toFixed(2) : '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="location" label="位置" min-width="180" show-overflow-tooltip />
        <el-table-column prop="longitude" label="经度" width="100">
          <template #default="{ row }">
            {{ row.longitude ? row.longitude.substring(0, 8) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="latitude" label="纬度" width="100">
          <template #default="{ row }">
            {{ row.latitude ? row.latitude.substring(0, 7) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="soilType" label="土壤类型" width="100" />
        <el-table-column prop="ownerName" label="负责人" width="100" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="row.status === 'USE' ? 'success' : row.status === 'FALLOW' ? 'warning' : 'info'"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" icon="View" @click="handleView(row)">详情</el-button>
            <el-button link type="success" icon="MapLocation" @click="handleShowOnMap(row)">定位</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      :close-on-click-modal="false"
      @opened="handleDialogOpened"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地块编号" prop="farmlandCode">
              <el-input v-model="formData.farmlandCode" placeholder="请输入地块编号" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地块名称" prop="farmlandName">
              <el-input v-model="formData.farmlandName" placeholder="请输入地块名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="面积(亩)" prop="area">
              <el-input-number v-model="formData.area" :precision="2" :min="0" :max="9999" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="位置" prop="location">
              <el-input v-model="formData.location" placeholder="请输入位置" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度">
              <el-input v-model="formData.longitude" placeholder="点击地图选择或手动输入">
                <template #suffix>
                  <el-icon><Location /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度">
              <el-input v-model="formData.latitude" placeholder="点击地图选择或手动输入">
                <template #suffix>
                  <el-icon><Location /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="地图定位">
          <div style="width: 100%; height: 300px; border: 1px solid #dcdfe6; border-radius: 4px;">
            <AMapComponent
              ref="mapPickerRef"
              :mode="'edit'"
              :center-lng="formData.longitude || 115.65"
              :center-lat="formData.latitude || 34.44"
              :zoom="14"
              :show-search="true"
              :show-toolbar="true"
              :selected-position="formData.longitude && formData.latitude ? { lng: parseFloat(formData.longitude), lat: parseFloat(formData.latitude) } : null"
              @position-change="handlePositionChange"
            />
          </div>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="土壤类型" prop="soilType">
              <el-select v-model="formData.soilType" placeholder="请选择土壤类型" style="width: 100%">
                <el-option label="沙土" value="沙土" />
                <el-option label="壤土" value="壤土" />
                <el-option label="黏土" value="黏土" />
                <el-option label="沙壤土" value="沙壤土" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="土壤pH值">
              <el-input-number v-model="formData.soilPh" :precision="2" :min="0" :max="14" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人姓名" prop="ownerName">
              <el-input v-model="formData.ownerName" placeholder="请输入负责人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="ownerPhone">
              <el-input v-model="formData.ownerPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="使用中" value="USE" />
                <el-option label="休耕" value="FALLOW" />
                <el-option label="闲置" value="IDLE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="图片">
              <el-input v-model="formData.imageUrl" placeholder="请输入图片URL" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="地块详情" width="800px" @opened="handleDetailOpened">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="地块编号">{{ detailData.farmlandCode }}</el-descriptions-item>
        <el-descriptions-item label="地块名称">{{ detailData.farmlandName }}</el-descriptions-item>
        <el-descriptions-item label="面积(亩)">{{ detailData.area }}</el-descriptions-item>
        <el-descriptions-item label="位置">{{ detailData.location }}</el-descriptions-item>
        <el-descriptions-item label="经度">{{ detailData.longitude }}</el-descriptions-item>
        <el-descriptions-item label="纬度">{{ detailData.latitude }}</el-descriptions-item>
        <el-descriptions-item label="土壤类型">{{ detailData.soilType }}</el-descriptions-item>
        <el-descriptions-item label="土壤pH值">{{ detailData.soilPh }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ detailData.ownerName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.ownerPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 'USE' ? 'success' : 'warning'">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createdTime }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px;">
        <el-divider content-position="left">地块位置</el-divider>
        <div style="width: 100%; height: 250px; border: 1px solid #dcdfe6; border-radius: 4px;">
          <AMapComponent
            ref="detailMapRef"
            :mode="'view'"
            :center-lng="detailData.longitude || 115.65"
            :center-lat="detailData.latitude || 34.44"
            :zoom="15"
            :show-search="false"
            :show-toolbar="false"
            :selected-position="detailData.longitude && detailData.latitude ? { lng: parseFloat(detailData.longitude), lat: parseFloat(detailData.latitude) } : null"
          />
        </div>
      </div>
    </el-dialog>

    <!-- 地图视图对话框 -->
    <el-dialog v-model="mapViewVisible" title="农田地图视图" width="90%" top="5vh" @opened="handleMapViewOpened">
      <div style="width: 100%; height: 500px; border: 1px solid #dcdfe6; border-radius: 4px;">
        <AMapComponent
          ref="allMapRef"
          :mode="'view'"
          :center-lng="115.65"
          :center-lat="34.44"
          :zoom="10"
          :show-search="true"
          :show-toolbar="true"
          :markers="mapMarkers"
          @marker-click="handleMarkerClick"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Location } from '@element-plus/icons-vue'
import AMapComponent from '@/components/AMapComponent.vue'
import { getFarmlandList, createFarmland, updateFarmland, deleteFarmland, getFarmlandById } from '@/api/farmland'

const searchForm = reactive({
  keyword: '',
  status: ''
})

const farmlandList = ref([])
const loading = ref(false)
const multipleSelection = ref([])

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增地块')
const isEdit = ref(false)
const submitLoading = ref(false)

const formData = reactive({
  id: null,
  farmlandCode: '',
  farmlandName: '',
  area: null,
  location: '',
  longitude: '',
  latitude: '',
  soilType: '',
  soilPh: null,
  ownerName: '',
  ownerPhone: '',
  status: 'USE',
  imageUrl: '',
  description: ''
})

const formRules = {
  farmlandCode: [{ required: true, message: '请输入地块编号', trigger: 'blur' }],
  farmlandName: [{ required: true, message: '请输入地块名称', trigger: 'blur' }],
  area: [{ required: true, message: '请输入面积', trigger: 'blur' }],
  location: [{ required: true, message: '请输入位置', trigger: 'blur' }],
  soilType: [{ required: true, message: '请选择土壤类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const detailVisible = ref(false)
const detailData = ref({})

const mapViewVisible = ref(false)
const mapMarkers = computed(() => {
  return farmlandList.value
    .filter(item => item.longitude && item.latitude)
    .map(item => ({
      id: item.id,
      lng: parseFloat(item.longitude),
      lat: parseFloat(item.latitude),
      name: item.farmlandName,
      title: item.farmlandCode + ' - ' + item.farmlandName,
      status: item.status
    }))
})

const tableRef = ref(null)
const formRef = ref(null)
const mapPickerRef = ref(null)
const detailMapRef = ref(null)
const allMapRef = ref(null)

const loadFarmlandList = async () => {
  loading.value = true
  try {
    const response = await getFarmlandList({
      page: pagination.page,
      size: pagination.size,
      keyword: searchForm.keyword,
      status: searchForm.status
    })
    farmlandList.value = response.data.list || response.data.records || []
    pagination.total = response.data.total
  } catch (error) {
    console.error('加载农田列表失败:', error)
    ElMessage.error('加载农田列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadFarmlandList()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.status = ''
  pagination.page = 1
  loadFarmlandList()
}

const handleAdd = () => {
  dialogTitle.value = '新增地块'
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    farmlandCode: '',
    farmlandName: '',
    area: null,
    location: '',
    longitude: '',
    latitude: '',
    soilType: '',
    soilPh: null,
    ownerName: '',
    ownerPhone: '',
    status: 'USE',
    imageUrl: '',
    description: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑地块'
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDialogOpened = () => {
  if (mapPickerRef.value) {
    mapPickerRef.value.initMap()
  }
}

const handleDetailOpened = () => {
  if (detailMapRef.value) {
    detailMapRef.value.initMap()
  }
}

const handleMapViewOpened = () => {
  if (allMapRef.value) {
    allMapRef.value.initMap()
  }
}

const handleView = async (row) => {
  try {
    const response = await getFarmlandById(row.id)
    detailData.value = response.data
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const handleShowOnMap = (row) => {
  if (row.longitude && row.latitude) {
    detailData.value = row
    mapViewVisible.value = true
  } else {
    ElMessage.warning('该地块没有设置经纬度坐标')
  }
}

const showMapView = () => {
  mapViewVisible.value = true
}

const handleMarkerClick = (marker) => {
  ElMessage.info(`点击了: ${marker.name}`)
  const farmland = farmlandList.value.find(item => item.id === marker.id)
  if (farmland) {
    mapViewVisible.value = false
    handleView(farmland)
  }
}

const handlePositionChange = (position) => {
  formData.longitude = position.lng.toString()
  formData.latitude = position.lat.toString()
  ElMessage.success(`已选择位置: 经度 ${position.lng.toFixed(6)}, 纬度 ${position.lat.toFixed(6)}`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该地块吗？', '提示', {
      type: 'warning'
    })
    await deleteFarmland(row.id)
    ElMessage.success('删除成功')
    loadFarmlandList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm('确认删除选中的地块吗？', '提示', {
      type: 'warning'
    })
    for (const item of multipleSelection.value) {
      await deleteFarmland(item.id)
    }
    ElMessage.success('删除成功')
    loadFarmlandList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true

    if (formData.id) {
      await updateFarmland(formData.id, formData)
      ElMessage.success('编辑成功')
    } else {
      await createFarmland(formData)
      ElMessage.success('新增成功')
    }

    dialogVisible.value = false
    loadFarmlandList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

const handleSelectionChange = (val) => {
  multipleSelection.value = val
}

const handleSizeChange = (val) => {
  pagination.size = val
  loadFarmlandList()
}

const handlePageChange = (val) => {
  pagination.page = val
  loadFarmlandList()
}

const getStatusText = (status) => {
  const statusMap = {
    USE: '使用中',
    FALLOW: '休耕',
    IDLE: '闲置'
  }
  return statusMap[status] || status
}

onMounted(() => {
  loadFarmlandList()
})
</script>

<style scoped>
.farmland-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}
</style>