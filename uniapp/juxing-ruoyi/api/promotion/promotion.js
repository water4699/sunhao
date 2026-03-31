import request from '@/utils/request'

// 获取所有活动详情
export function findAllPromotion() {
  return request({
    'url': '/system/promotion/all',
    'method': 'get'
  })
}