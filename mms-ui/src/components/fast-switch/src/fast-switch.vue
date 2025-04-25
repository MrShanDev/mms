<template>
  <el-switch
    v-if="dataList.length == 2"
    :model-value="modelValue + ''"
    :placeholder="placeholder"
    :active-value="dataList[0].dictValue"
    :inactive-value="dataList[1].dictValue"
    inline-prompt
    :active-text="dataList[0].dictLabel"
    :inactive-text="dataList[1].dictLabel"
    :disabled="disabled"
    @change="$emit('update:modelValue', $event)"
  ></el-switch>
  <span v-if="dataList.length == 0">字典为空</span>
  <span v-if="dataList.length == 1">字典数量应该是2个</span>
  <span v-if="dataList.length > 2">字典数量应该是2个</span>
</template>

<script setup lang="ts" name="FastSelect">
import { getDictDataList } from "/@/utils/mms";
import { useAppStore } from "/@/stores/app";

const appStore = useAppStore();
const props = defineProps({
  modelValue: {
    type: [Number, String],
    required: true,
  },
  dictType: {
    type: String,
    required: true,
  },
  clearable: {
    type: Boolean,
    required: false,
    default: () => false,
  },
  placeholder: {
    type: String,
    required: false,
    default: () => "",
  },
  disabled: {
    type: Boolean,
    required: false,
    default: () => false,
  }
});

const dataList = getDictDataList(appStore.dictList, props.dictType);
</script>
<style lang="scss" scoped>
.w-full {
  min-width: 150px !important;
}
</style>
