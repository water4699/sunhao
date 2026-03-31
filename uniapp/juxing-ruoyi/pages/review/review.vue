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
		getReviewMag
	} from '@/api/review/review'
	import {
		baseUrl
	} from '../../config'
	
export default {
  data() {
    return {
      // 评论列表数据保持不变
      reviewList: [
        {
          studentName: "陈雅婷",
          createdAt: "2024-01-15 14:30",
          rating: 4.8,
          comment: "王老师的教学方法非常适合我家孩子，讲课细致入微，善于引导思考。经过半年的辅导，孩子的数学成绩有了很大提升，特别感谢！",
        },
      ],
      isLoadingMore: false,
    };
  },
mounted() {
			this.init();
		},
  methods: {
	  init() {
	  	getReviewMag().then(res => {
	  		this.reviewList = res.rows;
	  	})
	  },
    // 下拉刷新逻辑保持不变
    onPullDownRefresh() {
      setTimeout(() => {
        this.commentList.unshift({
          avatar: "/static/default-avatar.png",
          username: "新用户",
          time: "2024-01-16 10:00",
          score: 4.6,
          content: "新的评价内容...",
        });
        uni.stopPullDownRefresh();
      }, 1000);
    },
    // 上拉加载更多逻辑保持不变
    onReachBottom() {
      this.isLoadingMore = true;
      setTimeout(() => {
        const moreData = {
          avatar: "/static/default-avatar.png",
          username: "更多用户",
          time: "2024-01-17 11:00",
          score: 4.5,
          content: "更多评价内容...",
        };
        this.commentList.push(moreData);
        this.isLoadingMore = false;
      }, 1000);
    },
    // 点击头像查看用户详情保持不变
    goUserDetail() {
      uni.navigateTo({
        url: "/pages/user-detail/user-detail",
      });
    },
    // 获取评论列表逻辑保持不变
    getCommentList() {
      // 实际项目中调用后端接口
    },
  },
  // 页面事件处理函数保持不变
  onPullDownRefresh() {
    this.onPullDownRefresh();
  },
  onReachBottom() {
    this.onReachBottom();
  },
};
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