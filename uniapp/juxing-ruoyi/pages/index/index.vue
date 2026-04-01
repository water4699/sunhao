<template>
	<view class="container">
		<!-- 轮播图 -->
		<view class="swiper-section">
			<swiper class="swiper" circular autoplay interval="3000" duration="500" @change="onSwiperChange">
				<swiper-item v-for="(item, index) in bannerList" :key="index">
					<image :src="item.imageUrl" mode="aspectFill" class="swiper-image" />
				</swiper-item>
			</swiper>
			<view class="swiper-dots">
				<view v-for="(item, index) in bannerList" :key="index"
					:class="['dot', currentSwiperIndex === index ? 'active' : '']">
				</view>
			</view>
		</view>
		<!-- 核心功能区 -->
		<view class="core-functions">
			<view class="function-card" @click="handleNavigateToTeacher">
				<image class="function-icon" src="../../static/icon/find_teacher.png" mode="aspectFit" />
				<text class="function-title">找老师</text>
				<text class="function-desc">大量生源/随时随地接单</text>
			</view>
			<view class="function-card" @click="teacherJoin">
				<image class="function-icon" src="../../static/icon/teacher_enter.png" mode="aspectFit" />
				<text class="function-title">老师入驻</text>
				<text class="function-desc">任意查询/随时随地接单</text>
			</view>
		</view>
		<view class="core-functions-extra">
			<view class="function-card-wide" @click="goReviewList">
				<text class="function-title">家长评价</text>
				<text class="function-desc">已通过审核的真实评价</text>
			</view>
		</view>
		<!-- 服务承诺区 -->
		<view class="service-promises">
			<view class="promise-title">聚星教育服务承诺</view>
			<view class="promise-grid">
				<view class="promise-item" v-for="(item, index) in promises" :key="index">
					<text class="promise-icon" :class="item.icon"></text>
					<view class="promise-content">
						<text class="promise-name">{{ item.title }}</text>
						<text class="promise-desc">{{ item.desc }}</text>
					</view>
				</view>
			</view>
		</view>

<!-- 学习动态 -->
		<view class="learning-updates">
			<view class="section-title">
				<text class="title-text">热门活动</text>
			</view>
			<view class="updates-list">
				<view class="update-item" v-for="(item, index) in learningUpdates" :key="index">
					<view class="update-content">
						<text class="update-text">{{ item.title }} {{ item.description }}</text>
					</view>
				</view>
			</view>
		</view>


		<!-- 热门课程 -->
		<view class="hot-courses">
			<view class="section-title">
				<text class="title-text">热门课程</text>
				<view class="more-link" @click="gotoMoreCourses">
					<text class="more-text">更多</text>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
			<scroll-view class="course-scroll" scroll-x>
				<view class="course-list">
					<view class="course-card" v-for="(course, index) in hotCourses" :key="index">
						<image class="course-image" :src="course.imageUrl" mode="aspectFill" />
						<view class="course-info">
							<text class="course-name">{{ course.name }}</text>
							<view class="course-stats">
								<text class="course-price">¥{{ course.price }}/课时</text>
								<text class="course-rating">{{ course.rating }}分</text>
							</view>
							<text class="course-teacher">{{ course.teacher }}</text>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 优秀教师 -->
		<!-- <view class="featured-teachers">
			<view class="section-title">
				<text class="title-text">优秀教师</text>
				<view class="more-link" @click="gotoMoreTeachers">
					<text class="more-text">更多</text>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
			<view class="teacher-grid">
				<view class="teacher-card" v-for="(teacher, index) in featuredTeachers" :key="index">
					<image class="teacher-avatar" :src="teacher.avatar" mode="aspectFill" />
					<text class="teacher-name">{{ teacher.name }}</text>
					<text class="teacher-subject">{{ teacher.subject }}</text>
					<view class="teacher-tags">
						<text class="tag" v-for="(tag, tagIndex) in teacher.tags" :key="tagIndex">{{ tag }}</text>
					</view>
				</view>
			</view>
		</view> -->

		
	</view>
