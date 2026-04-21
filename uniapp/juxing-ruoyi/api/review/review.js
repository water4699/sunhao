import request from '@/utils/request'

// 评价列表（管理端权限，分页参数可选）
export function getReviewMag(query) {
  return request({
    url: '/system/review/list',
    method: 'get',
    params: query || {}
  })
}

/** 小程序：仅展示已审核评价 */
export function getReviewAppList(query) {
  return request({
    url: '/system/review/app/list',
    method: 'get',
    params: query || {}
  })
}

/** 小程序：提交评价（关联课程预约 bookingId = courseId） */
export function submitReviewApp(data) {
  return request({
    url: '/system/review/app/submit',
    method: 'post',
    data
  })
}
