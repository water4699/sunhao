<script>
  import config from './config'
  import { getToken } from '@/utils/auth'
  import storage from '@/utils/storage'
  import constant from '@/utils/constant'

  export default {
    onLaunch: function() {
      this.initApp()
    },
    onShow: function() {
      this.syncTabBarByRole()
    },
    methods: {
      // 初始化应用
      initApp() {
        // 初始化应用配置
        this.initConfig()
        // 检查用户登录状态
        //#ifdef H5
        this.checkLogin()
        //#endif
        this.syncTabBarByRole()
      },
      initConfig() {
        this.globalData.config = config
      },
      syncTabBarByRole() {
        let roles = storage.get(constant.roles)
        if (typeof roles === 'string' && roles.startsWith('[')) {
          try {
            roles = JSON.parse(roles)
          } catch (e) {}
        }
        if (!Array.isArray(roles)) {
          roles = roles ? [roles] : []
        }
        const isTeacher = roles.includes('teacher')
        uni.setTabBarItem({
          index: 1,
          text: isTeacher ? '预约管理' : '找老师'
        })
      },
      checkLogin() {
        if (!getToken()) {
          this.$tab.reLaunch('/pages/login') 
        }
      }
    }
  }
</script>

<style lang="scss">
  @import '@/static/scss/index.scss'
</style>
