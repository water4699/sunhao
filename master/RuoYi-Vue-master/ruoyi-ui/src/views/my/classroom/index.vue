<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="教室名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入教室名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在建筑" prop="building">
        <el-input
          v-model="queryParams.building"
          placeholder="请输入所在建筑"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房间号" prop="roomNumber">
        <el-input
          v-model="queryParams.roomNumber"
          placeholder="请输入房间号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="设施列表" prop="facilities">-->
<!--        <el-input-->
<!--          v-model="queryParams.facilities"-->
<!--          placeholder="请输入设施列表"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="每小时费用" prop="hourlyRate">-->
<!--        <el-input-->
<!--          v-model="queryParams.hourlyRate"-->
<!--          placeholder="请输入每小时费用"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="${comment}" prop="createdAt">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.createdAt"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择${comment}">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:classroom:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:classroom:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:classroom:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:classroom:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="classroomList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="教室序号" align="center" prop="classroomId" />
      <el-table-column label="教室名称" align="center" prop="name" />
      <el-table-column label="地区ID" align="center" prop="locationId" />
      <el-table-column label="所在建筑" align="center" prop="building" />
      <el-table-column label="房间号" align="center" prop="roomNumber" />
      <el-table-column label="容量" align="center" prop="capacity" />
      <el-table-column label="设施列表" align="center" prop="facilities" />
      <el-table-column label="每小时费用" align="center" prop="hourlyRate" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="管理管理员" align="center" prop="adminId" />
      <el-table-column label="${comment}" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:classroom:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:classroom:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改教室对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="教室名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入教室名称" />
        </el-form-item>
        <el-form-item label="所在建筑" prop="building">
          <el-input v-model="form.building" placeholder="请输入所在建筑" />
        </el-form-item>
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="form.roomNumber" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="设施列表" prop="facilities">
          <el-input v-model="form.facilities" placeholder="请输入设施列表" />
        </el-form-item>
        <el-form-item label="每小时费用" prop="hourlyRate">
          <el-input v-model="form.hourlyRate" placeholder="请输入每小时费用" />
        </el-form-item>
        <el-form-item label="${comment}" prop="createdAt">
          <el-date-picker clearable
            v-model="form.createdAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择${comment}">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listClassroom, getClassroom, delClassroom, addClassroom, updateClassroom } from "@/api/my/classroom"

export default {
  name: "Classroom",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 教室表格数据
      classroomList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        locationId: null,
        building: null,
        roomNumber: null,
        capacity: null,
        facilities: null,
        hourlyRate: null,
        status: null,
        adminId: null,
        createdAt: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "教室名称不能为空", trigger: "blur" }
        ],
        locationId: [
          { required: true, message: "地区ID不能为空", trigger: "blur" }
        ],
        building: [
          { required: true, message: "所在建筑不能为空", trigger: "blur" }
        ],
        roomNumber: [
          { required: true, message: "房间号不能为空", trigger: "blur" }
        ],
        capacity: [
          { required: true, message: "容量不能为空", trigger: "blur" }
        ],
        facilities: [
          { required: true, message: "设施列表不能为空", trigger: "blur" }
        ],
        hourlyRate: [
          { required: true, message: "每小时费用不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        adminId: [
          { required: true, message: "管理管理员不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询教室列表 */
    getList() {
      this.loading = true
      listClassroom(this.queryParams).then(response => {
        this.classroomList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        classroomId: null,
        name: null,
        locationId: null,
        building: null,
        roomNumber: null,
        capacity: null,
        facilities: null,
        hourlyRate: null,
        status: null,
        adminId: null,
        createdAt: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.classroomId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加教室"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const classroomId = row.classroomId || this.ids
      getClassroom(classroomId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改教室"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.classroomId != null) {
            updateClassroom(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addClassroom(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const classroomIds = row.classroomId || this.ids
      this.$modal.confirm('是否确认删除教室编号为"' + classroomIds + '"的数据项？').then(function() {
        return delClassroom(classroomIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/classroom/export', {
        ...this.queryParams
      }, `classroom_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
