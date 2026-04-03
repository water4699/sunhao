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
				<text class="empty-hint">若您已提交「老师入驻」，需管理员在后台将教师审核状态设为「通过」后，才会出现在此列表。</text>
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
	} from '../../../config'

	export default {
		data() {
			return {
				searchText: '',
				activeDropdown: null,
				areaRows: [],
				gradeRows: [],
				subjectRows: [],
				filterOptions: {
					area: [],
					grade: [],
					subject: []
				},
				activeFilters: {
					area: '',
					grade: '',
					subject: ''
				},
				teacherList: [],
				total: 0,
				page: 1,
				pageSize: 10,
				loadStatus: 'loadmore',
				filtersReady: false
			}
		},
		mounted() {
			this.bootstrap()
		},
			onShow() {
				try {
					const roles = (this.$store && this.$store.getters && this.$store.getters.roles) || []
					uni.setTabBarItem({
						index: 1,
						text: roles.includes('teacher') ? '发布课程' : '找老师'
					})
					if (roles.includes('teacher')) {
						uni.redirectTo({ url: '/pages/coursePublish/coursePublish?from=tab' })
						return
					}
				} catch (e) {}
			},
		methods: {
			async bootstrap() {
				try {
					await this.loadFilterOptions()
				} catch (e) {
					console.error(e)
				} finally {
					this.filtersReady = true
					this.refreshList()
				}
			},
			async loadFilterOptions() {
				const [ar, gr, sr] = await Promise.all([
					findAllArea(),
					findAllGrade(),
					findAllSubject()
				])
				this.areaRows = ar.data || []
				this.gradeRows = gr.data || []
				this.subjectRows = sr.data || []
				this.filterOptions.area = ['全部', ...this.areaRows.map(i => i.name)]
				this.filterOptions.grade = ['全部', ...this.gradeRows.map(i => i.name)]
				this.filterOptions.subject = ['全部', ...this.subjectRows.map(i => i.name)]
			},
			resolveAreaId(name) {
				if (!name) return undefined
				const r = this.areaRows.find(i => i.name === name)
				return r ? r.areaId : undefined
			},
			resolveGradeId(name) {
				if (!name) return undefined
				const r = this.gradeRows.find(i => i.name === name)
				return r ? r.gradeId : undefined
			},
			resolveSubjectId(name) {
				if (!name) return undefined
				const r = this.subjectRows.find(i => i.name === name)
				if (!r || r.subjectId == null || r.subjectId === '') return undefined
				const n = parseInt(r.subjectId, 10)
				return isNaN(n) ? undefined : n
			},
			getImage(url) {
				if (!url) return ''
				if (String(url).startsWith('http')) return url
				return baseUrl + url
			},
			goToTeacherDetail(input) {
				uni.navigateTo({
					url: `/pages/findteacher/findteacher/teacherDetail/teacherDetail?id=${input}`
				})
			},
			refreshList() {
				this.page = 1
				this.teacherList = []
				this.loadStatus = 'loadmore'
				this.loadMore()
			},
			handleSearch() {
				this.refreshList()
			},
			clearSearch() {
				this.searchText = ''
				this.handleSearch()
			},
			toggleFilterDropdown(groupKey) {
				this.activeDropdown = this.activeDropdown === groupKey ? null : groupKey
			},
			toggleFilter(type, value) {
				if (value === '全部' || this.activeFilters[type] === value) {
					this.activeFilters[type] = ''
				} else {
					this.activeFilters[type] = value
				}
				this.activeDropdown = null
				this.handleSearch()
			},
			async loadMore() {
				if (!this.filtersReady) return
				if (this.loadStatus === 'nomore' || this.loadStatus === 'loading') return
				this.loadStatus = 'loading'
				try {
					const params = {
						pageNum: this.page,
						pageSize: this.pageSize
					}
					if (this.searchText) {
						params.realName = this.searchText
					}
					const sid = this.resolveSubjectId(this.activeFilters.subject)
					if (sid != null) {
						params.subjectId = sid
					}
					// 仅展示已审核通过教师（与管理端约定：1=通过；0=待审）
					params.status = 1
					const aid = this.resolveAreaId(this.activeFilters.area)
					if (aid) {
						params.areaId = aid
					}
					const gid = this.resolveGradeId(this.activeFilters.grade)
					if (gid) {
						params.gradeId = gid
					}
					const res = await getTeacherMag(params)
					const rows = res.rows || []
					if (this.page === 1) {
						this.teacherList = rows
					} else {
						this.teacherList = [...this.teacherList, ...rows]
					}
					this.total = res.total != null ? res.total : 0
					this.page++
					if (rows.length === 0 || this.teacherList.length >= this.total) {
						this.loadStatus = 'nomore'
					} else {
						this.loadStatus = 'loadmore'
					}
				} catch (error) {
					console.error('加载教师数据失败:', error)
					this.loadStatus = 'loadmore'
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

	.empty-hint {
		margin-top: 24rpx;
		padding: 0 48rpx;
		color: #bbb;
		font-size: 24rpx;
		line-height: 1.5;
		text-align: center;
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
