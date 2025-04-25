<template>
  <div class="upload-box">
    <el-upload
        ref="uploadImage"
        v-model:file-list="_fileList"
        action="#"
        list-type="picture-card"
        :class="['upload', self_disabled ? 'disabled' : '', drag ? 'no-border' : '']"
        :multiple="true"
        :disabled="self_disabled"
        :limit="limit"
        :http-request="handleHttpUpload"
        :before-upload="beforeUpload"
        :on-exceed="handleExceed"
        :on-success="uploadSuccess"
        :on-error="uploadError"
        :drag="drag"
        :accept="fileType.join(',')"
    >
      <div class="upload-empty">
        <slot name="empty">
          <el-icon><Plus /></el-icon>
        </slot>
      </div>
      <template #file="{ file }">
        <img :src="file.url" class="upload-image img-cropper" :alt="file.name" :id="file.uid"  />
        <div class="upload-handle" @click.stop>

          <div v-if="!self_disabled" class="handle-icon" @click="openDialog(file)">
            <el-icon><Delete /></el-icon>
            <span>裁剪</span>
          </div>
          <div class="handle-icon" @click="handlePictureCardPreview(file)">
            <el-icon><ZoomIn /></el-icon>
            <span>查看</span>
          </div>
          <div v-if="!self_disabled" class="handle-icon" @click="handleRemove(file)">
            <el-icon><Delete /></el-icon>
            <span>删除</span>
          </div>
        </div>
      </template>
    </el-upload>
    <div class="el-upload__tip">
      <slot name="tip"></slot>
    </div>
    <el-image-viewer v-if="imgViewVisible" :url-list="[viewImageUrl]" @close="imgViewVisible = false" />
  </div>
</template>

<script setup lang="ts" name="UploadImgs">
import {ref, computed, inject, watch, reactive, nextTick,onMounted} from 'vue'
import {Delete, Plus, UploadFilled, ZoomIn} from '@element-plus/icons-vue'
import { uploadImg } from '/@/api/system/upload'
import {UploadProps, UploadFile, UploadUserFile, UploadRequestOptions, ElMessage} from 'element-plus'
import { ElNotification, formContextKey, formItemContextKey } from 'element-plus'

interface UploadFileProps {
  fileList: UploadUserFile[]
  drag?: boolean // 是否支持拖拽上传 ==> 非必传（默认为 true）
  disabled?: boolean // 是否禁用上传组件 ==> 非必传（默认为 false）
  limit?: number // 最大图片上传数 ==> 非必传（默认为 5张）
  fileSize?: number // 图片大小限制 ==> 非必传（默认为 5M）
  fileType?: any[] // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
  height?: string // 组件高度 ==> 非必传（默认为 150px）
  width?: string // 组件宽度 ==> 非必传（默认为 150px）
  borderRadius?: string // 组件边框圆角 ==> 非必传（默认为 8px）
}
const props = defineProps( {
  modelValue: {
      type: String,
      default: ''
    },
  fileList: {
    type: Array<UploadUserFile>,
    default: () => []
  },
  drag:{
    type:Boolean,
    default:true
  },
  disabled:{
    type:Boolean,
    default:false
  },
  limit:{
    type:Number,
    default:5
  },
  fileSize:{
    type:Number,
    default:5
  },
  fileType:{
    type: Array,
    default: () => ['image/jpeg', 'image/png', 'image/gif']
  },
  height:{
    type:String,
    default:'150px'
  },
  width:{
    type:String,
    default:'150px'
  },
  borderRadius:{
    type:String,
    default:'8px'
  },
  cropperWidth:{
    type:Number,
    default:400
  },
  cropperHeight:{
    type:Number,
    default:400
  },
})
const emit = defineEmits<{
  'update:modelValue': [value: string],
  'success': [value: any]
}>();
// 获取 el-form 组件上下文
const formContext = inject(formContextKey, void 0)
// 获取 el-form-item 组件上下文
const formItemContext = inject(formItemContextKey, void 0)
// 判断是否禁用上传和删除
const self_disabled = computed(() => {
  return props.disabled || formContext?.disabled
})
// 默认回显图片
const _fileList = ref<UploadUserFile[]>([])
// 在组件挂载后执行一些逻辑
onMounted(() => {
    if(props.modelValue==null||props.modelValue.length==0){
        return;
    }
    const arryIngs:Array<string>= props.modelValue.split(',');
      if(arryIngs.length>0){
        arryIngs.map((item:string)=>{
            if(item.length>0){
                _fileList.value.push({url:item,name:item.length+""})
            }
      })
    }

    // 可以在这里执行初始化逻辑，比如获取数据等
});


