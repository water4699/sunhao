<template>
	<view class="page">
		<!-- 搜索栏 -->
		<view class="search-container">
			<view class="search-box">
				<input v-model="searchText" class="search-input" placeholder="请输入老师姓名" @confirm="handleSearch">
				<view v-if="searchText" class="clear-icon" @click="searchText = ''">×</view>
			</view>
		</view>

		<!-- 下拉筛选 -->
		<view class="filter-bar">
			<view class="filter-group" v-for="(group, groupKey) in filterOptions" :key="groupKey">
				<view class="filter-header" @click="toggleFilterDropdown(groupKey)">
					<text>{{ groupKey === 'area' ? '地区' : groupKey === 'grade' ? '年级' : groupKey === 'subject' ? '科目' : '类别' }}</text>
					<text class="filter-value">{{ activeFilters[groupKey] }}</text>

				</view>

				<view v-show="activeDropdown === groupKey" class="filter-dropdown">
					<view class="filter-item" v-for="(option, index) in group" :key="index"
						:class="{ 'active': activeFilters[groupKey] === option }"
						@click="toggleFilter(groupKey, option)">{{ option }}</view>
				</view>
			</view>
		</view>

		<!-- 老师列表 -->
		<scroll-view class="teacher-list" scroll-y @scrolltolower="loadMore">
			<view v-for="teacher in teacherList" :key="teacher.teacherId" class="teacher-card"
				@click="goToTeacherDetail(teacher.teacherId)">
				<image class="teacher-avatar" :src="getImage(teacher.image)" mode="aspectFill"></image>
				<view class="teacher-info">
					<view class="teacher-header">
						<text class="teacher-name">{{ teacher.realName }}</text>
						<text class="teacher-tag">{{ teacher.education }}</text>
					</view>
					<view class="teacher-subjects">{{ teacher.subjectName }}</view>
					<view class="teacher-tags"><text :key="tagIndex" class="tag">{{ teacher.university }}</text></view>
				</view>
				<view class="teacher-right">
					<view class="teacher-price">¥{{ teacher.hourlyRate }}/起</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>
<script>
	import {
		findAllArea
	} from '@/api/teacher/area'
	import {
		findAllGrade
	} from '@/api/teacher/grade'
	import {
		findAllSubject
	} from '@/api/teacher/subject'
	import {
		getTeacherMag
	} from '@/api/teacher/getTeacherMag'
	import {
		baseUrl
	} from '../../../config';

	export default {
		data() {
			return {
				searchText: '',
				activeDropdown: null,
				filterOptions: {
					area: [],
					grade: [],
					subject: [],
				},

				teacherList: [{
					name: '张老师',
					image: '',
					type: '金牌讲师',
					subjects: '高中数学',
					tags: ['重点中学', '高考冲刺', '提分快'],
					price: 200,
				}],
				total: 20,
				page: 1,
				pageSize: 4,
				loadStatus: 'loadmore'
			}
		},

		mounted() {
			this.loadMore();
			this.init();
		},
		methods: {
			init() {
				findAllArea().then(res => {
						this.filterOptions.area = res.data.map(item => item.name);
					}),
					findAllGrade().then(res => {
						this.filterOptions.grade = res.data.map(item => item.name);
					}),
					findAllSubject().then(res => {
						this.filterOptions.subject = res.data.map(item => item.name);
					}),
					getTeacherMag().then(res => {
						this.teacherList = res.rows;
					})

			},
			getImage(url) {
				return baseUrl + url;
			},
			goToTeacherDetail(input) {
				uni.navigateTo({
					url: `/pages/findteacher/findteacher/teacherDetail/teacherDetail?id=${input}`
				});
			},
			handleSearch() {
				this.page = 1;
				this.loadMore()
			},
			toggleFilterDropdown(groupKey) {
				this.activeDropdown = this.activeDropdown === groupKey ? null : groupKey
			},
			toggleFilter(type, value) {
				this.activeFilters[type] = value;
				this.page = 1;
				this.loadMore();
				this.activeDropdown = null
			},
			loadMore() {
				if (this.loadStatus === 'nomore') return;
				this.loadStatus = 'loading';
			}
		}
	}
</script>
<style>
	page {
		height: 100%;
		background-color: #f5f5f5;
	}

	.page {
		display: flex;
		flex-direction: column;
		height: 100%;
	}

	.search-container {
		padding: 20rpx;
		background-color: #ffffff;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	}

	.search-box {
		display: flex;
		align-items: center;
		background-color: #f5f5f5;
		border-radius: 40rpx;
		padding: 16rpx 30rpx;
		position: relative;
	}

	.search-icon {
		width: 32rpx;
		height: 32rpx;
		margin-right: 16rpx;
	}

	.search-input {
		flex: 1;
		height: 60rpx;
		font-size: 28rpx;
		color: #666;
		background-color: transparent;
	}

	.clear-icon {
		position: absolute;
		right: 20rpx;
		font-size: 32rpx;
		color: #999;
	}

	.filter-bar {
		display: flex;
		justify-content: space-between;
		padding: 20rpx;
		background-color: #ffffff;
		margin-bottom: 20rpx;
	}

	.filter-group {
		flex: 1;
		margin: 0 10rpx;
		position: relative;
	}

	.filter-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 16rpx;
		background-color: #f9f9f9;
		border-radius: 12rpx;
		font-size: 28rpx;
		color: #333;
	}

	.filter-value {
		color: #666;
	}

	.arrow-icon {
		width: 24rpx;
		height: 24rpx;
	}

	.filter-dropdown {
		position: absolute;
		top: 100%;
		left: 0;
		right: 0;
		background-color: #ffffff;
		border-radius: 0 0 12rpx 12rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		z-index: 10;
	}

	.filter-item {
		padding: 16rpx;
		font-size: 28rpx;
		color: #666;
	}

	.filter-item.active {
		color: #4080FF;
		font-weight: 500;
	}

	.teacher-list {
		flex: 1;
		overflow: auto;
		padding: 20rpx;
	}

	.teacher-card {
		display: flex;
		padding: 30rpx;
		background: #ffffff;
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	}

	.teacher-avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.teacher-info {
		flex: 1;
		margin-right: 20rpx;
	}

	.teacher-header {
		display: flex;
		align-items: center;
		margin-bottom: 12rpx;
	}

	.teacher-name {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		margin-right: 16rpx;
	}

	.teacher-tag {
		font-size: 24rpx;
		color: #FFC300;
		background: rgba(255, 195, 0, 0.1);
		padding: 4rpx 12rpx;
		border-radius: 4rpx;
	}

	.teacher-subjects {
		font-size: 28rpx;
		color: #666;
		margin-bottom: 8rpx;
	}

	.teacher-exp {
		font-size: 24rpx;
		color: #999;
		margin-bottom: 12rpx;
	}

	.teacher-tags {
		display: flex;
		flex-wrap: wrap;
	}

	.tag {
		font-size: 24rpx;
		color: #666;
		background: #f5f5f5;
		padding: 4rpx 12rpx;
		border-radius: 4rpx;
		margin-right: 12rpx;
		margin-bottom: 8rpx;
	}

	.teacher-right {
		text-align: right;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: flex-end;
	}

	.teacher-price {
		font-size: 32rpx;
		color: #FF5252;
		font-weight: bold;
	}

	.load-more {
		padding: 30rpx;
		text-align: center;
		font-size: 28rpx;
		color: #999;
	}

	.loading-icon {
		width: 24rpx;
		height: 24rpx;
		margin-right: 8rpx;
		vertical-align: middle;
	}
</style>