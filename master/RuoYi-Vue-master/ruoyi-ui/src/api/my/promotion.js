import request from '@/utils/request'

// 查询优惠活动列表
export function listPromotion(query) {
  return request({
    url: '/system/promotion/list',
    method: 'get',
    params: query
  })
}

// 查询优惠活动详细
export function getPromotion(promotionId) {
  return request({
    url: '/system/promotion/' + promotionId,
    method: 'get'
  })
}

// 新增优惠活动
export function addPromotion(data) {
  return request({
    url: '/system/promotion',
    method: 'post',
    data: data
  })
}

// 修改优惠活动
export function updatePromotion(data) {
  return request({
    url: '/system/promotion',
    method: 'put',
    data: data
  })
}

// 删除优惠活动
export function delPromotion(promotionId) {
  return request({
    url: '/system/promotion/' + promotionId,
    method: 'delete'
  })
}
