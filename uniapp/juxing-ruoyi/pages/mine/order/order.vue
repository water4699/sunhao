<template>
  <view class="page">
    <view class="main-tabs">
      <view
        :class="['main-tab', topTab === 0 ? 'active' : '']"
        @click="switchTop(0)"
      >课程预约</view>
      <view
        :class="['main-tab', topTab === 1 ? 'active' : '']"
        @click="switchTop(1)"
      >商品订单</view>
    </view>

    <!-- 课程预约 -->
    <view v-if="topTab === 0" class="sub-page">
      <view class="filter-section">
        <view class="filter-tabs">
          <view
            v-for="(tab, index) in tabs"
            :key="tab.id"
            :class="['tab-item', currentStatusId === tab.id ? 'active' : '']"
            @click="handleTabChange(tab.id)"
          >
            {{ tab.name }}
            <text v-if="tab.count > 0" class="tab-badge">{{ tab.count }}</text>
          </view>
        </view>
      </view>

      <view v-if="filteredOrderList.length === 0 && isLoaded" class="empty-state">
        <text class="empty-text">暂无课程预约记录</text>
        <text class="empty-hint">在「找老师」中预约后将显示在此</text>
      </view>

      <scroll-view
        v-else
        class="order-list"
        scroll-y
        @refresherrefresh="onRefresh"
        :refresher-enabled="true"
        :refresher-triggered="isRefreshing"
      >
        <view class="order-item" v-for="(order, index) in filteredOrderList" :key="order.id">
          <view class="order-header">
            <view class="order-number">预约号：{{ order.orderNumber }}</view>
            <view :class="['order-status', getStatusClass(order.status)]">
              {{ order.status }}
            </view>
          </view>
          <view class="order-content">
            <view class="order-info">
              <view class="teacher-name">{{ order.teacherName }}</view>
              <view class="order-time">{{ order.orderTime }}</view>
              <view v-if="order.address" class="order-addr">{{ order.address }}</view>
            </view>
            <view class="order-price">
              <text class="price-label">￥</text>
              <text class="price-value">{{ order.price }}</text>
            </view>
          </view>
          <view v-if="order.courseStatus === 1" class="row-actions">
            <text class="link-btn" @click.stop="goReview(order.id)">去评价</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 商品订单 -->
    <view v-else class="sub-page">
      <view v-if="productOrderList.length === 0 && productLoaded" class="empty-state">
        <text class="empty-text">暂无商品订单</text>
        <text class="empty-hint">在「进步商品」购买后将显示在此</text>
      </view>
      <scroll-view
        v-else
        class="order-list"
        scroll-y
        @refresherrefresh="onRefreshProduct"
        :refresher-enabled="true"
        :refresher-triggered="isRefreshingProduct"
      >
        <view class="order-item" v-for="po in productOrderList" :key="po.id">
          <view class="order-header">
            <view class="order-number">订单号：{{ po.orderNumber }}</view>
            <view class="order-status paid">已支付</view>
          </view>
          <view class="order-content">
            <view class="order-info">
              <view class="teacher-name">{{ po.title }}</view>
              <view class="order-time">{{ po.orderTime }}</view>
            </view>
            <view class="order-price">
              <text class="price-label">￥</text>
              <text class="price-value">{{ po.price }}</text>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { mapState } from 'vuex'
import { listCourse } from '@/api/course/course'
import { listAppProductOrder } from '@/api/order/order'

