<template>
  <view class="page">
    <view v-if="isTeacher" class="teacher-workbench-page">
      <view class="teacher-head">
        <text class="teacher-title">老师工作台</text>
        <text class="teacher-sub">统一管理入驻、家教信息、预约和评价</text>
      </view>

      <view class="workbench-grid">
        <view :class="['workbench-card', teacherWorkMode === 'demand' ? 'active' : '']" @click="switchTeacherWorkMode('demand')">
          <text class="workbench-card-title">接单信息</text>
          <text class="workbench-card-sub">查看家长需求并提交接单</text>
        </view>
        <view class="workbench-card primary" @click="goPublishTutorPost">
          <text class="workbench-card-title">发布家教信息</text>
          <text class="workbench-card-sub">让学生主动预约你</text>
        </view>
        <view :class="['workbench-card', teacherWorkMode === 'posts' ? 'active' : '']" @click="switchTeacherWorkMode('posts')">
          <text class="workbench-card-title">我的家教信息</text>
          <text class="workbench-card-sub">编辑、上下架、删除</text>
        </view>
        <view :class="['workbench-card', teacherWorkMode === 'booking' ? 'active' : '']" @click="switchTeacherWorkMode('booking')">
          <text class="workbench-card-title">预约管理</text>
          <text class="workbench-card-sub">处理学生预约和取消</text>
        </view>
      </view>

      <view v-if="teacherWorkMode === 'demand'" class="teacher-section">
        <view class="section-title">接单信息</view>
        <scroll-view class="teacher-booking-list" scroll-y @scrolltolower="loadMoreTeacherDemands">
          <view v-if="teacherDemandList.length === 0 && teacherDemandLoadStatus !== 'loading'" class="empty-state compact">
            <text class="empty-text">暂无可接需求</text>
            <text class="empty-hint">家长发布「找家教需求」后会出现在这里。</text>
          </view>
          <view v-else>
            <view class="booking-card" v-for="item in teacherDemandList" :key="'d_' + item.requestId">
              <view class="booking-row between">
                <text class="booking-no">需求单：{{ item.requestId }}</text>
                <text class="booking-status pending">待接单</text>
              </view>
              <view class="booking-row">科目：{{ item.subjectName || '-' }}</view>
              <view class="booking-row">年级：{{ item.gradeName || '-' }}</view>
              <view class="booking-row">地区：{{ item.areaName || '-' }}</view>
              <view class="booking-row">预算：¥{{ item.budgetMin || 0 }} - ¥{{ item.budgetMax || 0 }}/课时</view>
              <view class="booking-row">时段：{{ item.preferredTime || '待沟通' }}</view>
              <view class="booking-row">需求：{{ item.demandDesc || '-' }}</view>
              <view class="booking-actions">
                <button class="btn approve" size="mini" @click="submitBid(item)">我要接单</button>
              </view>
            </view>
            <view v-if="teacherDemandLoadStatus === 'loading'" class="load-more">加载中...</view>
            <view v-if="teacherDemandLoadStatus === 'nomore' && teacherDemandList.length > 0" class="load-more">没有更多了</view>
          </view>
        </scroll-view>
      </view>

      <view v-else-if="teacherWorkMode === 'booking'" class="teacher-section">
        <view class="section-title">预约管理</view>
        <view class="teacher-status-tabs">
          <view
            v-for="t in teacherTabs"
            :key="t.value"
            :class="['teacher-tab', teacherStatusFilter === t.value ? 'active' : '']"
            @click="switchTeacherStatus(t.value)"
          >{{ t.label }}</view>
        </view>

        <scroll-view class="teacher-booking-list" scroll-y @scrolltolower="loadMoreTeacherBookings">
          <view v-if="teacherBookings.length === 0 && teacherLoadStatus !== 'loading'" class="empty-state compact">
            <text class="empty-text">暂无预约请求</text>
            <text class="empty-hint">学生在「老师库」或「家教信息」中预约后，会自动出现在这里</text>
          </view>

          <view v-else>
            <view class="booking-card" v-for="item in teacherBookings" :key="item.courseId">
              <view class="booking-row between">
                <text class="booking-no">预约号：{{ item.courseId }}</text>
                <text :class="['booking-status', statusClass(item.status)]">{{ statusText(item.status) }}</text>
              </view>
              <view class="booking-row">学员：{{ item.studentName || ('学员' + (item.studentId || '-')) }}</view>
              <view class="booking-row" v-if="bookingContact(item)">联系方式：{{ bookingContact(item) }}</view>
              <view class="booking-row">日期：{{ item.startDate || '-' }}</view>
              <view class="booking-row">地址：{{ bookingAddress(item) }}</view>
              <view class="booking-row" v-if="bookingNote(item)">留言：{{ bookingNote(item) }}</view>
              <view class="booking-row" v-if="bookingCancelReason(item)">取消原因：{{ bookingCancelReason(item) }}</view>
              <view class="booking-row">课时费：¥{{ item.hourlyRate || 0 }}</view>

              <view v-if="Number(item.status) === 0" class="booking-actions">
                <button class="btn reject" size="mini" @click="decide(item, 2)">拒绝</button>
                <button class="btn approve" size="mini" @click="decide(item, 1)">同意</button>
              </view>
              <view v-else-if="Number(item.status) === 3" class="booking-actions">
                <button class="btn reject" size="mini" @click="decideCancel(item)">同意取消</button>
              </view>
            </view>

            <view v-if="teacherLoadStatus === 'loading'" class="load-more">加载中...</view>
            <view v-if="teacherLoadStatus === 'nomore' && teacherBookings.length > 0" class="load-more">没有更多了</view>
          </view>
        </scroll-view>
      </view>

      <view v-else class="teacher-section">
        <view class="section-title">我的家教信息</view>
        <scroll-view class="teacher-post-list" scroll-y @scrolltolower="loadMoreMyPublishedPosts">
          <view v-if="myPublishedPosts.length === 0 && postLoadStatus !== 'loading'" class="empty-state compact">
            <text class="empty-text">暂无家教信息</text>
            <text class="empty-hint">点击「发布家教信息」后，学生可在「家教信息」中看到并预约。</text>
          </view>
          <view v-else>
            <view class="post-card" v-for="item in myPublishedPosts" :key="item.publishId">
              <view class="booking-row between">
                <text class="booking-no">发布编号：{{ item.publishId }}</text>
                <text :class="['post-status', postStatusClass(item.status)]">{{ postStatusText(item.status) }}</text>
              </view>
              <view class="post-title">{{ tutoringTitle(item) }}</view>
              <view class="booking-row">科目：{{ item.subjectName || '-' }}</view>
              <view class="booking-row">年级：{{ item.gradeName || '-' }}</view>
              <view class="booking-row">可开始日期：{{ tutoringStartDate(item) }}</view>
              <view class="booking-row">说明：{{ item.address || '-' }}</view>
              <view class="booking-row">预约次数：{{ item.bookingCount || 0 }}</view>
              <view class="tutoring-feed-footer">
                <view>
                  <view class="tutoring-feed-price-label">家教课时费</view>
                  <view class="tutoring-feed-price">¥{{ item.hourlyRate || 0 }}/课时</view>
                </view>
                <text class="tutoring-feed-no">{{ item.expectedHours || 1 }}课时起</text>
              </view>
              <view class="booking-actions">
                <button class="btn plain" size="mini" @click="editPost(item)">编辑</button>
                <button class="btn approve" size="mini" @click="togglePostStatus(item)">{{ Number(item.status) === 0 ? '下架' : '上架' }}</button>
                <button class="btn reject" size="mini" @click="deletePost(item)">删除</button>
              </view>
            </view>
            <view v-if="postLoadStatus === 'loading'" class="load-more">加载中...</view>
            <view v-if="postLoadStatus === 'nomore' && myPublishedPosts.length > 0" class="load-more">没有更多了</view>
          </view>
        </scroll-view>
      </view>
    </view>

    <view v-else class="student-page">
      <view class="student-switch">
        <view
          :class="['student-switch-item', studentViewMode === 'teacher' ? 'active' : '']"
          @click="switchStudentView('teacher')"
        >老师库</view>
        <view
          :class="['student-switch-item', studentViewMode === 'tutoring' ? 'active' : '']"
          @click="switchStudentView('tutoring')"
        >家教信息</view>
        <view
          :class="['student-switch-item', studentViewMode === 'demand' ? 'active' : '']"
          @click="switchStudentView('demand')"
        >找家教需求</view>
      </view>

      <view class="search-container">
        <view class="search-box">
          <input v-model="searchText" class="search-input" :placeholder="searchPlaceholder" @confirm="handleSearch">
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

      <view v-if="studentViewMode === 'demand'" class="demand-actions">
        <button class="btn plain" size="mini" @click="goCreateDemand">发布需求</button>
        <button class="btn approve" size="mini" @click="goMyDemands">我的需求</button>
      </view>

      <scroll-view class="teacher-list" scroll-y @scrolltolower="loadMore">
        <view v-if="teacherList.length === 0 && loadStatus !== 'loading'" class="empty-state">
          <text class="empty-text">{{ studentViewMode === 'teacher' ? '未找到符合条件的老师' : '暂无匹配的家教信息' }}</text>
          <text class="empty-hint">{{ studentViewMode === 'teacher' ? '已审核通过的老师会显示在老师库，可按地区、年级、科目筛选。' : (studentViewMode === 'tutoring' ? '老师发布的家教信息会显示在这里，家长可直接点击预约。' : '家长发布找家教需求后，老师可在工作台接单。') }}</text>
        </view>

        <view v-else>
          <block v-if="studentViewMode === 'teacher'">
            <view
              v-for="teacher in teacherList"
              :key="teacher.teacherId"
              class="teacher-card"
              @click="goToTeacherDetail(teacher.teacherId)"
            >
              <image class="teacher-avatar" :src="getImage(teacher.image)" mode="aspectFill"></image>
              <view class="teacher-info">
                <view class="teacher-header">
                  <text class="teacher-name">{{ teacher.realName || '老师' }}</text>
                  <text class="teacher-tag">{{ teacher.education || '老师' }}</text>
                </view>
                <view class="teacher-subjects">{{ teacher.subjectName || '授课信息待完善' }}</view>
                <view class="teacher-tags">
                  <text class="tag" v-if="teacher.university">{{ teacher.university }}</text>
                  <text class="tag" v-if="teacher.gradeName">{{ teacher.gradeName }}</text>
                </view>
                <view class="teacher-desc">{{ teacher.areaName || '授课地区待完善' }}</view>
              </view>
              <view class="teacher-right">
                <view class="teacher-price-label">预期时薪</view>
                <view class="teacher-price">¥{{ teacher.hourlyRate || 0 }}/课时</view>
              </view>
            </view>
          </block>

          <block v-else-if="studentViewMode === 'tutoring'">
            <view
              v-for="teacher in teacherList"
              :key="teacher.publishId"
              class="tutoring-feed-card"
              @click="goToTutoringAppointment(teacher)"
            >
              <view class="tutoring-feed-head">
                <view class="tutoring-feed-badge">家教信息</view>
                <text class="tutoring-feed-no">编号 {{ teacher.publishId }}</text>
              </view>
              <view class="tutoring-feed-title">{{ tutoringTitle(teacher) }}</view>
              <view class="tutoring-feed-line">老师：{{ teacher.teacherName || '老师' }}</view>
              <view class="tutoring-feed-line">辅导科目：{{ teacher.subjectName || '待完善' }}</view>
              <view class="tutoring-feed-line">适合年级：{{ teacher.gradeName || '待完善' }}</view>
              <view class="tutoring-feed-line">可开课时间：{{ tutoringStartDate(teacher) }}</view>
              <view class="tutoring-feed-line">建议课时：{{ teacher.expectedHours || '1' }}课时</view>
              <view class="tutoring-feed-line">授课说明：{{ teacher.address || '老师暂未填写详细说明' }}</view>
              <view class="tutoring-feed-footer">
                <view>
                  <view class="tutoring-feed-price-label">家教课时费</view>
                  <view class="tutoring-feed-price">¥{{ teacher.hourlyRate || 0 }}/课时</view>
                </view>
                <text class="tutoring-feed-action">点击预约</text>
              </view>
            </view>
          </block>

          <block v-else>
            <view
              v-for="item in teacherList"
              :key="item.requestId"
              class="tutoring-feed-card"
            >
              <view class="tutoring-feed-head">
                <view class="tutoring-feed-badge">需求单</view>
                <text class="tutoring-feed-no">编号 {{ item.requestId }}</text>
              </view>
              <view class="tutoring-feed-title">{{ (item.gradeName || '') + (item.subjectName || '') + '家教需求' }}</view>
              <view class="tutoring-feed-line">地区：{{ item.areaName || '-' }}</view>
              <view class="tutoring-feed-line">预算：¥{{ item.budgetMin || 0 }} - ¥{{ item.budgetMax || 0 }}/课时</view>
              <view class="tutoring-feed-line">时段：{{ item.preferredTime || '待沟通' }}</view>
              <view class="tutoring-feed-line">说明：{{ item.demandDesc || '-' }}</view>
              <view class="tutoring-feed-footer">
                <view class="tutoring-feed-price-label">发布者：{{ item.parentName || '家长' }}</view>
                <text class="tutoring-feed-action">老师可在工作台接单</text>
              </view>
            </view>
          </block>

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
import { listPublishedCourses, listTeacherBookings, decideTeacherBooking, listMyPublishedCourses, updateMyPublishedCourseStatus, deleteMyPublishedCourse } from '@/api/course/course'
import { listOpenTutorRequests, bidTutorRequest } from '@/api/tutorRequest/tutorRequest'
import { baseUrl } from '../../../config'

