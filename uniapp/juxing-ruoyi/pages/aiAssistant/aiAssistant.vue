<template>
  <view class="page">
    <view class="tip">登录后可使用智能助手：可接大模型（服务端配置密钥），未配置时自动按条件推荐已审核教师。</view>

    <view class="form-card">
      <view class="row">
        <text class="label">地区</text>
        <picker mode="selector" :range="areaLabels" @change="onAreaChange">
          <view class="picker-val">{{ areaLabels[areaIndex] || '全部' }}</view>
        </picker>
      </view>
      <view class="row">
        <text class="label">年级</text>
        <picker mode="selector" :range="gradeLabels" @change="onGradeChange">
          <view class="picker-val">{{ gradeLabels[gradeIndex] || '全部' }}</view>
        </picker>
      </view>
      <view class="row">
        <text class="label">科目</text>
        <picker mode="selector" :range="subjectLabels" @change="onSubjectChange">
          <view class="picker-val">{{ subjectLabels[subjectIndex] || '全部' }}</view>
        </picker>
      </view>
      <view class="row block">
        <text class="label">想问的（选填）</text>
        <textarea v-model="message" class="textarea" placeholder="例如：孩子初二数学基础弱，怎么选老师？" maxlength="300" />
      </view>
      <button class="submit" type="primary" @click="submit" :loading="loading">获取建议</button>
    </view>

    <view v-if="reply" class="result-card">
      <view class="result-title">助手回复</view>
      <text class="result-text">{{ reply }}</text>
      <view v-if="meta" class="meta">{{ meta }}</view>
    </view>

    <view v-if="teachers.length" class="teachers">
      <view class="result-title">推荐教师</view>
      <view v-for="t in teachers" :key="t.teacherId" class="t-card" @click="goDetail(t.teacherId)">
        <text class="t-name">{{ t.realName }}</text>
        <text class="t-sub">{{ t.university || '' }} · ¥{{ t.hourlyRate }}/课时</text>
      </view>
    </view>
  </view>
</template>

<script>
import { findAllArea } from '@/api/teacher/area'
import { findAllGrade } from '@/api/teacher/grade'
import { findAllSubject } from '@/api/teacher/subject'
import { aiAssistant } from '@/api/ai/assistant'

export default {
  data() {
    return {
      areaRows: [],
      gradeRows: [],
      subjectRows: [],
      areaIndex: 0,
      gradeIndex: 0,
      subjectIndex: 0,
      message: '',
      loading: false,
      reply: '',
      meta: '',
      teachers: []
    }
  },
  computed: {
    areaLabels() {
      return ['全部', ...this.areaRows.map(r => r.name)]
    },
    gradeLabels() {
      return ['全部', ...this.gradeRows.map(r => r.name)]
    },
    subjectLabels() {
      return ['全部', ...this.subjectRows.map(r => r.name)]
    }
  },
  onLoad() {
    this.loadDict()
  },
  methods: {
    async loadDict() {
      try {
        const [a, g, s] = await Promise.all([
          findAllArea(),
          findAllGrade(),
          findAllSubject()
        ])
        this.areaRows = a.data || []
        this.gradeRows = g.data || []
        this.subjectRows = s.data || []
      } catch (e) {
        uni.showToast({ title: '加载筛选项失败', icon: 'none' })
      }
    },
    onAreaChange(e) {
      this.areaIndex = Number(e.detail.value)
    },
    onGradeChange(e) {
      this.gradeIndex = Number(e.detail.value)
    },
    onSubjectChange(e) {
      this.subjectIndex = Number(e.detail.value)
    },
    pickAreaId() {
      if (this.areaIndex <= 0) return undefined
      const r = this.areaRows[this.areaIndex - 1]
      return r && r.areaId != null ? String(r.areaId) : undefined
    },
    pickGradeId() {
      if (this.gradeIndex <= 0) return undefined
      const r = this.gradeRows[this.gradeIndex - 1]
      return r && r.gradeId != null ? String(r.gradeId) : undefined
    },
    pickSubjectId() {
      if (this.subjectIndex <= 0) return undefined
      const r = this.subjectRows[this.subjectIndex - 1]
      return r && r.subjectId != null ? parseInt(r.subjectId, 10) : undefined
    },
    async submit() {
      this.loading = true
      this.reply = ''
      this.meta = ''
      this.teachers = []
      try {
        const data = {
          message: this.message || undefined,
          areaId: this.pickAreaId(),
          gradeId: this.pickGradeId(),
          subjectId: this.pickSubjectId()
        }
        const res = await aiAssistant(data)
        const d = res.data || {}
        this.reply = d.reply || ''
        const parts = []
        if (d.usedAi) parts.push('已调用大模型')
        else parts.push('规则回退')
        if (d.source) parts.push('来源: ' + d.source)
        this.meta = parts.join(' · ')
        this.teachers = d.teachers || []
      } catch (e) {
        // request 已 toast
      } finally {
        this.loading = false
      }
    },
    goDetail(teacherId) {
      if (!teacherId) return
      uni.navigateTo({
        url: '/pages/findteacher/findteacher/teacherDetail/teacherDetail?id=' + teacherId
      })
    }
  }
}
</script>

<style scoped>
.page {
  padding: 24rpx;
  background: #f5f6f7;
  min-height: 100vh;
}
.tip {
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
  margin-bottom: 24rpx;
}
.form-card, .result-card, .teachers {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}
.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1px solid #eee;
}
.row.block {
  flex-direction: column;
  align-items: stretch;
}
.label {
  font-size: 28rpx;
  color: #333;
}
.picker-val {
  font-size: 28rpx;
  color: #007aff;
}
.textarea {
  width: 100%;
  min-height: 160rpx;
  margin-top: 16rpx;
  padding: 16rpx;
  background: #f8f8f8;
  border-radius: 8rpx;
  font-size: 28rpx;
}
.submit {
  margin-top: 32rpx;
}
.result-title {
  font-size: 30rpx;
  font-weight: 600;
  margin-bottom: 16rpx;
}
.result-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
  white-space: pre-wrap;
}
.meta {
  margin-top: 12rpx;
  font-size: 24rpx;
  color: #999;
}
.t-card {
  padding: 20rpx 0;
  border-bottom: 1px solid #f0f0f0;
}
.t-name {
  font-size: 30rpx;
  color: #333;
  display: block;
}
.t-sub {
  font-size: 24rpx;
  color: #888;
}
</style>
