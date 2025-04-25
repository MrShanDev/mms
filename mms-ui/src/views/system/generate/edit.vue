<template>
  <el-drawer class="pb-5 pr-5 pl-5" v-model="visible" title="编辑" :size="1500" :with-header="false">

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="属性设置" name="field">
        <vxe-table
          ref="fieldTable"
          border
          :row-config="{ keyField: 'id' }"
          class="sortable-row-gen"
          :data="fieldList"
          :checkbox-config="{ checkStrictly: true }"
          :edit-config="{ trigger: 'click', mode: 'cell' }"
        >
          <vxe-column type="seq" width="60"></vxe-column>
          <vxe-column width="60" title="拖动">
            <template #default>
              <span class="drag-btn">
                <i class="vxe-icon-sort"></i>
              </span>
            </template>
            <template #header>
              <el-tooltip
                class="item"
                effect="dark"
                content="按住后可以上下拖动排序"
                placement="top-start"
              >
                <i class="vxe-icon-question-circle-fill"></i>
              </el-tooltip>
            </template>
          </vxe-column>
          <vxe-column field="fieldName" title="字段名"></vxe-column>
          <vxe-column
            field="fieldComment"
            title="说明"
            :edit-render="{ name: 'input' }"
          ></vxe-column>
          <vxe-column field="fieldType" title="字段类型"></vxe-column>
          <vxe-column
            field="attrName"
            title="属性名"
            :edit-render="{ name: 'input' }"
          ></vxe-column>
          <vxe-column field="attrType" title="属性类型">
            <template #default="{ row }">
              <el-select v-model="row.attrType">
                <el-option
                  v-for="item in typeList"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                ></el-option>
              </el-select>
            </template>
          </vxe-column>
          <vxe-column field="autoFill" title="自动填充">
            <template #default="{ row }">
              <el-select v-model="row.autoFill">
                <el-option
                  v-for="item in fillList"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                ></el-option>
              </el-select>
            </template>
          </vxe-column>
          <vxe-column field="primaryPk" title="主键">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.primaryPk"></vxe-checkbox>
            </template>
          </vxe-column>
        </vxe-table>
      </el-tab-pane>
      <el-tab-pane label="表单配置" name="form">
        <vxe-table
          ref="formTable"
          border
          :row-config="{ keyField: 'id' }"
          :data="fieldList"
          :checkbox-config="{ checkStrictly: true }"
          :edit-config="{ trigger: 'click', mode: 'cell' }"
        >
          <vxe-column field="attrName" title="属性名"></vxe-column>
          <vxe-column field="fieldComment" title="说明"></vxe-column>
          <vxe-column field="formItem" title="表单显示">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.formItem"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="formRequired" title="表单必填">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.formRequired"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="formValidator" title="表单效验">
            <template #default="{ row }">
              <el-select v-model="row.formValidator">
                <el-option
                    v-for="item in formValidatorList"
                    :key="item.value"
                    :value="item.value"
                    :label="item.label"
                ></el-option>
              </el-select>
            </template>
          </vxe-column>
          <vxe-column field="formType" title="表单类型">
            <template #default="{ row }">
              <el-select v-model="row.formType">
                <el-option
                  v-for="item in formTypeList"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                ></el-option>
              </el-select>
            </template>
          </vxe-column>
          <vxe-column field="formDict" title="表单字典类型">
            <template #default="{ row }">
              <el-select v-model="row.formDict" @change="changeFormType(row)">
                <el-option value="" label="请选择"></el-option>
                <el-option
                  v-for="item in formTypeListDict"
                  :key="item.id"
                  :value="item.fieldName"
                  :label="item.name"
                ></el-option>
              </el-select>
            </template>
          </vxe-column>
        </vxe-table>
      </el-tab-pane>
      <el-tab-pane label="列表配置" name="grid">
        <vxe-table
          ref="gridTable"
          border
          :row-config="{ keyField: 'id' }"
          :data="fieldList"
          :checkbox-config="{ checkStrictly: true }"
          :edit-config="{ trigger: 'click', mode: 'cell' }"
        >
          <vxe-column field="attrName" title="属性名"></vxe-column>
          <vxe-column field="fieldComment" title="说明"></vxe-column>
          <vxe-column field="gridItem" title="列表显示">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.gridItem"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="gridSort" title="列表排序">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.gridSort"></vxe-checkbox>
            </template>
          </vxe-column>
        </vxe-table>
      </el-tab-pane>
      <el-tab-pane label="查询配置" name="query">
        <vxe-table
          ref="queryTable"
          border
          :row-config="{ keyField: 'id' }"
          :data="fieldList"
          :checkbox-config="{ checkStrictly: true }"
          :edit-config="{ trigger: 'click', mode: 'cell' }"
        >
          <vxe-column field="attrName" title="属性名"></vxe-column>
          <vxe-column field="fieldComment" title="说明"></vxe-column>
          <vxe-column field="queryItem" title="查询显示">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.queryItem"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="queryType" title="查询方式">
            <template #default="{ row }">
              <el-select v-model="row.queryType"  @change="changeQueryType(row)">
                <el-option
                  v-for="item in queryList"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                ></el-option>
              </el-select>
            </template>
          </vxe-column>
          <vxe-column field="queryFormType" title="查询表单类型">
            <template #default="{ row }">
              <el-select v-model="row.queryFormType">
                <el-option
                  v-for="item in formTypeList"
                  :key="item.value"
                  :value="item.value"
                  :label="item.label"
                ></el-option>
              </el-select>
            </template>
          </vxe-column>
        </vxe-table>
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { nextTick, reactive, ref } from "vue";
import { ElMessage, TabsPaneContext } from "element-plus/es";
import Sortable from "sortablejs";
import { useTableFieldSubmitApi,useTableApi,useFieldTypeListApi } from "/@/api/gen";
import { dictApi } from "/@/api/system/dict";
import { VxeTableInstance } from "vxe-table";
import {NextLoading} from "/@/utils/loading";
import {RowDictType} from "/@/api/system/dict/type";
// 引入 api 请求接口
const baseApi = dictApi();