export default {
  data() {
    return {
      searchText: '',
      activeDropdown: null,
      studentViewMode: 'teacher',
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
        { label: '已拒绝', value: 2 },
        { label: '取消申请', value: 3 }
      ],
      teacherStatusFilter: 0,
      teacherBookings: [],
      teacherPage: 1,
      teacherTotal: 0,
      teacherLoadStatus: 'loadmore',
      teacherRequestSeq: 0,
      teacherWorkMode: 'demand',
      teacherDemandList: [],
      teacherDemandPage: 1,
      teacherDemandTotal: 0,
      teacherDemandLoadStatus: 'loadmore',
      teacherDemandRequestSeq: 0,
      myPublishedPosts: [],
      postPage: 1,
      postTotal: 0,
      postLoadStatus: 'loadmore',
      postRequestSeq: 0
    }
  },
  computed: {
    roles() {
      return (this.$store && this.$store.getters && this.$store.getters.roles) || []
    },
    isTeacher() {
      return this.roles.includes('teacher')
    },
    searchPlaceholder() {
      if (this.studentViewMode === 'teacher') return '请输入老师姓名'
      if (this.studentViewMode === 'tutoring') return '请输入家教信息关键词'
      return '请输入需求关键词'
    }
  },
  mounted() {
    this.bootstrap()
  },
  onShow() {
    try {
      uni.setTabBarItem({ index: 1, text: this.isTeacher ? '工作台' : '找老师' })
    } catch (e) {}
    if (this.isTeacher) {
      if (this.teacherWorkMode === 'posts') this.refreshMyPublishedPosts()
      else if (this.teacherWorkMode === 'booking') this.refreshTeacherBookings()
      else this.refreshTeacherDemands()
    }
  },
  methods: {
    async bootstrap() {
      if (this.isTeacher) {
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
    goTeacherJoin() {
      uni.navigateTo({ url: '/pages/teacherJoin/teacherJoin' })
    },
    goPublishTutorPost() {
      uni.navigateTo({ url: '/pages/coursePublish/coursePublish' })
    },
    goCreateDemand() {
      uni.navigateTo({ url: '/pages/tutorRequest/create' })
    },
    goMyDemands() {
      uni.navigateTo({ url: '/pages/tutorRequest/my' })
    },
    switchTeacherWorkMode(mode) {
      if (this.teacherWorkMode === mode) return
      this.teacherWorkMode = mode
      if (mode === 'posts') this.refreshMyPublishedPosts()
      else if (mode === 'booking') this.refreshTeacherBookings()
      else this.refreshTeacherDemands()
    },
    refreshTeacherDemands() {
      this.teacherDemandPage = 1
      this.teacherDemandList = []
      this.teacherDemandLoadStatus = 'loadmore'
      this.loadMoreTeacherDemands()
    },
    async loadMoreTeacherDemands() {
      if (!this.isTeacher) return
      if (this.teacherDemandLoadStatus === 'loading' || this.teacherDemandLoadStatus === 'nomore') return
      this.teacherDemandLoadStatus = 'loading'
      const requestSeq = ++this.teacherDemandRequestSeq
      try {
        const params = { pageNum: this.teacherDemandPage, pageSize: this.pageSize }
        const res = await listOpenTutorRequests(params)
        if (requestSeq !== this.teacherDemandRequestSeq) return
        const rows = res.rows || []
        if (this.teacherDemandPage === 1) this.teacherDemandList = rows
        else this.teacherDemandList = [...this.teacherDemandList, ...rows]
        this.teacherDemandTotal = res.total != null ? res.total : this.teacherDemandList.length
        this.teacherDemandPage += 1
        this.teacherDemandLoadStatus = (rows.length === 0 || this.teacherDemandList.length >= this.teacherDemandTotal) ? 'nomore' : 'loadmore'
      } catch (e) {
        if (requestSeq !== this.teacherDemandRequestSeq) return
        this.teacherDemandLoadStatus = 'loadmore'
      }
    },
    submitBid(item) {
      uni.showModal({
        title: '确认接单',
        content: '确定对该需求提交接单吗？',
        success: async (r) => {
          if (!r.confirm) return
          try {
            await bidTutorRequest(item.requestId, {})
            uni.showToast({ title: '已提交接单', icon: 'success' })
            this.refreshTeacherDemands()
          } catch (e) {}
        }
      })
    },
    switchStudentView(mode) {
      if (this.studentViewMode === mode) return
      this.studentViewMode = mode
      this.searchText = ''
      this.activeDropdown = null
      this.refreshList()
    },
    statusText(s) {
      const n = Number(s)
      if (n === 0) return '待处理'
      if (n === 1) return '已同意'
      if (n === 2) return '已拒绝'
      if (n === 3) return '取消申请'
      return '未知'
    },
    statusClass(s) {
      const n = Number(s)
      if (n === 0) return 'pending'
      if (n === 1) return 'approved'
      if (n === 2) return 'rejected'
      if (n === 3) return 'pending'
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
      const requestSeq = ++this.teacherRequestSeq
      try {
        const params = { pageNum: this.teacherPage, pageSize: this.pageSize }
        if (this.teacherStatusFilter >= 0) params.status = this.teacherStatusFilter
        const res = await listTeacherBookings(params)
        if (requestSeq !== this.teacherRequestSeq) return
        const rows = res.rows || []
        if (this.teacherPage === 1) this.teacherBookings = rows
        else this.teacherBookings = [...this.teacherBookings, ...rows]
        this.teacherTotal = res.total != null ? res.total : this.teacherBookings.length
        this.teacherPage += 1
        this.teacherLoadStatus = (rows.length === 0 || this.teacherBookings.length >= this.teacherTotal) ? 'nomore' : 'loadmore'
      } catch (e) {
        if (requestSeq !== this.teacherRequestSeq) return
        this.teacherLoadStatus = 'loadmore'
      }
    },
    refreshMyPublishedPosts() {
      this.postPage = 1
      this.myPublishedPosts = []
      this.postLoadStatus = 'loadmore'
      this.loadMoreMyPublishedPosts()
    },
    async loadMoreMyPublishedPosts() {
      if (!this.isTeacher) return
      if (this.postLoadStatus === 'loading' || this.postLoadStatus === 'nomore') return
      this.postLoadStatus = 'loading'
      const requestSeq = ++this.postRequestSeq
      try {
        const res = await listMyPublishedCourses({ pageNum: this.postPage, pageSize: this.pageSize })
        if (requestSeq !== this.postRequestSeq) return
        const rows = res.rows || []
        if (this.postPage === 1) this.myPublishedPosts = rows
        else this.myPublishedPosts = [...this.myPublishedPosts, ...rows]
        this.postTotal = res.total != null ? res.total : this.myPublishedPosts.length
        this.postPage += 1
        this.postLoadStatus = (rows.length === 0 || this.myPublishedPosts.length >= this.postTotal) ? 'nomore' : 'loadmore'
      } catch (e) {
        if (requestSeq !== this.postRequestSeq) return
        this.postLoadStatus = 'loadmore'
      }
    },
    postStatusText(s) {
      return Number(s) === 0 ? '上架中' : '已下架'
    },
    postStatusClass(s) {
      return Number(s) === 0 ? 'approved' : 'rejected'
    },
    editPost(item) {
      uni.navigateTo({ url: `/pages/coursePublish/coursePublish?publishId=${item.publishId}` })
    },
    togglePostStatus(item) {
      const next = Number(item.status) === 0 ? 1 : 0
      const text = next === 0 ? '上架' : '下架'
      uni.showModal({
        title: `确认${text}`,
        content: `确定要${text}该家教信息吗？`,
        success: async (r) => {
          if (!r.confirm) return
          try {
            await updateMyPublishedCourseStatus(item.publishId, next)
            uni.showToast({ title: `已${text}`, icon: 'success' })
            this.refreshMyPublishedPosts()
          } catch (e) {}
        }
      })
    },
    deletePost(item) {
      uni.showModal({
        title: '确认删除',
        content: '删除后学生将无法再看到该家教信息，确定删除吗？',
        success: async (r) => {
          if (!r.confirm) return
          try {
            await deleteMyPublishedCourse(item.publishId)
            uni.showToast({ title: '已删除', icon: 'success' })
            this.refreshMyPublishedPosts()
          } catch (e) {}
        }
      })
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
    decideCancel(item) {
      uni.showModal({
        title: '确认取消预约',
        content: '确定同意学生取消该预约吗？',
        success: async (r) => {
          if (!r.confirm) return
          try {
            await decideTeacherBooking(item.courseId, 2)
            uni.showToast({ title: '已取消', icon: 'success' })
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
      if (!url) return '/static/image/1.png'
      if (String(url).startsWith('http://')) return '/static/image/1.png'
      if (String(url).startsWith('http')) return url
      return baseUrl + url
    },
    tutoringTitle(row) {
      const grade = row.gradeName || ''
      const subject = row.subjectName || ''
      if (grade && subject) return `${grade}${subject}家教`
      return `${subject || grade || '家教'}信息`
    },
    tutoringStartDate(row) {
      if (!row || !row.startDate) return '待老师沟通'
      return String(row.startDate).split('T')[0]
    },
    parseBookingAddress(raw) {
      const text = String(raw || '')
      const lines = text.split('\n')
      const data = { time: '', address: '', contact: '', note: '' }
      lines.forEach((line) => {
        if (line.indexOf('时段：') === 0) data.time = line.slice(3)
        else if (line.indexOf('地址：') === 0) data.address = line.slice(3)
        else if (line.indexOf('联系方式：') === 0) data.contact = line.slice(5)
        else if (line.indexOf('留言：') === 0) data.note = line.slice(3)
      })
      if (!data.address && text && lines.length === 1) data.address = text
      return data
    },
    bookingAddress(item) {
      if (item && (item.timeSlot || item.classAddress)) {
        const parts = []
        if (item.timeSlot) parts.push(`时段：${item.timeSlot}`)
        if (item.classAddress) parts.push(item.classAddress)
        return parts.join('；') || '-'
      }
      const parsed = this.parseBookingAddress(item && item.address)
      const parts = []
      if (parsed.time) parts.push(`时段：${parsed.time}`)
      if (parsed.address) parts.push(parsed.address)
      return parts.join('；') || '-'
    },
    bookingContact(item) {
      if (item && item.contactInfo) return item.contactInfo
      const parsed = this.parseBookingAddress(item && item.address)
      return parsed.contact || item.studentPhone || ''
    },
    bookingNote(item) {
      if (item && item.contactNote) return item.contactNote
      const parsed = this.parseBookingAddress(item && item.address)
      return parsed.note || ''
    },
    bookingCancelReason(item) {
      return item && item.cancelReason ? item.cancelReason : ''
    },
    goToTeacherDetail(input) {
      uni.navigateTo({ url: `/pages/findteacher/findteacher/teacherDetail/teacherDetail?id=${input}` })
    },
    goToTutoringAppointment(item) {
      uni.navigateTo({
        url: `/pages/findteacher/findteacher/teacherDetail/appointmentTeacher?id=${item.teacherId}&publishId=${item.publishId || ''}`
      })
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
        const params = { pageNum: this.page, pageSize: this.pageSize }
        const sid = this.resolveSubjectId(this.activeFilters.subject)
        const aid = this.resolveAreaId(this.activeFilters.area)
        const gid = this.resolveGradeId(this.activeFilters.grade)
        let res
        if (this.studentViewMode === 'teacher') {
          params.status = 1
          if (this.searchText) params.realName = this.searchText
          if (sid != null) params.subjectId = sid
          if (aid) params.areaId = aid
          if (gid) params.gradeId = gid
          res = await getTeacherMag(params)
        } else if (this.studentViewMode === 'tutoring') {
          params.status = 0
          if (this.searchText) {
            params.keyword = this.searchText
          }
          if (sid != null) params.subjectId = sid
          if (aid) params.areaId = aid
          if (gid) params.gradeId = gid
          res = await listPublishedCourses(params)
        } else {
          if (sid != null) params.subjectId = sid
          if (aid) params.areaId = aid
          if (gid) params.gradeId = gid
          res = await listOpenTutorRequests(params)
          if (this.searchText) {
            const kw = String(this.searchText).toLowerCase()
            res.rows = (res.rows || []).filter(i => {
              const text = `${i.demandDesc || ''}${i.parentName || ''}${i.subjectName || ''}${i.gradeName || ''}${i.areaName || ''}`.toLowerCase()
              return text.indexOf(kw) >= 0
            })
            res.total = res.rows.length
          }
        }
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

.student-switch { display: flex; gap: 10rpx; padding: 20rpx 20rpx 0; background: #fff; }
.student-switch-item { flex: 1; text-align: center; padding: 18rpx 0; font-size: 28rpx; color: #666; background: #f5f5f5; border-radius: 14rpx; }
.student-switch-item.active { color: #fff; background: #2d8cf0; font-weight: 600; }

.search-container { padding: 20rpx; background-color: #ffffff; box-shadow: 0 2rpx 10rpx rgba(0,0,0,.05); }
.search-box { display: flex; align-items: center; background-color: #f5f5f5; border-radius: 40rpx; padding: 16rpx 30rpx; position: relative; }
.search-input { flex: 1; height: 60rpx; font-size: 28rpx; color: #666; background-color: transparent; padding-right: 80rpx; }
.clear-icon { position: absolute; right: 120rpx; top: 50%; transform: translateY(-50%); font-size: 36rpx; color: #999; padding: 10rpx; }
.search-button { position: absolute; right: 20rpx; background: #4a86e8; color: white; padding: 8rpx 20rpx; border-radius: 30rpx; font-size: 26rpx; }

.filter-bar { display: flex; justify-content: space-between; padding: 20rpx; background-color: #ffffff; margin-bottom: 20rpx; }
.demand-actions { display: flex; justify-content: flex-end; gap: 12rpx; padding: 8rpx 20rpx 14rpx; background: #fff; }
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
.teacher-desc { font-size: 24rpx; color: #999; line-height: 1.5; margin-top: 4rpx; }
.teacher-right { text-align: right; display: flex; flex-direction: column; justify-content: space-between; align-items: flex-end; }
.teacher-price-label { font-size: 22rpx; color: #999; margin-bottom: 6rpx; }
.teacher-price { font-size: 32rpx; color: #FF5252; font-weight: bold; }
.tutoring-feed-card {
  background: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 24rpx;
  padding: 28rpx 26rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.05);
  border: 1rpx solid #f1f1f1;
}
.tutoring-feed-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18rpx;
}
.tutoring-feed-badge {
  display: inline-flex;
  align-items: center;
  padding: 8rpx 16rpx;
  font-size: 22rpx;
  color: #ff8a00;
  background: #fff4e6;
  border-radius: 999rpx;
  font-weight: 600;
}
.tutoring-feed-no {
  font-size: 22rpx;
  color: #9a9a9a;
}
.tutoring-feed-title {
  font-size: 34rpx;
  line-height: 1.4;
  color: #222;
  font-weight: 700;
  margin-bottom: 18rpx;
}
.tutoring-feed-line {
  font-size: 28rpx;
  line-height: 1.75;
  color: #444;
  margin-bottom: 6rpx;
  word-break: break-all;
}
.tutoring-feed-footer {
  margin-top: 20rpx;
  padding-top: 18rpx;
  border-top: 1rpx dashed #ececec;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.tutoring-feed-price {
  font-size: 34rpx;
  color: #ff6a00;
  font-weight: 700;
}
.tutoring-feed-price-label {
  margin-bottom: 4rpx;
  font-size: 22rpx;
  color: #999;
}
.tutoring-feed-action {
  font-size: 24rpx;
  color: #2d8cf0;
  background: #eef6ff;
  padding: 10rpx 18rpx;
  border-radius: 999rpx;
}
.load-more { padding: 30rpx; text-align: center; font-size: 28rpx; color: #999; }

.teacher-workbench-page { height: 100%; display: flex; flex-direction: column; }
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

.workbench-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 18rpx; padding: 18rpx; background: #fff; }
.workbench-card { display: flex; flex-direction: column; justify-content: center; min-height: 112rpx; padding: 18rpx; border-radius: 16rpx; background: #f7f8fb; border: 2rpx solid transparent; }
.workbench-card.primary { background: linear-gradient(135deg, #fff4d6, #ffe6a3); }
.workbench-card.active { border-color: #2d8cf0; background: #eef6ff; }
.workbench-card-title { font-size: 28rpx; color: #222; font-weight: 700; }
.workbench-card-sub { margin-top: 8rpx; font-size: 22rpx; color: #888; line-height: 1.4; }
.teacher-section { flex: 1; display: flex; flex-direction: column; min-height: 0; }
.section-title { padding: 20rpx 28rpx 8rpx; background: #fff; font-size: 30rpx; font-weight: 700; color: #222; }
.teacher-post-list { flex: 1; padding: 20rpx; }
.post-card { background: #fff; border-radius: 14rpx; padding: 22rpx; margin-bottom: 20rpx; box-shadow: 0 2rpx 8rpx rgba(0,0,0,.04); }
.post-title { font-size: 30rpx; font-weight: 700; color: #222; margin-bottom: 14rpx; }
.post-status { padding: 4rpx 14rpx; border-radius: 24rpx; font-size: 22rpx; }
.post-status.approved { background: #f6ffed; color: #52c41a; }
.post-status.rejected { background: #fff1f0; color: #f5222d; }
.empty-state.compact { padding-top: 80rpx; }
.btn.plain { background: #f5f5f5; color: #333; }
</style>
