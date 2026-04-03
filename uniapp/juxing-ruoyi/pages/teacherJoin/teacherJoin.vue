<template>
	<view class="page">
		<view class="container">
			<!-- 页面标题 -->
			<view class="header">
				<text class="title">大学生家家教系统</text>
			</view>

			<!-- 表单卡片 -->
			<view class="form-card">
				<text class="form-title">老师入驻填写信息</text>

				<!-- 姓名输入 -->
				<view class="form-item">
					<text class="label">姓名<text class="required">*</text></text>
					<input v-model="formData.realName" placeholder="请输入真实姓名" maxlength="20" class="native-input" />
				</view>

				<!-- 性别选择 -->
				<view class="form-item">
					<text class="label">性别<text class="required">*</text></text>
					<picker mode="selector" range-key="text" :range="genderOptions" @change="handleGenderChange"
						class="native-picker">
						<view class="picker-text">
							{{ formData.gender === 'M' ? '男' : '女' }}
						</view>
					</picker>
				</view>

				<!-- 年龄输入
        <view class="form-item">
          <text class="label">年龄<text class="required">*</text></text>
          <input 
            v-model="formData.age"
            type="number"
            placeholder="请输入年龄(18-65岁)"
            class="native-input"
          />
        </view -->

				<!-- 联系方式输入 -->
				<!--      <view class="form-item">
          <text class="label">联系方式<text class="required">*</text></text>
          <input 
            v-model="formData.phone"
            type="number"
            placeholder="请输入手机号码"
            class="native-input"
          />
        </view> -->

				<!-- 所在地区 -->
				<view class="form-item">
					<text class="label">所在地区<text class="required">*</text></text>
					<picker mode="selector" :range="areaOptions" @change="handleAreaChange" class="native-picker">
						<view class="picker-text">
							{{ selectedArea || '请选择所在地区' }}
						</view>
					</picker>
				</view>

				<!-- 授课年级 -->
				<view class="form-item">
					<text class="label">授课年级<text class="required">*</text></text>
					<picker mode="selector" :range="gradeOptions" @change="handleGradeChange" class="native-picker">
						<view class="picker-text">
							{{ selectedGrade || '请选择授课年级' }}
						</view>
					</picker>
				</view>

				<!-- 教学科目选择 -->
				<view class="form-item">
					<text class="label">教学科目<text class="required">*</text></text>
					<picker mode="selector" :range="subjectOptions" @change="handleSubjectChange" class="native-picker">
						<view class="picker-text">
							{{ selectedSubject || '请选择教学科目' }}
						</view>
					</picker>
				</view>

				<!-- 最高学历选择 -->
				<view class="form-item">
					<text class="label">最高学历<text class="required">*</text></text>
					<picker mode="selector" :range="educationOptions" range-key="value" @change="handleEducationChange"
						class="native-picker">
						<view class="picker-text">
							{{ selectedEducation || '请选择学历' }}
						</view>
					</picker>
				</view>

				<!-- 所在大学输入 -->
				<view class="form-item">
					<text class="label">所在大学<text class="required">*</text></text>
					<input v-model="formData.university" placeholder="请输入您的学校名称" maxlength="50" class="native-input" />
				</view>

				<!-- 薪资 -->
				<view class="form-item">
					<text class="label">预期薪资<text class="required">*</text></text>
					<input v-model="formData.hourlyRate" placeholder="请输入您的预期薪资" maxlength="50" class="native-input" />
				</view>

				<!-- 头像上传 -->
				<!-- <view class="form-item">
					<text class="label">上传头像<text class="required">*</text></text>
					<view class="avatar-uploader">
						<view class="avatar-preview" v-if="formData.image"
							:style="{ backgroundImage: 'url(' + formData.image + ')' }"></view>
						<view v-else class="avatar-placeholder">
							<text>点击上传头像</text>
						</view>
						<button class="upload-btn" @click="uploadAvatar">选择图片</button>
					</view>
				</view> -->

				<!-- 头像上传 -->
				<view class="form-item">
					<text class="label">上传头像<text class="required">*</text></text>
					<view class="avatar-uploader">
						<view class="avatar-preview" v-if="formData.image"
							:style="{ backgroundImage: 'url(' + formData.image + ')' }">
							<view v-if="uploading" class="uploading-overlay">
								<view class="loading-spinner"></view>
							</view>
						</view>
						<view v-else class="avatar-placeholder">
							<text>点击上传头像</text>
						</view>
						<button class="upload-btn" @click="uploadAvatar" :disabled="uploading">
							{{ uploading ? '上传中...' : '选择图片' }}
						</button>
					</view>
				</view>

				<!-- 提交按钮 -->
				<view class="submit-area">
					<button class="submit-btn" @click="handleSubmit">提交</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		baseUrl
	} from '../../config';
	import {
		teacherJoin
	} from '@/api/teacherJoin/teacherJoin'
	import { getTeacherJoinStatus } from '@/api/teacherJoin/teacherJoin'
	import {
		getToken
	} from '@/utils/auth'
	import {
		findAllArea
	} from '@/api/teacher/area'
	import {
		findAllGrade
	} from '@/api/teacher/grade'
	import {
		findAllSubject
	} from '@/api/teacher/subject'

	export default {
		data() {
			return {
				uploading: false,
				areaRows: [],
				gradeRows: [],
				subjectRows: [],
				formData: {
					realName: '',
					areaId: '',
					gradeId: '',
					subjectId: '',
					gender: 'M',
					hourlyRate: '',
					education: '',
					university: '',
					image: ''
				},
				genderOptions: [{
						value: 'M',
						text: '男'
					},
					{
						value: 'W',
						text: '女'
					}
				],
				educationOptions: [{
						value: '本科',
						text: '本科'
					},
					{
						value: '研究生',
						text: '硕士'
					},
					{
						value: '博士生',
						text: '博士'
					}
				],
				selectedArea: '',
				selectedGrade: '',
				selectedSubject: '',
				selectedEducation: '',
				teacherJoinInfo: {
					hasSubmitted: false,
					approved: false,
					status: -1
				},
				approvedTipShown: false
			}
		},
		mounted() {
			this.loadDicts()
		},
		onShow() {
			this.checkRoleAndStatus()
		},
		computed: {
			roles() {
				return (this.$store && this.$store.getters && this.$store.getters.roles) || []
			},
			isTeacher() {
				return this.roles.includes('teacher')
			},
			areaOptions() {
				return this.areaRows.map(item => item.name)
			},
			gradeOptions() {
				return this.gradeRows.map(item => item.name)
			},
			subjectOptions() {
				return this.subjectRows.map(item => item.name)
			}
		},
		methods: {
			async checkRoleAndStatus() {
				if (!getToken()) {
					uni.navigateTo({ url: '/pages/login' })
					return
				}
				if (!this.isTeacher) {
					uni.showToast({ title: '仅老师账号可申请入驻', icon: 'none' })
					setTimeout(() => {
						uni.switchTab({ url: '/pages/index/index' })
					}, 800)
					return
				}
				try {
					const res = await getTeacherJoinStatus()
					const d = res.data || {}
					this.teacherJoinInfo = {
						hasSubmitted: !!d.hasSubmitted,
						approved: !!d.approved,
						status: Number(d.status)
					}
					if (this.teacherJoinInfo.approved) {
						if (!this.approvedTipShown) {
							this.approvedTipShown = true
							uni.showModal({
								title: '已入驻成功',
								content: '您的老师入驻已审核通过，可以直接去上架课程。',
								confirmText: '去发布',
								success: (r) => {
									if (r.confirm) {
										uni.navigateTo({ url: '/pages/coursePublish/coursePublish' })
									}
								}
							})
						}
					}
				} catch (e) {}
			},
			async loadDicts() {
				try {
					const [ar, gr, sr] = await Promise.all([
						findAllArea(),
						findAllGrade(),
						findAllSubject()
					])
					this.areaRows = ar.data || []
					this.gradeRows = gr.data || []
					this.subjectRows = sr.data || []
				} catch (e) {
					console.error(e)
					uni.showToast({
						title: '选项加载失败，请下拉重试',
						icon: 'none'
					})
				}
			},
			handleGenderChange(e) {
				const index = e.detail.value;
				this.formData.gender = this.genderOptions[index].value;
			},
			handleAreaChange(e) {
				const index = parseInt(e.detail.value, 10)
				const row = this.areaRows[index]
				if (row) {
					this.formData.areaId = row.areaId
					this.selectedArea = row.name
				}
			},
			handleGradeChange(e) {
				const index = parseInt(e.detail.value, 10)
				const row = this.gradeRows[index]
				if (row) {
					this.formData.gradeId = row.gradeId
					this.selectedGrade = row.name
				}
			},
			handleSubjectChange(e) {
				const index = parseInt(e.detail.value, 10)
				const row = this.subjectRows[index]
				if (row) {
					this.formData.subjectId = row.subjectId
					this.selectedSubject = row.name
				}
			},
			handleEducationChange(e) {
				const index = e.detail.value;
				this.formData.education = this.educationOptions[index].value;
				this.selectedEducation = this.educationOptions[index].text;
			},

			// 上传头像方法
			uploadAvatar() {
				const _this = this;
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'], // 压缩图片
					sourceType: ['album', 'camera'],
					success(res) {
						const tempFilePath = res.tempFilePaths[0];

						// 显示预览（临时路径）
						_this.formData.image = tempFilePath;

						// 开始上传
						_this.uploading = true;

						uni.uploadFile({
							url: baseUrl + '/common/upload', // 若依上传接口
							filePath: tempFilePath,
							name: 'file', // 必须与后端@RequestParam("file")一致
							header: {
								'Authorization': 'Bearer ' + getToken() // 添加认证token
							},
							success: (uploadRes) => {
								try {
									const resData = JSON.parse(uploadRes.data);
									if (resData.code === 200) {
										// 使用后端返回的URL
										_this.formData.image = resData.url;
										uni.showToast({
											title: '上传成功',
											icon: 'success'
										});
									} else {
										uni.showToast({
											title: resData.msg || '上传失败',
											icon: 'none'
										});
									}
								} catch (e) {
									uni.showToast({
										title: '解析响应失败',
										icon: 'none'
									});
								}
								_this.uploading = false;
							},
							fail: (error) => {
								_this.uploading = false;
								uni.showToast({
									title: '上传失败: ' + error.errMsg,
									icon: 'none'
								});
							}
						});
					}
				});
			},

			// 表单提交方法
			handleSubmit() {
				if (this.teacherJoinInfo.hasSubmitted) {
					uni.showToast({
						title: '您已提交过入驻申请，请等待管理员审核',
						icon: 'none'
					})
					return
				}
				// 表单验证
				const requiredFields = [{
						field: 'realName',
						message: '请输入真实姓名'
					},
					{
						field: 'areaId',
						message: '请选择所在地区'
					},
					{
						field: 'gradeId',
						message: '请选择授课年级'
					},
					{
						field: 'subjectId',
						message: '请选择教学科目'
					},
					{
						field: 'education',
						message: '请选择最高学历'
					},
					{
						field: 'university',
						message: '请输入所在大学'
					},
					{
						field: 'hourlyRate',
						message: '请输入预期薪资'
					},
					{
						field: 'image',
						message: '请上传头像'
					}
				];

				// 检查必填项
				for (const item of requiredFields) {
					if (!this.formData[item.field]) {
						uni.showToast({
							title: item.message,
							icon: 'none'
						});
						return;
					}
				}

				// 检查图片是否是临时路径
				if (this.formData.image.startsWith('http://tmp/')) {
					uni.showToast({
						title: '请等待图片上传完成',
						icon: 'none'
					});
					return;
				}

				// 显示加载状态
				uni.showLoading({
					title: '提交中...',
					mask: true
				});

				// 准备提交数据（如果后端只需要相对路径）
				const submitData = {
					...this.formData,
					image: this.formData.image.replace(baseUrl, '')
				}
				delete submitData.status
				delete submitData.userId
				const sid = parseInt(submitData.subjectId, 10)
				if (!isNaN(sid)) {
					submitData.subjectId = sid
				}

				teacherJoin(submitData).then(res => {
					uni.hideLoading();
					if (res.code === 200) {
						uni.showModal({
							title: '提交成功',
							content: '入驻申请已提交至管理端通知管理，需管理员审核通过后方可入驻成功。',
							showCancel: false,
							success: () => {
								this.resetForm();
								uni.switchTab({
									url: '/pages/index/index'
								})
							}
						});
					} else {
						uni.showToast({
							title: res.msg || '提交失败',
							icon: 'none',
							duration: 3000
						});
					}
					}).catch(error => {
					uni.hideLoading();
					let msg = '提交失败，请稍后重试'
					if (typeof error === 'string') {
						if (error === '500' || error === '403' || error === '401') {
							// 具体后端提示已由 request.js 弹出，这里避免出现 undefined
							msg = '提交失败，请检查提示后重试'
						} else if (error.trim()) {
							msg = error
						}
					} else if (error && (error.message || error.errMsg)) {
						msg = error.message || error.errMsg
					}
					uni.showToast({
						title: msg,
						icon: 'none',
						duration: 3000
					});
				});
			},

			// 重置表单方法
			resetForm() {
				this.formData = {
					realName: '',
					areaId: '',
					gradeId: '',
					subjectId: '',
					gender: 'M',
					hourlyRate: '',
					education: '',
					university: '',
					image: ''
				}
				this.selectedArea = ''
				this.selectedGrade = ''
				this.selectedSubject = ''
				this.selectedEducation = ''
			}
		}
	}