const val1 = ref()

const activeName = ref();
const fieldTable = ref<VxeTableInstance>();
const formTable = ref<VxeTableInstance>();
const gridTable = ref<VxeTableInstance>();
const queryTable = ref<VxeTableInstance>();

const handleClick = (tab: TabsPaneContext) => {
  if (tab.paneName !== "field") {
    formTable.value?.loadData(fieldList.value);
    gridTable.value?.loadData(fieldList.value);
    queryTable.value?.loadData(fieldList.value);
  }
};

const emit = defineEmits(["refreshDataList"]);
const visible = ref(false);
const dataFormRef = ref();

const sortable = ref() as any;

const typeList = ref([]) as any;
const tableId = ref();
const fieldList = ref([]);
const fillList = reactive([
  { label: "DEFAULT", value: "DEFAULT" },
  { label: "INSERT", value: "INSERT" },
  { label: "UPDATE", value: "UPDATE" },
  { label: "INSERT_UPDATE", value: "INSERT_UPDATE" },
]);

const queryList = reactive([
  { label: "=", value: "=" },
  { label: "!=", value: "!=" },
  { label: ">", value: ">" },
  { label: ">=", value: ">=" },
  { label: "<", value: "<" },
  { label: "<=", value: "<=" },
  { label: "like", value: "like" },
  { label: "left like", value: "left like" },
  { label: "right like", value: "right like" },
  { label: "between", value: "between" },

]);

const formTypeList = reactive([
  { label: "单行文本", value: "text" },
  { label: "多行文本", value: "textarea" },
  { label: "富文本编辑器", value: "editor" },
  { label: "下拉框", value: "select" },
  { label: "单选按钮", value: "radio" },
  { label: "复选框", value: "checkbox" },
  { label: "日期", value: "date" },
  { label: "日期时间", value: "datetime" },
  { label: "文件", value: "file" },
  { label: "图片", value: "image" },
  { label: "图片集", value: "images" },
]);

const formValidatorList = reactive([
  { label: "@NotEmpty", value: "@NotEmpty" },
  { label: "@NotBlank", value: "@NotBlank" },
  { label: "@NotNull", value: "@NotNull" },
]);

let formTypeListDict = reactive<RowDictType[]>([]);

const init = (id: number) => {
  visible.value = true;
  tableId.value = id;

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  activeName.value = "field";

  rowDrop();
  getTable(id);
  getFieldTypeList();
  getTableData();
};
const changeFormType = (row: any) => {
  row.formType = "select";
};
const changeQueryType = (row: any) => {
  row.queryFormType = "date";
};

const rowDrop = () => {
  nextTick(() => {
    const el: any = window.document.querySelector(
      ".body--wrapper>.vxe-table--body tbody"
    );
    sortable.value = Sortable.create(el, {
      handle: ".drag-btn",
      onEnd: (e: any) => {
        const { newIndex, oldIndex } = e;
        const currRow = fieldList.value.splice(oldIndex, 1)[0];
        fieldList.value.splice(newIndex, 0, currRow);
      },
    });
  });
};

const getTable = (id: number) => {
  useTableApi(id).then((res) => {
    fieldList.value = res.data.fieldList;
  });
};

const getFieldTypeList = async () => {
  typeList.value = [];

  // 获取数据
  const { data } = await useFieldTypeListApi();
  // 设置属性类型值
  data.forEach((item: any) => typeList.value.push({ label: item, value: item }));
  // 增加Object类型
  typeList.value.push({ label: "Object", value: "Object" });
};

// 表单提交
const submitHandle = () => {
  NextLoading.open()
  useTableFieldSubmitApi(tableId.value, fieldList.value).then(() => {
    NextLoading.close()
    ElMessage.success({
      message: "操作成功",
      duration: 500,
      onClose: () => {
        visible.value = false;
        emit("refreshDataList");
      },
    });
  });
};

const getTableData = () => {
  baseApi
    .list({ pageNum: 1, pageSize: 200 })
    .then((res) => {
      formTypeListDict = res.rows;
    })
    .catch(async (err) => {
      ElMessage.warning(err);
    })
    .finally(() => {});
};

defineExpose({
  init,
});
</script>

<style lang="scss">
.sortable-row-gen .drag-btn {
  cursor: move;
  font-size: 12px;
}

.sortable-row-gen .vxe-body--row.sortable-ghost,
.sortable-row-gen .vxe-body--row.sortable-chosen {
  background-color: #dfecfb;
}
.el-drawer .el-drawer__body {
  width: 100%;
  height: 100%;
  overflow: auto;
  padding: 10px;
}
</style>