export default {
  data() {
    return {
      topTab: 0,
      currentStatusId: 0,
      isRefreshing: false,
      isRefreshingProduct: false,
      isLoaded: false,
      productLoaded: false,
      tabs: [
        { id: 0, name: '全部', count: 0 },
        { id: 1, name: '待确认', count: 0 },
        { id: 2, name: '已确认', count: 0 }
      ],
      orderList: [],
      productOrderList: [],
      pageSize: 50
    }
  },
  computed: {
    ...mapState({
      userId: 'id'
    }),
    filteredOrderList() {
      if (this.currentStatusId === 0) {
        return this.orderList
      }
      if (this.currentStatusId === 1) {
        return this.orderList.filter(o => o.courseStatus === 0)
      }
      if (this.currentStatusId === 2) {
        return this.orderList.filter(o => o.courseStatus === 1)
      }
      return this.orderList
    }
  },
  mounted() {
    this.fetchCourses()
  },
  onShow() {
    if (this.userId) {
      this.fetchCourses()
      if (this.topTab === 1) {
        this.fetchProductOrders()
      }
    }
  },
  watch: {
    userId(val) {
      if (val) {
        this.isLoaded = false
        this.productLoaded = false
        this.fetchCourses()
        if (this.topTab === 1) {
          this.fetchProductOrders()
        }
      }
    }
  },
  methods: {
    switchTop(tab) {
      this.topTab = tab
      if (tab === 1) {
        this.fetchProductOrders()
      }
    },
    goReview(bookingId) {
      uni.navigateTo({
        url: '/pages/review/reviewSubmit?bookingId=' + encodeURIComponent(bookingId)
      })
    },
    statusText(s) {
      const n = Number(s)
      if (n === 0) return '待确认'
      if (n === 1) return '已确认'
      if (n === 2) return '已取消'
      return '进行中'
    },
    mapRow(row) {
      return {
        id: row.courseId,
        orderNumber: String(row.courseId),
        teacherName: '教师 ' + (row.teacherId || '-'),
        orderTime: row.startDate || '',
        status: this.statusText(row.status),
        courseStatus: Number(row.status),
        price: row.hourlyRate,
        address: row.address || ''
      }
    },
    updateTabCounts() {
      this.tabs[0].count = this.orderList.length
      this.tabs[1].count = this.orderList.filter(o => o.courseStatus === 0).length
      this.tabs[2].count = this.orderList.filter(o => o.courseStatus === 1).length
    },
    fetchCourses() {
      if (!this.userId) {
        this.orderList = []
        this.isLoaded = true
        this.updateTabCounts()
        return
      }
      listCourse({
        pageNum: 1,
        pageSize: this.pageSize,
        studentId: String(this.userId)
      }).then(res => {
        const rows = res.rows || []
        this.orderList = rows.map(r => this.mapRow(r))
        this.isLoaded = true
        this.updateTabCounts()
      }).catch(() => {
        this.isLoaded = true
      })
    },
    fetchProductOrders() {
      if (!this.userId) {
        this.productOrderList = []
        this.productLoaded = true
        return
      }
      listAppProductOrder({
        pageNum: 1,
        pageSize: this.pageSize
      }).then(res => {
        const rows = res.rows || []
        this.productOrderList = rows.map(r => ({
          id: r.orderId,
          orderNumber: String(r.orderId),
          title: r.productName ? r.productName : '商品 ID：' + (r.courseId || '-'),
          orderTime: r.createdAt || '',
          price: r.finalAmount != null ? r.finalAmount : r.amount
        }))
        this.productLoaded = true
      }).catch(() => {
        this.productLoaded = true
      })
    },
    handleTabChange(statusId) {
      this.currentStatusId = statusId
    },
    getStatusClass(status) {
      const classMap = {
        待确认: 'unpaid',
        已确认: 'completed',
        已取消: 'cancelled',
        进行中: 'paid'
      }
      return classMap[status] || ''
    },
    onRefresh() {
      this.isRefreshing = true
      this.fetchCourses()
      this.$nextTick(() => {
        this.isRefreshing = false
      })
    },
    onRefreshProduct() {
      this.isRefreshingProduct = true
      this.fetchProductOrders()
      this.$nextTick(() => {
        this.isRefreshingProduct = false
      })
    }
  }
}
</script>

<style>
page { height: 100%; }
.page { display: flex; flex-direction: column; height: 100%; background-color: #f5f5f5; }
.main-tabs { display: flex; background: #fff; border-bottom: 1px solid #eee; }
.main-tab { flex: 1; text-align: center; padding: 28rpx 0; font-size: 30rpx; color: #666; }
.main-tab.active { color: #FFB800; font-weight: 600; border-bottom: 4rpx solid #FFB800; }
.sub-page { flex: 1; display: flex; flex-direction: column; min-height: 0; }
.filter-section { background-color: #fff; padding: 20rpx 30rpx; }
.filter-tabs { display: flex; border-bottom: 1px solid #eee; }
.tab-item { position: relative; padding: 20rpx 40rpx; font-size: 14px; color: #666; }
.tab-item.active { color: #FFB800; }
.tab-item.active::after { content: ''; position: absolute; bottom: -1px; left: 40rpx; right: 40rpx; height: 2px; background-color: #FFB800; }
.tab-badge { display: inline-block; margin-left: 8rpx; padding: 2rpx 8rpx; background-color: #FFB800; color: #fff; font-size: 10px; border-radius: 4rpx; }
.order-list { flex: 1; overflow: auto; padding: 20rpx; }
.order-item { background-color: #fff; border-radius: 12rpx; padding: 30rpx; margin-bottom: 20rpx; }
.order-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20rpx; }
.order-number { font-size: 14px; color: #666; }
.order-status { padding: 4rpx 16rpx; border-radius: 4rpx; font-size: 12px; }
.unpaid { background-color: #E8F6F1; color: #07C160; }
.completed { background-color: #E6F7FF; color: #4080FF; }
.paid { background-color: #FFF7E6; color: #FFB800; }
.cancelled { background-color: #FFF0F0; color: #FF5252; }
.order-content { display: flex; justify-content: space-between; align-items: flex-end; }
.order-info { flex: 1; }
.teacher-name { font-size: 16px; color: #333; margin-bottom: 10rpx; }
.order-time { font-size: 12px; color: #999; }
.order-addr { font-size: 12px; color: #666; margin-top: 8rpx; }
.empty-hint { font-size: 24rpx; color: #bbb; margin-top: 16rpx; }
.order-price { font-size: 14px; color: #FF6B00; }
.price-label { font-size: 12px; }
.price-value { font-size: 20px; font-weight: 500; }
.empty-state { flex: 1; display: flex; flex-direction: column; justify-content: center; align-items: center; padding: 40rpx; color: #999; }
.empty-text { font-size: 28rpx; }
.row-actions { margin-top: 20rpx; padding-top: 16rpx; border-top: 1px solid #f0f0f0; }
.link-btn { font-size: 28rpx; color: #FFB800; }
</style>
