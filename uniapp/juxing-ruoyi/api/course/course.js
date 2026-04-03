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

/** 老师：上架课程 */
export function publishTeacherCourse(data) {
  return request({
    url: '/system/course/app/publish',
    method: 'post',
    data
  })
}

/** 老师发布课程列表（小程序端） */
export function listPublishedCourses(query) {
  const q = { ...(query || {}) }
  if (q.status === undefined || q.status === null || q.status === '') {
    q.status = 0
  }
  return request({
    url: '/system/course/app/published/list',
    method: 'get',
    params: q
  })
}

/** 老师发布课程详情（小程序端） */
export function getPublishedCourseDetail(publishId) {
  return request({
    url: `/system/course/app/published/${publishId}`,
    method: 'get'
  })
}