watch(
    () => props.modelValue,
    (n: string) => {
      _fileList.value=[];
      console.log('update:modelValue', n)
        if(n==null||n.length==0){
            return;
        }
        const arryIngs:Array<string>= n.split(',');
        if(arryIngs.length>0){
            arryIngs.map((item:string)=>{
                if(item.length>0){
                    _fileList.value.push({url:item,name:item.length+""})
                }
            })
        }
    }
)

/**
 * @description 文件上传之前判断
 * @param rawFile 选择的文件
 * */
const beforeUpload: UploadProps['beforeUpload'] = rawFile => {
  const imgSize = rawFile.size / 1024 / 1024 < props.fileSize
  const imgType = props.fileType.includes(rawFile.type)
  if (!imgType) {
    ElNotification({
      title: '温馨提示',
      message: '上传图片不符合所需的格式！',
      type: 'warning'
    })
  }
  if (!imgSize) {
    setTimeout(() => {
      ElNotification({
        title: '温馨提示',
        message: `上传图片大小不能超过 ${props.fileSize}M！`,
        type: 'warning'
      })
    }, 0)
  }
  return imgType && imgSize
}

/**
 * @description 图片上传
 * @param options upload 所有配置项
 * */
const handleHttpUpload = async (options: UploadRequestOptions) => {
  let formData = new FormData()
  formData.append('file', options.file)
  try {
    const { data } = await uploadImg(formData)
    options.onSuccess(data)
  } catch (error) {
    options.onError(error as any)
  }
}

/**
 * @description 图片上传成功
 * @param response 上传响应结果
 * @param uploadFile 上传的文件
 * */

const uploadSuccess = (response: { url: string } | undefined, uploadFile: UploadFile) => {
  if (!response) {
    return
  }
  uploadFile.url = response.url
  const imgs = _fileList.value.map(obj => obj.url).join(',');
  emit('update:modelValue', imgs)
  emit('success', imgs)
  // 调用 el-form 内部的校验方法（可自动校验）
  // 调用 el-form 内部的校验方法（可自动校验）
  formItemContext?.prop && formContext?.validateField([formItemContext.prop as string])
  ElNotification({
    title: '温馨提示',
    message: '图片上传成功！',
    type: 'success'
  })
}

/**
 * @description 删除图片
 * @param file 删除的文件
 * */
const handleRemove = (file: UploadFile) => {
  _fileList.value = _fileList.value.filter(item => item.url !== file.url || item.name !== file.name);
  const imgs = _fileList.value.map(obj => obj.url).join(',');
  emit('update:modelValue', imgs)
}

/**
 * @description 图片上传错误
 * */
const uploadError = () => {
  ElNotification({
    title: '温馨提示',
    message: '图片上传失败，请您重新上传！',
    type: 'error'
  })
}

/**
 * @description 文件数超出
 * */
const handleExceed = () => {
  ElNotification({
    title: '温馨提示',
    message: `当前最多只能上传 ${props.limit} 张图片，请移除后上传！`,
    type: 'warning'
  })
}

/**
 * @description 图片预览
 * @param file 预览的文件
 * */
const viewImageUrl = ref('')
const imgViewVisible = ref(false)
const handlePictureCardPreview: UploadProps['onPreview'] = file => {
  viewImageUrl.value = file.url!
  imgViewVisible.value = true
}

// ====================== 裁剪 ======================
import Cropper from 'cropperjs';
import 'cropperjs/dist/cropper.css';
const croppedImageSrc = ref(null);
const imageSrc = ref<string | ArrayBuffer | null>(null);
const uploadImage = ref(null);
const uid = ref(0);
// 自定义值
const state = reactive({
  dialogWidth: '800px',
  dialogTitle: "图片裁剪上传",
  isShowDialog: false,
  cropperImg: '',
  cropperImgBase64: '',
  cropper: '' as RefType,
});

/**
 * 打开 裁剪图片
 * @param file
 */
const openDialog = async (file: UploadFile) => {
  if (!file.url) return;
  uid.value=file.uid;
  imageSrc.value=file.url.toString();
  croppedImageSrc.value = null;
  state.dialogWidth="60%";
  state.isShowDialog = true;
  await nextTick();
  initCropper(file);
}

/**
 * 关闭 裁剪图片
 *
 */
const closeDialog = () => {
  state.isShowDialog = false;
}
/**
 * 裁剪初始化
 */
const initCropper = (file: UploadFile) => {
  if (state.cropper) {
    state.cropper.destroy();
  }
  if (!uploadImage.value) return;
  state.dialogWidth="60%";
  state.dialogTitle="图片采集上传( "+props.cropperWidth+" x "+props.cropperHeight+" )"
  state.cropper = new Cropper(uploadImage.value, {
    aspectRatio: props.cropperWidth / props.cropperHeight, // 设置裁剪框的宽高比
    viewMode: 1, // 限制裁剪框不超过画布大小
  });
};


