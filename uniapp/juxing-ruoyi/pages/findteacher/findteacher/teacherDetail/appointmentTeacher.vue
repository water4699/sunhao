<template>
  <view class="container">
    <!-- 教师信息卡片 -->
    <view class="teacher-card">
      <image class="teacher-avatar" :src="teacherAvatar" mode="aspectFill" />
      <view class="teacher-info">
        <text class="teacher-name">{{ teacherName }}</text>
        <!-- <view class="teacher-subjects">
          <text v-for="subject in teacherSubjects" :key="subject" class="subject-tag">{{ subject }}</text>
        </view> -->
		<view class="teacher-subjects">
		  <text class="subject-tag">{{ teacherSubjects }}</text>
		</view>
        <view class="teacher-rating">
          <view class="rating-stars">
            <view 
              v-for="i in 5" 
              :key="i" 
              class="star" 
              :class="{ 
                'star-filled': i <= fullStars,
                'star-half': i === fullStars + 1 && hasHalfStar 
              }"
            ></view>
          </view>
          <text class="rating-text">{{ rating }}分</text>
        </view>
      </view>
    </view>

    <!-- 预约表单 -->
    <view class="booking-form">
      <view class="form-item">
        <text class="form-label">选择日期</text>
        <picker mode="date" :start="startDate" :end="endDate" v-model="selectedDate" @change="handleDateChange">
          <view class="picker-box">
            <text class="picker-text">{{ selectedDate || '请选择日期' }}</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">选择时段</text>
        <picker mode="time" start="08:00" end="22:00" interval="30" v-model="selectedTime" @change="handleTimeChange">
          <view class="picker-box">
            <text class="picker-text">{{ selectedTime || '请选择时段' }}</text>
          </view>
        </picker>
      </view>

     <!-- <view class="form-item">
        <text class="form-label">选择科目</text>
        <picker mode="selector" :range="subjectOptions" v-model="selectedSubject" @change="handleSubjectChange">
          <view class="picker-box">
            <text class="picker-text">{{ subjectOptions[selectedSubject] || '请选择科目' }}</text>
          </view>
        </picker>
      </view> -->

      <view class="form-item">
        <text class="form-label">上课地址</text>
        <input v-model="address" placeholder="请输入详细地址" class="address-input" />
      </view>

      <view class="form-item">
        <text class="form-label">联系方式</text>
        <input v-model="contactPhone" placeholder="请输入手机号或微信号" class="address-input" />
      </view>

      <view class="form-item">
        <text class="form-label">留言</text>
        <textarea v-model="contactNote" maxlength="120" placeholder="可简单说明需求，方便老师联系" class="remark-input" />
      </view>
    </view>

    <!-- 确认按钮 -->
    <button class="submit-btn" @click="handleSubmit" :disabled="!isFormValid">
      确认预约
    </button>
  </view>
</template>

<script>
	import {
		getOneTeacher
	} from '@/api/teacher/getTeacherMag'
	import {
		addCourse
	} from '@/api/course/course'
	import {
		getPublishedCourseDetail
	} from '@/api/course/course'
	import {
		baseUrl
	} from '../../../../config'
	import {
		getToken
	} from '@/utils/auth'

	export default {
		data() {
			return {
				id: '',
				teacherAvatar: '',
				teacherName: '',
				teacherSubjects: '',
				teacherSubjectId: '',
				publishId: '',
				rating: 0,
				hourlyFee: 0,
				selectedDate: '',
				selectedTime: '',
				address: '',
				contactPhone: '',
				contactNote: '',
				startDate: '',
				endDate: ''
			}
		},
		computed: {
			isFormValid() {
				return !!(this.selectedDate && this.selectedTime && (this.address || '').trim() && (this.contactPhone || '').trim())
			},
			fullStars() {
				return Math.floor(Number(this.rating) || 0)
			},
			hasHalfStar() {
				return (Number(this.rating) || 0) % 1 >= 0.5
			}
		},
		onLoad(options) {
			if (!getToken()) {
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				})
				setTimeout(() => {
					uni.navigateTo({
						url: '/pages/login'
					})
				}, 800)
				return
			}
			this.id = (options && (options.id || options.teacherId)) || ''
			this.publishId = (options && options.publishId) || ''
			const d = new Date()
			this.startDate = this.fmtDate(d)
			const end = new Date(d.getTime() + 90 * 86400000)
			this.endDate = this.fmtDate(end)
		},
		mounted() {
			this.loadTeacher()
		},
		methods: {
			fmtDate(d) {
				const y = d.getFullYear()
				const m = String(d.getMonth() + 1).padStart(2, '0')
				const day = String(d.getDate()).padStart(2, '0')
				return `${y}-${m}-${day}`
			},
			loadTeacher() {
				if (!this.id) return
				getOneTeacher(this.id).then(res => {
					const t = res.data
					if (!t) return
					const img = t.image || ''
					if (!img) this.teacherAvatar = '/static/image/1.png'
					else if (String(img).startsWith('http://')) this.teacherAvatar = '/static/image/1.png'
					else if (String(img).startsWith('http')) this.teacherAvatar = img
					else this.teacherAvatar = baseUrl + img
					this.teacherName = t.realName || ''
					this.teacherSubjects = t.subjectName || ''
					const sid = t.subjectId
					this.teacherSubjectId = sid != null && sid !== '' ? String(sid) : ''
					this.rating = Number(t.rating) || 0
					this.hourlyFee = t.hourlyRate != null ? t.hourlyRate : 0
				}).catch(() => {})
				if (this.publishId) {
					getPublishedCourseDetail(this.publishId).then(res => {
						const row = res.data || {}
						if (!row) return
						if (row.subjectName) this.teacherSubjects = row.subjectName
						if (row.subjectId != null && row.subjectId !== '') this.teacherSubjectId = String(row.subjectId)
						if (row.hourlyRate != null) this.hourlyFee = row.hourlyRate
					}).catch(() => {})
				}
			},
			handleDateChange(e) {
				this.selectedDate = e.detail.value
			},
			handleTimeChange(e) {
				this.selectedTime = e.detail.value
			},
			handleSubmit() {
				if (!getToken()) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					})
					return
				}
				if (!this.isFormValid) {
					uni.showToast({
						title: '请填写日期、时段、地址和联系方式',
						icon: 'none'
					})
					return
				}
				const address = (this.address || '').trim()
				const contactPhone = (this.contactPhone || '').trim()
				const contactNote = (this.contactNote || '').trim()
				const payload = {
					teacherId: String(this.id),
					startDate: this.selectedDate,
					address: `时段：${this.selectedTime}\n地址：${address}\n联系方式：${contactPhone}${contactNote ? `\n留言：${contactNote}` : ''}`,
					hourlyRate: this.hourlyFee,
					expectedHours: '1',
					status: 0
				}
				if (this.teacherSubjectId) {
					payload.subjectId = this.teacherSubjectId
				}
				uni.showLoading({
					title: '提交中...'
				})
				addCourse(payload).then(() => {
					uni.hideLoading()
					uni.showToast({
						title: '预约已提交',
						icon: 'success'
					})
					setTimeout(() => {
						uni.navigateTo({
							url: '/pages/mine/order/order'
						})
					}, 1200)
				}).catch(() => {
					uni.hideLoading()
				})
			}
		}
	}