</template>
<script>
	import {
		findAllPromotion
	} from '@/api/promotion/promotion'

	export default {
		data() {
			return {
				currentSwiperIndex: 0,
				bannerList: [{
						imageUrl: '../../static/image/1.png'
					},
					{
						imageUrl: '../../static/image/2.png'
					}
				],
				findTeacherIcon: 'https://ai-public.mastergo.com/ai/img_res/4c5df4e9997c5fcaf0b5fef230563cc4.jpg',
				teacherJoinIcon: 'https://ai-public.mastergo.com/ai/img_res/e98755bec0c798e8e282a27eb8320049.jpg',
				features: [{
						icon: 'star',
						text: '大数据智能选师',
						color: '#FFB800'
					},
					{
						icon: 'staff',
						text: '专属助教跟进',
						color: '#FF6B00'
					},
					{
						icon: 'heart',
						text: '选师满意为止',
						color: '#FF4D4F'
					},
					{
						icon: 'shield',
						text: '安全放心',
						color: '#4080FF'
					}
				],
				promises: [{
						icon: 'checkbox-circle',
						title: '证件审核',
						desc: '严格审核老师的证件信息'
					},
					{
						icon: 'medal',
						title: '师资严入',
						desc: '不到10%的教师录取率'
					},
					{
						icon: 'refresh',
						title: '老师更换',
						desc: '任您选择'
					},
					{
						icon: 'wallet',
						title: '随时退款',
						desc: '不满意随时退余额'
					}
				],
				hotCourses: [{
						imageUrl: 'https://ai-public.mastergo.com/ai/img_res/e4f9c701113f7cb14672be2c13df1701.jpg',
						name: '高中数学 - 函数与导数',
						price: '150',
						rating: '4.9',
						teacher: '张老师'
					},
					{
						imageUrl: 'https://ai-public.mastergo.com/ai/img_res/753e1714efca28d02900b207e8fce9a6.jpg',
						name: '高中物理 - 力学专题',
						price: '160',
						rating: '4.8',
						teacher: '李老师'
					},
					{
						imageUrl: 'https://ai-public.mastergo.com/ai/img_res/df547b41988f827145fc4f5aa483d6a2.jpg',
						name: '高中英语 - 写作提升',
						price: '140',
						rating: '4.9',
						teacher: '王老师'
					}
				],
				featuredTeachers: [{
						avatar: 'https://ai-public.mastergo.com/ai/img_res/071139593022d150ee10eeb6c5394f31.jpg',
						name: '张晓华',
						subject: '数学教师',
						tags: ['特级教师', '10年经验']
					},
					{
						avatar: 'https://ai-public.mastergo.com/ai/img_res/b1725630251b3d6f2687d266203c005d.jpg',
						name: '李明',
						subject: '物理教师',
						tags: ['教学名师', '精品课程']
					},
					{
						avatar: 'https://ai-public.mastergo.com/ai/img_res/ccb6369aed59f51d73705282d847b7d7.jpg',
						name: '王琳',
						subject: '英语教师',
						tags: ['外教经验', '考研专家']
					}
				],
				// ----------------------------------
				learningUpdates: []
			}
		},
		mounted() {
			this.init();
		},
		methods: {

			init() {
				findAllPromotion().then(res => {
					this.learningUpdates = res.data;
				})
			},

			handleNavigateToTeacher() {
				uni.switchTab({
					url: '/pages/findteacher/findteacher/findteacher'
				});
			},
			teacherJoin() {
				uni.navigateTo({
					url: '/pages/teacherJoin/teacherJoin'
				});
			},
			goReviewList() {
				uni.navigateTo({
					url: '/pages/review/review'
				});
			},
			onSwiperChange(e) {
				this.currentSwiperIndex = e.detail.current;
			},
			gotoMoreCourses() {
				// 更多课程跳转逻辑
				uni.navigateTo({
					url: '/pages/courses/courses'
				});
			},
			gotoMoreTeachers() {
				// 更多教师跳转逻辑
				uni.navigateTo({
					url: '/pages/teachers/teachers'
				});
			}
		}
	}
