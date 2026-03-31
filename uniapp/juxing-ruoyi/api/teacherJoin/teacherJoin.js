import request from '@/utils/request'

// 向教师表里插入数据
export function teacherJoin(data) {
  return request({
    'url': '/system/teacher',
	'data': data,
    'method': 'post',
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