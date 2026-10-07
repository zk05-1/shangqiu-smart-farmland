/**
 * 农田管理相关 API
 */
import request from '@/utils/request'

/**
 * 分页查询农田列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getFarmlandList(params) {
  return request({
    url: '/farmland/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取农田详情
 * @param {Number} id - 农田ID
 * @returns {Promise}
 */
export function getFarmlandById(id) {
  return request({
    url: `/farmland/${id}`,
    method: 'get'
  })
}

/**
 * 创建农田
 * @param {Object} data - 农田数据
 * @returns {Promise}
 */
export function createFarmland(data) {
  return request({
    url: '/farmland',
    method: 'post',
    data
  })
}

/**
 * 更新农田
 * @param {Number} id - 农田ID
 * @param {Object} data - 更新数据
 * @returns {Promise}
 */
export function updateFarmland(id, data) {
  return request({
    url: `/farmland/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除农田
 * @param {Number} id - 农田ID
 * @returns {Promise}
 */
export function deleteFarmland(id) {
  return request({
    url: `/farmland/${id}`,
    method: 'delete'
  })
}

/**
 * 统计农田总数
 * @returns {Promise}
 */
export function countFarmland() {
  return request({
    url: '/farmland/count',
    method: 'get'
  })
}

/**
 * 统计使用中的农田数量
 * @returns {Promise}
 */
export function countActiveFarmland() {
  return request({
    url: '/farmland/count/active',
    method: 'get'
  })
}