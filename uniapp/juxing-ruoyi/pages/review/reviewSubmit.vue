<template>
  <view class="page">
    <view class="card">
      <text class="label">预约编号</text>
      <text class="val">{{ bookingId || '-' }}</text>
    </view>
    <view class="card">
      <text class="label">评分（1～5）</text>
      <picker mode="selector" :range="scores" :value="scoreIndex" @change="onScoreChange">
        <view class="picker">{{ scores[scoreIndex] }} 分</view>
      </picker>
    </view>
    <view class="card">
      <text class="label">评价内容</text>
      <textarea class="textarea" v-model="comment" placeholder="说说本次课程体验（选填）" maxlength="500" />
    </view>
    <button class="submit" type="primary" @click="submit">提交评价</button>
  </view>
</template>

<script>
import { submitReviewApp } from '@/api/review/review'

export default {
  data() {
    return {
      bookingId: '',
      scores: ['5', '4', '3', '2', '1'],
      scoreIndex: 0,
      comment: ''
    }
  },
  onLoad(options) {
    this.bookingId = (options && options.bookingId) ? String(options.bookingId) : ''
  },
  methods: {
    onScoreChange(e) {
      this.scoreIndex = Number(e.detail.value)
    },
    submit() {
      if (!this.bookingId) {
        uni.showToast({ title: '缺少预约信息', icon: 'none' })
        return
      }
      const rating = this.scores[this.scoreIndex]
      submitReviewApp({
        bookingId: this.bookingId,
        rating,
        comment: this.comment
      }).then(() => {
        uni.showToast({ title: '提交成功', icon: 'success' })
        setTimeout(() => {
          uni.navigateBack({ fail: () => uni.switchTab({ url: '/pages/mine/index' }) })
        }, 800)
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.page { padding: 24rpx; background: #f5f5f5; min-height: 100vh; }
.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}
.label { display: block; font-size: 26rpx; color: #999; margin-bottom: 12rpx; }
.val { font-size: 30rpx; color: #333; }
.picker { padding: 16rpx 0; font-size: 30rpx; color: #333; }
.textarea {
  width: 100%;
  min-height: 200rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}
.submit {
  margin-top: 40rpx;
  background: linear-gradient(to right, #FFD700, #FFA500);
  color: #333;
  border: none;
  border-radius: 48rpx;
}
</style>
