<template>
	<view class="page">
		<!-- 顶部个人信息区域 -->
		<view class="header">
			<view class="user-info" @click="handleUserClick">
				<image class="avatar" :src="avatar" mode="aspectFill" />
				<view class="info">
					<view v-if="!username" class="login-tip">
						点击登录
					</view>
					<view v-else>
						<view class="username">用户名：{{ username }}</view>
						<view class="more">个人信息 ></view>
					</view>
				</view>
			</view>
		</view>

		<!-- 功能区域 -->
		<view class="function-container">
			<view class="function-box" @click="handleToEditInfo">
				<uni-icons type="person" size="22" color="#666" />
				<text class="function-text">编辑资料</text>
				<uni-icons type="right" size="16" color="#ccc" />
			</view>

			<view class="function-box" @click="handleOrderClick">
				<uni-icons type="shop" size="22" color="#666" />
				<text class="function-text">我的订单</text>
				<uni-icons type="right" size="16" color="#ccc" />
			</view>

			<view class="function-box" @click="handleVipClick">
				<uni-icons type="vip" size="22" color="#666" />
				<text class="function-text">我的会员卡</text>
				<uni-icons type="right" size="16" color="#ccc" />
			</view>

			<view class="function-box" @click="handleAbout">
				<uni-icons type="info" size="22" color="#666" />
				<text class="function-text">关于我们</text>
				<uni-icons type="right" size="16" color="#ccc" />
			</view>

			<view class="function-box" @click="handleToSetting">
				<uni-icons type="settings" size="22" color="#666" />
				<text class="function-text">应用设置</text>
				<uni-icons type="right" size="16" color="#ccc" />
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {}
		},
		computed: {
			avatar() {
				return this.$store.state.user.avatar || '/static/default-avatar.png'
			},
			username() {
				return this.$store.state.user.name
			}
		},
		methods: {
			handleUserClick() {
				if (!this.username) {
					this.handleToLogin()
				} else {
					this.handleToInfo()
				}
			},
			handleToInfo() {
				this.$tab.navigateTo('/pages/mine/info/index')
			},
			handleToEditInfo() {
				this.$tab.navigateTo('/pages/mine/info/edit')
			},
			handleToSetting() {
				this.$tab.navigateTo('/pages/mine/setting/index')
			},
			handleToLogin() {
				this.$tab.reLaunch('/pages/login')
			},
			handleToAvatar() {
				this.$tab.navigateTo('/pages/mine/avatar/index')
			},
			handleAbout() {
				this.$tab.navigateTo('/pages/mine/about/index')
			},
			handleOrderClick() {
				this.$tab.navigateTo('/pages/mine/order/order')
			},
			handleVipClick() {
				uni.showToast({ title: '会员卡功能暂未开放', icon: 'none' })
			},
			
		}
	}
</script>

<style>
	page {
		background-color: #FFF9E6;
	}

	.page {
		min-height: 100vh;
		padding: 20rpx;
	}

	.header {
		background: linear-gradient(to right, #FFD700, #FFA500);
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 10rpx rgba(255, 165, 0, 0.2);
	}

	.user-info {
		display: flex;
		align-items: center;
	}

	.avatar {
		width: 140rpx;
		height: 140rpx;
		border-radius: 50%;
		margin-right: 30rpx;
		border: 4rpx solid rgba(255, 255, 255, 0.5);
		background-color: #fff;
		box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.1);
	}

	.info {
		flex: 1;
	}

	.login-tip {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
	}

	.username {
		font-size: 36rpx;
		color: #333;
		font-weight: 600;
		margin-bottom: 8rpx;
	}

	.more {
		font-size: 28rpx;
		color: #666;
	}

	/* 功能区域样式 */
	.function-container {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.function-box {
		display: flex;
		align-items: center;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		padding: 28rpx 30rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
		transition: all 0.2s;
	}

	.function-box:active {
		background-color: #f5f5f5;
		transform: translateY(2rpx);
	}

	.function-text {
		flex: 1;
		font-size: 32rpx;
		color: #333;
		margin-left: 20rpx;
	}
</style>