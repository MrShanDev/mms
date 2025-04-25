<template>
  <div class="block">
    <!-- 功能栏  -->
    <div class="views-tool">
      <div class="tool-left">
        <!-- 功能栏 -->
        <el-form
          :inline="true"
          size="default"
          :model="state.tableData.param"
          class="form-tool"
           @keyup.enter="getTableData"
        >
          <el-form-item>
            <el-input
              v-model="state.tableData.param.configName"
              placeholder="配置名称"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="state.tableData.param.configKey"
              placeholder="配置键"
            ></el-input>
          </el-form-item>
          <el-form-item class="mb-0">
            <el-button
              size="default"
              type="primary"
              @click="getTableData"
              v-auth="'system:user:list'"
            >
              <SvgIcon name="iconfont icon-search1" />
              {{ $t("message.form.search") }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="system-config-container layout-padding">
      <el-card shadow="hover" class="layout-padding-auto">
        <el-container>
          <el-header>
            <!-- 新增/导入/导出/打印 -->
            <TableTool
              ref="tableToolRef"
              tableComment="系统配置"
              functionName="config"
              :key="componentKey"
              :param="state.tableData.param"
              @close="componentKey = generateUUID()"
              @insert="onCURD"
              @deletes="onCURD"
            />
          </el-header>
          <el-main>
            <!-- Table -->
            <el-table
              :data="state.tableData.data"
              v-loading="state.tableData.loading"
              @selection-change="handleSelectionChange"
              style="width: 100%"
            >
              <el-table-column type="selection" label="序号" width="50" />
              <el-table-column
                v-show="false"
                prop="id"
                label="主键ID"
                header-align="center"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="configName"
                label="配置名称"
                header-align="center"
                align="center"
              ></el-table-column>
              <el-table-column prop="configKey" label="配置键" header-align="center" align="center"></el-table-column>
              <fast-table-column prop="configType" label="配置类型" dict-type="CONFIG_TYPE" />
              <fast-table-column prop="status" label="状态" dict-type="SYS_STATE" />
              <el-table-column
                prop="createdTime"
                width="170"
                label="创建时间"
                show-overflow-tooltip
              >
                <template #default="scope">
                  {{ $ut.parseTime(scope.row.createdTime, "{y}-{m}-{d} {h}:{i}:{s}") }}
                </template>
              </el-table-column>
              <el-table-column fixed="right" width="100" label="操作">
                <template #default="scope">
                  <el-tooltip placement="top" :content="$t('message.form.edit')">
                    <el-icon
                      class="mr10"
                      color="blue"
                      v-auths="['system:config:query', 'system:config:edit']"
                      @click="onCURD({ type: curdEnum.EDIT, ids: scope.row.id })"
                    >
                      <ele-Edit />
                    </el-icon>
                  </el-tooltip>
                  <el-tooltip placement="top" :content="$t('message.form.delete')">
                    <el-icon
                      class="mr10"
                      color="blue"
                      v-auth="'system:config:delete'"
                      @click="onCURD({ type: curdEnum.DELETE, ids: scope.row.id })"
                    >
                      <ele-Delete />
                    </el-icon>
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>
          </el-main>
          <el-footer>
            <!-- 分页 -->
            <el-pagination
              @size-change="onHandleSizeChange"
              @current-change="onHandleCurrentChange"
              class="mt15"
              :pager-count="5"
              :page-sizes="[10, 20, 30, 50, 100]"
              v-model:current-page="state.tableData.param.pageNum"
              background
              size="default"
              v-model:page-size="state.tableData.param.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="state.tableData.total"
            >
            </el-pagination>
          </el-footer>
        </el-container>
      </el-card>
      <SysConfigDialog ref="sysConfigDialogRef" @refresh="formSubmit" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { defineAsyncComponent, onMounted, reactive, ref } from "vue";
import { sysConfigApi } from "/@/api/system/config";
import { ElMessage, ElMessageBox } from "element-plus";
import FastTableColumn from "/@/components/fast-table-column/src/fast-table-column.vue";
import { CURDEnum } from "/@/enums/CURDEnum";
import { isEmpty, generateUUID } from "/@/utils/mms";
const baseApi = sysConfigApi();
const SysConfigDialog = defineAsyncComponent(
  () => import("/src/views/system/config/dialog.vue")
);
const TableTool = defineAsyncComponent(
  () => import("/@/components/table-tool/index.vue")
);
const sysConfigDialogRef = ref();
const curdEnum= CURDEnum;
const props = defineProps({
  value: {
    type: Array,
    default: () => [],
  },
});
const state = reactive<SysConfigState>({
  tableData: {
    data: [],
    total: 0,
    loading: false,
    param: {
      selectIds: "",
      configName: "",
      configKey: "",
      configType: "2",
      pageNum: 1,
      pageSize: 10,
    },
  },
});
const tableToolRef = ref();
const componentKey = ref(generateUUID());
// 初始化表格数据
const getTableData = () => {
  state.tableData.loading = true;
  baseApi
    .list(state.tableData.param)
    .then((res) => {
      state.tableData.data = res.rows;
      state.tableData.total = res.total;
      state.tableData.loading = false;
    })
    .catch(async (err) => {
      ElMessage.warning(err);
    })
    .finally(() => {});
};

//搜索按钮操作
function handleQuery() {
  state.tableData.param.pageNum = 1;
  getTableData();
}

// 打开修改用户弹窗
const onCURD = (obj: { type: CURDEnum; ids?: string }) => {
  if (obj.type === CURDEnum.INSERT) {
    sysConfigDialogRef.value.openDialog(obj.type);
    return false;
  }
  if (obj.type === CURDEnum.DELETE) {
    ElMessageBox.confirm(`此操作将永久删除吗?`, "提示", {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        baseApi
          .delete(obj.ids)
          .then((res) => {
            getTableData();
            ElMessage.success("删除成功");
          })
          .catch(async (err) => {
            ElMessage.warning(err);
          })
          .finally(() => {});
      })
      .catch(() => {});
    return false;
  }
  if (obj.type === CURDEnum.EDIT){
    baseApi
    .query(obj.ids)
    .then((res) => {
      sysConfigDialogRef.value.openDialog(obj.type, res.data);
    })
    .catch(async (err) => {
      ElMessage.warning(err);
    })
    .finally(() => {});
  }
};

// 接收子组件传值
const formSubmit = (row: RowSysConfigType) => {
  if (isEmpty(row.id)) {
    //新增
    baseApi
      .insert(row)
      .then((row) => {
        sysConfigDialogRef.value.closeDialog();
        ElMessage.success(row.msg);
        setTimeout(() => {
          getTableData();
        }, 1000);
      })
      .catch(async (err) => {
        ElMessage.warning(err);
      })
      .finally(() => {});
  } else {
    //更新
    baseApi
      .edit(row)
      .then((row) => {
        sysConfigDialogRef.value.closeDialog();
        ElMessage.success(row.msg);
        setTimeout(() => {
          getTableData();
        }, 1000);
      })
      .catch(async (err) => {
        ElMessage.warning(err);
      })
      .finally(() => {});
  }
};
// 分页改变
const onHandleSizeChange = (val: number) => {
  state.tableData.param.pageSize = val;
  getTableData();
};
// 分页改变
const onHandleCurrentChange = (val: number) => {
  state.tableData.param.pageNum = val;
  getTableData();
};
//选择项改变
const handleSelectionChange = (val: RowSysConfigType[]) => {
  state.tableData.param.selectIds = val.map((item: RowSysConfigType) => item.id).join(",");
};
onMounted(() => {
  getTableData();
});
// 暴露变量
defineExpose({
  getTableData,
});
</script>
<style scoped lang="css"></style>
