<template>
	<view class="login-container">
		<view class="logo-section">
			<image class="logo-img" src="/static/images/login/logo.jpg" mode="aspectFit" />
		</view>

		<view class="login-form-content">
			<!-- 登录方式切换 -->
			<view class="login-type-tabs">
				<view class="tab-item" :class="{ active: loginType === 'code' }" @click="switchType('code')">验证码登录
				</view>
				<view class="tab-item" :class="{ active: loginType === 'password' }" @click="switchType('password')">
					密码登录</view>
			</view>
			<!-- 注册入口：首屏可见，避免密码+验证码时底部链接被挡在屏外 -->
			<view class="register-hint" @click="goRegister">
				<text>没有账号？</text><text class="register-strong">立即注册</text>
			</view>

			<!-- 验证码登录 -->
			<view v-if="loginType === 'code'">
				<view class="input-item flex align-center">
					<view class="iconfont icon-user icon"></view>
					<input v-model="codeLoginForm.phone" class="input" type="number" placeholder="请输入手机号"
						maxlength="11" />
				</view>
				<view class="input-item flex align-center">
					<view class="iconfont icon-code icon"></view>
					<input v-model="codeLoginForm.code" type="number" class="input" placeholder="请输入验证码"
						maxlength="6" />
					<button @click="getSmsCode" class="get-code-btn"
						:disabled="smsCodeBtn.disabled">{{ smsCodeBtn.text }}</button>
				</view>
			</view>

			<!-- 密码登录 -->
			<view v-if="loginType === 'password'">
				<view class="input-item flex align-center">
					<view class="iconfont icon-user icon"></view>
					<input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" />
				</view>
				<view class="input-item flex align-center">
					<view class="iconfont icon-password icon"></view>
					<input v-model="loginForm.password" type="password" class="input" placeholder="请输入密码"
						maxlength="20" />
				</view>
				<view class="input-item flex align-center" style="width: 60%;margin: 0px;" v-if="captchaEnabled">
					<view class="iconfont icon-code icon"></view>
					<input v-model="loginForm.code" type="number" class="input" placeholder="请输入验证码" maxlength="4" />
					<view class="login-code">
						<image :src="codeUrl" @click="getCode" class="login-code-img"></image>
					</view>
				</view>
			</view>

			<view class="action-btn">
				<button @click="handleLogin" class="login-btn cu-btn block bg-yellow lg round">登录</button>
			</view>
			<view class="footer-links">
				<text class="link-text" @click="goRegister">没有账号？去注册</text>
			</view>
		</view>
	</view>
</template>


<script>
	import {
		mapActions
	} from 'vuex'
	import {
		getUserCode,
		getCodeImg,
		login,
		userCodeLogin
	} from '@/api/login'
	import {
		setToken
	} from '@/utils/auth.js'

	export default {
		data() {
			return {
				codeUrl: "",
				captchaEnabled: true,
				// 用户注册开关
				register: false,
				loginType: 'code',
				loginForm: {
					username: '',
					password: '',
					code: '',
					uuid: ''
				},
				codeLoginForm: {
					phone: '',
					code: ''
				},
				smsCodeBtn: {
					text: '获取验证码',
					disabled: false,
					timer: null,
					count: 60
				}
			}
		},
		created() {
			this.getCode()
		},
		methods: {
			...mapActions(['Login', 'GetInfo']),

			switchType(type) {
				this.loginType = type
			},

			goRegister() {
				uni.navigateTo({
					url: '/pages/register'
				})
			},

			// 获取图形验证码
			getCode() {
				getCodeImg().then(res => {
					this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
					if (this.captchaEnabled) {
						this.codeUrl = 'data:image/gif;base64,' + res.img
						this.loginForm.uuid = res.uuid
					}
				})
			},

			handleLogin() {
				if (this.loginType === 'password') {
					if (!this.loginForm.username) {
						return uni.showToast({
							title: '请输入账号',
							icon: 'none'
						})
					}
					if (!this.loginForm.password) {
						return uni.showToast({
							title: '请输入密码',
							icon: 'none'
						})
					}
					uni.showLoading({
						title: '登录中，请耐心等待...'
					})
					this.pwdLogin()
				} else {
					if (!this.codeLoginForm.phone) {
						return uni.showToast({
							title: '请输入手机号',
							icon: 'none'
						})
					}
					if (!/^1\d{10}$/.test(this.codeLoginForm.phone)) {
						return uni.showToast({
							title: '请输入正确的手机号',
							icon: 'none'
						})
					}
					if (!this.codeLoginForm.code) {
						return uni.showToast({
							title: '请输入验证码',
							icon: 'none'
						})
					}
					uni.showLoading({
						title: '登录中，请耐心等待...'
					})
					this.codeLogin()
				}
			},

			getSmsCode() {
				if (this.smsCodeBtn.disabled) return

				if (!this.codeLoginForm.phone) {
					return uni.showToast({
						title: '请输入手机号码',
						icon: 'none'
					})
				}
				if (!/^1\d{10}$/.test(this.codeLoginForm.phone)) {
					return uni.showToast({
						title: '请输入正确的手机号码',
						icon: 'none'
					})
				}

				this.smsCodeBtn.disabled = true

				getUserCode(this.codeLoginForm.phone).then(res => {
					if (res.code === 200) {
						uni.showToast({
							title: '验证码发送成功',
							icon: 'none'
						})

						this.codeLoginForm.code = res.data

						this.smsCodeBtn.timer = setInterval(() => {
							this.smsCodeBtn.count--
							this.smsCodeBtn.text = `${this.smsCodeBtn.count}秒`
							if (this.smsCodeBtn.count <= 0) {
								clearInterval(this.smsCodeBtn.timer)
								this.smsCodeBtn.timer = null
								this.smsCodeBtn.count = 60
								this.smsCodeBtn.text = '获取验证码'
								this.smsCodeBtn.disabled = false
							}
						}, 1000)
					} else {
						this.smsCodeBtn.disabled = false
						uni.showToast({
							title: res.msg || '验证码发送失败，请重试',
							icon: 'none'
						})
					}
				}).catch(() => {
					this.smsCodeBtn.disabled = false
					uni.showToast({
						title: '验证码发送失败，请检查网络',
						icon: 'none'
					})
				})
			},

			async pwdLogin() {
				try {
					await login(this.loginForm).then(res => {
						if (res.code == 200) {
							setToken(res.token)
							this.loginSuccess()
						} else {
							uni.showToast({ title: res.msg || '登录失败', icon: 'none' })
						}
						uni.hideLoading()
					})
				} catch (e) {
					uni.hideLoading()
					console.error(e)
				}
			},

			async codeLogin() {
				try {
					await userCodeLogin(this.codeLoginForm).then(res => {
						if (res.code == 200) {
							setToken(res.token)
							this.loginSuccess()
						} else {
							uni.showToast({ title: res.msg || '登录失败', icon: 'none' })
						}
						uni.hideLoading()
					})
				} catch (e) {
					uni.hideLoading()
					console.error(e)
				}
			},

			loginSuccess() {
				this.GetInfo().then(() => {
					try {
						const roles = (this.$store && this.$store.getters && this.$store.getters.roles) || []
						uni.setTabBarItem({
							index: 1,
							text: roles.includes('teacher') ? '预约管理' : '找老师'
						})
					} catch (e) {}
					uni.reLaunch({
						url: '/pages/index/index'
					})
				})
			}
		},
		beforeDestroy() {
			if (this.smsCodeBtn.timer) {
				clearInterval(this.smsCodeBtn.timer)
				this.smsCodeBtn.timer = null
			}
		}
	}
