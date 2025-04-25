<template>
  <div class="system-menu-dialog-container">
    <el-dialog
      :title="state.dialog.title"
      v-model="state.dialog.isShowDialog"
      width="769px"
    >
      <el-form
        ref="menuDialogFormRef"
        :model="state.ruleForm"
        size="default"
        label-width="100px"
      >
        <el-row :gutter="35">
          <el-col
            v-show="state.ruleForm.parentId > 0"
            :xs="24"
            :sm="24"
            :md="24"
            :lg="24"
            :xl="24"
            class="mb20"
          >
            <el-form-item label="上级菜单">
              <el-cascader
                :options="state.menuData"
                :props="{ checkStrictly: true, value: 'id', label: 'name' }"
                placeholder="请选择上级菜单"
                clearable
                @change="change"
                class="w100"
                v-model="state.ruleForm.menuSuperior"
              >
                <template #default="{ node, data }">
                  <span>{{ data.name }}</span>
                  <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
                </template>
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col
            v-show="state.ruleForm.parentId > 0"
            :xs="24"
            :sm="12"
            :md="12"
            :lg="12"
            :xl="12"
            class="mb20"
          >
            <el-form-item label="菜单类型">
              <el-radio-group v-model="state.ruleForm.type">
                <el-radio :value="1">{{
                  state.ruleForm.parentId > 0 ? "页面" : "菜单"
                }}</el-radio>
                <el-radio :value="2">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="菜单名称">
              <el-input
                v-model="state.ruleForm.name"
                placeholder="路由中的 name 值"
                clearable
              ></el-input>
            </el-form-item>
          </el-col>
          <template v-if="state.ruleForm.type === 1">
            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="路由路径">
                <el-input
                  v-model="state.ruleForm.path"
                  placeholder="路由中的 path 值 ( '/' 开始)"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="菜单国际化">
                <el-input
                  v-model="state.ruleForm.languageCode"
                  placeholder="i18n编码（格式：message.xxx.xxx）"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="路由重定向">
                <el-input
                  v-model="state.ruleForm.redirectPath"
                  placeholder="请输入路由重定向"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="菜单图标">
                <IconSelector
                  placeholder="请输入菜单图标"
                  v-model="state.ruleForm.icon"
                />
              </el-form-item>
            </el-col>
            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="组件路径">
                <el-input
                  v-model="state.ruleForm.component"
                  placeholder="组件路径 ( '/' 开始)"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="权限标识">
                <el-select
                  v-model="state.ruleForm.roles"
                  multiple
                  placeholder="取角色管理"
                  clearable
                  class="w100"
                >
                  <el-option label="admin" value="admin"></el-option>
                  <el-option label="common" value="common"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="是否隐藏">
                <el-radio-group v-model="state.ruleForm.visible">
                  <el-radio :value="1">隐藏</el-radio>
                  <el-radio :value="-1">显示</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="页面缓存">
                <el-radio-group v-model="state.ruleForm.keepAlive">
                  <el-radio :value="1">缓存</el-radio>
                  <el-radio :value="-1">不缓存</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="是否固定">
                <el-radio-group v-model="state.ruleForm.alwaysShow">
                  <el-radio :value="1">固定</el-radio>
                  <el-radio :value="-1">不固定</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="是否内嵌">
                <el-radio-group
                  v-model="state.ruleForm.isIframe"
                  @change="onSelectIframeChange"
                >
                  <el-radio :value="1">是</el-radio>
                  <el-radio :value="-1">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="链接地址">
                <el-input
                  v-model="state.ruleForm.isLink"
                  placeholder="外链/内嵌时链接地址（http:xxx.com）"
                  clearable
                  :disabled="!(state.ruleForm.isOpenLink == 1)"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col
              v-show="state.ruleForm.parentId > 0"
              :xs="24"
              :sm="12"
              :md="12"
              :lg="12"
              :xl="12"
              class="mb20"
            >
              <el-form-item label="是否外链">
                <el-radio-group
                  v-model="state.ruleForm.isOpenLink"
                  :disabled="state.ruleForm.isIframe == 1"
                >
                  <el-radio :value="1">是</el-radio>
                  <el-radio :value="-1">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </template>

          <template v-if="state.ruleForm.type === 2">
            <!-- 按钮 -->
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="权限标识">
                <el-input
                  v-model="state.ruleForm.permission"
                  placeholder="请输入权限标识"
                  clearable
                ></el-input>
              </el-form-item>
            </el-col>
          </template>

          <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" class="mb20">
            <el-form-item label="菜单排序">
              <el-input-number
                v-model="state.ruleForm.sort"
                controls-position="right"
                placeholder="请输入排序"
                class="w100"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDialog" size="default">取 消</el-button>
          <el-button type="primary"
                     :disabled="state.dialog.loading"
                     :loading-icon="Eleme"
                     :loading="state.dialog.loading"
                     @click="onSubmit"
                     size="default"
          >{{ state.dialog.submitTxt }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="systemMenuDialog">
import { defineAsyncComponent, reactive, onMounted, ref } from "vue";
import { useRoutesList } from "/@/stores/routesList";
import { useMenuApi } from "/@/api/system/menu";
// 定义子组件向父组件传值/事件
const emit = defineEmits(["refresh"]);
import { CURDEnum } from '/@/enums/CURDEnum';
import {RouteItems} from "/@/types/global";
import {Eleme} from "@element-plus/icons-vue";
// 引入组件
const IconSelector = defineAsyncComponent(
  () => import("/@/components/iconSelector/index.vue")
);
// 引入 api 请求接口
const baseApi = useMenuApi();
// 定义变量内容
const menuDialogFormRef = ref();
const state = reactive({
  // 参数请参考 `/src/router/route.ts` 中的 `dynamicRoutes` 路由菜单格式
  ruleForm: {
    id: 0,
    parentId: 0,
    menuSuperior: [] as number[], // 上级菜单
    name: "", // 路由名称
    type: 1, // 菜单类型
    languageCode: "", //i18n编码
    component: "", // 组件路径
    componentName: "", // 组件路径别名
    isLink: "", // 链接地址
    sort: 0, // 菜单排序
    path: "", // 路由路径
    redirectPath: "", // 路由重定向，有子集 children 时
    title: "", // 菜单名称
    icon: "", // 菜单图标
    visible: -1, // 是否隐藏
    keepAlive: -1, // 是否缓存
    alwaysShow: -1, // 是否固定
    isOpenLink: -1, // 是否外链
    isIframe: -1, // 是否内嵌，开启条件，`1、isIframe:true 2、isLink：链接地址不为空`
    roles: "", // 权限标识，取角色管理
    permission: "", // 菜单类型为按钮时，权限标识
    status: 0,
  },
  menuData: [] as RouteItems, // 上级菜单数据
  dialog: {
    loading: false,
    isShowDialog: false,
    type: "",
    title: "",
    submitTxt: "",
  },
});

// 重置
const resetForm = () => {
  state.dialog.loading = false;
  state.ruleForm = {
    id: 0,
    parentId: 0,
    menuSuperior: [] as number[], // 上级菜单
    name: "", // 路由名称
    type: 1, // 菜单类型
    languageCode: "", //i18n编码
    component: "", // 组件路径
    componentName: "", // 组件路径别名
    isLink: "", // 链接地址
    sort: 0, // 菜单排序
    path: "", // 路由路径
    redirectPath: "", // 路由重定向，有子集 children 时
    title: "", // 菜单名称
    icon: "", // 菜单图标
    visible: -1, // 是否隐藏
    keepAlive: -1, // 是否缓存
    alwaysShow: -1, // 是否固定
    isOpenLink: -1, // 是否外链
    isIframe: -1, // 是否内嵌，开启条件，`1、isIframe:true 2、isLink：链接地址不为空`
    roles: "", // 权限标识，取角色管理
    permission: "", // 菜单类型为按钮时，权限标识
    status: 0,
  };
};

// 获取菜单
const getMenuData = () => {
  baseApi.list().then((res) => {
    state.menuData = res.data;
  });
};
// 打开弹窗
const openDialog = (type: string, row: FunctionType) => {
  resetForm();
  getMenuData();
  if (type === CURDEnum.EDIT) {
    state.ruleForm = row;
    state.dialog.title = "修改菜单";
    state.dialog.submitTxt = "修 改";
    state.dialog.type = CURDEnum.EDIT;
  } else {
    state.dialog.title = "新增菜单";
    state.dialog.submitTxt = "新 增";
    state.dialog.type = CURDEnum.INSERT;
    state.ruleForm.parentId = row.id;
    state.ruleForm.menuSuperior = [...row.menuSuperior, ...[row.id]];
  }
  state.dialog.type = type;
  state.dialog.isShowDialog = true;
};
// 关闭弹窗
const closeDialog = () => {
  state.dialog.loading = false;
  state.dialog.isShowDialog = false;
};
const resetLoading = () => {
    state.dialog.loading = false;
};
// 是否内嵌下拉改变
const onSelectIframeChange = () => {
  if (state.ruleForm.isIframe == 1) state.ruleForm.isOpenLink = 1;
  else state.ruleForm.isOpenLink = -1;
};

// 提交
const onSubmit = () => {
  state.dialog.loading = true;
  emit("refresh", state.ruleForm);
};
// 请选择上级菜单
const change = (arr: number[]) => {
  state.ruleForm.parentId = arr[arr.length - 1];
};
// 页面加载时
onMounted(() => {

});

// 暴露变量
defineExpose({
  openDialog,
  closeDialog,
  resetLoading
});
</script>
<style>
.el-input-group__prepend i {
  color: #656464;
}
.el-input-number i {
  color: #656464;
}
.el-tabs--top{
  margin-top: 35px;
}
</style>
