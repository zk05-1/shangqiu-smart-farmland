/**
 * Vue Router 路由配置
 * 商丘市农业农村局智能农田管理系统
 */
import { createRouter, createWebHistory } from 'vue-router'

/**
 * 路由配置
 * 采用嵌套路由，MainLayout 作为主布局组件
 */
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', noAuth: true }
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '数据看板', icon: 'DataBoard' }
      },
      {
        path: 'farmland',
        name: 'Farmland',
        component: () => import('@/views/farmland/index.vue'),
        meta: { title: '农田档案管理', icon: 'Grid' }
      },
      {
        path: 'planting',
        name: 'Planting',
        component: () => import('@/views/planting/index.vue'),
        meta: { title: '作物种植管理', icon: 'Plant' }
      },
      {
        path: 'irrigation',
        name: 'Irrigation',
        component: () => import('@/views/irrigation/index.vue'),
        meta: { title: '智能灌溉管理', icon: 'Umbrella' }
      },
      {
        path: 'fertilization',
        name: 'Fertilization',
        component: () => import('@/views/fertilization/index.vue'),
        meta: { title: '施肥管理', icon: 'Box' }
      },
      {
        path: 'pest',
        name: 'Pest',
        component: () => import('@/views/pest/index.vue'),
        meta: { title: '病虫害管理', icon: 'Bug' }
      },
      {
        path: 'weather',
        name: 'Weather',
        component: () => import('@/views/weather/index.vue'),
        meta: { title: '气象数据', icon: 'Cloudy' }
      },
      {
        path: 'sensor',
        name: 'Sensor',
        component: () => import('@/views/sensor/index.vue'),
        meta: { title: '传感器数据', icon: 'Gauge' }
      },
      {
        path: 'yield',
        name: 'Yield',
        component: () => import('@/views/yield/index.vue'),
        meta: { title: '产量预测', icon: 'TrendCharts' }
      },
      {
        path: 'system',
        name: 'System',
        component: () => import('@/views/system/index.vue'),
        meta: { title: '系统管理', icon: 'Setting' }
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '404', noAuth: true }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

/**
 * 创建路由实例
 */
const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 全局路由守卫
 * 检查用户登录状态，未登录用户跳转到登录页
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 商丘市农业农村局智能农田管理系统`
  }

  // 不需要认证的页面直接放行
  if (to.meta.noAuth) {
    next()
    return
  }

  // 检查登录状态
  const token = localStorage.getItem('token')
  if (!token) {
    // 未登录，跳转登录页
    next('/login')
    return
  }

  // 已登录，放行
  next()
})

export default router