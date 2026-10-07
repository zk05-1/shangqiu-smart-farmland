<template>
  <div class="system-container">
    <el-card shadow="never">
      <template #header><span>系统管理</span></template>
      <el-tabs v-model="activeTab">
        <!-- 用户管理 -->
        <el-tab-pane label="用户管理" name="user">
          <div class="toolbar">
            <el-button type="primary" icon="Plus" @click="handleAddUser">新增用户</el-button>
          </div>
          <el-table :data="userList" v-loading="userLoading" border stripe style="width: 100%">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="username" label="用户名" width="150" />
            <el-table-column prop="realName" label="真实姓名" width="120" />
            <el-table-column prop="phone" label="手机号" width="150" />
            <el-table-column prop="email" label="邮箱" width="200" />
            <el-table-column prop="gender" label="性别" width="80" align="center">
              <template #default="{ row }">{{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}</template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
            </el-table-column>
            <el-table-column prop="createdTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="180" align="center">
              <template #default="{ row }">
                <el-button link type="primary" icon="Edit" @click="handleEditUser(row)">编辑</el-button>
                <el-button link type="warning" icon="Key" @click="handleResetPassword(row)">重置密码</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteUser(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-model:current-page="userPagination.page" v-model:page-size="userPagination.size" :total="userPagination.total" layout="total, prev, pager, next" style="margin-top: 20px; justify-content: flex-end" />
        </el-tab-pane>

        <!-- 角色管理 -->
        <el-tab-pane label="角色管理" name="role">
          <div class="toolbar">
            <el-button type="primary" icon="Plus" @click="handleAddRole">新增角色</el-button>
          </div>
          <el-table :data="roleList" v-loading="roleLoading" border stripe style="width: 100%">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="roleName" label="角色名称" width="150" />
            <el-table-column prop="roleCode" label="角色编码" width="150" />
            <el-table-column prop="description" label="描述" width="200" />
            <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template #default="{ row }">
                <el-button link type="primary" icon="Edit" @click="handleEditRole(row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteRole(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 菜单管理 -->
        <el-tab-pane label="菜单管理" name="menu">
          <div class="toolbar">
            <el-button type="primary" icon="Plus" @click="handleAddMenu">新增菜单</el-button>
          </div>
          <el-table :data="menuList" v-loading="menuLoading" border stripe style="width: 100%" row-key="id" default-expand-all>
            <el-table-column prop="menuName" label="菜单名称" width="200" />
            <el-table-column prop="menuType" label="类型" width="80" align="center">
              <template #default="{ row }"><el-tag :type="row.menuType === 'DIR' ? 'info' : row.menuType === 'MENU' ? 'primary' : 'success'">{{ row.menuType === 'DIR' ? '目录' : row.menuType === 'MENU' ? '菜单' : '按钮' }}</el-tag></template>
            </el-table-column>
            <el-table-column prop="path" label="路由路径" width="150" />
            <el-table-column prop="icon" label="图标" width="100" align="center" />
            <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template #default="{ row }">
                <el-button link type="primary" icon="Edit" @click="handleEditMenu(row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteMenu(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 操作日志 -->
        <el-tab-pane label="操作日志" name="log">
          <div class="toolbar">
            <el-button type="danger" icon="Delete" @click="handleClearLogs">清空日志</el-button>
          </div>
          <el-table :data="logList" v-loading="logLoading" border stripe style="width: 100%">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="username" label="操作用户" width="120" />
            <el-table-column prop="operation" label="操作内容" width="200" />
            <el-table-column prop="method" label="请求方法" width="200" show-overflow-tooltip />
            <el-table-column prop="ip" label="IP地址" width="150" />
            <el-table-column prop="executionTime" label="耗时(ms)" width="100" align="right" />
            <el-table-column prop="createdTime" label="操作时间" width="180" />
            <el-table-column label="操作" width="80" align="center">
              <template #default="{ row }">
                <el-button link type="primary" icon="View" @click="handleViewLog(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-model:current-page="logPagination.page" v-model:page-size="logPagination.size" :total="logPagination.total" layout="total, prev, pager, next" style="margin-top: 20px; justify-content: flex-end" />
        </el-tab-pane>

        <!-- 系统设置 -->
        <el-tab-pane label="系统设置" name="setting">
          <el-form label-width="150px" style="max-width: 600px; padding: 20px;">
            <el-form-item label="系统名称"><el-input v-model="settings.systemName" /></el-form-item>
            <el-form-item label="系统版本"><el-input v-model="settings.version" disabled /></el-form-item>
            <el-form-item label="数据库连接"><el-input v-model="settings.dbUrl" disabled /></el-form-item>
            <el-form-item><el-button type="primary" @click="saveSettings">保存设置</el-button></el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 用户对话框 -->
    <el-dialog v-model="userDialogVisible" :title="userDialogTitle" width="500px">
      <el-form ref="userFormRef" :model="userForm" :rules="userRules" label-width="100px">
        <el-form-item label="用户名" prop="username"><el-input v-model="userForm.username" :disabled="isUserEdit" /></el-form-item>
        <el-form-item label="真实姓名" prop="realName"><el-input v-model="userForm.realName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="userForm.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="userForm.email" /></el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="userForm.gender"><el-radio :label="1">男</el-radio><el-radio :label="2">女</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="userForm.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isUserEdit"><el-input v-model="userForm.password" type="password" show-password /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleUserSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 重置密码对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="重置密码" width="400px">
      <el-form ref="passwordFormRef" :model="passwordForm" label-width="100px">
        <el-form-item label="用户名"><el-input :value="passwordForm.username" disabled /></el-form-item>
        <el-form-item label="新密码"><el-input v-model="passwordForm.password" type="password" show-password /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePasswordSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 角色对话框 -->
    <el-dialog v-model="roleDialogVisible" :title="roleDialogTitle" width="500px">
      <el-form ref="roleFormRef" :model="roleForm" :rules="roleRules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName"><el-input v-model="roleForm.roleName" /></el-form-item>
        <el-form-item label="角色编码" prop="roleCode"><el-input v-model="roleForm.roleCode" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="roleForm.description" type="textarea" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="roleForm.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="roleForm.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleRoleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 菜单对话框 -->
    <el-dialog v-model="menuDialogVisible" :title="menuDialogTitle" width="500px">
      <el-form ref="menuFormRef" :model="menuForm" :rules="menuRules" label-width="100px">
        <el-form-item label="上级菜单">
          <el-select v-model="menuForm.parentId" placeholder="请选择上级菜单" clearable style="width: 100%">
            <el-option label="顶级菜单" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName"><el-input v-model="menuForm.menuName" /></el-form-item>
        <el-form-item label="菜单类型">
          <el-radio-group v-model="menuForm.menuType"><el-radio label="DIR">目录</el-radio><el-radio label="MENU">菜单</el-radio><el-radio label="BUTTON">按钮</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="路由路径"><el-input v-model="menuForm.path" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="menuForm.icon" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="menuForm.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="权限标识"><el-input v-model="menuForm.permission" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="menuForm.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleMenuSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 日志详情对话框 -->
    <el-dialog v-model="logDialogVisible" title="日志详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="操作用户">{{ logDetail.username }}</el-descriptions-item>
        <el-descriptions-item label="操作内容">{{ logDetail.operation }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ logDetail.method }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ logDetail.ip }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ logDetail.executionTime }}ms</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ logDetail.createdTime }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2"><el-input v-model="logDetail.params" type="textarea" :rows="4" readonly /></el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const activeTab = ref('user')

