<template>
  <view class="page">
    <view class="hero">
      <text class="hero-title">发布找家教需求</text>
      <text class="hero-sub">填写学习情况后，老师可在工作台接单</text>
    </view>

    <view class="form-card">
      <view class="field">
        <text class="label">辅导科目</text>
        <picker mode="selector" :range="subjectOptions" range-key="name" :value="subjectIndex" @change="onSubjectChange">
          <view class="picker">{{ subjectText }}</view>
        </picker>
      </view>

      <view class="field">
        <text class="label">学生年级</text>
        <picker mode="selector" :range="gradeOptions" range-key="name" :value="gradeIndex" @change="onGradeChange">
          <view class="picker">{{ gradeText }}</view>
        </picker>
      </view>

      <view class="field">
        <text class="label">所在地区</text>
        <picker mode="selector" :range="areaOptions" range-key="name" :value="areaIndex" @change="onAreaChange">
          <view class="picker">{{ areaText }}</view>
        </picker>
      </view>

      <view class="split-row">
        <view class="split-item">
          <text class="label">最低预算</text>
          <input v-model="form.budgetMin" class="input" type="digit" placeholder="元/课时" placeholder-style="color:#a6aeb8;" />
        </view>
        <view class="split-item">
          <text class="label">最高预算</text>
          <input v-model="form.budgetMax" class="input" type="digit" placeholder="元/课时" placeholder-style="color:#a6aeb8;" />
        </view>
      </view>

      <view class="field">
        <text class="label">联系方式</text>
        <input v-model="form.contactInfo" class="input" placeholder="手机号 / 微信号" placeholder-style="color:#a6aeb8;" />
      </view>

      <view class="field">
        <text class="label">授课方式</text>
        <picker mode="selector" :range="teachModes" :value="teachModeIndex" @change="onTeachModeChange">
          <view class="picker">{{ teachModeText }}</view>
        </picker>
      </view>

      <view class="field">
        <text class="label">期望时段</text>
        <input v-model="form.preferredTime" class="input" placeholder="例如：周六下午 14:00-16:00" placeholder-style="color:#a6aeb8;" />
      </view>

      <view class="field col">
        <text class="label">需求描述</text>
        <textarea
          v-model="form.demandDesc"
          class="textarea"
          maxlength="300"
          placeholder="请描述学生基础情况、目标分数、期望上课频率等"
          placeholder-style="color:#a6aeb8;"
        />
        <view class="counter">{{ (form.demandDesc || '').length }}/300</view>
      </view>

      <button class="submit" @click="submit">发布需求</button>
    </view>
  </view>
</template>

<script>
import { createTutorRequest } from '@/api/tutorRequest/tutorRequest'
import { findAllArea } from '@/api/teacher/area'
import { findAllGrade } from '@/api/teacher/grade'
import { findAllSubject } from '@/api/teacher/subject'

