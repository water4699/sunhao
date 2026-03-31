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
		baseUrl
	} from '../../../../config';
	
export default {
  data() {
    return {
      teacherAvatar: 'https://ai-public.mastergo.com/ai/img_res/ed1d9ac872926869a023748b95562aab.jpg',
      teacherName: '王老师',
      teacherSubjects: ['数学', '物理'],
      rating: 4.5,
      selectedDate: '',
      selectedTime: '',
      // selectedSubject: '',
      address: '',
      subjectOptions: ['数学', '物理', '化学', '英语'],
      startDate: '2023-01-01',
      endDate: '2024-12-31'
    }
  },
  computed: {
    isFormValid() {
      return this.selectedDate && this.selectedTime && this.selectedSubject !== '' && this.address;
    },
    fullStars() {
      return Math.floor(this.rating);
    },
    hasHalfStar() {
      return this.rating % 1 >= 0.5;
    }
  },
  
  onLoad(e){
	  console.log(e.id);
  	this.id = e.id;
  },
  mounted() {
  	this.init();
  },
  methods: {
	  init() {
	  	getOneTeacher(this.id).then(res => {
	  		var msg = res.data;
			console.log(msg);
	  		this.teacherAvatar = baseUrl + msg.image;
	  		this.teacherName = msg.realName;
	  		this.gender = msg.gender;
	  		this.education =msg.education;
	  		this.school =msg.university;
	  		this.teacherSubjects = msg.subjectName;
	  		this.hourlyFee = msg.hourlyRate;
	  		this.score = msg.rating;
	  	})
	  	
	  },
    handleDateChange(e) {
      this.selectedDate = e.detail.value;
    },
    handleTimeChange(e) {
      this.selectedTime = e.detail.value;
    },
    handleSubjectChange(e) {
      this.selectedSubject = e.detail.value;
    },
    handleSubmit() {
      if (!this.isFormValid) {
        uni.showToast({
          title: '请完善所有预约信息',
          icon: 'none',
        });
        return;
      }
      
      uni.showToast({
        title: '预约成功',
        icon: 'success',
		duration: 1500,
		success: () => {
		    // 在 toast 消失后跳转
		    setTimeout(() => {
		        uni.switchTab({
		            url: `/pages/findteacher/findteacher/findteacher`,
		        })
		    }, 1500)
		}
      });
      
      // 重置表单
      this.selectedDate = '';
      this.selectedTime = '';
      this.selectedSubject = '';
      this.address = '';
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