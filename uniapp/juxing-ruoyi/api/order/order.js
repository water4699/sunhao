import request from '@/utils/request'

/** 小程序：当前用户商品订单列表 */
export function listAppProductOrder(query) {
  return request({
    url: '/system/order/app/list',
    method: 'get',
    params: query || {}
  })
}

/** 小程序：创建商品订单（模拟支付） */
export function createAppProductOrder(data) {
  return request({
    url: '/system/order/app/product',
    method: 'post',
    data
  })
}
