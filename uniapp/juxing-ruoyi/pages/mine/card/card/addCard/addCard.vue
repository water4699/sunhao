<template>
	<view class="page">
		<view class="header">
			<text class="title">精选课程卡</text>
			<text class="subtitle">为您推荐高性价比学习方案</text>
		</view>

		<scroll-view class="card-list" scroll-y>
			<view class="card-item" v-for="(card, index) in cardList" :key="index"
				:class="cardTypeClasses[index % cardTypeClasses.length]" @tap="handleCardTap(index)">
				<view class="card-content">
					<view class="card-header">
						<view class="card-title">{{ card.cardName }}</view>
						<view class="price-group">
							<text class="original-price">¥{{ card.price }}</text>
							<text class="current-price">¥{{ card.price }}</text>
						</view>
					</view>

					<view class="card-features">
						<view class="feature-item">
							<uni-icons :type="card.icon1" size="20" :color="card.iconColor" />
							<text class="feature-text">{{ card.validity }}</text>
						</view>
						<view class="feature-item">
							<uni-icons :type="card.icon2" size="20" :color="card.iconColor" />
							<text class="feature-text">{{ card.description }}</text>
						</view>
						<view class="feature-item">
							<uni-icons :type="card.icon3" size="20" :color="card.iconColor" />
							<text class="feature-text">{{ card.durationCount }}</text>
						</view>
					</view>

					<view class="card-footer">
						<view class="price-info">
							<text class="price-per-class">{{ card.discountRate }}</text>
						</view>
						<button class="purchase-btn" @click="handlePurchase(card)">
							<text>立即购买</text>
							<uni-icons type="arrowright" size="18" color="#fff" />
						</button>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import {
		getCardMag
	} from '@/api/card/getCardMag'
	import {
		baseUrl
	} from '../../../../../config'

	export default {
		data() {
			return {

				cardList: [{
					cardId: 'C001',
					cardName: '初高中1对1辅导课时卡',
					price: 360,
					price: 288,
					discountRate: 80,
					durationCount: "30次",
					description: "专业教师团队，个性化辅导方案",
					discountRate: 9.60
				}],

				// 定义类名映射关系，替代方法调用
				cardTypeClasses: ['card-type1', 'card-type2', 'card-type3']
			}
		},
		// onLoad() {
		// 	this.loadData()
		// },
		mounted() {

			this.init();
		},
		methods: {
			init() {
				getCardMag().then(res => {
					this.cardList = res.rows;
				})
			},
			handleCardTap(index) {
				uni.showToast({
					title: `查看${this.cards[index].title}详情`,
					icon: 'none'
				});
			},
			handlePurchase(card) {
				uni.navigateTo({
					url: `/pages/mine/card/card/addCard/addCardOrder?id=${card.cardId}`
				});
			}
		}
	}
</script>

<style>
	page {
		height: 100%;
		background-color: #F5F7FA;
	}

	.page {
		height: 100%;
		display: flex;
		flex-direction: column;
	}

	.header {
		padding: 40rpx 30rpx 30rpx;
		background: linear-gradient(135deg, #409EFF, #69b1ff);
		border-radius: 0 0 30rpx 30rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 6rpx 20rpx rgba(64, 158, 255, 0.2);
	}

	.title {
		font-size: 38rpx;
		font-weight: bold;
		color: #fff;
		margin-bottom: 10rpx;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
	}

	.subtitle {
		font-size: 26rpx;
		color: rgba(255, 255, 255, 0.9);
	}

	.card-list {
		flex: 1;
		padding: 0 30rpx 40rpx;
		background-color: #F5F7FA;
	}

	.card-item {
		background-color: #fff;
		border-radius: 24rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.08);
		overflow: hidden;
		transition: all 0.3s ease;
		transform: translateY(0);
	}

	.card-item:active {
		transform: translateY(4rpx);
		box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.05);
	}

	.card-item.card-type1 {
		background: linear-gradient(145deg, #FFF3E0, #FFE0B2);
	}

	.card-item.card-type2 {
		background: linear-gradient(145deg, #E8F5E9, #CCE5FF);
	}

	.card-item.card-type3 {
		background: linear-gradient(145deg, #F3E5F5, #E8EAF6);
	}

	.card-content {
		padding: 36rpx;
		position: relative;
		z-index: 10;
		background-color: transparent;
	}

	.card-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 30rpx;
	}

	.card-title {
		font-size: 34rpx;
		font-weight: bold;
		color: #333;
		max-width: 400rpx;
	}

	.price-group {
		text-align: right;
	}

	.original-price {
		font-size: 22rpx;
		color: #999;
		text-decoration: line-through;
		margin-right: 10rpx;
		display: block;
	}

	.current-price {
		font-size: 36rpx;
		color: #FF5722;
		font-weight: bold;
		display: block;
	}

	.card-features {
		margin-bottom: 36rpx;
	}

	.feature-item {
		display: flex;
		align-items: center;
		margin-bottom: 18rpx;
	}

	.feature-text {
		font-size: 24rpx;
		color: #666;
		margin-left: 14rpx;
	}

	.card-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-top: 24rpx;
		border-top: 1rpx solid rgba(0, 0, 0, 0.05);
	}

	.price-info {
		flex: 1;
	}

	.price-per-class {
		font-size: 26rpx;
		color: #FF5722;
		font-weight: 500;
	}

	.purchase-btn {
		background: linear-gradient(135deg, #409EFF, #69b1ff);
		color: #fff;
		font-size: 28rpx;
		font-weight: 500;
		padding: 16rpx 36rpx;
		border-radius: 40rpx;
		border: none;
		box-shadow: 0 6rpx 16rpx rgba(64, 158, 255, 0.3);
		display: flex;
		align-items: center;
		justify-content: center;
		transition: all 0.2s ease;
		white-space: nowrap;
	}

	.purchase-btn:active {
		opacity: 0.9;
		transform: scale(0.96);
		box-shadow: 0 4rpx 10rpx rgba(64, 158, 255, 0.2);
	}

	.purchase-btn uni-icons {
		margin-left: 10rpx;
	}
</style>