// 用户管理
const userList = ref([])
const userLoading = ref(false)
const userPagination = reactive({ page: 1, size: 10, total: 0 })
const userDialogVisible = ref(false)
const userDialogTitle = ref('新增用户')
const isUserEdit = ref(false)
const submitLoading = ref(false)
const userFormRef = ref(null)
const userForm = reactive({ id: null, username: '', realName: '', phone: '', email: '', gender: 1, status: 1, password: '' })
const userRules = { username: [{ required: true, message: '请输入用户名', trigger: 'blur' }], realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }], password: [{ required: true, message: '请输入密码', trigger: 'blur' }] }

// 重置密码
const passwordDialogVisible = ref(false)
const passwordFormRef = ref(null)
const passwordForm = reactive({ id: null, username: '', password: '' })

// 角色管理
const roleList = ref([])
const roleLoading = ref(false)
const roleDialogVisible = ref(false)
const roleDialogTitle = ref('新增角色')
const roleFormRef = ref(null)
const roleForm = reactive({ id: null, roleName: '', roleCode: '', description: '', sortOrder: 0, status: 1 })
const roleRules = { roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }], roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }] }

// 菜单管理
const menuList = ref([])
const menuLoading = ref(false)
const menuDialogVisible = ref(false)
const menuDialogTitle = ref('新增菜单')
const menuFormRef = ref(null)
const menuForm = reactive({ id: null, parentId: 0, menuName: '', menuType: 'MENU', path: '', icon: '', sortOrder: 0, permission: '', status: 1 })
const menuRules = { menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }] }

