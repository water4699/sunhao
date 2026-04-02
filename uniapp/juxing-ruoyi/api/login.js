import request from '@/utils/request'

// 业务端密码登录（users 表，非管理端 sys_user）
export function login(loginForm) {
  return request({
    'url': '/app/auth/login',
    headers: {
      isToken: false
    },
    'method': 'post',
    'data': loginForm
  })
}

// 注册方法（PC/通用：registerRole 可空，空则绑定 common）
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data
  })
}

/** 小程序注册：registerRole 为 student | parent | teacher（学生与家长同属选课侧） */
export function registerApp(data) {
  return request({
    url: '/register/app',
    headers: {
      isToken: false
    },
    method: 'post',
    data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    'url': '/getInfo',
    'method': 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    'url': '/logout',
    'method': 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    'url': '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

// 获取手机验证码
export function getUserCode(phone) {
	return request({
		'url': '/system/users/getUserCode',
		headers: {
		  isToken: false
		},
		method: 'get',
		timeout: 20000,
		params: {
			phone: phone
		}
	})
}

// 验证码登录
export function userCodeLogin(codeLoginForm) {
	return request({
	  'url': '/codeLogin',
	  headers: {
	    isToken: false
	  },
	  'method': 'post',
	  'data': codeLoginForm
	})
}
