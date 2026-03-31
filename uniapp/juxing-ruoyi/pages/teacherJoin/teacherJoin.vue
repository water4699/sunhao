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
<!-- 
<script>
	import {
		baseUrl
	} from '../../config';
	import {
		teacherJoin
	} from '@/api/teacherJoin/teacherJoin'
	import {
		teacherImage
	} from '@/api/teacherJoin/teacherJoin'

	export default {
		data() {
			return {
				formData: {
					realName: '',
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
					},
				],
				subjects: [{
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
				selectedSubject: '',
				selectedEducation: ''
			}
		},
		computed: {
			subjectOptions() {
				return this.subjects.map(item => item.text);
			}
		},
		methods: {
			handleGenderChange(e) {
				var index = e.detail.value;
				this.formData.gender = this.genderOptions[index].value;
			},
			handleSubjectChange(e) {
				const index = e.detail.value;
				this.formData.subjectId = this.subjects[index].value;
				this.selectedSubject = this.subjects[index].text;
				console.log(this.formData.subject);
			},
			handleEducationChange(e) {
				const index = e.detail.value;
				this.formData.education = this.educationOptions[index].value;
				this.selectedEducation = this.educationOptions[index].text;
			},
			uploadAvatar() {
				const _this = this;
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success(res) {
						
						_this.formData.avatarUrl = res.tempFilePaths[0];
					}
				});
			},
			handleSubmit() {

				teacherJoin(this.formData).then(res => {
					if (res.code === 200) {
						uni.showToast({
							title: '提交成功',
							icon: 'success'
						});
					} else {
						uni.showToast({
							title: '提交失败',
							icon: 'error'
						});
					}
				})
			}
		}
	}
</script>
 -->

<script>
	import {
		baseUrl
	} from '../../config';
	import {
		teacherJoin
	} from '@/api/teacherJoin/teacherJoin'
	import {
		getToken
	} from '@/utils/auth' // 引入获取token的方法

	export default {
		data() {
			return {
				uploading: false, // 添加上传状态
				formData: {
					realName: '',
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
				subjects: [{
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
				selectedSubject: '',
				selectedEducation: ''
			}
		},
		computed: {
			subjectOptions() {
				return this.subjects.map(item => item.text);
			}
		},
		methods: {
			handleGenderChange(e) {
				const index = e.detail.value;
				this.formData.gender = this.genderOptions[index].value;
			},
			handleSubjectChange(e) {
				const index = e.detail.value;
				this.formData.subjectId = this.subjects[index].value;
				this.selectedSubject = this.subjects[index].text;
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
				// 表单验证
				const requiredFields = [{
						field: 'realName',
						message: '请输入真实姓名'
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
					image: this.formData.image.replace(baseUrl, '') // 去掉baseUrl部分
				};

				teacherJoin(submitData).then(res => {
					uni.hideLoading();
					if (res.code === 200) {
						uni.switchTab({
							url: '/pages/findteacher/findteacher/findteacher'
						});
						uni.showToast({
							title: '提交成功',
							icon: 'success',
							duration: 2000,
							success: () => {
								// 2秒后重置表单
								setTimeout(() => {
									this.resetForm();
								}, 2000);
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
					uni.showToast({
						title: '提交失败: ' + (error.message || error.errMsg),
						icon: 'none',
						duration: 3000
					});
				});
			},

			// 重置表单方法
			resetForm() {
				this.formData = {
					realName: '',
					subjectId: '',
					gender: 'M',
					hourlyRate: '',
					education: '',
					university: '',
					image: ''
				};
				this.selectedSubject = '';
				this.selectedEducation = '';
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