// 操作日志
const logList = ref([])
const logLoading = ref(false)
const logPagination = reactive({ page: 1, size: 10, total: 0 })
const logDialogVisible = ref(false)
const logDetail = reactive({ username: '', operation: '', method: '', ip: '', executionTime: 0, params: '', createdTime: '' })

// 系统设置
const settings = reactive({ systemName: '商丘市农业农村局智能农田管理系统', version: '1.0.0', dbUrl: 'jdbc:mysql://localhost:3306/shangqiu_farmland' })

// 加载用户列表
const loadUsers = async () => {
  userLoading.value = true
  try {
    const response = await request.get('/system/user/list', { params: { page: userPagination.page, size: userPagination.size } })
    userList.value = response.data.records || response.data.list || []
    userPagination.total = response.data.total
  } catch (error) { ElMessage.error('加载用户失败') } finally { userLoading.value = false }
}

// 加载角色列表
const loadRoles = async () => {
  roleLoading.value = true
  try {
    const response = await request.get('/system/role/all')
    roleList.value = response.data || []
  } catch (error) { ElMessage.error('加载角色失败') } finally { roleLoading.value = false }
}

// 加载菜单列表
const loadMenus = async () => {
  menuLoading.value = true
  try {
    const response = await request.get('/system/menu/list')
    menuList.value = response.data || []
  } catch (error) { ElMessage.error('加载菜单失败') } finally { menuLoading.value = false }
}

// 加载日志列表
const loadLogs = async () => {
  logLoading.value = true
  try {
    const response = await request.get('/system/log/list', { params: { page: logPagination.page, size: logPagination.size } })
    logList.value = response.data.records || response.data.list || []
    logPagination.total = response.data.total
  } catch (error) { ElMessage.error('加载日志失败') } finally { logLoading.value = false }
}

// 用户操作
const handleAddUser = () => { userDialogTitle.value = '新增用户'; isUserEdit.value = false; Object.assign(userForm, { id: null, username: '', realName: '', phone: '', email: '', gender: 1, status: 1, password: '' }); userDialogVisible.value = true }
const handleEditUser = (row) => { userDialogTitle.value = '编辑用户'; isUserEdit.value = true; Object.assign(userForm, { ...row, password: '' }); userDialogVisible.value = true }
const handleDeleteUser = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该用户吗？', '提示', { type: 'warning' })
    await request.delete(`/system/user/${row.id}`)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (error) { if (error !== 'cancel') ElMessage.error('删除失败') }
}
const handleResetPassword = (row) => { Object.assign(passwordForm, { id: row.id, username: row.username, password: '' }); passwordDialogVisible.value = true }
const handleUserSubmit = async () => {
  try {
    submitLoading.value = true
    if (isUserEdit.value) {
      await request.put(`/system/user/${userForm.id}`, userForm)
      ElMessage.success('更新成功')
    } else {
      await request.post('/system/user', userForm)
      ElMessage.success('创建成功')
    }
    userDialogVisible.value = false
    loadUsers()
  } catch (error) { ElMessage.error(error.response?.data?.message || '操作失败') } finally { submitLoading.value = false }
}
const handlePasswordSubmit = async () => {
  try {
    await request.put(`/system/user/${passwordForm.id}/password`, { password: passwordForm.password })
    ElMessage.success('密码重置成功')
    passwordDialogVisible.value = false
  } catch (error) { ElMessage.error('密码重置失败') }
}

