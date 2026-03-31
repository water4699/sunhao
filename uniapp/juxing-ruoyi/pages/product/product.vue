<template>
    <view class="container">
        <!-- 分类区域 -->
        <view class="category-section">
            <view class="category-card" :class="{ 'active': activeCategory === 'all' }" @click="handleCategoryClick('all')">
                <image class="category-image"
                       src="https://ai-public.mastergo.com/ai/img_res/b1bb33ddae0c9ebdb91647effa420f43.jpg"
                       mode="aspectFill"></image>
                <text class="category-title">所有分类</text>
            </view>
            <view class="category-card" :class="{ 'active': activeCategory === '书籍' }" @click="handleCategoryClick('书籍')">
                <image class="category-image"
                       src="https://ai-public.mastergo.com/ai/img_res/b1bb33ddae0c9ebdb91647effa420f43.jpg"
                       mode="aspectFill"></image>
                <text class="category-title">书籍</text>
            </view>
            <view class="category-card" :class="{ 'active': activeCategory === '文具' }" @click="handleCategoryClick('文具')">
                <image class="category-image"
                       src="https://ai-public.mastergo.com/ai/img_res/2dd222afc9f4daee761dafcea85a4c8d.jpg"
                       mode="aspectFill"></image>
                <text class="category-title">文具</text>
            </view>
        </view>

        <!-- 推荐商品区域 -->
        <view class="recommend-section">
            <view class="section-title">
                <text>{{ activeCategory === 'all' ? '所有商品' : activeCategory + '类商品' }}</text>
            </view>
            <view class="product-grid">
                <view 
                    v-for="(item, index) in filteredProductList" 
                    :key="index" 
                    class="product-card" 
                    @click="findProductDetail(item.productId)"
                >
                    <image class="product-image" :src="getImage(item.image)" mode="aspectFill"></image>
                    <view class="product-info">
                        <text class="product-name">{{item.name}}</text>
                        <text class="product-price">¥{{item.price}}</text>
                        <text class="product-type">{{item.type}}</text>
                        <button class="buy-btn" @click.stop="handleBuyClick(item)">立即购买</button>
                    </view>
                </view>
            </view>
            
            <!-- 无商品提示 -->
            <view v-if="filteredProductList.length === 0" class="empty-tip">
                该分类暂无商品
            </view>
        </view>
    </view>
</template>

<script>
    import { findAllProduct } from '@/api/Product/findAllProduct'
    import { baseUrl } from '../../config';
    
    export default {
        data() {
            return {
                activeCategory: 'all',
                productList: []
            }
        },
        computed: {
            filteredProductList() {
                if (this.activeCategory === 'all') {
                    return this.productList;
                }
                return this.productList.filter(item => item.type === this.activeCategory);
            }
        },

        mounted() {
            this.init();
        },
        methods: {
            init() {
                findAllProduct().then(res => {
                    // 确保数据中有 type 字段
                    this.productList = res.rows.map(item => {
                        return {
                            ...item,
                            // 如果后端没有返回 type，设置默认值
                            type: item.type || '书籍'
                        }
                    });
                })
            },
            getImage(url) {
                return baseUrl + url;
            },
            handleCategoryClick(category) {
                this.activeCategory = category;
                console.log('当前分类:', category);
            },
            handleBuyClick(item) {
                uni.showToast({
                    title: `已加入购物车：${item.name}`,
                    icon: 'none'
                });
            },
            findProductDetail(input){
                console.log(input);
                uni.navigateTo({
                    url: `/pages/product/productDetail/product_det?id=${input}`
                });
            }
        }
    }
</script>

<style>
/* 添加空状态提示样式 */
.empty-tip {
    text-align: center;
    padding: 40rpx;
    font-size: 28rpx;
    color: #999;
}

/* 添加商品类型样式 */
.product-type {
    display: block;
    font-size: 24rpx;
    color: #666;
    margin: 6rpx 0;
    background-color: #f0f0f0;
    padding: 4rpx 12rpx;
    border-radius: 20rpx;
    align-self: flex-start;
}

page {
    height: 100%;
    background-color: #f8f8f8;
}

.container {
    display: flex;
    flex-direction: column;
    height: 100%;
    background-color: #ffffff;
    padding-bottom: 20rpx;
}

.category-section {
    display: flex;
    padding: 30rpx;
    gap: 20rpx;
}

.category-card {
    flex: 1;
    display: flex;
	width: 30%;
    flex-direction: column;
    align-items: center;
    padding: 30rpx;
    background-color: #fff;
    border-radius: 16rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.category-image {
    width: 200rpx;
    height: 200rpx;
    border-radius: 12rpx;
}

.category-title {
    margin-top: 20rpx;
    font-size: 16px;
    font-weight: bold;
    color: #333333;
}

.category-desc {
    margin-top: 8rpx;
    font-size: 12px;
    color: #666666;
}

.recommend-section {
    flex: 1;
    padding: 0 30rpx;
    overflow: auto;
}

.section-title {
    margin-bottom: 30rpx;
    font-size: 18px;
    font-weight: bold;
    color: #333333;
}

.product-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20rpx;
}

.product-card {
    background-color: #ffffff;
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.product-image {
    width: 100%;
    height: 320rpx;
}

.product-info {
    padding: 20rpx;
}

.product-name {
    font-size: 14px;
    color: #333333;
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.product-price {
    display: block;
    margin: 10rpx 0;
    font-size: 16px;
    color: #ff6b00;
    font-weight: bold;
}

.buy-btn {
    width: 100%;
    margin-top: 10rpx;
    font-size: 14px;
    background-color: #ff6b00;
    color: white;
    border-radius: 8rpx;
    line-height: 2;
    border: none;
    outline: none;
    padding: 6rpx 0;
    text-align: center;
}

.category-card.active {
    border: 2rpx solid #ff6b00;
}
</style>