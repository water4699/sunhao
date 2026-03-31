<template>
	<view class="goods-detail-page">
		<!-- 商品图片展示区 -->
		<image class="goods-img" :src="imgUrl" mode="widthFix" @click="previewImage"></image>

		<!-- 商品基本信息区 -->
		<view class="goods-info">
			<text class="goods-name">{{ goodsName }}</text>
			<view class="goods-type" @click="filterByType">{{ goodsType }}</view>
			<view class="price-stock">
				<text class="price" :animation="priceAnimation">{{ `¥${price}` }}</text>
				<text class="stock">{{ `库存 ${stock} 件` }}</text>
			</view>
		</view>

		<!-- 商品详情描述 -->
		<view class="goods-desc">
			<view class="desc-title">商品描述</view>
			<view class="desc-content">
				<text>{{ descContent }}</text>
			</view>
		</view>

		<!-- 底部购买按钮 -->
		<button class="buy-btn" @click="findProductOrder(input)">立即购买</button>
	</view>
</template>

<script>
	
	import {
		getOneProduct
	} from '@/api/Product/findAllProduct'
	import {
		baseUrl
	} from '../../../config';
	
	export default {
		data() {
			return {
				// 商品数据，实际可从接口获取
				id:'',
				imgUrl: 'https://example.com/your-img-url.jpg', // 替换为真实商品主图地址
				goodsName: '高中数学精品教材',
				goodsType: '教育书籍',
				price: 399,
				stock: 42,
				descContent: '本书针对高中数学重难点知识进行深入讲解，帮助学生快速掌握解题技巧，提高解题效率。内容涵盖函数、导数、概率统计等重要章节，配套习题详解和答案解析。采用全彩印刷，图文并茂，知识点讲解清晰易懂。',
				priceAnimation: {}, // 价格放大动画
			};
		},
		onLoad(e){
			this.id = e.id;
			console.log(e.id);
		},
		onReady() {
			// 初始化价格动效
			this.initPriceAnimation();
		},
		
		mounted() {
			this.init();
		},
		methods: {
			init() {
				getOneProduct(this.id).then(res => {
					var msg = res.data;
					this.goodsName=msg.name;
					this.imgUrl = baseUrl + msg.image;
					this.goodsType = msg.type;
					this.price = msg.price;
					this.stock = msg.stock;
					this.descContent = msg.description;
				})
				
			},
			findProductOrder(){
			    console.log('传送到的参数:',this.id);
			    uni.navigateTo({
			        url: `/pages/product/productDetail/order/order?id=${this.id}`
			    });
			},
			// 预览大图
			previewImage() {
				uni.previewImage({
					urls: [this.imgUrl],
				});
			},
			// 按类型筛选（可扩展实际筛选逻辑）
			filterByType() {
				uni.showToast({
					title: `筛选 ${this.goodsType} 类型商品`,
					icon: 'none',
				});
			},
			// 初始化价格放大动效
			initPriceAnimation() {
				const animation = uni.createAnimation({
					duration: 300,
					timingFunction: 'ease-in-out',
				});
				animation.scale(1.1).step();
				animation.scale(1).step();
				this.priceAnimation = animation.export();
			},
			// 购买按钮点击事件
			handleBuy() {
				// 简单的点击反馈，可扩展调用接口等逻辑
				uni.showToast({
					title: '点击了立即购买',
					icon: 'none',
				});
				// 轻微缩放效果（也可用 animation 实现更复杂效果）
				const btn = this.$mp.page.selectComponent('.buy-btn');
				btn.$el.style.transform = 'scale(0.95)';
				setTimeout(() => {
					btn.$el.style.transform = 'scale(1)';
				}, 150);
			},
		},
	};
</script>

<style scoped>
	.goods-detail-page {
		background-color: #fff;
		padding-bottom: 120rpx;
		/* 增加底部留白，避免内容被按钮遮挡 */
	}

	/* 商品图片展示区 */
	.goods-img {
		width: 100%;
		aspect-ratio: 3/2;
		/* 图片比例 3:2 */
		border-radius: 12rpx;
		padding: 20rpx;
	}

	/* 商品基本信息区 */
	.goods-info {
		padding: 0 20rpx;
	}

	.goods-name {
		font-size: 36rpx;
		font-weight: bold;
		margin-bottom: 10rpx;
	}

	.goods-type {
		display: inline-block;
		background-color: #ffedcf;
		padding: 6rpx 12rpx;
		border-radius: 6rpx;
		margin-bottom: 10rpx;
	}

	.price-stock {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.price {
		color: red;
		font-size: 32rpx;
	}

	.stock {
		font-size: 24rpx;
		color: #999;
	}

	/* 商品详情描述 */
	.goods-desc {
		margin: 20rpx;
		background-color: #fff;
		border-radius: 12rpx;
		overflow: hidden;
	}

	.desc-title {
		background-color: #ffedcf;
		padding: 12rpx 20rpx;
		font-size: 30rpx;
		font-weight: bold;
	}

	.desc-content {
		padding: 20rpx;
		font-size: 26rpx;
		line-height: 1.6;
	}

	/* 底部购买按钮 - 位置上移 */
	.buy-btn {
		position: fixed;
		bottom: 20rpx;
		/* 距离底部20rpx，按钮位置上移 */
		left: 20rpx;
		width: calc(100% - 40rpx);
		/* 左右各留20rpx边距 */
		height: 88rpx;
		background-color: #ffedcf;
		color: #fff;
		font-size: 32rpx;
		border: none;
		z-index: 999;
		border-radius: 44rpx;
		/* 圆角设计 */
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		/* 轻微阴影 */
	}
</style>