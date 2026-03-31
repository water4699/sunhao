<template>
	<view class="page">
		<!-- 搜索栏 -->
		<!-- 搜索栏部分修改 -->
		<view class="search-container">
			<view class="search-box">
				<input v-model="searchText" class="search-input" placeholder="请输入老师姓名" @confirm="handleSearch">
				<!-- 点击清除图标时，调用 clearSearch 方法 -->
				<view v-if="searchText" class="clear-icon" @click="clearSearch">×</view>
				<!-- 移除不合适的空状态提示 -->
				<view class="search-button" @click="handleSearch">
					搜索
				</view>
			</view>
		</view>

		<!-- 下拉筛选 -->
		<view class="filter-bar">
			<view class="filter-group" v-for="(group, groupKey) in filterOptions" :key="groupKey">
				<view class="filter-header" @click="toggleFilterDropdown(groupKey)">
					<text>{{ groupKey === 'area' ? '地区' : groupKey === 'grade' ? '年级' : groupKey === 'subject' ? '科目' : '类别' }}</text>
					<text class="filter-value">{{ activeFilters[groupKey] || '全部' }}</text>

				</view>

				<view v-show="activeDropdown === groupKey" class="filter-dropdown">
					<view class="filter-item" v-for="(option, index) in group" :key="index"
						:class="{ 'active': activeFilters[groupKey] === option || (option === '全部' && !activeFilters[groupKey]) }"
						@click="toggleFilter(groupKey, option)">{{ option }}</view>
				</view>
			</view>
		</view>

		<!-- 老师列表 -->
		<scroll-view class="teacher-list" scroll-y @scrolltolower="loadMore">
			<!-- 添加空状态视图 -->
			<view v-if="teacherList.length === 0 && loadStatus !== 'loading'" class="empty-state">
				<text class="empty-text">未找到符合条件的老师</text>
			</view>

			<view v-else>
				<view v-for="teacher in teacherList" :key="teacher.teacherId" class="teacher-card"
					@click="goToTeacherDetail(teacher.teacherId)">
					<image class="teacher-avatar" :src="getImage(teacher.image)" mode="aspectFill"></image>
					<view class="teacher-info">
						<view class="teacher-header">
							<text class="teacher-name">{{ teacher.realName }}</text>
							<text class="teacher-tag">{{ teacher.education }}</text>
						</view>
						<view class="teacher-subjects">{{ teacher.subjectName }}</view>
						<view class="teacher-tags"><text :key="tagIndex" class="tag">{{ teacher.university }}</text>
						</view>
					</view>
					<view class="teacher-right">
						<view class="teacher-price">¥{{ teacher.hourlyRate }}/起</view>
					</view>
				</view>

				<!-- 加载状态提示 -->
				<view v-if="loadStatus === 'loading'" class="load-more">
					<text>加载中...</text>
				</view>
				<view v-if="loadStatus === 'nomore' && teacherList.length > 0" class="load-more">
					<text>没有更多了</text>
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
				subjectList: [{
						value: '4',
						text: '数学'
					},
					{
						value: '6',
						text: '语文'
					},
					{
						value: '5',
						text: '英语'
					},
					{
						value: '3',
						text: '物理'
					},
					{
						value: '2',
						text: '化学'
					},
					{
						value: '1',
						text: '历史'
					}
				],
				filterOptions: {
					area: [],
					grade: [],
					subject: [],
				},
				activeFilters: { // 添加筛选状态对象
					area: '',
					grade: '',
					subject: '',
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
						this.filterOptions.area = ['全部', ...res.data.map(item => item.name)];
					}),
					findAllGrade().then(res => {
						this.filterOptions.grade = ['全部', ...res.data.map(item => item.name)];
					}),
					this.filterOptions.subject = ['全部', ...this.subjectList.map(item => item.text)];
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
				this.teacherList = []; // 清空列表
				this.loadStatus = 'loadmore'; // 修复：重置加载状态，确保可以发起新搜索
				this.loadMore();
			},
			toggleFilterDropdown(groupKey) {
				this.activeDropdown = this.activeDropdown === groupKey ? null : groupKey
			},
			toggleFilter(type, value) {
				if (value === '全部' || this.activeFilters[type] === value) {
					this.activeFilters[type] = '';
				} else {
					this.activeFilters[type] = value;
				}

				if (type === 'subject') {
					this.handleSearch();
				}

				this.activeDropdown = null;
			},
			async loadMore() {
				if (this.loadStatus === 'nomore') return;
				this.loadStatus = 'loading';

				try {
					const params = {
						pageNum: this.page,
						// pageSize: this.pageSize,
						realName: this.searchText, // 姓名查询参数
					};

					// 仅添加科目筛选参数
					if (this.activeFilters.subject) {
						const selectedSubject = this.subjectList.find(s => s.text === this.activeFilters.subject);
						if (selectedSubject) {
							params.subjectId = selectedSubject.value;
						}
					}

					console.log('向后端发送的查询参数:', params); // 在此处添加日志

					const res = await getTeacherMag(params);

					if (this.page === 1) {
						this.teacherList = res.rows;
					} else {
						this.teacherList = [...this.teacherList, ...res.rows];
					}

					this.total = res.total;
					this.page++;

					// 判断是否还有更多数据
					if (this.teacherList.length >= this.total) {
						this.loadStatus = 'nomore';
					} else {
						this.loadStatus = 'loadmore';
					}

				} catch (error) {
					console.error('加载教师数据失败:', error);
					this.loadStatus = 'loadmore';
				}
			}
		}
	}
</script>
<style>
	page {
		height: 100%;
		background-color: #f5f5f5;
	}

	/* 新增空状态样式 */
	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding-top: 200rpx;
	}

	.empty-text {
		color: #999;
		font-size: 28rpx;
	}

	/* 搜索框样式修改 */
	.search-box {
		display: flex;
		align-items: center;
		background-color: #f5f5f5;
		border-radius: 40rpx;
		padding: 16rpx 30rpx;
		position: relative;
	}

	.search-input {
		flex: 1;
		height: 60rpx;
		font-size: 28rpx;
		color: #666;
		background-color: transparent;
		padding-right: 80rpx;
		/* 为按钮留出空间 */
	}

	.clear-icon {
		position: absolute;
		right: 120rpx;
		top: 50%;
		transform: translateY(-50%);
		font-size: 36rpx;
		color: #999;
		padding: 10rpx;
	}

	/* 搜索按钮样式 */
	.search-button {
		position: absolute;
		right: 20rpx;
		background: #4a86e8;
		color: white;
		padding: 8rpx 20rpx;
		border-radius: 30rpx;
		font-size: 26rpx;
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
		right: 120rpx;
		top: 50%;
		transform: translateY(-50%);
		font-size: 36rpx;
		color: #999;
		padding: 10rpx;
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