</script>

<style>
page {
  height: 100%;
  background: linear-gradient(180deg, #FFF9E6 0%, #FFF3CD 100%);
}

.container {
  padding: 30rpx;
  box-sizing: border-box;
}

.teacher-card {
  background-color: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
  box-shadow: 0 6rpx 16rpx rgba(240, 218, 154, 0.4);
}

.teacher-avatar {
  width: 140rpx;
  height: 140rpx;
  border-radius: 70rpx;
  margin-right: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

.teacher-info {
  flex: 1;
}

.teacher-name {
  font-size: 20px;
  color: #333333;
  font-weight: bold;
  margin-bottom: 12rpx;
}

.teacher-subjects {
  display: flex;
  gap: 16rpx;
  margin-bottom: 16rpx;
}

.subject-tag {
  background-color: #D4B483;
  color: #FFFFFF;
  padding: 6rpx 20rpx;
  border-radius: 20rpx;
  font-size: 14px;
  white-space: nowrap;
}

.teacher-rating {
  display: flex;
  align-items: center;
}

.rating-stars {
  display: flex;
  margin-right: 16rpx;
}

.star {
  width: 32rpx;
  height: 32rpx;
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16"><path fill="%23ddd" d="M8 1L9.83 5h4.17L14 9l-3 2.67L8 16l-3-2.33L2 9l4.17-4H8.17L8 1z"/></svg>');
  background-size: 100% 100%;
  background-repeat: no-repeat;
}

.star-filled {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16"><path fill="%23FFB90F" d="M8 1L9.83 5h4.17L14 9l-3 2.67L8 16l-3-2.33L2 9l4.17-4H8.17L8 1z"/></svg>');
}

.star-half {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16"><path fill="%23FFB90F" d="M8 1L9.83 5h4.17L14 9l-3 2.67L8 16l-3-2.33L2 9l4.17-4H8.17L8 1z"/><path fill="%23FFF" d="M9.5 5.5l1.33 3.34L14 9l-2.83 1.67L8 16l-1.67-3.33L2 9l3.67-4L8 1l1.5 3.5z"/></svg>');
}

.rating-text {
  color: #B4916E;
  font-size: 16px;
}

.booking-form {
  background-color: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 60rpx;
  box-shadow: 0 6rpx 16rpx rgba(240, 218, 154, 0.4);
}

.form-item {
  margin-bottom: 40rpx;
}

.form-item:last-child {
  margin-bottom: 0;
}

.form-label {
  display: block;
  color: #8B4513;
  font-size: 18px;
  margin-bottom: 20rpx;
  font-weight: 500;
}

.picker-box {
  height: 90rpx;
  line-height: 90rpx;
  background-color: #FFF3CD;
  border-radius: 16rpx;
  padding: 0 30rpx;
  color: #666666;
  font-size: 18px;
  border: 1rpx solid #FFEBCD;
}

.picker-text {
  color: #666;
}

.address-input {
  height: 90rpx;
  width: 100%;
  padding: 0 30rpx;
  background-color: #FFF3CD;
  border-radius: 16rpx;
  font-size: 18px;
  border: 1rpx solid #FFEBCD;
  box-sizing: border-box;
  color: #333333;
}

.remark-input {
  width: 100%;
  min-height: 180rpx;
  padding: 24rpx 30rpx;
  background-color: #FFF3CD;
  border-radius: 16rpx;
  font-size: 18px;
  border: 1rpx solid #FFEBCD;
  box-sizing: border-box;
  color: #333333;
}

.submit-btn {
  width: 90%;
  height: 96rpx;
  background: linear-gradient(135deg, #FFB90F 0%, #FF9F00 100%);
  color: #8B4513;
  font-size: 20px;
  font-weight: bold;
  border-radius: 48rpx;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(255, 185, 15, 0.4);
  border: none;
}

.submit-btn:active {
  opacity: 0.9;
  transform: translateY(2rpx);
  box-shadow: 0 6rpx 16rpx rgba(255, 185, 15, 0.3);
}

.submit-btn[disabled] {
  background-color: #F5E6CA;
  color: #B4916E;
  box-shadow: none;
}
</style>
