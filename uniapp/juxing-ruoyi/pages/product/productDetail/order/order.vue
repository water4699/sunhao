<template>
	<view class="page-container">
		<view class="header">
			<uni-icons type="arrowleft" size="24" color="#333" @click="cancel"></uni-icons>
			<text class="header-title">确认订单</text>
		</view>
		<view class="content">
			<view class="goods-card">
				<image class="goods-img" :src="goodsInfo.image" mode="aspectFill"></image>
				<view class="goods-detail">
					<view class="goods-name">{{ goodsInfo.name }}</view>
					<view class="goods-desc">{{ goodsInfo.description }}</view>
					<view class="price-section">
						<text class="price-label">单价</text>
						<text class="goods-price">¥{{ goodsInfo.price }}</text>
					</view>
					<view class="count-section">
						<text class="count-label">数量</text>
						<view class="count-control">
							<view class="count-btn" @click="reduceCount">-</view>
							<view class="count-num">{{ count }}</view>
							<view class="count-btn" @click="addCount">+</view>
						</view>
					</view>
				</view>
			</view>
			<view class="total-section">
				<text class="total-label">合计</text>
				<text class="total-price">¥{{ totalPrice }}</text>
			</view>
		</view>
		<view class="footer">
			<view class="btn cancel" @click="cancel">取消</view>
			<view class="btn confirm" @click="confirm">确认支付（模拟）</view>
		</view>
	</view>
</template>

<script>
import { getOneProduct } from '@/api/Product/findAllProduct'
import { createAppProductOrder } from '@/api/order/order'
import { getToken } from '@/utils/auth'
import { baseUrl } from '../../../../config'

export default {
	data() {
		return {
			id: '',
			count: 1,
			goodsInfo: {
				name: '',
				price: 0,
				description: '',
				image: ''
			}
		}
	},
	computed: {
		totalPrice() {
			const p = Number(this.goodsInfo.price) || 0
			return (p * this.count).toFixed(2)
		}
	},
	onLoad(options) {
		this.id = (options && options.id) ? options.id : ''
		this.init()
	},
	methods: {
		init() {
			if (!this.id) return
			getOneProduct(this.id).then(res => {
				const msg = res.data
				if (!msg) return
				this.goodsInfo.name = msg.name
				this.goodsInfo.image = msg.image ? baseUrl + msg.image : ''
				this.goodsInfo.price = msg.price
				this.goodsInfo.description = msg.description || ''
			})
		},
		addCount() {
			this.count++
		},
		reduceCount() {
			if (this.count > 1) this.count--
		},
		cancel() {
			uni.navigateBack()
		},
		confirm() {
			if (!getToken()) {
				uni.showToast({ title: '请先登录', icon: 'none' })
				return
			}
			if (!this.id) {
				uni.showToast({ title: '商品无效', icon: 'none' })
				return
			}
			createAppProductOrder({
				productId: this.id,
				quantity: this.count
			}).then(() => {
				uni.showToast({
					title: '支付成功',
					icon: 'success',
					duration: 1500,
					success: () => {
						setTimeout(() => {
							uni.switchTab({ url: '/pages/mine/index' })
						}, 1500)
					}
				})
			}).catch(() => {})
		}
	}
}
</script>

<style>
	page {
		height: 100%;
		background-color: #f7f7f7;
	}

	.page-container {
		height: 100%;
		display: flex;
		flex-direction: column;
	}

	.header {
		height: 88rpx;
		padding: 0 32rpx;
		display: flex;
		align-items: center;
		background-color: #fff;
		border-bottom: 1px solid #f0f0f0;
	}

	.header-title {
		font-size: 18px;
		font-weight: 500;
		margin-left: 24rpx;
		color: #333;
	}

	.content {
		flex: 1;
		padding: 32rpx;
		overflow-y: auto;
	}

	.goods-card {
		background-color: #fff;
		border-radius: 16rpx;
		padding: 32rpx;
		display: flex;
		margin-bottom: 32rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.04);
	}

	.goods-img {
		width: 200rpx;
		height: 200rpx;
		border-radius: 12rpx;
		margin-right: 32rpx;
	}

	.goods-detail {
		flex: 1;
	}

	.goods-name {
		font-size: 18px;
		color: #333;
		font-weight: 600;
		margin-bottom: 16rpx;
	}

	.goods-desc {
		font-size: 14px;
		color: #999;
		margin-bottom: 32rpx;
	}

	.price-section,
	.count-section {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 24rpx;
	}

	.price-label,
	.count-label {
		font-size: 15px;
		color: #666;
	}

	.goods-price {
		font-size: 20px;
		color: #ff9500;
		font-weight: bold;
	}

	.count-control {
		display: flex;
		align-items: center;
	}

	.count-btn {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: #f5f5f5;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 24px;
		color: #666;
	}

	.count-btn:active {
		background-color: #eee;
	}

	.count-num {
		margin: 0 24rpx;
		font-size: 18px;
		min-width: 60rpx;
		text-align: center;
	}

	.footer {
		padding: 32rpx;
		background-color: #fff;
		border-top: 1px solid #f0f0f0;
		display: flex;
		justify-content: space-between;
	}

	.btn {
		width: 48%;
		height: 96rpx;
		border-radius: 48rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 16px;
		font-weight: 500;
	}

	.cancel {
		background-color: #fff;
		color: #666;
		border: 1px solid #ddd;
	}

	.cancel:active {
		background-color: #f5f5f5;
	}

	.confirm {
		background-color: #ff9500;
		color: #fff;
	}

	.confirm:active {
		background-color: #ff8c00;
	}

	.total-section {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 24rpx 32rpx;
		background-color: #fff;
		border-radius: 16rpx;
		margin-bottom: 32rpx;
	}

	.total-label {
		font-size: 16px;
		color: #333;
		font-weight: 500;
	}

	.total-price {
		font-size: 20px;
		color: #ff9500;
		font-weight: 600;
	}
</style>
