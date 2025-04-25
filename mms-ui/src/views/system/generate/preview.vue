<template>
  <el-drawer
    class="pb-5 pr-5 pl-5"
    v-model="visible"
    title="代码预览"
    size="95%"
    @close="emit('close')"
    :with-header="false"
  >
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane
        v-for="(item, index) in preViewList"
        :key="index"
        :label="item.fileName"
        :name="index"
      >
      <el-scrollbar height="90vh">
        <div class="block">
          <div class="copy-btn" @click="copy">复制</div>
          <pre>{{ content }}</pre>
        </div>
      </el-scrollbar>
        
      </el-tab-pane>
    </el-tabs>
  </el-drawer>
</template>

<script setup lang="ts">
import { nextTick, reactive, ref } from "vue";
import { ElMessage, TabsPaneContext } from "element-plus/es";
import useClipboard from "vue-clipboard3";
const { toClipboard } = useClipboard();
const activeName = ref(0);
const content = ref("");
const handleClick = (tab: TabsPaneContext) => {
  let index: number = Number(tab.index);
  content.value = preViewList[index].content;
};

const emit = defineEmits(["close"]);
const visible = ref(false);

interface PreView {
  fileName: string;
  content: string;
}

let preViewList = reactive<PreView[]>([]);

const init = (data: PreView[]) => {
  visible.value = true;
  preViewList = data;
  content.value = data[activeName.value].content;
};
const copy = () => {
  toClipboard(content.value)
    .then(() => {
      ElMessage.success("复制成功！");
    })
    .catch(() => {
      ElMessage.warning("复制失败！");
    });
};
defineExpose({
  init,
});
</script>

<style lang="scss">
.sortable-row-gen .drag-btn {
  cursor: move;
  font-size: 12px;
}

.sortable-row-gen .vxe-body--row.sortable-ghost,
.sortable-row-gen .vxe-body--row.sortable-chosen {
  background-color: #dfecfb;
}
.el-drawer .el-drawer__body {
  width: 100%;
  height: 100%;
  overflow: auto;
  padding: 10px;
}
.copy-btn {
  position: absolute;
  right: 20px;
  top: 20px;
  z-index: 999;
  cursor: pointer;
  color: #409eff;
  font-size: 14px;
}
pre {
  margin: 0;
  padding: 0;
  background-color: #f6f8fa;
  border-radius: 5px;
  padding: 10px;
  font-size: 14px;
  line-height: 1.5;
  color: #24292e;
  font-family: Consolas, Menlo, Monaco, "Courier New", monospace;
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-x: auto;
  position: relative;
}
</style>
