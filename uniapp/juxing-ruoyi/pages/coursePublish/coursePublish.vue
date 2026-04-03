<template>
  <view class="page">
    <view class="top-back" @click="goBack">
      <text class="back-arrow">‹</text>
      <text class="back-text">返回</text>
    </view>
    <view class="card">
      <view class="title">上架课程</view>

      <view class="form-item">
        <text class="label">开课日期</text>
        <picker mode="date" :start="startDate" :end="endDate" @change="onDateChange">
          <view class="picker">{{ form.startDate || '请选择开课日期' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">授课科目</text>
        <picker mode="selector" :range="subjectOptions" @change="onSubjectChange">
          <view class="picker">{{ selectedSubject || '请选择科目（可选）' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">授课年级</text>
        <picker mode="selector" :range="gradeOptions" @change="onGradeChange">
          <view class="picker">{{ selectedGrade || '请选择年级（可选）' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">课时费（元）</text>
        <input v-model="form.hourlyRate" type="digit" class="input" placeholder="请输入课时费用" />
      </view>

      <view class="form-item">
        <text class="label">预期课时</text>
        <input v-model="form.expectedHours" type="number" class="input" placeholder="例如 1 / 2 / 3" />
      </view>

      <view class="form-item">
        <text class="label">课程说明/上课地址</text>
        <textarea v-model="form.address" class="textarea" maxlength="200" placeholder="请输入课程简介或授课地址"></textarea>
      </view>

      <button class="submit" @click="submit">发布课程</button>
    </view>
  </view>
</template>

<script>
import { publishTeacherCourse } from '@/api/course/course'
import { findAllSubject } from '@/api/teacher/subject'
import { findAllGrade } from '@/api/teacher/grade'
import { getTeacherJoinStatus } from '@/api/teacherJoin/teacherJoin'

export default {
  data() {
    return {
      form: {
        startDate: '',
        subjectId: '',
        gradeId: '',
        hourlyRate: '',
        expectedHours: '1',
        address: ''
      },
      startDate: '',
      endDate: '',
      subjectRows: [],
      gradeRows: [],
      selectedSubject: '',
      selectedGrade: ''
    }
  },
  computed: {
    subjectOptions() {
      return this.subjectRows.map(i => i.name)
    },
    gradeOptions() {
      return this.gradeRows.map(i => i.name)
    }
  },
  async onLoad() {
    const pages = getCurrentPages()
    const current = pages && pages.length ? pages[pages.length - 1] : null
    this._fromTab = !!(current && current.options && current.options.from === 'tab')
    const now = new Date()
    this.startDate = this.formatDate(now)
    this.endDate = this.formatDate(new Date(now.getTime() + 180 * 24 * 3600 * 1000))
    const ok = await this.checkTeacherApproved()
    if (!ok) return
    this.loadOptions()
  },
  onShow() {
    try {
      const roles = (this.$store && this.$store.getters && this.$store.getters.roles) || []
      uni.setTabBarItem({
        index: 1,
        text: roles.includes('teacher') ? '发布课程' : '找老师'
      })
    } catch (e) {}
  },
  methods: {
    goBack() {
      if (this._fromTab) {
        uni.switchTab({ url: '/pages/index/index' })
        return
      }
      const pages = getCurrentPages()
      if (pages && pages.length > 1) {
        const prev = ((pages[pages.length - 2] || {}).route || '')
        if (prev.indexOf('pages/findteacher/findteacher/findteacher') === 0) {
          uni.switchTab({ url: '/pages/index/index' })
          return
        }
        uni.navigateBack()
        return
      }
      uni.switchTab({ url: '/pages/index/index' })
    },
    formatDate(d) {
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    },
    async checkTeacherApproved() {
      const roles = (this.$store && this.$store.getters && this.$store.getters.roles) || []
      if (!roles.includes('teacher')) {
        uni.showToast({ title: '仅老师账号可发布课程', icon: 'none' })
        setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 600)
        return false
      }
      try {
        const res = await getTeacherJoinStatus()
        const d = res.data || {}
        if (!d.approved) {
          uni.showModal({
            title: '暂不可发布',
            content: '请先完成老师入驻并等待管理员审核通过。',
            showCancel: false,
            success: () => uni.navigateTo({ url: '/pages/teacherJoin/teacherJoin' })
          })
          return false
        }
      } catch (e) {
        uni.showToast({ title: '状态校验失败，请重试', icon: 'none' })
        return false
      }
      return true
    },
    async loadOptions() {
      try {
        const [s, g] = await Promise.all([findAllSubject(), findAllGrade()])
        this.subjectRows = s.data || []
        this.gradeRows = g.data || []
      } catch (e) {}
    },
    onDateChange(e) {
      this.form.startDate = e.detail.value
    },
    onSubjectChange(e) {
      const row = this.subjectRows[Number(e.detail.value)]
      if (!row) return
      this.form.subjectId = String(row.subjectId)
      this.selectedSubject = row.name
    },
    onGradeChange(e) {
      const row = this.gradeRows[Number(e.detail.value)]
      if (!row) return
      this.form.gradeId = String(row.gradeId)
      this.selectedGrade = row.name
    },
    submit() {
      if (!this.form.startDate) {
        uni.showToast({ title: '请选择开课日期', icon: 'none' })
        return
      }
      if (!this.form.hourlyRate) {
        uni.showToast({ title: '请输入课时费', icon: 'none' })
        return
      }
      if (!this.form.address) {
        uni.showToast({ title: '请输入课程说明或地址', icon: 'none' })
        return
      }
      uni.showLoading({ title: '发布中...' })
      publishTeacherCourse(this.form).then(res => {
        uni.hideLoading()
        if (res.code === 200) {
          uni.showModal({
            title: '发布成功',
            content: '课程已上架成功。',
            showCancel: false,
            success: () => uni.switchTab({ url: '/pages/index/index' })
          })
        } else {
          uni.showToast({ title: res.msg || '发布失败', icon: 'none' })
        }
      }).catch(() => {
        uni.hideLoading()
      })
    }
  }
}
</script>

<style>
page { background: #f6f7fb; }
.page { padding: 24rpx; }
.top-back {
  display: inline-flex;
  align-items: center;
  padding: 8rpx 6rpx 18rpx 2rpx;
}
.back-arrow {
  font-size: 44rpx;
  line-height: 1;
  color: #333;
}
.back-text {
  margin-left: 8rpx;
  font-size: 26rpx;
  color: #333;
}
.card { background: #fff; border-radius: 16rpx; padding: 28rpx; }
.title { font-size: 34rpx; font-weight: 600; color: #333; margin-bottom: 20rpx; }
.form-item { margin-bottom: 24rpx; }
.label { font-size: 28rpx; color: #555; margin-bottom: 12rpx; display: block; }
.picker, .input { height: 84rpx; line-height: 84rpx; background: #f7f8fb; border-radius: 10rpx; padding: 0 20rpx; font-size: 28rpx; color: #333; }
.textarea { width: 100%; min-height: 180rpx; background: #f7f8fb; border-radius: 10rpx; padding: 16rpx 20rpx; font-size: 28rpx; box-sizing: border-box; }
.submit { margin-top: 12rpx; background: #ffb800; color: #fff; border-radius: 44rpx; height: 88rpx; line-height: 88rpx; font-size: 30rpx; }
</style>
