import request from '@/utils/request'

// 获取所有地区名称
export function findAllArea() {
  return request({
    'url': '/system/jinan/all',
    'method': 'get'
  })
}