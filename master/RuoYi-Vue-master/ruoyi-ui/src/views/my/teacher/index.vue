<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="关联科目" prop="subjectId">-->
<!--        <el-input-->
<!--          v-model="queryParams.subjectId"-->
<!--          placeholder="请输入关联科目"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
      <el-form-item label="真实姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入真实姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在大学" prop="university">
        <el-input
          v-model="queryParams.university"
          placeholder="请输入所在大学"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时薪" prop="hourlyRate">
        <el-input
          v-model="queryParams.hourlyRate"
          placeholder="请输入时薪"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平均评分" prop="rating">
        <el-input
          v-model="queryParams.rating"
          placeholder="请输入平均评分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--&lt;!&ndash;      <el-form-item label="时间" prop="creatTime">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-date-picker clearable&ndash;&gt;-->
<!--&lt;!&ndash;          v-model="queryParams.creatTime"&ndash;&gt;-->
<!--&lt;!&ndash;          type="date"&ndash;&gt;-->
<!--&lt;!&ndash;          value-format="yyyy-MM-dd"&ndash;&gt;-->
<!--&lt;!&ndash;          placeholder="请选择时间">&ndash;&gt;-->
<!--&lt;!&ndash;        </el-date-picker>&ndash;&gt;-->
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
          v-hasPermi="['system:teacher:add']"
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
          v-hasPermi="['system:teacher:edit']"
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
          v-hasPermi="['system:teacher:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:teacher:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="teacherList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="教师编号" align="center" prop="teacherId" />
<!--      <el-table-column label="关联用户ID" align="center" prop="userId" />-->
      <el-table-column label="关联科目" align="center" prop="subjectName" />
      <el-table-column label="真实姓名" align="center" prop="realName" />
      <el-table-column label="性别" align="center" prop="gender" />
      <el-table-column label="学历" align="center" prop="education" />
      <el-table-column label="所在大学" align="center" prop="university" />
      <el-table-column label="时薪" align="center" prop="hourlyRate" />
      <el-table-column label="平均评分" align="center" prop="rating" />
      <el-table-column label="认证状态" align="center" prop="status" />
      <el-table-column label="图片" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50"/>
        </template>
      </el-table-column>

      <el-table-column label="时间" align="center" prop="creatTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.creatTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:teacher:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:teacher:remove']"
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

    <!-- 添加或修改教师信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联科目" prop="subjectId">
          <el-input v-model="form.subjectId" placeholder="请输入关联科目" />
        </el-form-item>
        <el-form-item label="学历" prop="education">
          <el-select v-model="form.education" placeholder="请选择">
            <el-option
              v-for="item in educations"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="所在大学" prop="university">
          <el-input v-model="form.university" placeholder="请输入所在大学" />
        </el-form-item>
        <el-form-item label="时薪" prop="hourlyRate">
          <el-input v-model="form.hourlyRate" placeholder="请输入时薪" />
        </el-form-item>
        <el-form-item label="平均评分" prop="rating">
          <el-input v-model="form.rating" placeholder="请输入平均评分" />
        </el-form-item>
        <el-form-item label="图片" prop="image">
          <image-upload v-model="form.image"/>
        </el-form-item>
<!--        <el-form-item label="时间" prop="creatTime">-->
<!--          <el-date-picker clearable-->
<!--            v-model="form.creatTime"-->
<!--            type="date"-->
<!--            value-format="yyyy-MM-dd"-->
<!--            placeholder="请选择时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTeacher, getTeacher, delTeacher, addTeacher, updateTeacher } from "@/api/my/teacher"

export default {
  name: "Teacher",
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
      // 教师信息表格数据
      teacherList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        subjectId: null,
        realName: null,
        gender: null,
        education: null,
        university: null,
        hourlyRate: null,
        rating: null,
        status: null,
        image: null,
        creatTime: null
      },
      // 学历下拉框备选数据
      educations: [{
        value: '本科',
        label: '本科'
      },{
        value: '研究生',
        label: '研究生'
      },{
        value: '博士生',
        label: '博士生'
      }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "关联用户ID不能为空", trigger: "blur" }
        ],
        education: [
          {required: true, message: "学历不能为空", trigger: "blur"}
        ],
        subjectId: [
          { required: true, message: "关联科目不能为空", trigger: "blur" }
        ],
        realName: [
          { required: true, message: "真实姓名不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "性别不能为空", trigger: "blur" }
        ],
        university: [
          { required: true, message: "所在大学不能为空", trigger: "blur" }
        ],
        hourlyRate: [
          { required: true, message: "时薪不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "认证状态不能为空", trigger: "change" }
        ],
        image: [
          { required: true, message: "图片不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询教师信息列表 */
    getList() {
      this.loading = true
      listTeacher(this.queryParams).then(response => {
        this.teacherList = response.rows
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
        teacherId: null,
        userId: null,
        subjectId: null,
        realName: null,
        gender: null,
        education: null,
        university: null,
        hourlyRate: null,
        rating: null,
        status: null,
        image: null,
        creatTime: null
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
      this.ids = selection.map(item => item.teacherId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加教师信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const teacherId = row.teacherId || this.ids
      getTeacher(teacherId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改教师信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.teacherId != null) {
            updateTeacher(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTeacher(this.form).then(response => {
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
      const teacherIds = row.teacherId || this.ids
      this.$modal.confirm('是否确认删除教师信息编号为"' + teacherIds + '"的数据项？').then(function() {
        return delTeacher(teacherIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/teacher/export', {
        ...this.queryParams
      }, `teacher_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
