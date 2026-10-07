/**
 * 用户状态管理 Store
 * 商丘市农业农村局智能农田管理系统
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, getUserInfo } from '@/api/user'
import router from '@/router'

/**
 * 用户 Store
 * 管理用户登录状态、用户信息、Token、权限等
 */
export const useUserStore = defineStore('user', () => {
  // ========== 状态 ==========
  /** JWT Token */
  const token = ref(localStorage.getItem('token') || '')
  /** 用户信息 */
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))
  /** 用户权限列表 */
  const permissions = ref(JSON.parse(localStorage.getItem('permissions') || '[]'))
  /** 用户角色列表 */
  const roles = ref(JSON.parse(localStorage.getItem('roles') || '[]'))

  // ========== 计算属性 ==========
  /** 是否已登录 */
  const isLoggedIn = computed(() => !!token.value)
  /** 用户名 */
  const username = computed(() => userInfo.value?.username || '')
  /** 真实姓名 */
  const realName = computed(() => userInfo.value?.realName || '')
  /** 头像 */
  const avatar = computed(() => userInfo.value?.avatar || '')

  // ========== 方法 ==========

  /**
   * 用户登录
   * @param {Object} loginForm - 登录表单 { username, password }
   */
  const login = async (loginForm) => {
    try {
      const res = await loginApi(loginForm)
      if (res.code === 200 && res.data) {
        const { token: jwtToken, userId, username, realName, avatar, roles: userRoles, permissions: userPermissions } = res.data

        // 保存 Token
        token.value = jwtToken
        localStorage.setItem('token', jwtToken)

        // 保存用户信息
        const info = { userId, username, realName, avatar }
        userInfo.value = info
        localStorage.setItem('userInfo', JSON.stringify(info))

        // 保存角色和权限
        roles.value = userRoles || []
        permissions.value = userPermissions || []
        localStorage.setItem('roles', JSON.stringify(userRoles || []))
        localStorage.setItem('permissions', JSON.stringify(userPermissions || []))

        return true
      }
      return false
    } catch (error) {
      console.error('登录失败:', error)
      return false
    }
  }

  /**
   * 用户登出
   * 清除所有本地状态并跳转登录页
   */
  const logout = () => {
    token.value = ''
    userInfo.value = null
    permissions.value = []
    roles.value = []

    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('permissions')
    localStorage.removeItem('roles')

    router.push('/login')
  }

  /**
   * 检查是否有某个权限
   * @param {string} permission - 权限标识，如 'sys:user:add'
   */
  const hasPermission = (permission) => {
    if (roles.value.includes('ADMIN')) return true // 管理员拥有所有权限
    return permissions.value.includes(permission)
  }

  /**
   * 检查是否有某个角色
   * @param {string} role - 角色编码，如 'ADMIN'
   */
  const hasRole = (role) => {
    return roles.value.includes(role)
  }

  return {
    token,
    userInfo,
    permissions,
    roles,
    isLoggedIn,
    username,
    realName,
    avatar,
    login,
    logout,
    hasPermission,
    hasRole
  }
})
