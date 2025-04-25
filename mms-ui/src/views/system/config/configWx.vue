<template>
  <el-form ref="deptDialogFormRef" :model="configRuleForm" size="default" label-width="150px">
    <el-row>
      <el-col :span="9" :offset="0">
        <el-form-item label="微信配置" prop="status">
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
        <el-form-item label="模式" prop="status">
          <el-switch
              v-model="configRuleForm[10].configValue"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="小程序"
              inactive-text="公众号"
              active-value="2"
              inactive-value="1"
              inline-prompt
              style="--el-switch-on-color: #ec0a6b; --el-switch-off-color: #2e88ff"
              >
          </el-switch>
          <el-text class="ml10" size="small" type="danger">备注: 公众号/小程序模式只能选择一个</el-text>
        </el-form-item>

        <el-form-item v-show="configRuleForm[10].configValue=='1'" label="Appid" prop="status">
          <el-input type="text" v-model="configRuleForm[1].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item v-show="configRuleForm[10].configValue=='1'" label="AppSecret" prop="status">
          <el-input type="text" v-model="configRuleForm[2].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>

        <el-form-item v-show="configRuleForm[10].configValue=='2'" label="Appid" prop="status">
          <el-input type="text" v-model="configRuleForm[3].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item v-show="configRuleForm[10].configValue=='2'" label="AppSecret" prop="status">
          <el-input type="text" v-model="configRuleForm[4].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item label="Token" prop="status">
          <el-input type="textarea" v-model="configRuleForm[11].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item label="AesKey" prop="status">
          <el-input type="textarea" v-model="configRuleForm[12].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item label="扫码回复内容" prop="status">
          <el-input type="textarea" v-model="configRuleForm[13].configValue"  placeholder="请输入" ></el-input>
          <el-text class="ml10" size="small" type="danger">备注: 公众号/小程序，扫码登录后的回复内容</el-text>
        </el-form-item>
        <el-form-item label="微信商户ID" prop="status">
          <el-input type="text" v-model="configRuleForm[5].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item label="商户API证书" prop="status">
          <el-input type="textarea" v-model="configRuleForm[6].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item label="秘钥类型" prop="status">
          <el-switch
              v-model="configRuleForm[8].configValue"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="V2"
              inactive-text="V3"
              active-value="1"
              inline-prompt
              style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
              inactive-value="2">
          </el-switch>
          <el-text class="ml10" size="small" type="danger">备注: 秘钥类型，需要对应的秘钥</el-text>
        </el-form-item>
        <el-form-item label="商户API秘钥" prop="status">
          <el-input type="textarea" v-model="configRuleForm[7].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>

        <el-form-item label="支付回调" prop="status">
          <el-input type="textarea" v-model="configRuleForm[9].configValue"  placeholder="请输入" ></el-input>
        </el-form-item>
        <el-form-item label="退款回调" prop="status">
          <el-input type="textarea" v-model="configRuleForm[14].configValue"  placeholder="请输入" ></el-input>
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
    configName: '微信配置状态',
    configKey: 'sys_wx_state',
    configType: '1',
    valueType:'String',
    configValue: '2'
  },
  {
    index:1,
    configName: '公众号Appid',
    configKey: 'sys_wx_mp_appid',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:2,
    configName: '微信公众号AppSecret',
    configKey: 'sys_wx_mp_appsecret',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:3,
    configName: '小程序AppId',
    configKey: 'sys_wx_miniapp_appid',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:4,
    configName: '小程序AppSecret',
    configKey: 'sys_wx_miniapp_appsecret',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:5,
    configName: '微信商户ID',
    configKey: 'sys_wx_pay_id',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:6,
    configName: '微信商户证书',
    configKey: 'sys_wx_pay_path',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:7,
    configName: '微信商户秘钥',
    configKey: 'sys_wx_pay_appsecret',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:8,
    configName: '微信商户秘钥类型',
    configKey: 'sys_wx_pay_type',
    configType: '1',
    valueType:'String',
    configValue: '1'
  },
  {
    index:9,
    configName: '微信支付回调',
    configKey: 'sys_wx_pay_notifyUrl',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:10,
    configName: '微信模式',
    configKey: 'sys_wx_model',
    configType: '1',
    valueType:'String',
    configValue: '1'
  },
  {
    index:11,
    configName: 'AppToken',
    configKey: 'sys_wx_token',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:12,
    configName: 'AppAesKey',
    configKey: 'sys_wx_aesKey',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
  {
    index:13,
    configName: '扫码关注回复内容',
    configKey: 'sys_wx_attention_msg',
    configType: '1',
    valueType:'String',
    configValue: ''
  },
    {
        index:14,
        configName: '退款回调',
        configKey: 'sys_wx_refund_path',
        configType: '1',
        valueType:'String',
        configValue: ''
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
    configRuleForm[6].configValue = res.data[6].configValue;
    configRuleForm[7].configValue = res.data[7].configValue;
    configRuleForm[8].configValue = res.data[8].configValue;
    configRuleForm[9].configValue = res.data[9].configValue;
    configRuleForm[10].configValue = res.data[10].configValue;
    configRuleForm[11].configValue = res.data[11].configValue;
    configRuleForm[12].configValue = res.data[12].configValue;
    configRuleForm[13].configValue = res.data[13].configValue;
    configRuleForm[14].configValue = res.data[14].configValue;

    //console.log(configRuleForm);
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
