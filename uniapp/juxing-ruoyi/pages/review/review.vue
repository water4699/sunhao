<template>
  <view class="teacher-evaluation">
    <!-- 评论列表 -->
    <view class="comment-list">
      <!-- 循环渲染评论数据 -->
      <view 
        class="comment-item" 
        v-for="(item, index) in reviewList" 
        :key="index"
      >
        <!-- 用户信息区 -->
        <view class="user-info">
        <!--  <image 
            class="avatar" 
            :src="item.avatar" 
            mode="aspectFill"
            @click="goUserDetail"
          ></image> -->
          <view class="user-text">
            <text class="username">{{ item.studentName }}</text>
            <text class="comment-time">{{ item.createdAt }}</text>
          </view>
          <text class="score">{{ item.rating }}分</text>
        </view>

        <!-- 评论内容区 -->
        <view class="comment-content">
          <text>  {{item.comment }}</text>
        </view>
      </view>

      <!-- 加载更多提示 -->
      <view class="load-more" v-if="isLoadingMore">加载中...</view>
    </view>
  </view>
</template>

<script>
	import {
		getReviewAppList
	} from '@/api/review/review'

export default {
  data() {
    return {
      reviewList: [],
      isLoadingMore: false
    }
  },
  mounted() {
    this.loadList()
  },
  onPullDownRefresh() {
    this.loadList().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    loadList() {
      return getReviewAppList({
        pageNum: 1,
        pageSize: 50
      }).then(res => {
        this.reviewList = res.rows || []
      }).catch(() => {
        this.reviewList = []
      })
    },
    goUserDetail() {
      uni.navigateTo({
        url: '/pages/user-detail/user-detail'
      })
    }
  }
}
</script>

<style scoped>
/* 页面整体样式保持不变 */
.teacher-evaluation {
  background-color: #fff;
}

/* 评论列表样式保持不变 */
.comment-list {
  padding: 10px;
}
.comment-item {
  background-color: #fff;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 10px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* 用户信息区样式保持不变 */
.user-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.user-text {
  flex: 1;
  margin-left: 8px;
}
.username {
  font-weight: bold;
  font-size: 15px;
  color: #333;
}
.comment-time {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
  display: block;
}
.score {
  font-size: 15px;
  color: #ff9900;
  font-weight: bold;
}

/* 评论内容区样式保持不变 */
.comment-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

/* 加载更多样式保持不变 */
.load-more {
  text-align: center;
  padding: 10px;
  color: #999;
}
</style>