import request from '@/utils/request'

// 获取所有卡详情
export function getCardMag() {
  return request({
    'url': '/system/card/list',
    'method': 'get'
  })
}
  
  // 根据id査老师
  export function getOneCard(input) {
    return request({
      'url': '/system/card/' + input,
      'method': 'get'
    })
}