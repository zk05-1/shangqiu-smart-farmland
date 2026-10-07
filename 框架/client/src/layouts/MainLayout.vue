<template>
  <div class="main-layout">
    <!-- 侧边栏 -->
    <el-container class="layout-container">
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <el-icon :size="32" color="#409EFF"><Grid /></el-icon>
          <span class="logo-text">智能农田系统</span>
        </div>

        <el-menu
          :default-active="activeMenu"
          router
          class="sidebar-menu"
        >
          <el-menu-item index="/dashboard">
            <el-icon><DataBoard /></el-icon>
            <span>数据看板</span>
          </el-menu-item>

          <el-menu-item index="/farmland">
            <el-icon><Grid /></el-icon>
            <span>农田档案</span>
          </el-menu-item>

          <el-menu-item index="/planting">
            <el-icon><Document /></el-icon>
            <span>种植管理</span>
          </el-menu-item>

          <el-menu-item index="/irrigation">
            <el-icon><Umbrella /></el-icon>
            <span>灌溉管理</span>
          </el-menu-item>

          <el-menu-item index="/fertilization">
            <el-icon><Box /></el-icon>
            <span>施肥管理</span>
          </el-menu-item>

          <el-menu-item index="/pest">
            <el-icon><Warning /></el-icon>
            <span>病虫害管理</span>
          </el-menu-item>

          <el-menu-item index="/weather">
            <el-icon><Cloudy /></el-icon>
            <span>气象数据</span>
          </el-menu-item>

          <el-menu-item index="/sensor">
            <el-icon><Monitor /></el-icon>
            <span>传感器数据</span>
          </el-menu-item>

          <el-menu-item index="/yield">
            <el-icon><TrendCharts /></el-icon>
            <span>产量预测</span>
          </el-menu-item>

          <el-menu-item index="/system">
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-container>
        <!-- 顶部导航栏 -->
        <el-header class="header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>

          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="32" :src="userInfo.avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span class="username">{{ userInfo.realName || userInfo.username }}</span>
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    个人信息
                  </el-dropdown-item>
                  <el-dropdown-item command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主内容区 -->
        <el-main class="main">
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  DataBoard,
  Grid,
  Document,
  Umbrella,
  Box,
  Warning,
  Cloudy,
  Monitor,
  TrendCharts,
  Setting,
  User,
  ArrowDown,
  SwitchButton
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 用户信息
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

// 当前激活菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentTitle = computed(() => route.meta.title || '')

/**
 * 处理下拉菜单命令
 */
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人信息功能待开发')
      break
    case 'logout':
      handleLogout()
      break
  }
}

/**
 * 处理退出登录
 */
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确认退出登录吗？', '提示', {
      type: 'warning'
    })

    // 清除登录信息
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')

    ElMessage.success('已退出登录')

    // 跳转到登录页
    router.push('/login')
  } catch (error) {
    // 用户取消
  }
}
</script>

<style scoped>
.main-layout {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.layout-container {
  height: 100%;
}

.sidebar {
  background: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  overflow-y: auto;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #eee;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  margin-left: 10px;
  color: #333;
}

.sidebar-menu {
  border: none;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  margin-left: 10px;
  font-size: 14px;
  color: #333;
}

.main {
  background: #f5f7fa;
  overflow-y: auto;
}
</style>