<template>
  <el-form
    ref="deptDialogFormRef"
    :model="configRuleForm"
    size="default"
    label-width="150px"
  >
    <el-row>
      <el-col :span="9" :offset="0">
        <el-form-item label="系统名称" prop="status">
          <el-input
            v-model="configRuleForm[0].configValue"
            placeholder="请设置系统名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="系统描述" prop="status">
          <el-input
            type="textarea"
            v-model="configRuleForm[1].configValue"
            placeholder="请设置系统名称"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="系统LOGO" prop="status">
          <fast-img
            v-model="configRuleForm[2].configValue"
            :fileUrl="configRuleForm[2].configValue"
            @update:fileUrl="onUrlMainImage"
          />
        </el-form-item>
        <el-form-item label="登录页背景图" prop="status"  >
          <fast-img  
            v-model="configRuleForm[5].configValue"
            :fileUrl="configRuleForm[5].configValue"
            @update:fileUrl="onUrlLoginImage"
            width="400px"
            :cropperHeight="950"
            :cropperWidth="1500"
          />
        </el-form-item>
          
        <el-form-item label="登录图形验证码" prop="status">
          <el-switch
            v-model="configRuleForm[3].configValue"
            active-color="#13ce66"
            inactive-color="#ff4949"
            inline-prompt
            active-text="启用"
            inactive-text="禁用"
            active-value="0"
            inactive-value="1"
          >
          </el-switch>
        </el-form-item>
        <el-form-item label="登录方式" prop="status">
          <el-checkbox-group v-model="configRuleForm[4].configValues">
            <el-checkbox label="账号密码" value="1" />
            <el-checkbox label="手机短信验证" value="2" />
            <el-checkbox label="二维码" value="3"  />
          </el-checkbox-group>
          <el-text size="small" type="danger">手机短信验证，需要配置短信服务</el-text>
        </el-form-item>
        <el-form-item>
          <el-button size="default" @click="onSubmitConfig" type="primary" class="ml10"
            >保存</el-button
          >
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script setup lang="ts">
import { onMounted, reactive } from "vue";
import { sysConfigApi } from "/@/api/system/config";
const baseApi = sysConfigApi();
import FastImg from "/@/components/fast-upload/img.vue";
import { ElMessage } from "element-plus";
const props = defineProps({
  value: {
    type: Array,
    default: () => [],
  },
});
const configRuleForm = reactive([
  {
    index: 0,
    configName: "系统名称",
    configKey: "sys_base_title",
    configType: "1",
    valueType: "String",
    configValue: "模块化管理系统",
  },
  {
    index: 1,
    configName: "系统描述",
    configKey: "sys_base_description",
    configType: "1",
    valueType: "String",
    configValue:
      "模块化管理系统 Modular management system  简称：MMS 一款灵活、高效、低代码模块化管理系统",
  },
  {
    index: 2,
    configName: "系统LOGO",
    configKey: "sys_base_logo",
    configType: "1",
    valueType: "String",
    configValue: "https://doc.sxpcwlkj.com/logo.png",
  },
  {
    index: 3,
    configName: "登录图形验证码",
    configKey: "sys_base_captcha_state",
    configType: "1",
    valueType: "String",
    configValue: "1",
  },
  {
    index: 4,
    configName: "系统登录方式",
    configKey: "sys_base_login_type",
    configType: "1",
    valueType: "Array",
    configValues: ["1"],
  },
  {
    index: 5,
    configName: "登录页背景",
    configKey: "sys_base_login_bg",
    configType: "1",
    valueType: "String",
    configValue: "https://doc.sxpcwlkj.com/logo.png",
  },
]);
const onUrlMainImage = (data: string) => {
  configRuleForm[2].configValue = data;
};
const onUrlLoginImage = (data: string) => {
  configRuleForm[5].configValue = data;
};
const onSubmitConfig = () => {
  baseApi.configs(configRuleForm).then((res) => {
    if (res.code === 200) {
      ElMessage.success("保存成功！");
    }
  });
};

const initData = () => {
  baseApi.getConfigs(configRuleForm).then((res) => {
    configRuleForm[0].configValue = res.data[0].configValue;
    configRuleForm[1].configValue = res.data[1].configValue;
    configRuleForm[2].configValue = res.data[2].configValue;
    configRuleForm[3].configValue = res.data[3].configValue;
    configRuleForm[4].configValues = res.data[4].configValues;
    configRuleForm[5].configValue = res.data[5].configValue;
  });
};
onMounted(() => {
  initData();
});
// 暴露变量
defineExpose({
  initData,
});
</script>
