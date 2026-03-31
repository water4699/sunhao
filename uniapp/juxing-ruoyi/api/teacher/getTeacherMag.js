import request from '@/utils/request'

// 获取所有活动详情
export function getTeacherMag(query) {
  return request({
    'url': '/system/teacher/list',
    'method': 'get',
    'params': query
  })
}
  
  // 根据id査老师
  export function getOneTeacher(input) {
    return request({
      'url': '/system/teacher/' + input,
      'method': 'get'
    })
}