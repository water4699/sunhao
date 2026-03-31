import request from '@/utils/request'

// 获取所有活动详情
export function findAllProduct() {
  return request({
    'url': '/system/product/list',
    'method': 'get'
  })
}

  // 根据id査商品
  export function getOneProduct(input) {
    return request({
      'url': '/system/product/' + input,
      'method': 'get'
    })
}