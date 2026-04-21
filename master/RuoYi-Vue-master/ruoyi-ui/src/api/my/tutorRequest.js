import request from '@/utils/request'

export function listTutorRequest(query) {
  return request({
    url: '/system/tutor-request/list',
    method: 'get',
    params: query
  })
}

export function getTutorRequest(requestId) {
  return request({
    url: '/system/tutor-request/' + requestId,
    method: 'get'
  })
}

export function listTutorRequestBid(requestId, query) {
  return request({
    url: '/system/tutor-request/' + requestId + '/bid/list',
    method: 'get',
    params: query
  })
}
