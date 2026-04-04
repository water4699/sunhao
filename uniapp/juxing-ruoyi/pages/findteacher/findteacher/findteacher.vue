<template>
  <view class="page">
    <view v-if="isTeacher" class="teacher-booking-page">
      <view class="teacher-head">
        <text class="teacher-title">预约管理</text>
        <text class="teacher-sub">学生提交的预约会显示在这里</text>
      </view>

      <view class="teacher-status-tabs">
        <view
          v-for="t in teacherTabs"
          :key="t.value"
          :class="['teacher-tab', teacherStatusFilter === t.value ? 'active' : '']"
          @click="switchTeacherStatus(t.value)"
        >{{ t.label }}</view>
      </view>

      <scroll-view class="teacher-booking-list" scroll-y @scrolltolower="loadMoreTeacherBookings">
        <view v-if="teacherBookings.length === 0 && teacherLoadStatus !== 'loading'" class="empty-state">
          <text class="empty-text">暂无预约请求</text>
          <text class="empty-hint">学生在「找老师」预约后，会自动出现在这里</text>
        </view>

        <view v-else>
          <view class="booking-card" v-for="item in teacherBookings" :key="item.courseId">
            <view class="booking-row between">
              <text class="booking-no">预约号：{{ item.courseId }}</text>
              <text :class="['booking-status', statusClass(item.status)]">{{ statusText(item.status) }}</text>
            </view>
            <view class="booking-row">学员：{{ item.studentName || ('学员' + (item.studentId || '-')) }}</view>
            <view class="booking-row" v-if="item.studentPhone">电话：{{ item.studentPhone }}</view>
            <view class="booking-row">日期：{{ item.startDate || '-' }}</view>
            <view class="booking-row">地址：{{ item.address || '-' }}</view>
            <view class="booking-row">课时费：¥{{ item.hourlyRate || 0 }}</view>

            <view v-if="Number(item.status) === 0" class="booking-actions">
              <button class="btn reject" size="mini" @click="decide(item, 2)">拒绝</button>
              <button class="btn approve" size="mini" @click="decide(item, 1)">同意</button>
            </view>
          </view>

          <view v-if="teacherLoadStatus === 'loading'" class="load-more">加载中...</view>
          <view v-if="teacherLoadStatus === 'nomore' && teacherBookings.length > 0" class="load-more">没有更多了</view>
        </view>
      </scroll-view>
    </view>

    <view v-else class="student-page">
      <view class="search-container">
        <view class="search-box">
          <input v-model="searchText" class="search-input" placeholder="请输入老师姓名" @confirm="handleSearch">
          <view v-if="searchText" class="clear-icon" @click="clearSearch">×</view>
          <view class="search-button" @click="handleSearch">搜索</view>
        </view>
      </view>

      <view class="filter-bar">
        <view class="filter-group" v-for="(group, groupKey) in filterOptions" :key="groupKey">
          <view class="filter-header" @click="toggleFilterDropdown(groupKey)">
            <text>{{ groupKey === 'area' ? '地区' : groupKey === 'grade' ? '年级' : '科目' }}</text>
            <text class="filter-value">{{ activeFilters[groupKey] || '全部' }}</text>
          </view>

          <view v-show="activeDropdown === groupKey" class="filter-dropdown">
            <view
              class="filter-item"
              v-for="(option, index) in group"
              :key="index"
              :class="{ 'active': activeFilters[groupKey] === option || (option === '全部' && !activeFilters[groupKey]) }"
              @click="toggleFilter(groupKey, option)"
            >{{ option }}</view>
          </view>
        </view>
      </view>

      <scroll-view class="teacher-list" scroll-y @scrolltolower="loadMore">
        <view v-if="teacherList.length === 0 && loadStatus !== 'loading'" class="empty-state">
          <text class="empty-text">未找到符合条件的老师</text>
          <text class="empty-hint">若您已提交「老师入驻」，需管理员在后台将教师审核状态设为「通过」后，才会出现在此列表。</text>
        </view>

        <view v-else>
          <view v-for="teacher in teacherList" :key="teacher.teacherId" class="teacher-card" @click="goToTeacherDetail(teacher.teacherId)">
            <image class="teacher-avatar" :src="getImage(teacher.image)" mode="aspectFill"></image>
            <view class="teacher-info">
              <view class="teacher-header">
                <text class="teacher-name">{{ teacher.realName }}</text>
                <text class="teacher-tag">{{ teacher.education }}</text>
              </view>
              <view class="teacher-subjects">{{ teacher.subjectName }}</view>
              <view class="teacher-tags"><text class="tag">{{ teacher.university }}</text></view>
            </view>
            <view class="teacher-right">
              <view class="teacher-price">¥{{ teacher.hourlyRate }}/起</view>
            </view>
          </view>

          <view v-if="loadStatus === 'loading'" class="load-more"><text>加载中...</text></view>
          <view v-if="loadStatus === 'nomore' && teacherList.length > 0" class="load-more"><text>没有更多了</text></view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { findAllArea } from '@/api/teacher/area'
