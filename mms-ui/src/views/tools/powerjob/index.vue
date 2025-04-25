<template>
  <iframe ref="iframe" :src="iframeUrl" style="height: 90vh" ></iframe>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { iframeResizer } from 'iframe-resizer';

const iframeUrl = 'http://localhost:7700/'; // 你想在iframe中加载的页面URL
const iframe = ref(null);

onMounted(() => {
  iframeResizer({
    log: false, // 是否在控制台中记录信息
    scrolling: 'no', // 是否允许iframe滚动
    autoResize: true,
    heightCalculationMethod: 'taggedElement', // 高度计算方法
    height: '90vh'
  }, iframe.value);
});
</script>
