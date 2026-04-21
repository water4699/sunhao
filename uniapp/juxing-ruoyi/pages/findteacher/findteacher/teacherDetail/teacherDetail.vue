<template>
	<view class="teacher-detail">
		<!-- 教师信息头部 -->
		<view class="teacher-info">
			<image class="teacher-avatar" :src="avatar" mode="aspectFill"></image>
			<view class="teacher-base">
				<view class="teacher-name">
					<text>{{teacherName}}</text>
					<text class="gender">{{gender}}</text>
				</view>
				<view class="teacher-desc">{{education}}</view>
				<view class="teacher-desc">{{school}}</view>
				<view class="teacher-score">评分：{{score}}分</view>
			</view>
		</view>

		<!-- 授课科目 -->
		<view class="subject-title">授课科目</view>
		<view class="subject-list">
			<view class="subject-item" >{{subjects}}</view>
		</view>

		<!-- 课时费用 -->
		<view class="fee-box">
			<view class="fee-title">课时费用</view>
			<view class="fee-content">
				<text class="fee-money">¥{{hourlyFee}}</text>
				<text class="fee-unit">/小时</text>
			</view>
			<view class="fee-tip">* 费用包含备课及课后答疑时间</view>
		</view>

		<view v-if="publishedInfo" class="post-box">
			<view class="post-title">老师发布的家教信息</view>
			<view class="post-line">{{ publishedInfo.gradeName || '' }} {{ publishedInfo.subjectName || '' }}</view>
			<view class="post-line" v-if="publishedInfo.startDate">可开始授课：{{ formatDate(publishedInfo.startDate) }}</view>
			<view class="post-line" v-if="publishedInfo.expectedHours">建议课时：{{ publishedInfo.expectedHours }} 课时</view>
			<view class="post-line" v-if="publishedInfo.address">说明：{{ publishedInfo.address }}</view>
		</view>

		<!-- 教师评价 -->
		<view class="eval-title">
			<text>教师评价</text>
			<text class="more-eval" @click="toMoreEval">更多></text>
		</view>

		<!-- 立即预约按钮 -->
		<view @click="goToAppointmentTeacher()">
			<button class="reserve-btn">立即预约</button>
		</view>
	</view>
</template>

<script>
	import {
		getOneTeacher
	} from '@/api/teacher/getTeacherMag'
	import { listPublishedCourses } from '@/api/course/course'
	import {
		baseUrl
	} from '../../../../config';

	export default {
		data() {
			return {
				id: "",
				avatar: 'https://via.placeholder.com/80x80?text=Image', // 占位头像，实际替换为真实图片地址
				teacherName: '张雨晨',
				gender: '女',
				education: '教育学硕士',
				school: '北京师范大学',
				score: 4.8,
				subjects: ['语文'],
				hourlyFee: 280,
				publishedInfo: null
			}
		},
		onLoad(e){
			this.id = e.id;
		},
		mounted() {
			this.init();
		},
		methods: {
			init() {
				getOneTeacher(this.id).then(res => {
					var msg = res.data;
					const img = msg.image || ''
					this.avatar = this.resolveImage(img)
					this.teacherName = msg.realName;
					this.gender = msg.gender;
					this.education =msg.education;
					this.school =msg.university;
					this.subjects = msg.subjectName;
					this.hourlyFee = msg.hourlyRate;
					this.score = msg.rating;
				})
				listPublishedCourses({
					pageNum: 1,
					pageSize: 1,
					teacherId: this.id,
					status: 0
				}).then(res => {
					const rows = res.rows || []
					this.publishedInfo = rows.length ? rows[0] : null
				}).catch(() => {
					this.publishedInfo = null
				})
				
			},
			resolveImage(url) {
				if (!url) return '/static/image/1.png'
				if (String(url).startsWith('http://')) return '/static/image/1.png'
				if (String(url).startsWith('http')) return url
				return baseUrl + url
			},
			formatDate(v) {
				if (!v) return ''
				return String(v).split('T')[0]
			},
			goToAppointmentTeacher() {
			     uni.navigateTo({
			       url: `/pages/findteacher/findteacher/teacherDetail/appointmentTeacher?id=${this.id}`,
			     });
			   },
			toMoreEval() {
				uni.navigateTo({
					url: '/pages/review/review',
				});
			},
		}
	}
</script>

<style scoped>
	.teacher-detail {
		padding: 15px;
		background-color: #fff;
	}

	/* 教师信息头部样式 */
	.teacher-info {
		display: flex;
		align-items: center;
		margin-bottom: 15px;
	}

	.teacher-avatar {
		width: 80px;
		height: 80px;
		border-radius: 50%;
		margin-right: 10px;
	}

	.teacher-base {
		display: flex;
		flex-direction: column;
	}

	.teacher-name {
		display: flex;
		align-items: center;
		margin-bottom: 5px;
	}

	.teacher-name text:first-child {
		font-size: 18px;
		font-weight: bold;
		margin-right: 5px;
	}

	.gender {
		font-size: 14px;
		color: #999;
	}

	.teacher-desc {
		font-size: 14px;
		color: #666;
		margin-bottom: 3px;
	}

	.teacher-score {
		font-size: 14px;
		color: #666;
	}

	/* 授课科目样式 */
	.subject-title {
		font-size: 16px;
		font-weight: bold;
		margin: 15px 0 10px;
	}

	.subject-list {
		display: flex;
		flex-wrap: wrap;
	}

	.subject-item {
		padding: 6px 12px;
		background-color: #fce9d2;
		border-radius: 20px;
		margin-right: 10px;
		margin-bottom: 10px;
		font-size: 14px;
		color: #333;
	}

	/* 课时费用样式 */
	.fee-box {
		background-color: #f9f9f9;
		padding: 15px;
		border-radius: 8px;
		margin: 15px 0;
	}

	.fee-title {
		font-size: 16px;
		font-weight: bold;
		margin-bottom: 10px;
	}

	.fee-content {
		display: flex;
		align-items: baseline;
	}

	.fee-money {
		font-size: 20px;
		font-weight: bold;
		color: #f00;
		margin-right: 5px;
	}

	.fee-unit {
		font-size: 14px;
		color: #666;
	}

	.fee-tip {
		font-size: 12px;
		color: #999;
		margin-top: 5px;
	}

	.post-box {
		background-color: #fff7ef;
		border-radius: 10px;
		padding: 14px;
		margin: 15px 0;
	}

	.post-title {
		font-size: 16px;
		font-weight: bold;
		color: #333;
		margin-bottom: 8px;
	}

	.post-line {
		font-size: 14px;
		color: #555;
		line-height: 1.7;
	}

	/* 教师评价样式 */
	.eval-title {
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 16px;
		font-weight: bold;
		margin-bottom: 10px;
	}

	.more-eval {
		font-size: 14px;
		color: #007aff;
	}

	/* 立即预约按钮样式 */
	.reserve-btn {
		width: 100%;
		background-color: #fce9d2;
		color: #333;
		font-size: 16px;
		padding: 12px 0;
		border-radius: 8px;
	}
</style>
