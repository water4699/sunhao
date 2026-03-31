import request from '@/utils/request'

// 查询济南地区列表
export function listJinan(query) {
  return request({
    url: '/system/jinan/list',
    method: 'get',
    params: query
  })
}

// 查询济南地区详细
export function getJinan(areaId) {
  return request({
    url: '/system/jinan/' + areaId,
    method: 'get'
  })
}

// 新增济南地区
export function addJinan(data) {
  return request({
    url: '/system/jinan',
    method: 'post',
    data: data
  })
}

// 修改济南地区
export function updateJinan(data) {
  return request({
    url: '/system/jinan',
    method: 'put',
    data: data
  })
}

// 删除济南地区
export function delJinan(areaId) {
  return request({
    url: '/system/jinan/' + areaId,
    method: 'delete'
  })
}