</script>


<style>
	page {
		height: 100%;
		background: linear-gradient(180deg, #FFF9E6 0%, #FFF3CD 100%);
	}

	/* --------------------------------------- */
	/* 上传加载动画 */
	.uploading-overlay {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.3);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.loading-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 4rpx solid rgba(255, 255, 255, 0.3);
		border-radius: 50%;
		border-top: 4rpx solid #ffffff;
		animation: spin 1s linear infinite;
	}

	@keyframes spin {
		0% {
			transform: rotate(0deg);
		}

		100% {
			transform: rotate(360deg);
		}
	}

	/* 禁用按钮样式 */
	.upload-btn[disabled] {
		opacity: 0.6;
	}

	/* --------------------------------------- */

	.container {
		padding: 30rpx;
	}

	.header {
		text-align: center;
		margin: 40rpx 0 60rpx;
	}

	.title {
		font-size: 38rpx;
		color: #8B5A2B;
		font-weight: bold;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
	}

	.form-card {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 40rpx;
		box-shadow: 0 8rpx 24rpx rgba(240, 218, 154, 0.4);
		border: 1rpx solid #FFEBCD;
	}

	.form-title {
		font-size: 34rpx;
		color: #8B5A2B;
		margin-bottom: 40rpx;
		font-weight: 600;
		text-align: center;
		display: block;
		padding-bottom: 20rpx;
		border-bottom: 2rpx solid #FFEBCD;
	}

	.form-item {
		margin-bottom: 36rpx;
	}

	.label {
		display: block;
		font-size: 28rpx;
		color: #66512C;
		margin-bottom: 16rpx;
		font-weight: 500;
	}

	.required {
		color: #E65100;
		margin-left: 6rpx;
	}

	.native-input {
		height: 88rpx;
		width: 100%;
		padding: 0 30rpx;
		background-color: #FFF9E6;
		border-radius: 12rpx;
		font-size: 28rpx;
		border: 1rpx solid #FFEBCD;
		box-sizing: border-box;
		color: #333;
	}

	.native-picker {
		height: 88rpx;
		line-height: 88rpx;
		background-color: #FFF9E6;
		border-radius: 12rpx;
		font-size: 28rpx;
		padding: 0 30rpx;
		margin-top: 8rpx;
		border: 1rpx solid #FFEBCD;
		width: 100%;
		box-sizing: border-box;
		color: #666;
	}

	.picker-text {
		color: #66512C;
	}

	.submit-area {
		margin-top: 60rpx;
		padding: 0 20rpx;
	}

	.submit-btn {
		background: linear-gradient(135deg, #FFA500 0%, #FF8C00 100%);
		color: #FFFFFF;
		height: 96rpx;
		line-height: 96rpx;
		border-radius: 48rpx;
		font-size: 34rpx;
		font-weight: 600;
		width: 100%;
		border: none;
		box-shadow: 0 6rpx 16rpx rgba(255, 165, 0, 0.3);
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
	}

	.submit-btn:active {
		transform: translateY(2rpx);
		box-shadow: 0 4rpx 12rpx rgba(255, 165, 0, 0.2);
	}

	/* 头像上传样式 */
	.avatar-uploader {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-top: 20rpx;
	}

	.avatar-preview {
		width: 240rpx;
		height: 240rpx;
		border-radius: 50%;
		background-color: #FFF3CD;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin-bottom: 24rpx;
		border: 4rpx dashed #FFEBCD;
		box-shadow: 0 4rpx 12rpx rgba(240, 218, 154, 0.5);
	}

	.avatar-preview img {
		width: 100%;
		height: 100%;
		border-radius: 50%;
		object-fit: cover;
	}

	.avatar-placeholder {
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.avatar-placeholder text {
		margin-top: 16rpx;
		color: #8B5A2B;
		font-size: 26rpx;
	}

	.avatar-placeholder .iconfont {
		font-size: 60rpx;
		color: #FFD700;
	}

	.upload-btn {
		background-color: #FFA500;
		color: white;
		padding: 16rpx 50rpx;
		border-radius: 36rpx;
		font-size: 28rpx;
		border: none;
		box-shadow: 0 4rpx 12rpx rgba(255, 165, 0, 0.3);
	}

	.upload-btn:active {
		transform: translateY(2rpx);
		box-shadow: 0 2rpx 8rpx rgba(255, 165, 0, 0.2);
	}

	/* 引入字体图标 */
	@font-face {
		font-family: 'iconfont';
		src: url('https://at.alicdn.com/t/font_1678543_6v02l9n965i.ttf') format('truetype');
	}

	.iconfont {
		font-family: "iconfont" !important;
		font-size: 36rpx;
		font-style: normal;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
	}

	.icon-camera:before {
		content: "\e601";
	}
</style>
