<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="科目ID" prop="subjectId">
        <el-input v-model="queryParams.subjectId" placeholder="请输入科目ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="年级ID" prop="gradeId">
        <el-input v-model="queryParams.gradeId" placeholder="请输入年级ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 120px">
          <el-option label="待接单" :value="0" />
          <el-option label="已接单" :value="1" />
          <el-option label="已关闭" :value="2" />
          <el-option label="已取消" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="需求单ID" align="center" prop="requestId" />
      <el-table-column label="家长ID" align="center" prop="userId" />
      <el-table-column label="科目" align="center" prop="subjectName" />
      <el-table-column label="年级" align="center" prop="gradeName" />
      <el-table-column label="地区" align="center" prop="areaName" />
      <el-table-column label="预算范围" align="center">
        <template slot-scope="scope">
          <span>¥{{ scope.row.budgetMin || 0 }} - ¥{{ scope.row.budgetMax || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系方式" align="center" prop="contactInfo" />
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="statusTagType(scope.row.status)">{{ statusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewBids(scope.row)"
            v-hasPermi="['system:tutorRequest:query']"
          >查看接单</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="接单记录" :visible.sync="bidOpen" width="900px" append-to-body>
      <el-table v-loading="bidLoading" :data="bidListData">
        <el-table-column label="接单ID" align="center" prop="bidId" />
        <el-table-column label="老师ID" align="center" prop="teacherId" />
        <el-table-column label="老师姓名" align="center" prop="teacherName" />
        <el-table-column label="期望时薪" align="center" prop="expectedRate" />
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="bidStatusTagType(scope.row.status)">{{ bidStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="留言" align="center" prop="message" />
        <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listTutorRequest, listTutorRequestBid } from '@/api/my/tutorRequest'

export default {
  name: 'TutorRequest',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      list: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        subjectId: null,
        gradeId: null,
        status: null
      },
      bidOpen: false,
      bidLoading: false,
      bidListData: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    statusText(status) {
      const n = Number(status)
      if (n === 0) return '待接单'
      if (n === 1) return '已接单'
      if (n === 2) return '已关闭'
      if (n === 3) return '已取消'
      return '-'
    },
    statusTagType(status) {
      const n = Number(status)
      if (n === 0) return 'warning'
      if (n === 1) return 'success'
      if (n === 2) return 'info'
      if (n === 3) return 'danger'
      return ''
    },
    bidStatusText(status) {
      const n = Number(status)
      if (n === 0) return '待确认'
      if (n === 1) return '已确认'
      if (n === 2) return '已拒绝'
      if (n === 3) return '已撤回'
      return '-'
    },
    bidStatusTagType(status) {
      const n = Number(status)
      if (n === 0) return 'warning'
      if (n === 1) return 'success'
      if (n === 2) return 'danger'
      return 'info'
    },
    getList() {
      this.loading = true
      listTutorRequest(this.queryParams).then(res => {
        this.list = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleViewBids(row) {
      this.bidOpen = true
      this.bidLoading = true
      this.bidListData = []
      listTutorRequestBid(row.requestId, { pageNum: 1, pageSize: 50 }).then(res => {
        this.bidListData = res.rows || []
        this.bidLoading = false
      }).catch(() => {
        this.bidLoading = false
      })
    }
  }
}
</script>
