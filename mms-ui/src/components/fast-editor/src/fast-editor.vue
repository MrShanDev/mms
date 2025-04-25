<template>
  <div class="editor-container">
    <Toolbar :editor="editorRef" :mode="mode" />
    <Editor
      :mode="mode"
      :defaultConfig="state.editorConfig"
      :style="{ height }"
      v-model="state.editorVal"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>

<script setup lang="ts" name="wngEditor">
// https://www.wangeditor.com/v5/for-frame.html#vue3
import "@wangeditor/editor/dist/css/style.css";
import { reactive, shallowRef, watch, onBeforeUnmount } from "vue";
import { IDomEditor, IEditorConfig } from "@wangeditor/editor";
import { Toolbar, Editor } from "@wangeditor/editor-for-vue";
import { Session } from "/@/utils/storage";
import { getEnv } from "/@/utils/mms";
import { ElMessage } from "element-plus";
// 定义父组件传过来的值
const props = defineProps({
  // 是否禁用
  disable: {
    type: Boolean,
    default: () => false,
  },
  // 内容框默认 placeholder
  placeholder: {
    type: String,
    default: () => "请输入内容...",
  },
  // https://www.wangeditor.com/v5/getting-started.html#mode-%E6%A8%A1%E5%BC%8F
  // 模式，可选 <default|simple>，默认 default
  mode: {
    type: String,
    default: () => "default",
  },
  // 高度
  height: {
    type: String,
    default: () => "310px",
  },
  // 双向绑定，用于获取 editor.getHtml()
  getHtml: String,
  // 双向绑定，用于获取 editor.getText()
  getText: String,
});

// 定义子组件向父组件传值/事件
const emit = defineEmits(["update:getHtml", "update:getText"]);

// 定义变量内容
const editorRef = shallowRef();
const state = reactive({
  editorConfig: <IEditorConfig>{
    placeholder: props.placeholder,
    MENU_CONF: {},
  },
  editorVal: props.getHtml,
});
if (state.editorConfig) {
  const menuConf = state.editorConfig.MENU_CONF;
}
// 上传图片
if (state.editorConfig.MENU_CONF) {
  state.editorConfig.MENU_CONF["uploadImage"] = {
    server: getEnv() + "/system/oss/wangEditor",
    fieldName: "file",
    headers: {
      Authorization: `${Session.get("token")}`,
    },
    customInsert(res: any, insertFn: any) {
      // TS 语法
      // customInsert(res, insertFn) {                  // JS 语法
      // res 即服务端的返回结果
      // 从 res 中找到 url alt href ，然后插入图片
      insertFn(res.data.url, "", "");
    },
    onSuccess(file: File, res: any) {
      console.log(`${file.name} 上传成功`, res);
    },
    onFailed(file: File, res: any) {
      console.log(`${file.name} 上传失败`, res);
    },
    onError(file: File, err: any, res: any) {
      ElMessage.warning("出错了！，（" + err + "）");
      console.log(`${file.name} 上传出错`, err, res);
    },
  };
}

// 编辑器回调函数
const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor;
};
// 编辑器内容改变时
const handleChange = (editor: IDomEditor) => {
  emit("update:getHtml", editor.getHtml());
  emit("update:getText", editor.getText());
};
// 页面销毁时
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

watch(
  () => props.disable,
  (bool) => {
    const editor = editorRef.value;
    if (editor == null) return;
    bool ? editor.disable() : editor.enable();
  },
  {
    deep: true,
  }
);
// 监听双向绑定值改变，用于回显
watch(
  () => props.getHtml,
  (val) => {
    state.editorVal = val;
  },
  {
    deep: true,
  }
);
</script>
