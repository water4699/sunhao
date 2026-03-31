<template>
	<view class="container">
		<!-- 头部区域 -->
		<view class="header">
			<view class="header-bg"></view>
			<text class="title">我的会员卡</text>
			<text class="subtitle">尊享专属权益</text>
		</view>

		<!-- 卡片列表区域 -->
		<view class="card-container">
			<view class="section-header">
				<text class="section-title">当前会员卡</text>
				<text class="section-count">{{ cards.length }}张</text>
			</view>

			<view class="card-list">
				<view class="card-item" v-for="(card, index) in cards" :key="card.id" :class="cardClasses[index]"
					@click="handleCardClick(index)">
					<view class="card-gradient" :class="cardClasses[index]"></view>
					<view class="card-content">
						<view class="card-left">
							<uni-icons :type="card.icon || 'vip'" size="32" color="#fff"></uni-icons>
						</view>
						<view class="card-right">
							<text class="card-name">{{ card.name || '会员卡' + (index + 1) }}</text>
							<text class="card-count">剩余{{ card.count || 0 }}次</text>
							<text class="card-desc">{{ card.description || '暂无描述' }}</text>
						</view>
						<view class="card-badge">
							<text>{{ badgeTexts[index] }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 操作区域 -->
		<view class="action-container">
			<!-- <view class="info-box">
				<view class="info-icon">
					<uni-icons type="info-circle" size="28" color="#409EFF"></uni-icons>
				</view>
				<view class="info-content">
					<text class="info-title">会员权益</text>
					<text class="info-desc">不同等级会员卡可享受不同折扣和专属服务</text>
				</view>
			</view> -->

			<!-- <view class="benefits-grid">
				<view class="benefit-item" v-for="(benefit, index) in benefits" :key="index">
					<view class="benefit-icon">
						<uni-icons :type="benefit.icon" size="36" :color="benefit.color"></uni-icons>
					</view>
					<text class="benefit-text">{{ benefit.text }}</text>
				</view>
			</view> -->

			<view class="footer">
				<button class="buy-btn" @click="handleBuy">
					<uni-icons type="plus" size="24" color="#333"></uni-icons>
					<text>购买新卡</text>
				</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				cards: [{
						id: 1,
						icon: 'vip',
						name: '黄金会员卡',
						count: 30,
						description: '可用于全部门店服务，每次最高可抵扣50元'
					},
					{
						id: 2,
						icon: 'vip-filled',
						name: '白金会员卡',
						count: 15,
						description: '可用于全部门店服务，每次最高可抵扣100元'
					},
					{
						id: 3,
						icon: 'medal',
						name: '钻石会员卡',
						count: 10,
						description: '可用于全部门店VIP服务，每次最高可抵扣200元'
					}
				],
				benefits: [{
						icon: 'gift',
						text: '专属折扣',
						color: '#E6A23C'
					},
					{
						icon: 'gift',
						text: '生日礼遇',
						color: '#909399'
					},
					{
						icon: 'calendar',
						text: '优先预约',
						color: '#409EFF'
					},
					{
						icon: 'star',
						text: '积分加速',
						color: '#F56C6C'
					}
				],
				colorSchemes: [{
						class: 'card-gold',
						gradient: 'linear-gradient(135deg, #FFD700, #FFA500)',
						badge: 'GOLD'
					},
					{
						class: 'card-platinum',
						gradient: 'linear-gradient(135deg, #E5E4E2, #C0C0C0)',
						badge: 'PLATINUM'
					},
					{
						class: 'card-diamond',
						gradient: 'linear-gradient(135deg, #B9F2FF, #1E90FF)',
						badge: 'DIAMOND'
					},
					{
						class: 'card-silver',
						gradient: 'linear-gradient(135deg, #C0C0C0, #9E9E9E)',
						badge: 'SILVER'
					},
					{
						class: 'card-gem',
						gradient: 'linear-gradient(135deg, #FF69B4, #FF1493)',
						badge: 'GEM'
					},
					{
						class: 'card-onyx',
						gradient: 'linear-gradient(135deg, #333333, #1A1A1A)',
						badge: 'ONYX'
					}
				],
				cardClasses: [], // 动态生成的卡片类名数组
				badgeTexts: [] // 动态生成的徽章文本数组
			}
		},
		created() {
			this.updateCardClasses(); // 初始化卡片类名
		},
		methods: {
			handleBuy() {
				uni.navigateTo({
					url: '/pages/mine/card/card/addCard/addCard'
				});
			},


			handleCardClick(index) {
				uni.showToast({
					title: `查看${this.cards[index].name}详情`,
					icon: 'none'
				});
			},
			// 更新卡片类名和徽章文本
			updateCardClasses() {
				this.cardClasses = this.cards.map((_, index) => {
					const schemeIndex = index % this.colorSchemes.length;
					return this.colorSchemes[schemeIndex].class;
				});

				this.badgeTexts = this.cards.map((_, index) => {
					const schemeIndex = index % this.colorSchemes.length;
					return this.colorSchemes[schemeIndex].badge;
				});
			},
			getRandomIcon() {
				const icons = ['vip', 'vip-filled', 'medal', 'star', 'gift', 'diamond'];
				return icons[Math.floor(Math.random() * icons.length)];
			},
			getRandomCardName() {
				const names = ['翡翠会员卡', '玛瑙会员卡', '琥珀会员卡', '琉璃会员卡', '珍珠会员卡'];
				return names[Math.floor(Math.random() * names.length)];
			}
		}
	}
