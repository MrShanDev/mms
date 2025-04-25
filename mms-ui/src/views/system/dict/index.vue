<template>
  <div class="block">
    <!-- 功能栏 -->
    <div class="views-tool">
      <div class="tool-left">
        <el-form
          :inline="true"
          size="default"
          :model="state.tableData.param"
          class="form-tool"
           @keyup.enter="getTableData"
        >
          <el-form-item>
            <el-input
              size="default"
              v-model="state.tableData.param.name"
              placeholder="请输入字典名称"
              style="max-width: 180px"
              clearable
            >
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              size="default"
              type="primary"
              :disabled="state.tableData.loading"
              :loading-icon="Eleme"
              :loading="state.tableData.loading"
              @click="getTableData"
              v-auth="'system:dict:list'"
            >
              <SvgIcon name="iconfont icon-search1" />
              {{ $t("message.form.search") }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="system-dict-container layout-padding">
      <el-card shadow="hover" class="layout-padding-auto">
        <el-container>
          <el-header>
            <!-- 新增/导入/导出/打印 -->
            <TableTool
              ref="tableToolRef"
              tableComment="系统字典"
              functionName="dict"
              :key="componentKey"
              :param="state.tableData.param"
              @close="componentKey = generateUUID()"
              @insert="onCURD"
              @deletes="onCURD"
            />
          </el-header>
          <el-main>
            <el-table
              :data="state.tableData.data"
              v-loading="state.tableData.loading"
              @selection-change="handleSelectionChange"
              style="width: 100%"
            >
              <el-table-column type="selection" label="序号" width="50" />
              <el-table-column
                prop="name"
                label="字典名称"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                prop="fieldName"
                label="字段名"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column prop="status" label="字典状态" show-overflow-tooltip>
                <template #default="scope">
                  <el-tag type="success" v-if="scope.row.status == 0">启用</el-tag>
                  <el-tag type="info" v-else>禁用</el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="remark"
                label="字典描述"
                show-overflow-tooltip
              ></el-table-column>
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
                      v-auths="['system:dict:query', 'system:dict:edit']"
                      @click="onCURD({type: curdEnum.EDIT, ids: scope.row.id})"
                    >
                      <ele-Edit />
                    </el-icon>
                  </el-tooltip>
                  <el-tooltip placement="top" :content="$t('message.form.delete')">
                    <el-icon
                      class="mr10"
                      color="blue"
                      v-auth="'system:dict:delete'"
                      @click="onCURD({type: curdEnum.DELETE, ids: scope.row.id})"
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
      <DicDialog ref="dicDialogRef" @refresh="formSubmit" />
    </div>
  </div>
</template>

<script setup lang="ts" name="systemDic">
import { defineAsyncComponent, reactive, onMounted, ref } from "vue";
import { ElMessageBox, ElMessage } from "element-plus";
import { dictApi } from "/@/api/system/dict";
import { isEmpty, generateUUID } from "/@/utils/mms";
import { CURDEnum } from "/@/enums/CURDEnum";
import {RowDictType, SysDictState} from "/@/api/system/dict/type";
import {NextLoading} from "/@/utils/loading";
import {Eleme} from "@element-plus/icons-vue";
// 引入组件
const DicDialog = defineAsyncComponent(() => import("/@/views/system/dict/dialog.vue"));
const TableTool = defineAsyncComponent(
  () => import("/@/components/table-tool/index.vue")
);
// 引入 api 请求接口
const baseApi = dictApi();
const curdEnum= CURDEnum;
// 定义变量内容
const dicDialogRef = ref();
const tableToolRef = ref();
const componentKey = ref(generateUUID());
const state = reactive<SysDictState>({
  tableData: {
    data: [],
    total: 0,
    loading: false,
    param: {
      selectIds: "",
      name: "",
      pageNum: 1,
      pageSize: 10,
    },
  },
});
// 接受子组件传值
const formSubmit = (row: RowDictType) => {
  if (isEmpty(row.id)) {
    //新增
    NextLoading.open();
    baseApi
      .insert(row)
      .then((row) => {
        dicDialogRef.value.closeDialog();
        ElMessage.success(row.msg);
        setTimeout(() => {
          getTableData();
        }, 1000);
      })
      .catch(async (err) => {
        dicDialogRef.value.resetLoading();
        ElMessage.warning(err);
      })
      .finally(() => {
          NextLoading.close();
      });
  } else {
    //更新
      NextLoading.open();
    baseApi
      .edit(row)
      .then((row) => {
        dicDialogRef.value.closeDialog();
        ElMessage.success(row.msg);
        setTimeout(() => {
          getTableData();
        }, 1000);
      })
      .catch(async (err) => {
        dicDialogRef.value.resetLoading();
        ElMessage.warning(err);
      })
      .finally(() => {
          NextLoading.close();
      });
  }
};
// 初始化表格数据
const getTableData = () => {
  state.tableData.loading = true;
  const data = [];
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
// 打开新增字典弹窗
const onCURD = (obj: { type: string; ids: string }) => {
  if (obj.type === CURDEnum.INSERT) {
    dicDialogRef.value.openDialog(obj.type);
  }
  if (obj.type === CURDEnum.EDIT) {
    baseApi
      .query(obj.ids)
      .then((res) => {
        dicDialogRef.value.openDialog(obj.type, res.data);
      })
      .catch(async (err) => {
        ElMessage.warning(err);
      })
      .finally(() => {});
  }
  if (obj.type === CURDEnum.DELETE) {
    ElMessageBox.confirm(
      `此操作将永久删除是否继续?`,
      "提示",
      {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }
    )
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
const handleSelectionChange = (val: RowDictType[]) => {
  state.tableData.param.selectIds = val.map((item: any) => item.id).join(",");
};
// 页面加载时
onMounted(() => {
  getTableData();
});
</script>