</script>
<style>
	page {
		height: 100%;
		background-color: #F5F6F7;
	}

	.container {
		min-height: 100%;
		background-color: #F5F6F7;
	}

	.swiper-section {
		position: relative;
		width: 750rpx;
		height: 300rpx;
	}

	.swiper {
		width: 100%;
		height: 100%;
	}

	.swiper-image {
		width: 100%;
		height: 100%;
	}

	.swiper-dots {
		position: absolute;
		bottom: 20rpx;
		left: 0;
		right: 0;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.dot {
		width: 12rpx;
		height: 12rpx;
		border-radius: 6rpx;
		background-color: rgba(255, 255, 255, 0.6);
		margin: 0 8rpx;
	}

	.dot.active {
		width: 24rpx;
		background-color: #FFFFFF;
	}

	.core-functions {
		padding: 30rpx;
		display: flex;
		justify-content: space-between;
	}

	.function-card {
		width: 330rpx;
		height: 180rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 24rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.core-functions-extra {
		padding: 0 30rpx 24rpx;
	}

	.function-card-wide {
		width: 100%;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 28rpx 32rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
		display: flex;
		flex-direction: column;
		align-items: flex-start;
	}

	.function-icon {
		width: 80rpx;
		height: 80rpx;
		margin-bottom: 12rpx;
	}

	.function-title {
		font-size: 16px;
		color: #333333;
		font-weight: 500;
		margin-bottom: 8rpx;
	}

	.function-desc {
		font-size: 12px;
		color: #999999;
	}

	.service-promises {
		margin: 30rpx;
		padding: 30rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
	}

	.promise-title {
		font-size: 16px;
		font-weight: 500;
		color: #333333;
		margin-bottom: 24rpx;
		text-align: center;
	}

	.promise-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 24rpx;
	}

	.promise-item {
		display: flex;
		align-items: center;
		padding: 16rpx;
		background: #F8F9FA;
		border-radius: 12rpx;
	}

	.promise-icon {
		font-size: 32rpx;
		margin-right: 16rpx;
	}

	.promise-content {
		margin-left: 16rpx;
	}

	.promise-name {
		font-size: 14px;
		color: #333333;
		font-weight: 500;
		display: block;
	}

	.promise-desc {
		font-size: 12px;
		color: #999999;
		margin-top: 4rpx;
		display: block;
	}

	/* 热门课程样式 */
	.hot-courses {
		margin: 30rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.section-title {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 24rpx;
	}

	.title-text {
		font-size: 16px;
		font-weight: 500;
		color: #333333;
	}

	.more-link {
		display: flex;
		align-items: center;
		cursor: pointer;
	}

	.more-text {
		font-size: 14px;
		color: #666666;
		margin-right: 4rpx;
	}

	.course-scroll {
		width: 100%;
		overflow: hidden;
	}

	.course-list {
		display: flex;
		padding: 8rpx 0;
	}

	.course-card {
		flex-shrink: 0;
		width: 300rpx;
		margin-right: 24rpx;
		border-radius: 12rpx;
		overflow: hidden;
		background: #FFFFFF;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.course-image {
		width: 300rpx;
		height: 200rpx;
	}

	.course-info {
		padding: 16rpx;
	}

	.course-name {
		font-size: 14px;
		color: #333333;
		font-weight: 500;
		display: block;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.course-stats {
		display: flex;
		justify-content: space-between;
		margin-top: 8rpx;
	}

	.course-price {
		font-size: 14px;
		color: #FF6B00;
		font-weight: 500;
	}

	.course-rating {
		font-size: 12px;
		color: #666666;
	}

	.course-teacher {
		font-size: 12px;
		color: #999999;
		margin-top: 8rpx;
		display: block;
	}

	/* 优秀教师样式 */
	.featured-teachers {
		margin: 30rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.teacher-grid {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 24rpx;
	}

	.teacher-card {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 20rpx;
	}

	.teacher-avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 60rpx;
		margin-bottom: 12rpx;
	}

	.teacher-name {
		font-size: 14px;
		color: #333333;
		font-weight: 500;
		margin-bottom: 4rpx;
	}

	.teacher-subject {
		font-size: 12px;
		color: #666666;
		margin-bottom: 8rpx;
	}

	.teacher-tags {
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
		gap: 8rpx;
	}

	.tag {
		font-size: 10px;
		color: #4080FF;
		background: rgba(64, 128, 255, 0.1);
		padding: 4rpx 12rpx;
		border-radius: 4rpx;
	}

	/* 学习动态样式 */
	.learning-updates {
		margin: 30rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.updates-list {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.update-item {
		display: flex;
		align-items: center;
		padding: 16rpx;
		background: #F8F9FA;
		border-radius: 12rpx;
	}

	.student-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		margin-right: 16rpx;
	}

	.update-content {
		flex: 1;
	}

	.update-text {
		font-size: 14px;
		color: #333333;
		margin-bottom: 4rpx;
		display: block;
	}

	.update-time {
		font-size: 12px;
		color: #999999;
		display: block;
	}
</style>