</script>

<style>
	/* 样式部分与之前保持一致 */
	page {
		height: 100%;
		background-color: #f8f9fa;
	}

	.container {
		min-height: 100%;
		display: flex;
		flex-direction: column;
		padding: 30rpx 24rpx;
		box-sizing: border-box;
	}

	/* 头部样式 */
	.header {
		position: relative;
		height: 130rpx;
		margin-bottom: 30rpx;
		/* border-radius: 20rpx; */
		overflow: hidden;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.header-bg {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: linear-gradient(135deg, #F0E68C, #FFD700);
		opacity: 0.9;
	}

	.title {
		position: relative;
		z-index: 10;
		font-size: 40rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 10rpx;
	}

	.subtitle {
		position: relative;
		z-index: 10;
		font-size: 26rpx;
		color: #666;
	}

	/* 卡片容器样式 */
	.card-container {
		background-color: #fff;
		border-radius: 20rpx;
		padding: 30rpx 24rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
	}

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 24rpx;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.section-count {
		font-size: 24rpx;
		color: #999;
	}

	.card-list {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}

	/* 卡片样式 */
	.card-item {
		position: relative;
		height: 180rpx;
		border-radius: 16rpx;
		overflow: hidden;
		box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.08);
		transition: all 0.3s ease;
		transform-origin: center;
	}

	.card-item:active {
		transform: scale(0.98);
	}

	.card-gradient {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		z-index: 1;
	}

	.card-gold .card-gradient,
	.card-gold {
		background: linear-gradient(135deg, #FFD700, #FFA500);
	}

	.card-platinum .card-gradient,
	.card-platinum {
		background: linear-gradient(135deg, #E5E4E2, #C0C0C0);
	}

	.card-diamond .card-gradient,
	.card-diamond {
		background: linear-gradient(135deg, #B9F2FF, #1E90FF);
	}

	.card-silver .card-gradient,
	.card-silver {
		background: linear-gradient(135deg, #C0C0C0, #9E9E9E);
	}

	.card-gem .card-gradient,
	.card-gem {
		background: linear-gradient(135deg, #FF69B4, #FF1493);
	}

	.card-onyx .card-gradient,
	.card-onyx {
		background: linear-gradient(135deg, #333333, #1A1A1A);
	}

	.card-content {
		position: relative;
		z-index: 10;
		width: 100%;
		height: 100%;
		padding: 30rpx 24rpx;
		display: flex;
		align-items: center;
		box-sizing: border-box;
	}

	.card-left {
		width: 64rpx;
		height: 64rpx;
		background: rgba(255, 255, 255, 0.2);
		backdrop-filter: blur(4rpx);
		border-radius: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.card-right {
		margin-left: 24rpx;
		flex: 1;
	}

	.card-name {
		font-size: 32rpx;
		color: #fff;
		font-weight: bold;
		display: block;
		margin-bottom: 12rpx;
	}

	.card-count {
		font-size: 28rpx;
		color: #fff;
		display: block;
		margin-bottom: 8rpx;
	}

	.card-desc {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.8);
		display: block;
	}

	.card-badge {
		position: absolute;
		top: 16rpx;
		right: 16rpx;
		background-color: rgba(255, 255, 255, 0.2);
		backdrop-filter: blur(4rpx);
		padding: 6rpx 12rpx;
		border-radius: 10rpx;
		z-index: 20;
	}

	.card-badge text {
		font-size: 20rpx;
		font-weight: bold;
		color: #fff;
		text-transform: uppercase;
	}

	/* 操作区域样式 */
	.action-container {
		background-color: #fff;
		border-radius: 20rpx;
		padding: 30rpx 24rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
	}

	.info-box {
		display: flex;
		align-items: center;
		padding: 24rpx;
		margin-bottom: 30rpx;
		border-radius: 16rpx;
		background-color: #f8fafc;
	}

	.info-icon {
		width: 48rpx;
		height: 48rpx;
		background-color: rgba(64, 158, 255, 0.1);
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		margin-right: 20rpx;
	}

	.info-content {
		flex: 1;
	}

	.info-title {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.info-desc {
		font-size: 24rpx;
		color: #666;
		display: block;
	}

	/* 权益网格 */
	.benefits-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 24rpx;
		margin-bottom: 30rpx;
	}

	.benefit-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 24rpx 0;
		border-radius: 16rpx;
		background-color: #f8fafc;
		transition: all 0.3s ease;
	}

	.benefit-item:active {
		transform: scale(0.96);
	}

	.benefit-icon {
		width: 64rpx;
		height: 64rpx;
		border-radius: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16rpx;
	}

	.benefit-text {
		font-size: 24rpx;
		color: #333;
		text-align: center;
	}

	/* 底部按钮 */
	.footer {
		margin-top: 24rpx;
	}

	.buy-btn {
		width: 60%;
		background: linear-gradient(135deg, #F0E68C, #FFD700);
		color: #333;
		font-size: 32rpx;
		font-weight: bold;
		/* border-radius: 16rpx; */
		padding: 24rpx 0;
		border: none;
		outline: none;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 6rpx 16rpx rgba(240, 230, 140, 0.3);
		transition: all 0.3s ease;
	}

	.buy-btn:active {
		opacity: 0.8;
		transform: translateY(2rpx);
	}

	.buy-btn uni-icons {
		margin-right: 12rpx;
	}
</style>