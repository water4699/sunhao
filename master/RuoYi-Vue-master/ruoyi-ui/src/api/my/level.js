import request from '@/utils/request'

// 查询年级列表
export function listLevel(query) {
  return request({
    url: '/system/level/list',
    method: 'get',
    params: query
  })
}

// 查询年级详细
export function getLevel(gradeId) {
  return request({
    url: '/system/level/' + gradeId,
    method: 'get'
  })
}

// 新增年级
export function addLevel(data) {
  return request({
    url: '/system/level',
    method: 'post',
    data: data
  })
}

// 修改年级
export function updateLevel(data) {
  return request({
    url: '/system/level',
    method: 'put',
    data: data
  })
}

// 删除年级
export function delLevel(gradeId) {
  return request({
    url: '/system/level/' + gradeId,
    method: 'delete'
  })
}
