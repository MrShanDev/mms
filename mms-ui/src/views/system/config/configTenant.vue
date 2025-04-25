<template>
  <el-form ref="deptDialogFormRef" :model="configRuleForm" size="default" label-width="150px">
    <el-row>
      <el-col :span="9" :offset="0">
        <el-form-item label="多租户状态" prop="status">
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
        <el-form-item label="多租户排除的的表" prop="status">
          <el-input type="textarea" v-model="configRuleForm[1].configValue"  placeholder="多租户排除的的表(以,分隔)" ></el-input>
          <el-text size="small" type="danger">多租户排除的的表(以,分隔)</el-text>
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
    configName: '多租户状态',
    configKey: 'sys_tenant_state',
    configType: '1',
    valueType:'String',
    configValue: '2'
  },
  {
    index:1,
    configName: '多租户排除的的表',
    configKey: 'sys_tenant_exclusion_table',
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