import { findAllGrade } from '@/api/teacher/grade'
import { findAllSubject } from '@/api/teacher/subject'
import { getTeacherMag } from '@/api/teacher/getTeacherMag'
import { listTeacherBookings, decideTeacherBooking } from '@/api/course/course'
import { baseUrl } from '../../../config'

export default {
  data() {
    return {
      searchText: '',
      activeDropdown: null,
      areaRows: [],
      gradeRows: [],
      subjectRows: [],
      filterOptions: { area: [], grade: [], subject: [] },
      activeFilters: { area: '', grade: '', subject: '' },
      teacherList: [],
      total: 0,
      page: 1,
      pageSize: 10,
      loadStatus: 'loadmore',
      filtersReady: false,

      teacherTabs: [
        { label: '全部', value: -1 },
        { label: '待处理', value: 0 },
        { label: '已同意', value: 1 },
        { label: '已拒绝', value: 2 }
      ],
      teacherStatusFilter: 0,
      teacherBookings: [],
      teacherPage: 1,
      teacherTotal: 0,
      teacherLoadStatus: 'loadmore'
    }
  },
  computed: {
    roles() {
      return (this.$store && this.$store.getters && this.$store.getters.roles) || []
    },
    isTeacher() {
      return this.roles.includes('teacher')
    }
  },
  mounted() {
    this.bootstrap()
  },
  onShow() {
    try {
      uni.setTabBarItem({ index: 1, text: this.isTeacher ? '预约管理' : '找老师' })
    } catch (e) {}
    if (this.isTeacher) {
      this.refreshTeacherBookings()
    }
  },
  methods: {
    async bootstrap() {
      if (this.isTeacher) {
        this.refreshTeacherBookings()
        return
      }
      try {
        await this.loadFilterOptions()
      } catch (e) {
        console.error(e)
      } finally {
        this.filtersReady = true
        this.refreshList()
      }
    },
    statusText(s) {
      const n = Number(s)
      if (n === 0) return '待处理'
      if (n === 1) return '已同意'
      if (n === 2) return '已拒绝'
      return '未知'
    },
    statusClass(s) {
      const n = Number(s)
      if (n === 0) return 'pending'
      if (n === 1) return 'approved'
      if (n === 2) return 'rejected'
      return ''
    },
    switchTeacherStatus(v) {
      this.teacherStatusFilter = v
      this.refreshTeacherBookings()
    },
    refreshTeacherBookings() {
      this.teacherPage = 1
      this.teacherBookings = []
      this.teacherLoadStatus = 'loadmore'
      this.loadMoreTeacherBookings()
    },
    async loadMoreTeacherBookings() {
      if (!this.isTeacher) return
      if (this.teacherLoadStatus === 'loading' || this.teacherLoadStatus === 'nomore') return
      this.teacherLoadStatus = 'loading'
      try {
        const params = { pageNum: this.teacherPage, pageSize: this.pageSize }
        if (this.teacherStatusFilter >= 0) params.status = this.teacherStatusFilter
        const res = await listTeacherBookings(params)
        const rows = res.rows || []
        if (this.teacherPage === 1) this.teacherBookings = rows
        else this.teacherBookings = [...this.teacherBookings, ...rows]
        this.teacherTotal = res.total != null ? res.total : this.teacherBookings.length
        this.teacherPage += 1
        this.teacherLoadStatus = (rows.length === 0 || this.teacherBookings.length >= this.teacherTotal) ? 'nomore' : 'loadmore'
      } catch (e) {
        this.teacherLoadStatus = 'loadmore'
      }
    },
    decide(item, status) {
      const text = status === 1 ? '同意' : '拒绝'
      uni.showModal({
        title: `确认${text}预约`,
        content: `确定要${text}该预约请求吗？`,
        success: async (r) => {
          if (!r.confirm) return
          try {
            await decideTeacherBooking(item.courseId, status)
            uni.showToast({ title: `已${text}`, icon: 'success' })
            this.refreshTeacherBookings()
          } catch (e) {}
        }
      })
    },
    async loadFilterOptions() {
      const [ar, gr, sr] = await Promise.all([findAllArea(), findAllGrade(), findAllSubject()])
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
      uni.navigateTo({ url: `/pages/findteacher/findteacher/teacherDetail/teacherDetail?id=${input}` })
    },
    refreshList() {
      this.page = 1
      this.teacherList = []
      this.loadStatus = 'loadmore'
      this.loadMore()
    },
    handleSearch() { this.refreshList() },
    clearSearch() { this.searchText = ''; this.handleSearch() },
    toggleFilterDropdown(groupKey) { this.activeDropdown = this.activeDropdown === groupKey ? null : groupKey },
    toggleFilter(type, value) {
      if (value === '全部' || this.activeFilters[type] === value) this.activeFilters[type] = ''
      else this.activeFilters[type] = value
      this.activeDropdown = null
      this.handleSearch()
    },
    async loadMore() {
      if (this.isTeacher) return
      if (!this.filtersReady) return
      if (this.loadStatus === 'nomore' || this.loadStatus === 'loading') return
      this.loadStatus = 'loading'
      try {
        const params = { pageNum: this.page, pageSize: this.pageSize, status: 1 }
        if (this.searchText) params.realName = this.searchText
        const sid = this.resolveSubjectId(this.activeFilters.subject)
        if (sid != null) params.subjectId = sid
        const aid = this.resolveAreaId(this.activeFilters.area)
        if (aid) params.areaId = aid
        const gid = this.resolveGradeId(this.activeFilters.grade)
        if (gid) params.gradeId = gid
        const res = await getTeacherMag(params)
        const rows = res.rows || []
        if (this.page === 1) this.teacherList = rows
        else this.teacherList = [...this.teacherList, ...rows]
        this.total = res.total != null ? res.total : 0
        this.page += 1
        this.loadStatus = (rows.length === 0 || this.teacherList.length >= this.total) ? 'nomore' : 'loadmore'
      } catch (e) {
        this.loadStatus = 'loadmore'
      }
    }
  }
}
</script>

