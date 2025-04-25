<template>
  <el-drawer
    class="pb-5 pr-5 pl-5"
    v-model="visible"
    title="编辑"
    :size="1500"
    :with-header="false"
  >
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="数据源" name="data">
        <data-source ref="dataSourceRef" ></data-source>
      </el-tab-pane>
      <el-tab-pane label="字段映射" name="field">
       <field-type ref="fieldTypeRef" ></field-type>
      </el-tab-pane>
      <el-tab-pane label="基类管理" name="base">
        <base-class ref="baseClassRef" ></base-class>
      </el-tab-pane>
      <el-tab-pane label="项目配置" name="project">
        <project ref="projectRef" ></project>
      </el-tab-pane>
    </el-tabs>
  </el-drawer>
</template>
<script setup lang="ts">
import { nextTick, reactive, ref } from "vue";
import { ElMessage, TabsPaneContext } from "element-plus/es";
import DataSource from "./dataSource/index.vue";
import BaseClass from "./baseClass/index.vue";
import FieldType from "./fieldType/index.vue";
import Project from "./project/index.vue";

const dataSourceRef = ref();
const fieldTypeRef = ref();
const baseClassRef = ref();
const projectRef = ref();
const activeName = ref();
const visible = ref(false);
const dataFormRef = ref();
const handleClick = (tab: TabsPaneContext) => {
  if (tab.paneName == "data") {
    dataSourceRef.value.init();
  }
  if (tab.paneName == "field") {
    fieldTypeRef.value.init();
  }
  if (tab.paneName == "base") {
    baseClassRef.value.init();
  }
  if (tab.paneName == "project") {
    projectRef.value.init();
  }
};
const init = (id: number) => {
  visible.value = true;
  activeName.value = "data";
};
defineExpose({
  init,
});
</script>
