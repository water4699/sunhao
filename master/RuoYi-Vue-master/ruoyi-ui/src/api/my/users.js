import request from '@/utils/request'

// 查询用户总列表
export function listUsers(query) {
  return request({
    url: '/system/users/list',
    method: 'get',
    params: query
  })
}

// 查询用户总详细
export function getUsers(usersId) {
  return request({
    url: '/system/users/' + usersId,
    method: 'get'
  })
}

// 新增用户总
export function addUsers(data) {
  return request({
    url: '/system/users',
    method: 'post',
    data: data
  })
}

// 修改用户总
export function updateUsers(data) {
  return request({
    url: '/system/users',
    method: 'put',
    data: data
  })
}

// 删除用户总
export function delUsers(usersId) {
  return request({
    url: '/system/users/' + usersId,
    method: 'delete'
  })
}
