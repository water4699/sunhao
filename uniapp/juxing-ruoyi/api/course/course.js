import request from '@/utils/request'

/** 课程/预约列表（分页） */
export function listCourse(query) {
  return request({
    url: '/system/course/list',
    method: 'get',
    params: query
  })
}

/** 新增课程预约 */
export function addCourse(data) {
  return request({
    url: '/system/course',
    method: 'post',
    data: data
  })
}
