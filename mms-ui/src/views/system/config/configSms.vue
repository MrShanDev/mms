<template>
  <div class="m25">
    <div class="mb15">
      <el-segmented v-model="smsConfigValue" :options="options" block @change="changeSmsConfig">
        <template #default="{ item }">
          <div>{{ item.label }}</div>
        </template>
      </el-segmented>
    </div>
    <div class="m-t-10" v-if="smsConfigValue == 'test'">
      <el-form label-width="auto"
      label-position="top" >
          <el-form-item style="width: 200px;" label="手机号" prop="phone">
            <el-input v-model="testPhone" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item >
            <el-button type="primary" @click="sendSms">发送短信</el-button>
          </el-form-item>

      </el-form>
    </div>
    <el-form v-else ref="deptDialogFormRef" :model="configRuleForm" size="default" label-width="150px">
      <el-row>
        <el-col :span="9" :offset="0">
          <el-form-item label="是否启用" prop="status">
            <el-switch
                v-model="configRuleForm[0].configValue"
                inline-prompt
                active-text="启用"
                inactive-text="禁用"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-value="1"
                inactive-value="2">
            </el-switch>
          </el-form-item>
          <el-form-item label="AccessKeyId" prop="region">
            <el-input v-model="configRuleForm[1].configValue"
                      placeholder="请设置AccessKeyId"></el-input>
            <el-text size="small" type="danger">或者为：SdkAppId</el-text>
          </el-form-item>
          <el-form-item label="AccessKeySecret" prop="region">
            <el-input v-model="configRuleForm[2].configValue"
                      placeholder="请设置AccessKeySecret"></el-input>
          </el-form-item>
          <el-form-item label="TemplateId" prop="region">
            <el-input v-model="configRuleForm[3].configValue"
                      placeholder="请设置AccessKeySecret"></el-input>
          </el-form-item>
          <el-form-item label="Signature" prop="region">
            <el-input v-model="configRuleForm[4].configValue"
                      placeholder="请设置AccessKeySecret"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="default" @click="onSubmitConfig" type="primary" class="ml10">保存</el-button>
          </el-form-item>
        </el-col>

      </el-row>
    </el-form>
  </div>
</template>
<script setup lang="ts" name="systemSysConfigDialog">
const props = defineProps({
  value: {
    type: Array,
    default: () => []
  },
});
import {onMounted, reactive, ref} from "vue";
import {sysConfigApi} from '/@/api/system/config';
import {ElMessage} from "element-plus";
const baseApi = sysConfigApi();
const testPhone = ref('')
const smsConfigValue = ref('aliyun')
const options = [
  {
    label: "测试",
    value: "test",
  },
  {
    label: '阿里云',
    value: 'aliyun',
  },
  {
    label: '腾讯云',
    value: 'tencent',
  },
  {
    label: '华为云',
    value: 'huawei',
  },
  {
    label: '天翼云',
    value: 'ctyun',
  },
  {
    label: '七牛云',
    value: 'qiniu',
  }


]

const configRuleForm = reactive([
  {
    index:0,
    configName: '短信服务默认厂商',
    configKey: 'sys_sms_supplier',
    defKey: 'sys_sms_supplier',
    configType: '1',
    valueType:'String',
    configValue: smsConfigValue.value,
  },
  {
    index:1,
    configName: '短信服务厂商Key',
    configKey: 'sys_sms_accessKey',
    defKey: 'sys_sms_accessKey',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:2,
    configName: '短信服务厂商密钥',
    configKey: 'sys_sms_accessKeySecret',
    defKey: 'sys_sms_accessKeySecret',
    configType: '1',
    valueType:'String',
    configValue: ""
  },
  {
    index:3,
    configName: '短信服务厂商模版ID',
    configKey: 'sys_sms_template_id',
    defKey: 'sys_sms_template_id',
    configType: '1',
    valueType:'String',
    configValue: ""
  },
  {
    index:4,
    configName: '短信服务厂商签名',
    configKey: 'sys_sms_signature',
    defKey: 'sys_sms_signature',
    configType: '',
    valueType:'String',
    configValue: ''
  }
]);
const resetForm = () => {
  configRuleForm[0].configValue = '';
  configRuleForm[1].configValue = '';
  configRuleForm[2].configValue = '';
  configRuleForm[3].configValue = '';
  configRuleForm[4].configValue = '';
}
// 选择配置
const changeSmsConfig = (obj: string) => {
  resetForm();
  smsConfigValue.value = obj;

  initData();
}
const onSubmitConfig = () => {
  configRuleForm[0].configKey=configRuleForm[0].defKey+"_"+smsConfigValue.value;
  configRuleForm[1].configKey=configRuleForm[1].defKey+"_"+smsConfigValue.value;
  configRuleForm[2].configKey=configRuleForm[2].defKey+"_"+smsConfigValue.value;
  configRuleForm[3].configKey=configRuleForm[3].defKey+"_"+smsConfigValue.value;
  configRuleForm[4].configKey=configRuleForm[4].defKey+"_"+smsConfigValue.value;
  baseApi.configs(configRuleForm).then(res => {
    if (res.code === 200) {
      ElMessage.success('保存成功！');
    }
  });
}

const initData = () => {
  configRuleForm[0].configKey=configRuleForm[0].defKey+"_"+smsConfigValue.value;
  configRuleForm[1].configKey=configRuleForm[1].defKey+"_"+smsConfigValue.value;
  configRuleForm[2].configKey=configRuleForm[2].defKey+"_"+smsConfigValue.value;
  configRuleForm[3].configKey=configRuleForm[3].defKey+"_"+smsConfigValue.value;
  configRuleForm[4].configKey=configRuleForm[4].defKey+"_"+smsConfigValue.value;
  baseApi.getConfigs(configRuleForm).then(res => {
    configRuleForm[0].configValue = res.data[0].configValue;
    configRuleForm[1].configValue = res.data[1].configValue;
    configRuleForm[2].configValue = res.data[2].configValue;
    configRuleForm[3].configValue = res.data[3].configValue;
    configRuleForm[4].configValue = res.data[4].configValue;
  });
}

const sendSms=()=>{
  baseApi.sendSms(testPhone.value).then(res=>{
    if(res.code===200){
      ElMessage.success('发送成功！');
      testPhone.value="";
    }
  })
}

onMounted(() => {
  initData();
});
// 暴露变量
defineExpose({
  initData
});
</script>
<style scoped lang="scss">
.ml-10 {
  margin-left: 10px;
}
.m25{
  margin: 25px;
}
.el-segmented {
  --el-segmented-item-selected-color: #fff;
  --el-segmented-item-selected-bg-color: #666dff;
  --el-border-radius-base: 16px;
}
</style>
