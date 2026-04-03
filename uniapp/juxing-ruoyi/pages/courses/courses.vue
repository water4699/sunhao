<template>
  <view class="page">
    <view class="header">课程列表</view>
    <view v-if="list.length === 0" class="empty">暂无课程</view>
    <view v-for="item in list" :key="item.publishId || item.publish_id" class="card" @click="goDetail(item.publishId || item.publish_id)">
      <view class="title">{{ item.gradeName || item.grade_name || '' }} {{ item.subjectName || item.subject_name || '' }}</view>
      <view class="meta">老师：{{ item.teacherName || item.teacher_name || '-' }}</view>
      <view class="meta">开课：{{ item.startDate || '-' }}</view>
      <view class="meta">地址：{{ item.address || '-' }}</view>
      <view class="price">¥{{ item.hourlyRate || item.hourly_rate || 0 }}/课时</view>
    </view>
    <view v-if="hasMore" class="more" @click="loadMore">加载更多</view>
  </view>
</template>

<script>
import { listPublishedCourses } from '@/api/course/course'

export default {
  data() {
    return {
      list: [],
      pageNum: 1,
      pageSize: 10,
      hasMore: true
    }
  },
  onLoad() {
    this.fetchList(true)
  },
  onPullDownRefresh() {
    this.fetchList(true).finally(() => uni.stopPullDownRefresh())
  },
  methods: {
    async fetchList(reset = false) {
      if (reset) {
        this.pageNum = 1
        this.list = []
        this.hasMore = true
      }
      if (!this.hasMore) return
      try {
        const res = await listPublishedCourses({ pageNum: this.pageNum, pageSize: this.pageSize, status: 0 })
        const rows = Array.isArray(res)
          ? res
          : ((res && res.rows)
            || (res && res.data && res.data.rows)
            || (Array.isArray(res && res.data) ? res.data : []))
        this.list = reset ? rows : this.list.concat(rows)
        this.pageNum += 1
        this.hasMore = rows.length >= this.pageSize
      } catch (e) {
        uni.showToast({ title: '课程列表加载失败', icon: 'none' })
      }
    },
    loadMore() {
      this.fetchList(false)
    },
    goDetail(id) {
      uni.navigateTo({ url: `/pages/courses/courseDetail?id=${id}` })
    }
  }
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f6f7;
  padding: 24rpx;
}
.header {
  font-size: 34rpx;
  font-weight: 600;
  margin-bottom: 18rpx;
}
.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 16rpx;
}
.title {
  font-size: 30rpx;
  font-weight: 600;
  margin-bottom: 10rpx;
}
.meta {
  color: #666;
  font-size: 26rpx;
  margin-bottom: 6rpx;
}
.price {
  color: #f59a23;
  font-size: 28rpx;
  font-weight: 600;
}
.empty {
  color: #999;
  text-align: center;
  margin-top: 120rpx;
}
.more {
  text-align: center;
  color: #666;
  padding: 20rpx 0;
}
</style>