/**
 * 修改图片大小 正数为变大 负数变小
 * @param num
 */
const changeScale = (num:number) => {
  num = num || 1
  state.cropper.zoom(num)
}
/**
 * 向左边旋转90度
 * @param num
 */
const rotateLeft = (num:number) => {
  num = num || 1
  state.cropper.rotate(num)
}
/**
 * 向右边旋转90度
 * @param num
 */
const rotateRight = (num:number) => {
  num = num || 1
  state.cropper.rotate(num)
}
/**
 * 裁剪图片
 */
const cropImage = () => {
  if (state.cropper) {
    const canvas = state.cropper.getCroppedCanvas({
      width: props.cropperWidth,
      height: props.cropperHeight,
    });
    // base64 格式
    croppedImageSrc.value = canvas.toDataURL('image/png');
  }
};
/**
 * 上传图片
 */
const uploadCroppedImage = async () => {
  if (!croppedImageSrc.value) {
    ElMessage.error('请先裁剪图片');
    return;
  }
  try {
    const blob = dataURLToBlob(croppedImageSrc.value);
    const formData = new FormData();
    formData.append('file', blob, 'avatar.png'); // 注意这里的表单字段名应为'file'
    await uploadImg(formData).then(response => {
      if (response.code === 200) {
        _fileList.value.forEach((item, index) => {
          if (item.uid === uid.value) {
            _fileList.value[index].url = response.data.url
          }
        })
        const imgs = _fileList.value.map(obj => obj.url).join(',');
        emit('update:modelValue', imgs)
        ElMessage.success('上传成功');
        state.isShowDialog = false;
      } else {
        ElMessage.error(response.data.msg || '上传失败');
      }
    });
  } catch (error) {
    ElMessage.error('上传失败');
  }
};
const dataURLToBlob = (dataURL: any) => {
  const arr = dataURL.split(',');
  const mime = arr[0].match(/:(.*?);/)[1];
  const bstr = atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new Blob([u8arr], {type: mime});
};
</script>

<style scoped lang="scss">
.is-error {
  .upload {
    :deep(.el-upload--picture-card),
    :deep(.el-upload-dragger) {
      border: 1px dashed var(--el-color-danger) !important;
      &:hover {
        border-color: var(--el-color-primary) !important;
      }
    }
  }
}
:deep(.disabled) {
  .el-upload--picture-card,
  .el-upload-dragger {
    cursor: not-allowed;
    background: var(--el-disabled-bg-color) !important;
    border: 1px dashed var(--el-border-color-darker);
    &:hover {
      border-color: var(--el-border-color-darker) !important;
    }
  }
}
.upload-box {
  .no-border {
    :deep(.el-upload--picture-card) {
      border: none !important;
    }
  }
  :deep(.upload) {
    .el-upload-dragger {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      height: 100%;
      padding: 0;
      overflow: hidden;
      border: 1px dashed var(--el-border-color-darker);
      border-radius: v-bind(borderRadius);
      &:hover {
        border: 1px dashed var(--el-color-primary);
      }
    }
    .el-upload-dragger.is-dragover {
      background-color: var(--el-color-primary-light-9);
      border: 2px dashed var(--el-color-primary) !important;
    }
    .el-upload-list__item,
    .el-upload--picture-card {
      width: v-bind(width);
      height: v-bind(height);
      background-color: transparent;
      border-radius: v-bind(borderRadius);
    }
    .upload-image {
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
    .upload-handle {
      position: absolute;
      top: 0;
      right: 0;
      box-sizing: border-box;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      height: 100%;
      cursor: pointer;
      background: rgb(0 0 0 / 60%);
      opacity: 0;
      transition: var(--el-transition-duration-fast);
      .handle-icon {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 0 6%;
        color: aliceblue;
        .el-icon {
          margin-bottom: 15%;
          font-size: 140%;
        }
        span {
          font-size: 100%;
        }
      }
    }
    .el-upload-list__item {
      &:hover {
        .upload-handle {
          opacity: 1;
        }
      }
    }
    .upload-empty {
      display: flex;
      flex-direction: column;
      align-items: center;
      font-size: 12px;
      line-height: 30px;
      color: var(--el-color-info);
      .el-icon {
        font-size: 28px;
        color: var(--el-text-color-secondary);
      }
    }
  }
  .el-upload__tip {
    line-height: 15px;
    text-align: center;
  }
}
</style>
