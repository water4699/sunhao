<template>
	<view class="wrap">
		<view class="tip">会员卡功能暂未开放，后续对接后端后可在此恢复业务页。</view>
		<button class="btn" @click="goBack">返回</button>
	</view>
</template>

<script>
	export default {
		onLoad() {
			uni.setNavigationBarTitle({ title: '我的会员卡' })
		},
		methods: {
			goBack() {
				uni.navigateBack({
					fail: () => {
						uni.switchTab({ url: '/pages/mine/index' })
					}
				})
			}
		}
	}
</script>

<style scoped>
	.wrap {
		padding: 48rpx;
		min-height: 60vh;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		box-sizing: border-box;
	}

	.tip {
		color: #666;
		font-size: 30rpx;
		text-align: center;
		line-height: 1.6;
		margin-bottom: 48rpx;
	}

	.btn {
		width: 60%;
		background: linear-gradient(to right, #FFD700, #FFA500);
		color: #333;
		font-size: 28rpx;
		border: none;
		border-radius: 12rpx;
	}
</style>
