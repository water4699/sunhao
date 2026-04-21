<template>
  <view class="page">
    <view class="title">{{ (detail.gradeName || detail.grade_name || '') + ' ' + (detail.subjectName || detail.subject_name || '') }}</view>
    <view class="item">老师：{{ detail.teacherName || detail.teacher_name || '-' }}</view>
    <view class="item">课时费：¥{{ detail.hourlyRate || detail.hourly_rate || 0 }}/课时</view>
    <view class="item">开课日期：{{ detail.startDate || '-' }}</view>
    <view class="item">课时数：{{ detail.expectedHours || '-' }}</view>
    <view class="item">上课地址：{{ detail.address || '-' }}</view>
    <view class="item">发布时间：{{ detail.createdAt || '-' }}</view>
  </view>
</template>

<script>
import { getPublishedCourseDetail } from '@/api/course/course'

export default {
  data() {
    return {
      id: '',
      detail: {}
    }
  },
  onLoad(query) {
    this.id = query.id || ''
    this.fetchDetail()
  },
  methods: {
    async fetchDetail() {
      if (!this.id) {
        uni.showToast({ title: '课程参数缺失', icon: 'none' })
        return
      }
      try {
        const res = await getPublishedCourseDetail(this.id)
        this.detail = res.data || {}
      } catch (e) {
        uni.showToast({ title: '课程详情加载失败', icon: 'none' })
      }
    }
  }
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #fff;
  padding: 28rpx;
}
.title {
  font-size: 34rpx;
  font-weight: 700;
  margin-bottom: 20rpx;
}
.item {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 14rpx;
}
</style>