export default {
  data() {
    return {
      subjectOptions: [],
      gradeOptions: [],
      areaOptions: [],
      teachModes: ['上门授课', '线上授课', '均可'],
      subjectIndex: -1,
      gradeIndex: -1,
      areaIndex: -1,
      teachModeIndex: 0,
      form: {
        subjectId: '',
        gradeId: '',
        areaId: '',
        budgetMin: '',
        budgetMax: '',
        contactInfo: '',
        demandDesc: '',
        teachMode: '上门授课',
        preferredTime: ''
      }
    }
  },
  computed: {
    subjectText() {
      return this.subjectIndex >= 0 ? this.subjectOptions[this.subjectIndex].name : '请选择科目'
    },
    gradeText() {
      return this.gradeIndex >= 0 ? this.gradeOptions[this.gradeIndex].name : '请选择年级'
    },
    areaText() {
      return this.areaIndex >= 0 ? this.areaOptions[this.areaIndex].name : '请选择地区'
    },
    teachModeText() {
      return this.teachModes[this.teachModeIndex] || '请选择授课方式'
    }
  },
  async onLoad() {
    await this.loadOptions()
  },
  methods: {
    async loadOptions() {
      try {
        const [ar, gr, sr] = await Promise.all([findAllArea(), findAllGrade(), findAllSubject()])
        this.areaOptions = (ar.data || []).map((i) => ({
          ...i,
          id: i.areaId || i.id,
          name: i.name || i.areaName || ''
        }))
        this.gradeOptions = (gr.data || []).map((i) => ({
          ...i,
          id: i.gradeId || i.id,
          name: i.name || i.gradeName || i.levelName || ''
        }))
        this.subjectOptions = (sr.data || []).map((i) => ({
          ...i,
          id: i.subjectId || i.id,
          name: i.name || i.subjectName || ''
        }))
      } catch (e) {}
    },
    onSubjectChange(e) {
      this.subjectIndex = Number(e.detail.value)
      const item = this.subjectOptions[this.subjectIndex]
      this.form.subjectId = item ? String(item.id) : ''
    },
    onGradeChange(e) {
      this.gradeIndex = Number(e.detail.value)
      const item = this.gradeOptions[this.gradeIndex]
      this.form.gradeId = item ? String(item.id) : ''
    },
    onAreaChange(e) {
      this.areaIndex = Number(e.detail.value)
      const item = this.areaOptions[this.areaIndex]
      this.form.areaId = item ? String(item.id) : ''
    },
    onTeachModeChange(e) {
      this.teachModeIndex = Number(e.detail.value)
      this.form.teachMode = this.teachModes[this.teachModeIndex]
    },
    async submit() {
      if (!this.form.subjectId || !this.form.gradeId || !this.form.areaId) {
        return uni.showToast({ title: '请选择科目、年级和地区', icon: 'none' })
      }
      if (!this.form.budgetMin || !this.form.budgetMax) {
        return uni.showToast({ title: '请填写预算范围', icon: 'none' })
      }
      const min = Number(this.form.budgetMin)
      const max = Number(this.form.budgetMax)
      if (isNaN(min) || isNaN(max) || min <= 0 || max <= 0 || min > max) {
        return uni.showToast({ title: '预算范围不合法', icon: 'none' })
      }
      if (!this.form.contactInfo) {
        return uni.showToast({ title: '请填写联系方式', icon: 'none' })
      }
      if (!this.form.demandDesc) {
        return uni.showToast({ title: '请填写需求描述', icon: 'none' })
      }
      try {
        await createTutorRequest(this.form)
        uni.showToast({ title: '发布成功', icon: 'success' })
        setTimeout(() => uni.navigateBack(), 500)
      } catch (e) {}
    }
  }
}
</script>

<style>
page { background: #f3f5f9; }
.page { min-height: 100vh; padding: 24rpx; }
.hero {
  background: linear-gradient(135deg, #2d8cf0, #57a3f3);
  border-radius: 22rpx;
  padding: 28rpx 26rpx;
  color: #fff;
  box-shadow: 0 14rpx 30rpx rgba(45, 140, 240, .22);
}
.hero-title { display: block; font-size: 34rpx; font-weight: 700; }
.hero-sub { display: block; margin-top: 10rpx; font-size: 24rpx; opacity: .92; }

.form-card {
  margin-top: 18rpx;
  background: #fff;
  border-radius: 22rpx;
  padding: 24rpx;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, .04);
}
.field { margin-bottom: 22rpx; }
.field.col { margin-bottom: 12rpx; }
.label { display: block; color: #4a4a4a; font-size: 25rpx; margin-bottom: 10rpx; font-weight: 600; }
.input, .picker {
  background: #f7f8fa;
  border-radius: 14rpx;
  height: 88rpx;
  line-height: 88rpx;
  box-sizing: border-box;
  padding: 0 20rpx;
  font-size: 30rpx;
  color: #222;
  border: 1rpx solid #edf0f3;
}
.picker { color: #555; }
.input ::placeholder {
  color: #a6aeb8;
}
.split-row { display: flex; gap: 16rpx; margin-bottom: 22rpx; }
.split-item { flex: 1; }
.textarea {
  width: 100%;
  height: 220rpx;
  box-sizing: border-box;
  background: #f7f8fa;
  border-radius: 14rpx;
  padding: 18rpx;
  border: 1rpx solid #edf0f3;
  font-size: 30rpx;
  line-height: 1.6;
}
.counter {
  margin-top: 8rpx;
  text-align: right;
  color: #999;
  font-size: 22rpx;
}
.submit {
  margin-top: 20rpx;
  background: linear-gradient(135deg, #2d8cf0, #4aa3ff);
  color: #fff;
  border-radius: 44rpx;
  height: 86rpx;
  line-height: 86rpx;
  font-size: 30rpx;
  font-weight: 700;
  box-shadow: 0 12rpx 24rpx rgba(45, 140, 240, .28);
}
</style>
