import request from '@/utils/request'

/** AI 选课建议 / 问答（需登录；无密钥时服务端规则回退） */
export function aiAssistant(data) {
  return request({
    url: '/system/ai/assistant',
    method: 'post',
    data
  })
}
