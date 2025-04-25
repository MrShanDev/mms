<template>
  <div class="system-sysConfig-dialog-container">
    <el-dialog
      :title="state.dialog.title"
      v-model="state.dialog.isShowDialog"
      width="30vw"
      :close-on-click-modal="false"
    >
      <el-form
        ref="deptDialogFormRef"
        :model="state.ruleForm"
        size="default"
        label-width="100px"
      >
        <el-form-item v-show="false" label="主键ID" prop="id">
          <el-input v-model="state.ruleForm.id" placeholder="主键ID"></el-input>
        </el-form-item>
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="state.ruleForm.configName" placeholder="配置名称"></el-input>
        </el-form-item>
        <el-form-item label="配置键" prop="configKey">
          <el-input v-model="state.ruleForm.configKey" placeholder="配置键"></el-input>
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input
            type="textarea"
            v-model="state.ruleForm.configValue"
            placeholder="配置值"
          ></el-input>
        </el-form-item>
        <el-form-item label="配置状态" prop="status">
          <fast-switch
                  v-model="state.ruleForm.status"
                  dict-type="SYS_STATE"
                  placeholder="配置状态"
              ></fast-switch>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="state.ruleForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="onCancel" size="default">取 消</el-button>
        <el-button type="primary" @click="onSubmit" size="default">{{
          state.dialog.submitTxt
        }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="systemSysConfigDialog">
//ModuleName 系统配置
import { reactive, defineAsyncComponent, ref } from "vue";
// 引入组件
import FastSwitch from "/@/components/fast-switch/src/fast-switch.vue";
import { CURDEnum } from "/@/enums/CURDEnum";
// 定义子组件向父组件传值/事件
const emit = defineEmits(["refresh"]);
const ruleForm = reactive<RowSysConfigType>({
  id: "",
  configName: "",
  configKey: "",
  configValue: "",
  configType: 2,
  tenantId: "",
  revision: "",
  remark: "",
  status: 0,
  sort: "",
  createdBy: "",
  createdTime: "",
  updatedBy: "",
  updatedTime: "",
  columnName: "",
});
const state = reactive({
  ruleForm: ruleForm,
  dialog: {
    isShowDialog: false,
    type: "",
    title: "",
    submitTxt: "",
  },
});
// 重置
const resetForm = () => {
  state.ruleForm = {
    id: "",
    configName: "",
    configKey: "",
    configValue: "",
    configType: 2,
    tenantId: "",
    revision: "",
    remark: "",
    status: 0,
    sort: "",
    createdBy: "",
    createdTime: "",
    updatedBy: "",
    updatedTime: "",
    columnName: "",
  } as RowSysConfigType;
};
const openDialog = (type: CURDEnum, row: RowSysConfigType) => {
  resetForm();
  state.dialog.isShowDialog = true;
  if (type === CURDEnum.EDIT) {
    state.ruleForm = row;
    state.dialog.title = "修改";
    state.dialog.submitTxt = "修 改";
    state.dialog.type = CURDEnum.EDIT;
  }
  if (type === CURDEnum.INSERT) {
    state.dialog.title = "新增";
    state.dialog.submitTxt = "新 增";
    state.dialog.type = CURDEnum.INSERT;
  }
};
// 关闭弹窗
const closeDialog = () => {
  state.dialog.isShowDialog = false;
};
// 取消
const onCancel = () => {
  closeDialog();
};
// 提交
const onSubmit = () => {
  emit("refresh", state.ruleForm);
};

// 暴露变量
defineExpose({
  openDialog,
  closeDialog,
});
</script>
