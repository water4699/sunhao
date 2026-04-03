import request from '@/utils/request'

/** 老师入驻：仅登录即可；状态固定待审，由管理端审核 */
export function teacherJoin(data) {
  return request({
    url: '/system/teacher/app/join',
    method: 'post',
    data
  })
}

/** 老师入驻状态：-1未申请，0待审核，1已通过，2驳回 */
export function getTeacherJoinStatus() {
  return request({
    url: '/system/teacher/app/status',
    method: 'get'
  }).then((res) => {
    const payload = (res && res.data && typeof res.data === 'object')
      ? res.data
      : {
          hasSubmitted: res && res.hasSubmitted,
          approved: res && res.approved,
          status: res && res.status,
          teacherId: res && res.teacherId,
          realName: res && res.realName
        }
    return {
      ...res,
      data: payload
    }
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
