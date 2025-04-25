<template>
  <div class="system-sysConfig-container layout-padding mt-5">
    <el-tabs v-model="selectedName" @tab-click="tabClick" type="border-card">
      <el-tab-pane label="系统配置" name="system">
        <el-tabs v-model="selectedItemName"  tab-position="left" style="min-height: 500px;padding-left: 5%" @tab-click="tabClickItem" class="system-tabs">
          <el-tab-pane label="基础设置" name="base">
            <ConfigBase  ref="configBaseRef"></ConfigBase>
          </el-tab-pane>
          <el-tab-pane label="存储配置" name="oss" >
            <ConfigOss  ref="configOssRef"></ConfigOss>
          </el-tab-pane>
          <el-tab-pane label="多租户配置" name="tenant">
            <ConfigTenant  ref="configTenantRef"></ConfigTenant>
          </el-tab-pane>
          <el-tab-pane label="短信设置" name="sms">
            <ConfigSms  ref="configSmsRef"></ConfigSms>
          </el-tab-pane>
          <el-tab-pane label="邮件设置">
            <ConfigEmail ref="configEmailRef" />
          </el-tab-pane>
          <el-tab-pane label="微信设置">
            <ConfigWx ref="configWxRef" />
          </el-tab-pane>
          <el-tab-pane label="其他">Task</el-tab-pane>
        </el-tabs>
      </el-tab-pane>
      <el-tab-pane label="自定义" name="user">
        <ConfigUse ref="configUseRef"></ConfigUse>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts" name="systemSysConfig">
//ModuleName 系统配置

import {defineAsyncComponent, reactive, onMounted, ref} from 'vue';

import {TabsPaneContext} from "element-plus/es";
// 定义变量内容

//=============配置============
const selectedName = ref("system");
const selectedItemName = ref("base");
// 组件
//1.自定义
const ConfigUse = defineAsyncComponent(() => import('/@/views/system/config/configUse.vue'));
const configUseRef = ref();
//2.基础配置
const ConfigBase = defineAsyncComponent(() => import('/@/views/system/config/configBase.vue'));
const configBaseRef=ref();
//3.存储配置
const ConfigOss = defineAsyncComponent(() => import('/@/views/system/config/configOss.vue'));
const configOssRef = ref();
//4.多租户
const ConfigTenant = defineAsyncComponent(() => import('/@/views/system/config/configTenant.vue'));
const configTenantRef = ref();
//5.短信设置
const ConfigSms = defineAsyncComponent(() => import('/@/views/system/config/configSms.vue'));
const configSmsRef = ref();
//6.邮件设置
const ConfigEmail = defineAsyncComponent(() => import('/@/views/system/config/configEmail.vue'));
const configEmailRef = ref();
//7.邮件设置
const ConfigWx = defineAsyncComponent(() => import('/@/views/system/config/configWx.vue'));
const configWxRef = ref();

//一级tab点击事件
const tabClick = (pane: TabsPaneContext, ev: Event) => {
  //console.log("tab", pane.props.name, pane.props.label, "event", ev)
  if(pane.props.name==='system'){
    configBaseRef.value.initData(); // 调用子组件的方法
  }
  if(pane.props.name==='user'){
    configUseRef.value.getTableData();
  }
}

//二级tab点击事件
const tabClickItem = (pane: TabsPaneContext, ev: Event) => {
  console.log("tab", pane.props.name, pane.props.label, "event", ev)
  selectedItemName.value=pane.props.name+"";
  if(pane.props.name==="base"){
    if (configBaseRef.value) {
      configBaseRef.value.initData(); // 调用子组件的方法
    }
  }
  if(pane.props.name==="oss"){
    console.log(configOssRef.value)
    if (configOssRef.value) {
      configOssRef.value.initData(); // 调用子组件的方法
    }
  }
  //tenant
  if(pane.props.name==="tenant"){
    if (configTenantRef.value) {
      configTenantRef.value.initData(); // 调用子组件的方法
    }
  }
  //sms
  if(pane.props.name==="sms"){
    if (configSmsRef.value) {
      configSmsRef.value.initData(); // 调用子组件的方法
    }
  }
}

// 页面加载时
onMounted(() => {
   // getTableData();
});
</script>

<style scoped lang="scss">
.system-tabs{
  margin-top: 20px;
}
.system-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.el-tabs--right .el-tabs__content,
.el-tabs--left .el-tabs__content {
  height: 100%;
}
.system-tabs-item{
  padding-top: 40px;
  padding-bottom: 10px;
}
</style>
