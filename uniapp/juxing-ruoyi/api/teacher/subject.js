import request from '@/utils/request'

// 获取所有地区名称
export function findAllSubject() {
  return request({
    'url': '/system/subject/all',
    'method': 'get'
  })
}