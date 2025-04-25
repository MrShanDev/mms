<template>
  <div class="block">
    <div class="system-menu-container layout-padding mt-5">
      <el-card shadow="hover" class="layout-padding-auto">
        <div class="system-menu-search">
          <el-button size="small" type="primary"  @click="clickExpand">
            <el-icon><ele-Sort /></el-icon>
            {{ expand.state ? "全部关闭" : "全部展开" }}
          </el-button>
          <el-button size="small" type="success" class="ml10" @click="onCURD({ type: curdEnum.INSERT ,ids: '1'})">
            <el-icon>
              <ele-DocumentAdd />
            </el-icon>
            新增菜单
          </el-button>
        </div>
        <el-table
          :data="state.tableData.data"
          v-loading="state.tableData.loading"
          style="width: 100%"
          row-key="id"
          :key="expand.key"
          :default-expand-all="expand.state"
          :expand-row-keys="expandArr"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        >
          <el-table-column label="菜单名称" width="250" show-overflow-tooltip>
            <template #default="scope">
              <SvgIcon :name="scope.row.icon" />
              <span class="ml10">{{ $t(scope.row.name) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="类型" width="100" show-overflow-tooltip>
            <template #default="scope">
              <el-tag
                size="small"
                v-if="scope.row.type == 1 && scope.row.parentId != 1"
                :type="scope.row.parentId == 0 ? 'danger' : 'primary'"
                effect="dark"
                >{{ scope.row.parentId == 0 ? "顶级" : "页面" }}</el-tag
              >
              <el-tag
                size="small"
                v-if="scope.row.type == 1 && scope.row.parentId == 1"
                type="success"
                effect="dark"
                >菜单</el-tag
              >
              <el-tag size="small" v-if="scope.row.type == 2" type="warning" effect="dark"
                >按钮</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column
            prop="path"
            label="路由路径"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="组件路径" show-overflow-tooltip>
            <template #default="scope">
              <span v-if="scope.row.type == 1 && scope.row.component.length > 0">
                <el-tag type="warning" size="small">/@/views/</el-tag
                ><el-tag size="small">{{ scope.row.component }}</el-tag>
                <el-tag type="warning" size="small">.vue</el-tag></span
              >
            </template>
          </el-table-column>

          <el-table-column label="权限标识" show-overflow-tooltip>
            <template #default="scope">
              <span>{{ scope.row.permission }}</span>
            </template>
          </el-table-column>
          <el-table-column label="排序" show-overflow-tooltip width="80">
            <template #default="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" show-overflow-tooltip width="110">
            <template #default="scope">
              <el-tooltip placement="top" :content="$t('message.form.insertSon')">
                <el-icon
                  class="mr10"
                  color="blue"
                  v-auth="'system:function:insert'"
                  @click="onCURD({ type: curdEnum.INSERT, ids: scope.row.id })"
                >
                  <ele-FolderAdd />
                </el-icon>
              </el-tooltip>
              <el-tooltip placement="top" :content="$t('message.form.edit')">
                <el-icon
                  class="mr10"
                  color="blue"
                  v-auths="['system:function:query', 'system:function:edit']"
                  @click="onCURD({ type: curdEnum.EDIT, ids: scope.row.id })"
                >
                  <ele-Edit />
                </el-icon>
              </el-tooltip>
              <el-tooltip v-if="scope.row.parentId != 0" placement="top" :content="$t('message.form.delete')">
                <el-icon
                  class="mr10"
                  color="blue"
                  v-auth="'system:function:delete'"
                  @click="onCURD({ type: curdEnum.DELETE, ids: scope.row.id })"
                >
                  <ele-Delete />
                </el-icon>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <MenuDialog ref="menuDialogRef" @refresh="formSubmit" />
    </div>
  </div>
</template>

<script setup lang="ts" name="systemMenu">
import { defineAsyncComponent, ref, onMounted, reactive } from "vue";
import { RouteRecordRaw } from "vue-router";
import { ElMessageBox, ElMessage } from "element-plus";
import { useMenuApi } from "/@/api/system/menu";
import { isEmpty, tansParams } from "/@/utils/mms";
import { CURDEnum } from "/@/enums/CURDEnum";
import {NextLoading} from "/@/utils/loading";
// 引入组件
const MenuDialog = defineAsyncComponent(() => import("/@/views/system/menu/dialog.vue"));
// 引入 api 请求接口
const baseApi = useMenuApi();
const curdEnum= CURDEnum;
// 定义变量内容
// 展开/合闭
const expand = ref({
  state: false,
  key: 0,
});
// 默认展开行
let expandArr = ref<string[]>([]);
const menuDialogRef = ref();
const state = reactive({
  tableData: {
    data: [] as RouteRecordRaw[],
    loading: true,
  },
});

// 接受子组件传值
const formSubmit = (row: FunctionType) => {
  if (isEmpty(row.id)) {
    //新增
    NextLoading.open();
    baseApi
      .insert(row)
      .then((row) => {
        menuDialogRef.value.closeDialog();
        ElMessage.success(row.msg);
        setTimeout(() => {
          getTableData();
        }, 1000);
      })
      .catch(async (err) => {
        menuDialogRef.value.resetLoading();
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
        menuDialogRef.value.closeDialog();
        ElMessage.success(row.msg);
        setTimeout(() => {
          getTableData();
        }, 1000);
      })
      .catch(async (err) => {
        menuDialogRef.value.resetLoading();
        ElMessage.warning(err);
      })
      .finally(() => {
          NextLoading.close();
      });
  }
};

// 展开或折叠全部的方法
const clickExpand = () => {
  expand.value.key = +new Date();
  expand.value.state = !expand.value.state;
};

// 初始化表格数据
const getTableData = () => {
  state.tableData.loading = true;
  baseApi.list().then((res) => {
    state.tableData.data = res.data;
    state.tableData.loading = false;
    expandArr.value.push(String(1));
  });
};
// 打开新增菜单弹窗
const onCURD = (obj:{type: CURDEnum, ids?: string}) => {
  if (obj.type == CURDEnum.INSERT) {
    baseApi
      .query(obj.ids)
      .then((res) => {
        menuDialogRef.value.openDialog(obj.type, res.data);
      })
      .catch(async (err) => {
        ElMessage.warning(err);
      })
      .finally(() => {});
  }
  if (obj.type == CURDEnum.EDIT) {
    baseApi
      .query(obj.ids)
      .then((res) => {
        menuDialogRef.value.openDialog(obj.type, res.data);
      })
      .catch(async (err) => {
        ElMessage.warning(err);
      })
      .finally(() => {});
  }
  if (obj.type == CURDEnum.DELETE) {
    ElMessageBox.confirm(`此操作将永久删是否继续?`, "提示", {
      confirmButtonText: "删除",
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
  }
};
// 页面加载时
onMounted(() => {
  getTableData();
});
</script>
<style></style>
