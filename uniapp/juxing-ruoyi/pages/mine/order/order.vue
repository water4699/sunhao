<template>
  <view class="page">
    <!-- 订单状态筛选 -->
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

    <!-- 订单列表 - 空状态处理 -->
    <view v-if="filteredOrderList.length === 0 && isLoaded" class="empty-state">
      <image src="https://picsum.photos/120/120?random=empty" mode="aspectFit" class="empty-icon"></image>
      <text class="empty-text">没有找到相关订单</text>
    </view>

    <scroll-view 
      v-else
      class="order-list"
      scroll-y
      @scrolltolower="loadMore"
      @refresherrefresh="onRefresh"
      :refresher-enabled="true"
      :refresher-triggered="isRefreshing"
    >
      <view class="order-item" v-for="(order, index) in filteredOrderList" :key="order.id">
        <view class="order-header">
          <view class="order-number">订单号：{{ order.orderNumber }}</view>
          <view :class="['order-status', getStatusClass(order.status)]">
            {{ order.status }}
          </view>
        </view>
        <view class="order-content">
          <view class="order-info">
            <view class="teacher-name">{{ order.teacherName }}</view>
            <view class="order-time">{{ order.orderTime }}</view>
          </view>
          <view class="order-price">
            <text class="price-label">￥</text>
            <text class="price-value">{{ order.price }}</text>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentStatusId: 0, // 当前选中的状态ID（0:全部 1:未付款 2:已完成）
      isRefreshing: false,
      isLoaded: false,
      tabs: [
        { id: 0, name: '全部订单', count: 0 },
        { id: 1, name: '未付款', count: 0 },
        { id: 2, name: '已完成', count: 0 }
      ],
      mockOrders: [
        { id: 1, orderNumber: 'ORD20240101', teacherName: '数学王老师', orderTime: '2024-01-05 10:30', status: '未付款', price: 180, statusId: 1 },
        { id: 2, orderNumber: 'ORD20240102', teacherName: '英语李老师', orderTime: '2024-01-04 14:15', status: '已完成', price: 220, statusId: 2 },
        { id: 3, orderNumber: 'ORD20240103', teacherName: '物理张老师', orderTime: '2024-01-03 09:45', status: '未付款', price: 200, statusId: 1 },
        { id: 4, orderNumber: 'ORD20240104', teacherName: '语文刘老师', orderTime: '2024-01-02 16:20', status: '已完成', price: 190, statusId: 2 },
        { id: 5, orderNumber: 'ORD20240105', teacherName: '化学陈老师', orderTime: '2024-01-01 13:00', status: '已付款', price: 210, statusId: 3 }, // 新增已付款状态
        { id: 6, orderNumber: 'ORD20240106', teacherName: '生物周老师', orderTime: '2023-12-30 11:10', status: '已取消', price: 230, statusId: 4 }, // 新增已取消状态
        { id: 7, orderNumber: 'ORD20240107', teacherName: '历史赵老师', orderTime: '2023-12-29 09:30', status: '未付款', price: 170, statusId: 1 },
        { id: 8, orderNumber: 'ORD20240108', teacherName: '地理钱老师', orderTime: '2023-12-28 14:45', status: '已完成', price: 240, statusId: 2 },
        { id: 9, orderNumber: 'ORD20240109', teacherName: '政治孙老师', orderTime: '2023-12-27 16:10', status: '已付款', price: 190, statusId: 3 }
      ],
      orderList: [],
      page: 1,
      pageSize: 4
    }
  },

  mounted() {
    this.initData();
    this.loadOrderList();
  },

  computed: {
    filteredOrderList() {
      if (this.currentStatusId === 0) return this.orderList; // 全部订单显示所有状态
      return this.orderList.filter(order => order.statusId === this.currentStatusId);
    }
  },

  methods: {
    initData() {
      // 计算各状态订单数量
      const statusCount = {
        0: this.mockOrders.length, // 全部订单总数
        1: this.mockOrders.filter(o => o.statusId === 1).length, // 未付款
        2: this.mockOrders.filter(o => o.statusId === 2).length, // 已完成
        3: this.mockOrders.filter(o => o.statusId === 3).length, // 已付款
        4: this.mockOrders.filter(o => o.statusId === 4).length  // 已取消
      };
      // 更新标签计数（仅显示未付款和已完成）
      this.tabs[0].count = statusCount[0];
      this.tabs[1].count = statusCount[1];
      this.tabs[2].count = statusCount[2];
    },

    handleTabChange(statusId) {
      this.currentStatusId = statusId;
      this.page = 1;
      this.loadOrderList();
    },

    getStatusClass(status) {
      const classMap = {
        '未付款': 'unpaid',
        '已完成': 'completed',
        '已付款': 'paid',
        '已取消': 'cancelled'
      };
      return classMap[status] || '';
    },

    loadOrderList() {
      const start = (this.page - 1) * this.pageSize;
      const end = start + this.pageSize;
      this.orderList = this.mockOrders.slice(start, end);
      this.isLoaded = true;
      this.page++;
    },

    loadMore() {
      const loadedCount = this.filteredOrderList.length;
      const totalCount = this.currentStatusId === 0 
        ? this.mockOrders.length 
        : this.mockOrders.filter(o => o.statusId === this.currentStatusId).length;
      if (loadedCount < totalCount) {
        this.loadOrderList();
      }
    },

    onRefresh() {
      this.page = 1;
      this.loadOrderList();
      this.isRefreshing = false;
    }
  }
}
</script>

<style>
/* 样式保持不变，新增已付款和已取消状态样式 */
page { height: 100%; }
.page { display: flex; flex-direction: column; height: 100%; background-color: #f5f5f5; }
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
.order-price { font-size: 14px; color: #FF6B00; }
.price-label { font-size: 12px; }
.price-value { font-size: 20px; font-weight: 500; }
.empty-state { flex: 1; display: flex; flex-direction: column; justify-content: center; align-items: center; padding: 40rpx; color: #999; }
.empty-icon { width: 160rpx; height: 160rpx; margin-bottom: 30rpx; }
.empty-text { font-size: 28rpx; }
</style>