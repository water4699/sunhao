import request from '@/utils/request'

/** 老师入驻：仅登录即可；状态固定待审，由管理端审核 */
export function teacherJoin(data) {
  return request({
    url: '/system/teacher/app/join',
    method: 'post',
    data
  })
}

//插入图片
export function teacherImage(data) {
  return request({
    'url': '/common/upload',
	'data': data,
    'method': 'post',
  })
}