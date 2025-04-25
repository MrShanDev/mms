<template>
  <el-select class="w-full" :model-value="modelValue + ''" :placeholder="placeholder" :clearable="clearable" @change="$emit('update:modelValue', $event)">
    <el-option v-for="data in dataList" :key="data.dictValue" :label="data.dictLabel" :value="data.dictValue">{{ data.dictLabel }}</el-option>
  </el-select>
</template>

<script setup lang="ts" name="FastSelect">
import { getDictDataList } from '/@/utils/mms'
import { useAppStore } from '/@/stores/app'

const appStore = useAppStore()
const props = defineProps({
  modelValue: {
    type: [Number, String],
    required: true
  },
  dictType: {
    type: String,
    required: true
  },
  clearable: {
    type: Boolean,
    required: false,
    default: () => false
  },
  placeholder: {
    type: String,
    required: false,
    default: () => ''
  }
})

const dataList = getDictDataList(appStore.dictList, props.dictType)
</script>
<style lang="scss" scoped>
.w-full{
  min-width: 150px !important;
}
</style>
