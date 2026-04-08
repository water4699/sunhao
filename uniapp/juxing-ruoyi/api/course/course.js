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

/** 学生端：取消预约/申请取消 */
export function cancelBooking(courseId, cancelReason) {
  return request({
    url: `/system/course/app/booking/${courseId}/cancel`,
    method: 'post',
    data: { cancelReason: cancelReason || '' }
  })
}

/** 老师：发布家教信息 */
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

/** 老师端：我的家教信息列表 */
export function listMyPublishedCourses(query) {
  return request({
    url: '/system/course/app/my-published/list',
    method: 'get',
    params: query
  })
}

/** 老师端：更新我的家教信息 */
export function updateMyPublishedCourse(publishId, data) {
  return request({
    url: `/system/course/app/my-published/${publishId}`,
    method: 'put',
    data
  })
}

/** 老师端：上架/下架我的家教信息（0上架 1下架） */
export function updateMyPublishedCourseStatus(publishId, status) {
  return request({
    url: `/system/course/app/my-published/${publishId}/status`,
    method: 'post',
    data: { status }
  })
}

/** 老师端：删除我的家教信息 */
export function deleteMyPublishedCourse(publishId) {
  return request({
    url: `/system/course/app/my-published/${publishId}`,
    method: 'delete'
  })
}


/** 老师端：预约请求列表 */
export function listTeacherBookings(query) {
  return request({
    url: '/system/course/app/teacher/booking/list',
    method: 'get',
    params: query
  })
}

/** 老师端：处理预约（status: 1同意 2拒绝） */
export function decideTeacherBooking(courseId, status) {
  return request({
    url: `/system/course/app/teacher/booking/${courseId}/decision`,
    method: 'post',
    data: { status }
  })
}
