import request from '@/utils/request'

export function createTutorRequest(data) {
  return request({
    url: '/system/tutor-request/app/create',
    method: 'post',
    data
  })
}

export function listMyTutorRequests(query) {
  return request({
    url: '/system/tutor-request/app/my-list',
    method: 'get',
    params: query
  })
}

export function closeTutorRequest(requestId) {
  return request({
    url: `/system/tutor-request/app/${requestId}/close`,
    method: 'delete'
  })
}

export function listTutorRequestBids(requestId, query) {
  return request({
    url: `/system/tutor-request/app/${requestId}/bids`,
    method: 'get',
    params: query
  })
}

export function confirmTutorRequestBid(bidId) {
  return request({
    url: `/system/tutor-request/app/bid/${bidId}/confirm`,
    method: 'post'
  })
}

export function rejectTutorRequestBid(bidId) {
  return request({
    url: `/system/tutor-request/app/bid/${bidId}/reject`,
    method: 'post'
  })
}

export function listOpenTutorRequests(query) {
  return request({
    url: '/system/tutor-request/app/open-list',
    method: 'get',
    params: query
  })
}

export function bidTutorRequest(requestId, data) {
  return request({
    url: `/system/tutor-request/app/${requestId}/bid`,
    method: 'post',
    data
  })
}

export function listMyTutorBids(query) {
  return request({
    url: '/system/tutor-request/app/my-bids',
    method: 'get',
    params: query
  })
}
