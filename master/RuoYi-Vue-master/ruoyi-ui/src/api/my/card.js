import request from '@/utils/request'

// 查询会员卡类型列表
export function listCard(query) {
  return request({
    url: '/system/card/list',
    method: 'get',
    params: query
  })
}

// 查询会员卡类型详细
export function getCard(cardId) {
  return request({
    url: '/system/card/' + cardId,
    method: 'get'
  })
}

// 新增会员卡类型
export function addCard(data) {
  return request({
    url: '/system/card',
    method: 'post',
    data: data
  })
}

// 修改会员卡类型
export function updateCard(data) {
  return request({
    url: '/system/card',
    method: 'put',
    data: data
  })
}

// 删除会员卡类型
export function delCard(cardId) {
  return request({
    url: '/system/card/' + cardId,
    method: 'delete'
  })
}
