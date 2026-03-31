import request from '@/utils/request'

// 查询教室时间列表
export function listSchedule(query) {
  return request({
    url: '/system/schedule/list',
    method: 'get',
    params: query
  })
}

// 查询教室时间详细
export function getSchedule(scheduleId) {
  return request({
    url: '/system/schedule/' + scheduleId,
    method: 'get'
  })
}

// 新增教室时间
export function addSchedule(data) {
  return request({
    url: '/system/schedule',
    method: 'post',
    data: data
  })
}

// 修改教室时间
export function updateSchedule(data) {
  return request({
    url: '/system/schedule',
    method: 'put',
    data: data
  })
}

// 删除教室时间
export function delSchedule(scheduleId) {
  return request({
    url: '/system/schedule/' + scheduleId,
    method: 'delete'
  })
}
