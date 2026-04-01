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
      <el-form-item label="地区" prop="areaId">
        <el-select v-model="queryParams.areaId" clearable filterable placeholder="全部" style="width: 160px">
          <el-option
            v-for="o in areaOptions"
            :key="o.value"
            :label="o.label"
            :value="o.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年级" prop="gradeId">
        <el-select v-model="queryParams.gradeId" clearable filterable placeholder="全部" style="width: 160px">
          <el-option
            v-for="o in gradeOptions"
            :key="o.value"
            :label="o.label"
            :value="o.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="认证状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="全部" style="width: 150px">
          <el-option label="待审核" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已驳回" :value="2" />
        </el-select>
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
      <el-table-column label="地区" align="center" prop="areaId" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ formatAreaName(scope.row.areaId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="年级" align="center" prop="gradeId" width="120" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ formatGradeName(scope.row.gradeId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时薪" align="center" prop="hourlyRate" />
      <el-table-column label="平均评分" align="center" prop="rating" />
      <el-table-column label="认证状态" align="center" prop="status" width="110">
        <template slot-scope="scope">
          <el-tag v-if="Number(scope.row.status) === 0" type="warning" size="small">待审核</el-tag>
          <el-tag v-else-if="Number(scope.row.status) === 1" type="success" size="small">已通过</el-tag>
          <el-tag v-else-if="Number(scope.row.status) === 2" type="info" size="small">已驳回</el-tag>
          <span v-else>{{ scope.row.status }}</span>
        </template>
      </el-table-column>
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            v-if="Number(scope.row.status) === 0"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleApprove(scope.row)"
            v-hasPermi="['system:teacher:edit']"
          >通过</el-button>
          <el-button
            v-if="Number(scope.row.status) === 0"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleReject(scope.row)"
            v-hasPermi="['system:teacher:edit']"
          >驳回</el-button>
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
    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="认证状态" prop="status">
          <el-select
            v-if="form.teacherId != null"
            v-model="form.status"
            placeholder="请选择审核状态"
            style="width: 100%"
          >
            <el-option label="待审核" :value="0" />
            <el-option label="已通过（家长端「找老师」可见）" :value="1" />
            <el-option label="已驳回" :value="2" />
          </el-select>
          <el-input
            v-else
            value="待审核（保存后须管理员在「修改」中审核通过）"
            disabled
          />
        </el-form-item>
        <el-form-item label="关联科目" prop="subjectId">
          <el-input v-model="form.subjectId" placeholder="请输入科目ID" />
        </el-form-item>
        <el-form-item label="所在地区" prop="areaId">
          <el-select v-model="form.areaId" clearable filterable placeholder="请选择（可与地图API对接）" style="width: 100%">
            <el-option
              v-for="o in areaOptions"
              :key="o.value"
              :label="o.label"
              :value="o.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="授课年级" prop="gradeId">
          <el-select v-model="form.gradeId" clearable filterable placeholder="请选择" style="width: 100%">
            <el-option
              v-for="o in gradeOptions"
              :key="o.value"
              :label="o.label"
              :value="o.value"
            />
          </el-select>
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
import { listJinan } from "@/api/my/jinan"
import { listLevel } from "@/api/my/level"

export default {
  name: "Teacher",
  data() {
    return {
      areaOptions: [],
      gradeOptions: [],
      areaNameMap: {},
      gradeNameMap: {},

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
        creatTime: null,
        areaId: null,
        gradeId: null
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
    this.loadAreaGradeDict()
    this.getList()
  },
  methods: {
    loadAreaGradeDict() {
      listJinan({ pageNum: 1, pageSize: 999 }).then(res => {
        const rows = res.rows || []
        this.areaOptions = rows.map(r => ({
          label: r.name,
          value: r.areaId != null ? String(r.areaId) : ""
        })).filter(o => o.value)
        const m = {}
        rows.forEach(r => {
          if (r.areaId != null) m[String(r.areaId)] = r.name
        })
        this.areaNameMap = m
      })
      listLevel({ pageNum: 1, pageSize: 999 }).then(res => {
        const rows = res.rows || []
        this.gradeOptions = rows.map(r => ({
          label: r.name,
          value: r.gradeId != null ? String(r.gradeId) : ""
        })).filter(o => o.value)
        const m = {}
        rows.forEach(r => {
          if (r.gradeId != null) m[String(r.gradeId)] = r.name
        })
        this.gradeNameMap = m
      })
    },
    formatAreaName(id) {
      if (id == null || id === "") return "-"
      return this.areaNameMap[String(id)] || id
    },
    formatGradeName(id) {
      if (id == null || id === "") return "-"
      return this.gradeNameMap[String(id)] || id
    },
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
        status: 0,
        image: null,
        creatTime: null,
        areaId: null,
        gradeId: null
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
        const d = response.data || {}
        this.form = { ...d }
        if (this.form.areaId != null && this.form.areaId !== "") {
          this.form.areaId = String(this.form.areaId)
        }
        if (this.form.gradeId != null && this.form.gradeId !== "") {
          this.form.gradeId = String(this.form.gradeId)
        }
        if (this.form.status != null && this.form.status !== "") {
          this.form.status = Number(this.form.status)
        }
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
    handleApprove(row) {
      const teacherId = row.teacherId
      this.$modal.confirm('确认将该教师审核为「已通过」？通过后家长端「找老师」将展示该教师。').then(() => {
        return updateTeacher({ teacherId: teacherId, status: 1 })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("已审核通过")
      }).catch(() => {})
    },
    handleReject(row) {
      const teacherId = row.teacherId
      this.$modal.confirm('确认驳回该入驻申请？驳回后家长端不会展示该教师。').then(() => {
        return updateTeacher({ teacherId: teacherId, status: 2 })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("已驳回")
      }).catch(() => {})
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