<style>
page { height: 100%; background-color: #f5f5f5; }
.page { display: flex; flex-direction: column; height: 100%; }

.empty-state { display: flex; flex-direction: column; align-items: center; justify-content: center; padding-top: 200rpx; }
.empty-text { color: #999; font-size: 28rpx; }
.empty-hint { margin-top: 24rpx; padding: 0 48rpx; color: #bbb; font-size: 24rpx; line-height: 1.5; text-align: center; }

.search-container { padding: 20rpx; background-color: #ffffff; box-shadow: 0 2rpx 10rpx rgba(0,0,0,.05); }
.search-box { display: flex; align-items: center; background-color: #f5f5f5; border-radius: 40rpx; padding: 16rpx 30rpx; position: relative; }
.search-input { flex: 1; height: 60rpx; font-size: 28rpx; color: #666; background-color: transparent; padding-right: 80rpx; }
.clear-icon { position: absolute; right: 120rpx; top: 50%; transform: translateY(-50%); font-size: 36rpx; color: #999; padding: 10rpx; }
.search-button { position: absolute; right: 20rpx; background: #4a86e8; color: white; padding: 8rpx 20rpx; border-radius: 30rpx; font-size: 26rpx; }

.filter-bar { display: flex; justify-content: space-between; padding: 20rpx; background-color: #ffffff; margin-bottom: 20rpx; }
.filter-group { flex: 1; margin: 0 10rpx; position: relative; }
.filter-header { display: flex; align-items: center; justify-content: space-between; padding: 16rpx; background-color: #f9f9f9; border-radius: 12rpx; font-size: 28rpx; color: #333; }
.filter-value { color: #666; }
.filter-dropdown { position: absolute; top: 100%; left: 0; right: 0; background-color: #ffffff; border-radius: 0 0 12rpx 12rpx; box-shadow: 0 4rpx 12rpx rgba(0,0,0,.1); z-index: 10; }
.filter-item { padding: 16rpx; font-size: 28rpx; color: #666; }
.filter-item.active { color: #4080FF; font-weight: 500; }

.teacher-list { flex: 1; overflow: auto; padding: 20rpx; }
.teacher-card { display: flex; padding: 30rpx; background: #ffffff; border-radius: 16rpx; margin-bottom: 20rpx; box-shadow: 0 2rpx 10rpx rgba(0,0,0,.05); }
.teacher-avatar { width: 120rpx; height: 120rpx; border-radius: 50%; margin-right: 20rpx; flex-shrink: 0; }
.teacher-info { flex: 1; margin-right: 20rpx; }
.teacher-header { display: flex; align-items: center; margin-bottom: 12rpx; }
.teacher-name { font-size: 32rpx; font-weight: bold; color: #333; margin-right: 16rpx; }
.teacher-tag { font-size: 24rpx; color: #FFC300; background: rgba(255,195,0,.1); padding: 4rpx 12rpx; border-radius: 4rpx; }
.teacher-subjects { font-size: 28rpx; color: #666; margin-bottom: 8rpx; }
.teacher-tags { display: flex; flex-wrap: wrap; }
.tag { font-size: 24rpx; color: #666; background: #f5f5f5; padding: 4rpx 12rpx; border-radius: 4rpx; margin-right: 12rpx; margin-bottom: 8rpx; }
.teacher-right { text-align: right; display: flex; flex-direction: column; justify-content: space-between; align-items: flex-end; }
.teacher-price { font-size: 32rpx; color: #FF5252; font-weight: bold; }
.load-more { padding: 30rpx; text-align: center; font-size: 28rpx; color: #999; }

.teacher-booking-page { height: 100%; display: flex; flex-direction: column; }
.teacher-head { padding: 24rpx 28rpx 12rpx; background: #fff; }
.teacher-title { font-size: 34rpx; color: #222; font-weight: 600; display: block; }
.teacher-sub { font-size: 24rpx; color: #999; margin-top: 8rpx; display: block; }
.teacher-status-tabs { display: flex; background: #fff; padding: 0 18rpx 18rpx; }
.teacher-tab { padding: 10rpx 22rpx; margin-right: 14rpx; font-size: 24rpx; border-radius: 30rpx; color: #666; background: #f4f5f7; }
.teacher-tab.active { color: #fff; background: #2d8cf0; }
.teacher-booking-list { flex: 1; padding: 20rpx; }
.booking-card { background: #fff; border-radius: 14rpx; padding: 22rpx; margin-bottom: 20rpx; box-shadow: 0 2rpx 8rpx rgba(0,0,0,.04); }
.booking-row { font-size: 25rpx; color: #333; margin-bottom: 10rpx; }
.booking-row.between { display: flex; justify-content: space-between; align-items: center; }
.booking-no { color: #666; }
.booking-status { padding: 4rpx 14rpx; border-radius: 24rpx; font-size: 22rpx; }
.booking-status.pending { background: #fff7e6; color: #fa8c16; }
.booking-status.approved { background: #f6ffed; color: #52c41a; }
.booking-status.rejected { background: #fff1f0; color: #f5222d; }
.booking-actions { display: flex; justify-content: flex-end; gap: 14rpx; margin-top: 14rpx; }
.btn { min-width: 120rpx; border-radius: 28rpx; }
.btn.reject { background: #fff1f0; color: #f5222d; border: 1rpx solid #f5222d; }
.btn.approve { background: #2d8cf0; color: #fff; }
</style>
