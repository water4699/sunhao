<template>
  <view class="page">
    <view class="form-card">
      <view class="row"><text class="label">科目ID</text><input v-model="form.subjectId" class="input" placeholder="如：1" /></view>
      <view class="row"><text class="label">年级ID</text><input v-model="form.gradeId" class="input" placeholder="如：1" /></view>
      <view class="row"><text class="label">地区ID</text><input v-model="form.areaId" class="input" placeholder="如：1" /></view>
      <view class="row"><text class="label">预算最低</text><input v-model="form.budgetMin" class="input" type="digit" placeholder="元/课时" /></view>
      <view class="row"><text class="label">预算最高</text><input v-model="form.budgetMax" class="input" type="digit" placeholder="元/课时" /></view>
      <view class="row"><text class="label">联系方式</text><input v-model="form.contactInfo" class="input" placeholder="手机号/微信" /></view>
      <view class="row"><text class="label">授课方式</text><input v-model="form.teachMode" class="input" placeholder="上门/线上/到店" /></view>
      <view class="row"><text class="label">期望时段</text><input v-model="form.preferredTime" class="input" placeholder="如：周六下午" /></view>
      <view class="row col">
        <text class="label">需求描述</text>
        <textarea v-model="form.demandDesc" class="textarea" placeholder="请描述学生情况和辅导目标" />
      </view>
      <button class="submit" @click="submit">提交需求</button>
    </view>
  </view>
</template>

<script>
import { createTutorRequest } from '@/api/tutorRequest/tutorRequest'

export default {
  data() {
    return {
      form: {
        subjectId: '',
        gradeId: '',
        areaId: '',
        budgetMin: '',
        budgetMax: '',
        contactInfo: '',
        demandDesc: '',
        teachMode: '',
        preferredTime: ''
      }
    }
  },
  methods: {
    async submit() {
      if (!this.form.subjectId || !this.form.gradeId || !this.form.areaId) return uni.showToast({ title: '请完善科目年级地区', icon: 'none' })
      if (!this.form.budgetMin || !this.form.budgetMax) return uni.showToast({ title: '请填写预算', icon: 'none' })
      if (!this.form.contactInfo) return uni.showToast({ title: '请填写联系方式', icon: 'none' })
      if (!this.form.demandDesc) return uni.showToast({ title: '请填写需求描述', icon: 'none' })
      try {
        await createTutorRequest(this.form)
        uni.showToast({ title: '已发布', icon: 'success' })
        setTimeout(() => uni.navigateBack(), 500)
      } catch (e) {}
    }
  }
}
</script>

<style>
page { background: #f5f5f5; }
.page { padding: 20rpx; }
.form-card { background: #fff; border-radius: 16rpx; padding: 20rpx; }
.row { display: flex; align-items: center; margin-bottom: 18rpx; }
.row.col { display: block; }
.label { width: 140rpx; color: #666; font-size: 26rpx; }
.input { flex: 1; background: #f7f7f7; border-radius: 12rpx; padding: 14rpx 18rpx; font-size: 28rpx; }
.textarea { width: 100%; height: 200rpx; margin-top: 10rpx; background: #f7f7f7; border-radius: 12rpx; padding: 16rpx; font-size: 28rpx; }
.submit { margin-top: 20rpx; background: #2d8cf0; color: #fff; border-radius: 40rpx; }
</style>
