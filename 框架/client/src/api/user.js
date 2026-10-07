/**
 * 用户相关 API
 */
import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} data - 登录数据
 * @returns {Promise}
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户注册
 * @param {Object} data - 注册数据
 * @returns {Promise}
 */
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

/**
 * 获取用户信息
 * @param {String} username - 用户名
 * @returns {Promise}
 */
export function getUserInfo(username) {
  return request({
    url: `/auth/user/${username}`,
    method: 'get'
  })
}