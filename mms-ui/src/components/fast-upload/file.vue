<template>
  <el-upload
      class="upload-file"
      action="#"
      v-model:file-list="_fileList"
      :http-request="handleHttpUpload"
      :headers="httpFileHeaders"
      multiple
      :disabled="self_disabled"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :limit="fileSize"
      :on-exceed="handleExceed"
      :show-file-list="isShowMsg"
  >
    <el-button type="primary">点击上传</el-button>
    <template v-if="isShowMsg" #tip>
      <div class="el-upload__tip">
        jpg/png files with a size less than 500KB.
      </div>
    </template>
  </el-upload>
</template>
<script lang="ts" setup>
import { ref, computed, inject ,onMounted} from 'vue'
import { UploadProps, UploadRequestOptions, formContextKey, formItemContextKey,ElMessage, ElMessageBox, UploadFile, UploadUserFile } from 'element-plus'
import { uploadImg } from '/@/api/system/upload'
import {Session} from "/@/utils/storage";
const httpFileHeaders = ref({
  'Authorization': `${Session.get('token')}`
});

interface UploadFileProps {
  fileUrl: string // 图片地址 ==> 必传
  drag?: boolean // 是否支持拖拽上传 ==> 非必传（默认为 true）
  disabled?: boolean // 是否禁用上传组件 ==> 非必传（默认为 false）
  fileSize?: number // 图片大小限制 ==> 非必传（默认为 5M）
  fileType?: any[] // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
  height?: string // 组件高度 ==> 非必传（默认为 150px）
  width?: string // 组件宽度 ==> 非必传（默认为 150px）
  borderRadius?: string // 组件边框圆角 ==> 非必传（默认为 8px）
  isShowMsg?: boolean // 是否显示提示信息 ==> 非必传（默认为 true）
}
// 本地图片列表
const _fileList = ref<UploadUserFile[]>([])
// 接受父组件参数
const props = defineProps( {
  modelValue: {
      type: String,
      default: ''
    },
    drag:{
      type: Boolean,
      default: true
    },
    disabled:{
      type: Boolean,
      default: false
    },
    fileSize:{
      type: Number,
      default: 1
    },
    fileType:{
      type: Array,
      default: () => ['image/jpeg', 'image/png', 'image/gif','video/mp4']
    },
    height:{
      type: String,
      default: '150px'
    },
    width:{
      type: String,
      default: '150px'
    },
    borderRadius:{
      type: String,
      default: '8px'
    },
    isShowMsg:{
      type: Boolean,
      default: false
    }
});
// 在组件挂载后执行一些逻辑
onMounted(() => {
  if(props.modelValue!=null&&props.modelValue!=""){
    _fileList.value = [{url:props.modelValue,name:props.modelValue.length+""}];
  }
    // 可以在这里执行初始化逻辑，比如获取数据等
});
/**
 * @description 图片上传
 * @param options upload 所有配置项
 * */
const emit = defineEmits<{
  'update:modelValue': [value: any]
  'success': [value: any]
}>()

// 获取 el-form 组件上下文
const formContext = inject(formContextKey, void 0)
// 获取 el-form-item 组件上下文
const formItemContext = inject(formItemContextKey, void 0)
// 判断是否禁用上传和删除
const self_disabled = computed(() => {
  return props.disabled || formContext?.disabled
})
// 上传
const handleHttpUpload = async (options: UploadRequestOptions) => {
  let formData = new FormData()
  formData.append('file', options.file)
  try {
    await uploadImg(formData).then(res=>{
      emit('update:modelValue', res.data.url)
      emit('success', res.data)
      // 调用 el-form 内部的校验方法（可自动校验）
      formItemContext?.prop && formContext?.validateField([formItemContext.prop as string])
    })
  } catch (error) {
    options.onError(error as any)
  }
}
//删除
const handleRemove: UploadProps['onRemove'] = (file, uploadFiles) => {
  emit('update:modelValue', '')
}
// 点击
const handlePreview: UploadProps['onPreview'] = (uploadFile) => {

}
// 超出限制
const handleExceed: UploadProps['onExceed'] = (files, uploadFiles) => {
  ElMessage.warning(
      `最多可选择 ${files.length} 个文件！`
  )
}
//删除前
const beforeRemove: UploadProps['beforeRemove'] = (uploadFile, uploadFiles) => {
  return ElMessageBox.confirm(
      `确定要删除 ${uploadFile.name} ?`
  ).then(
      () => true,
      () => false
  )
}
</script>
