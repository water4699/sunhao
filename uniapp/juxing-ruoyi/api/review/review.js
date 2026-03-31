import request from '@/utils/request'

// 获取所有评论详情
export function getReviewMag() {
  return request({
    'url': '/system/review/list',
    'method': 'get'
  })
}
  