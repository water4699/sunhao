import request from '@/utils/request'

// 获取所有地区名称
export function findAllGrade() {
  return request({
    'url': '/system/level/all',
    'method': 'get'
  })
}