</script>




<style lang="scss" scoped>
	page {
		background-color: #ffffff;
	}

	.login-container {
		min-height: 100vh;
		background: linear-gradient(180deg, rgba(255, 237, 207, 0.3) 0%, rgba(255, 255, 255, 1) 100%);
		padding: 0 40rpx;
	}

	.logo-section {
		padding-top: 120rpx;
		text-align: center;
		margin-bottom: 60rpx;
	}

	.logo-img {
		width: 320rpx;
		height: 320rpx;
		border-radius: 24rpx;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.08);
	}

	.logo-content {
		width: 100%;
		font-size: 21px;
		text-align: center;
		padding-top: 15%;

		image {
			border-radius: 4px;
		}

		.title {
			margin-left: 10px;
		}
	}

	.login-form-content {
		text-align: center;
		margin: 20px auto;
		margin-top: 15%;
		width: 80%;

		.login-type-tabs {
			display: flex;
			justify-content: center;
			margin-bottom: 40rpx;

			.tab-item {
				font-size: 32rpx;
				color: #999;
				padding: 0 20rpx;
				margin: 0 20rpx;
				position: relative;
				cursor: pointer;

				&.active {
					color: #333;
					font-weight: bold;

					&::after {
						content: '';
						position: absolute;
						bottom: -10rpx;
						left: 50%;
						transform: translateX(-50%);
						width: 40rpx;
						height: 6rpx;
						background-color: #f1b346;
						border-radius: 3rpx;
					}
				}
			}
		}


		.input-item {
			margin: 20px auto;
			background-color: #f5f6f7;
			height: 45px;
			border-radius: 20px;

			.icon {
				font-size: 38rpx;
				margin-left: 10px;
				color: #999;
			}

			.input {
				width: 100%;
				font-size: 14px;
				line-height: 20px;
				text-align: left;
				padding-left: 15px;
			}

			.get-code-btn {
				background: transparent;
				height: 60px;
				border: none;
				color: #f1b346;
				font-size: 28rpx;
				padding: 0 20rpx;
				margin-right: 10rpx;
				white-space: nowrap;
				height: 100%;
				line-height: 45px;

				&::after {
					border: none;
				}

				&[disabled] {
					color: #999;
				}
			}

		}

		.login-btn {
			margin-top: 40px;
			height: 45px;
		}

		.reg {
			margin-top: 15px;
		}

		.xieyi {
			color: #333;
			margin-top: 20px;
		}

		.login-code {
			height: 38px;
			float: right;

			.login-code-img {
				height: 38px;
				position: absolute;
				margin-left: 10px;
				width: 200rpx;
			}
		}

		.register-hint {
			margin: -6rpx 0 28rpx;
			font-size: 28rpx;
			color: #666;
			text-align: center;

			.register-strong {
				color: #d48806;
				font-weight: 600;
				margin-left: 8rpx;
			}
		}

		.footer-links {
			margin-top: 28rpx;
			margin-bottom: calc(24rpx + env(safe-area-inset-bottom));
			text-align: center;
		}

		.link-text {
			font-size: 28rpx;
			color: #d48806;
		}
	}

	.abc {
		height: 100px;
	}
</style>
