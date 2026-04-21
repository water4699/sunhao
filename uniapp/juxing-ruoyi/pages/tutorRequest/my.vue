<template>
  <view class="page">
    <scroll-view class="list" scroll-y @scrolltolower="loadMore">
      <view v-for="item in list" :key="item.requestId" class="card">
        <view class="row between">
          <text>需求单：{{ item.requestId }}</text>
          <text class="status">{{ statusText(item.status) }}</text>
        </view>
        <view class="row">科目：{{ item.subjectName || '-' }}</view>
        <view class="row">年级：{{ item.gradeName || '-' }}</view>
        <view class="row">预算：¥{{ item.budgetMin || 0 }} - ¥{{ item.budgetMax || 0 }}/课时</view>
        <view class="row">时段：{{ item.preferredTime || '-' }}</view>
        <view class="row">说明：{{ item.demandDesc || '-' }}</view>
        <view class="ops">
          <button v-if="Number(item.status) === 0" class="btn reject" size="mini" @click="closeReq(item)">关闭</button>
          <button v-if="Number(item.status) === 0" class="btn approve" size="mini" @click="openBids(item)">查看接单</button>
        </view>
      </view>
      <view v-if="status === 'loading'" class="more">加载中...</view>
      <view v-if="status === 'nomore'" class="more">没有更多了</view>
    </scroll-view>
  </view>
</template>

<script>
import { listMyTutorRequests, closeTutorRequest, listTutorRequestBids, confirmTutorRequestBid } from '@/api/tutorRequest/tutorRequest'

export default {
  data() {
    return {
      list: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      status: 'loadmore'
    }
  },
  onShow() {
    this.pageNum = 1
    this.list = []
    this.status = 'loadmore'
    this.loadMore()
  },
  methods: {
    statusText(s) {
      const n = Number(s)
      if (n === 0) return '待接单'
      if (n === 1) return '已接单'
      if (n === 2) return '已关闭'
      if (n === 3) return '已取消'
      return '-'
    },
    async loadMore() {
      if (this.status === 'loading' || this.status === 'nomore') return
      this.status = 'loading'
      try {
        const res = await listMyTutorRequests({ pageNum: this.pageNum, pageSize: this.pageSize })
        const rows = res.rows || []
        if (this.pageNum === 1) this.list = rows
        else this.list = this.list.concat(rows)
        this.total = res.total || 0
        this.pageNum += 1
        this.status = (rows.length === 0 || this.list.length >= this.total) ? 'nomore' : 'loadmore'
      } catch (e) {
        this.status = 'loadmore'
      }
    },
    closeReq(item) {
      uni.showModal({
        title: '关闭需求',
        content: '确定关闭该需求单吗？',
        success: async (r) => {
          if (!r.confirm) return
          try {
            await closeTutorRequest(item.requestId)
            uni.showToast({ title: '已关闭', icon: 'success' })
            this.pageNum = 1
            this.list = []
            this.status = 'loadmore'
            this.loadMore()
          } catch (e) {}
        }
      })
    },
    async openBids(item) {
      try {
        const res = await listTutorRequestBids(item.requestId, { pageNum: 1, pageSize: 20 })
        const bids = res.rows || []
        if (!bids.length) {
          return uni.showToast({ title: '暂无老师接单', icon: 'none' })
        }
        const choices = bids.map(i => `${i.teacherName || '老师'} | ¥${i.expectedRate || '-'} | ${i.message || '无留言'}`)
        uni.showActionSheet({
          itemList: choices,
          success: async ({ tapIndex }) => {
            const picked = bids[tapIndex]
            if (!picked) return
            uni.showModal({
              title: '确认该老师接单？',
              content: `${picked.teacherName || '老师'}（确认后需求单关闭）`,
              success: async (r) => {
                if (!r.confirm) return
                try {
                  await confirmTutorRequestBid(picked.bidId)
                  uni.showToast({ title: '已确认接单', icon: 'success' })
                  this.pageNum = 1
                  this.list = []
                  this.status = 'loadmore'
                  this.loadMore()
                } catch (e) {}
              }
            })
          }
        })
      } catch (e) {}
    }
  }
}
</script>

<style>
page { background: #f5f5f5; }
.page { height: 100%; }
.list { height: 100%; padding: 20rpx; box-sizing: border-box; }
.card { background: #fff; border-radius: 14rpx; padding: 20rpx; margin-bottom: 16rpx; }
.row { font-size: 26rpx; color: #333; margin-bottom: 8rpx; }
.row.between { display: flex; justify-content: space-between; }
.status { color: #fa8c16; }
.ops { display: flex; justify-content: flex-end; gap: 12rpx; margin-top: 8rpx; }
.btn.reject { background: #fff1f0; color: #f5222d; border: 1rpx solid #f5222d; }
.btn.approve { background: #2d8cf0; color: #fff; }
.more { text-align: center; color: #999; padding: 20rpx 0; }
</style>
