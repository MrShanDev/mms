<template>
  <el-form ref="deptDialogFormRef" :model="configRuleForm" size="default" label-width="150px">
    <el-row>
      <el-col :span="9" :offset="0">
        <el-form-item label="邮件配置" prop="status">
          <el-switch 
              v-model="configRuleForm[0].configValue"
              active-color="#13ce66"
              inactive-color="#ff4949"
              inline-prompt
              active-text="启用"
              inactive-text="禁用"
              active-value="1"
              inactive-value="2">
          </el-switch>
        </el-form-item>
        <el-form-item label="服务器地址" prop="status">
          <el-input type="text" v-model="configRuleForm[1].configValue"  placeholder="请输入" ></el-input>
          <el-text size="small" type="danger">例如:smtp.163.com</el-text>
        </el-form-item>
        <el-form-item label="端口号" prop="status">
          <el-input type="text" v-model="configRuleForm[2].configValue"  placeholder="请输入" ></el-input>
          <el-text size="small" type="danger">例如:465</el-text>
        </el-form-item>
        <el-form-item label="邮件账号" prop="status">
          <el-input type="text" v-model="configRuleForm[3].configValue"  placeholder="请输入" ></el-input>
          <el-text size="small" type="danger">例如:mms@163.com</el-text>
        </el-form-item>
        <el-form-item label="身份验证" prop="status">
          <el-input type="password" v-model="configRuleForm[4].configValue"  placeholder="请输入" ></el-input>
          <el-text size="small" type="danger">对应的邮箱设置中获取</el-text>
        </el-form-item>
        <el-form-item label="SSL加密" prop="status">
          <el-switch
              v-model="configRuleForm[5].configValue"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-value="1"
              inactive-value="2">
          </el-switch>
          <el-text class="ml10" size="small" type="danger">推荐：开启</el-text>
        </el-form-item>
        <el-form-item>
          <el-button size="default" @click="onSubmitConfig" type="primary" class="ml10">保存</el-button>
        </el-form-item>
      </el-col>

    </el-row>
  </el-form>
</template>
<script setup lang="ts">
import {onMounted, reactive} from "vue";
import {sysConfigApi} from '/@/api/system/config';
const baseApi = sysConfigApi();
import {ElMessage} from "element-plus";
const props = defineProps({
  value: {
    type: Array,
    default: () => []
  },
});
const configRuleForm = reactive([
  {
    index:0,
    configName: '邮箱状态',
    configKey: 'sys_email_state',
    configType: '1',
    valueType:'String',
    configValue: '2'
  },
  {
    index:1,
    configName: '邮件服务器地址',
    configKey: 'sys_email_host',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:2,
    configName: '端口',
    configKey: 'sys_email_port',
    configType: '1',
    valueType:'String',
    configValue: '465'
  },
  {
    index:3,
    configName: '邮件账号',
    configKey: 'sys_email_from',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:4,
    configName: '邮箱密码',
    configKey: 'sys_email_pass',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:5,
    configName: 'SSL安全连接',
    configKey: 'sys_email_ssl',
    configType: '1',
    valueType:'String',
    configValue: '1'
  }
]);

const onSubmitConfig = () => {
  //console.log(configRuleForm);
  baseApi.configs(configRuleForm).then(res => {
    if (res.code === 200) {
      ElMessage.success('保存成功！');
    }
  });
}

const initData = () => {
  baseApi.getConfigs(configRuleForm).then(res => {
    configRuleForm[0].configValue = res.data[0].configValue;
    configRuleForm[1].configValue = res.data[1].configValue;
    configRuleForm[2].configValue = res.data[2].configValue;
    configRuleForm[3].configValue = res.data[3].configValue;
    configRuleForm[4].configValue = res.data[4].configValue;
    configRuleForm[5].configValue = res.data[5].configValue;
    //console.log(configRuleForm);
  });
}
onMounted(() => {
  initData();
});
// 暴露变量
defineExpose({
  initData
});
</script>