// 角色操作
const handleAddRole = () => { roleDialogTitle.value = '新增角色'; Object.assign(roleForm, { id: null, roleName: '', roleCode: '', description: '', sortOrder: 0, status: 1 }); roleDialogVisible.value = true }
const handleEditRole = (row) => { roleDialogTitle.value = '编辑角色'; Object.assign(roleForm, row); roleDialogVisible.value = true }
const handleDeleteRole = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该角色吗？', '提示', { type: 'warning' })
    await request.delete(`/system/role/${row.id}`)
    ElMessage.success('删除成功')
    loadRoles()
  } catch (error) { if (error !== 'cancel') ElMessage.error('删除失败') }
}
const handleRoleSubmit = async () => {
  try {
    submitLoading.value = true
    if (roleForm.id) {
      await request.put(`/system/role/${roleForm.id}`, roleForm)
      ElMessage.success('更新成功')
    } else {
      await request.post('/system/role', roleForm)
      ElMessage.success('创建成功')
    }
    roleDialogVisible.value = false
    loadRoles()
  } catch (error) { ElMessage.error(error.response?.data?.message || '操作失败') } finally { submitLoading.value = false }
}

// 菜单操作
const handleAddMenu = () => { menuDialogTitle.value = '新增菜单'; Object.assign(menuForm, { id: null, parentId: 0, menuName: '', menuType: 'MENU', path: '', icon: '', sortOrder: 0, permission: '', status: 1 }); menuDialogVisible.value = true }
const handleEditMenu = (row) => { menuDialogTitle.value = '编辑菜单'; Object.assign(menuForm, row); menuDialogVisible.value = true }
const handleDeleteMenu = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该菜单吗？', '提示', { type: 'warning' })
    await request.delete(`/system/menu/${row.id}`)
    ElMessage.success('删除成功')
    loadMenus()
  } catch (error) { if (error !== 'cancel') ElMessage.error('删除失败') }
}
const handleMenuSubmit = async () => {
  try {
    submitLoading.value = true
    if (menuForm.id) {
      await request.put(`/system/menu/${menuForm.id}`, menuForm)
      ElMessage.success('更新成功')
    } else {
      await request.post('/system/menu', menuForm)
      ElMessage.success('创建成功')
    }
    menuDialogVisible.value = false
    loadMenus()
  } catch (error) { ElMessage.error(error.response?.data?.message || '操作失败') } finally { submitLoading.value = false }
}

// 日志操作
const handleViewLog = (row) => { Object.assign(logDetail, row); logDialogVisible.value = true }
const handleClearLogs = async () => {
  try {
    await ElMessageBox.confirm('确认清空所有日志吗？此操作不可恢复！', '提示', { type: 'warning' })
    await request.delete('/system/log/clear')
    ElMessage.success('清空成功')
    loadLogs()
  } catch (error) { if (error !== 'cancel') ElMessage.error('清空失败') }
}

// 系统设置
const saveSettings = () => { ElMessage.success('设置已保存') }

// 监听tab切换加载对应数据
watch(activeTab, (val) => {
  if (val === 'user') loadUsers()
  else if (val === 'role') loadRoles()
  else if (val === 'menu') loadMenus()
  else if (val === 'log') loadLogs()
})

onMounted(() => { loadUsers() })
</script>

<style scoped>
.system-container { padding: 20px; }
.toolbar { margin-bottom: 20px; }
</style>