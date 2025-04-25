<template>
    <div class="upload-box">
        <el-dialog :title="state.dialogTitle" :width="state.dialogWidth" v-model="state.isShowDialog" :before-close="closeDialog">
            <!-- 裁剪预览 -->
            <div v-if="imageSrc" style="display: flex; justify-content: space-between; align-items: center;">
                <div style="width: 50%; padding: 10px; background-color: #f5f5f5; display: flex; justify-content: center; align-items: center;">
                    <img ref="previewImage" :src="croppedImageSrc || imageSrc.toString()" alt="Preview Image" style="max-width: 100%; height: auto;"/>
                </div>
                <div style="width: 50%; padding: 10px; display: flex; justify-content: center; align-items: center;">
                    <img ref="uploadImage" :src="imageSrc.toString()" alt="Source Image" style="max-width: 100%; height: auto;" @click="clearImage"/>
                </div>
            </div>

            <!-- 裁剪工具栏 -->
            <div v-if="imageSrc" style="margin-top: 20px; text-align: center;">
                <el-button @click="clearImage">重新选择</el-button>
                <el-button type="primary" @click="rotateLeft(-5)">左转</el-button>
                <el-button type="primary" @click="rotateRight(5)">右转</el-button>
                <el-button type="primary" @click="changeScale(0.1)">放大</el-button>
                <el-button type="primary" @click="changeScale(-0.1)">缩小</el-button>
                <el-button type="warning" @click="cropImage">裁剪图片</el-button>
                <el-button type="success" @click="uploadCroppedImage">上传</el-button>
            </div>

            <!-- Conditional display for upload component -->
            <div v-if="!imageSrc">
                <el-upload
                    v-if="isCropper"
                    class="upload-demo"
                    drag
                    multiple
                    v-model:file-list="localImageUrl"
                    limit="1"
                    :show-file-list="false"
                    :before-upload="beforeUpload"
                >
                    <el-icon class="el-icon--upload">
                        <upload-filled/>
                    </el-icon>
                    <div class="el-upload__text">
                        文件拖到这里 或者 <em>点击图标选取上传</em>
                    </div>
                    <template #tip>
                        <div class="el-upload__tip">
                            不大于500kb的Jpg /png文件
                        </div>
                    </template>
                </el-upload>
                <el-upload
                    :key="uuid"
                    v-else
                    class="upload-demo"
                    action="#"
                    :http-request="handleHttpUpload"
                    :headers="httpFileHeaders"
                    :on-remove="handleRemove"
                    :before-remove="beforeRemove"
                >
                    <el-icon class="el-icon--upload">
                        <upload-filled/>
                    </el-icon>
                    <div class="el-upload__text">
                        文件拖到这里 或者 <em>点击图标选取上传</em>
                    </div>
                    <template #tip>
                        <div class="el-upload__tip">
                            不大于500kb的Jpg /png文件
                        </div>
                    </template>
                </el-upload>
            </div>

        </el-dialog>
        <!--  最后图片展示  -->
        <div  v-if="props.endShow">
            <div class="upload">
                <div class="el-upload">
                    <template v-if="localImageUrl">
                        <img :src="localImageUrl" class="upload-image" alt=""/>
                        <div class="upload-handle" @click.stop>
<!--                            <div v-if="!self_disabled" class="handle-icon" @click="isCropper?openCropper:editImg">-->
<!--                                <el-icon>-->
<!--                                    <Edit />-->
<!--                                </el-icon>-->
<!--                                <span>{{isCropper?"裁剪":"编辑"}}</span>-->
<!--                            </div>-->
                            <div class="handle-icon" @click="imgViewVisible = true">
                                <el-icon>
                                    <ZoomIn />
                                </el-icon>
                                <span>查看</span>
                            </div>
                            <div v-if="!self_disabled" class="handle-icon" @click="deleteImg">
                                <el-icon>
                                    <Delete />
                                </el-icon>
                                <span>删除</span>
                            </div>
                        </div>
                    </template>
                    <template v-else>
                        <div class="upload-empty" @click="openDialog">
                            <slot name="empty">
                                <el-icon>
                                    <Plus/>
                                </el-icon>
                                <span>请上传图片</span>
                            </slot>
                        </div>
                    </template>
                </div>
            </div>
            <el-image-viewer v-if="imgViewVisible" :url-list="[localImageUrl]" @close="imgViewVisible = false" />
        </div>
    </div>
</template>
<script setup lang="ts">
import {ref, nextTick, onBeforeUnmount, reactive, computed, inject, watch,onMounted} from 'vue';
import {
    ElMessage,
    formContextKey,
    UploadProps,
    UploadRequestOptions,
    formItemContextKey,
    UploadUserFile,
    ElMessageBox
} from 'element-plus';
import Cropper from 'cropperjs';
import 'cropperjs/dist/cropper.css';
import {Delete, Plus, UploadFilled, ZoomIn,Edit} from "@element-plus/icons-vue";
import {uploadImg} from '/@/api/system/upload'
import {generateUUID} from "/@/utils/mms";
import {RefType} from "/@/types/global";
import {Session} from "/@/utils/storage";
const httpFileHeaders = ref({
    'Authorization': `${Session.get('token')}`
});
// 向父组件传递消息，可以传递任意类型的数据，需要 @ 符号 监听捕获
const emit = defineEmits<{
    'update:modelValue': [value: any]
    'success': [value: any]
}>();
// 接受父组件参数
const props = defineProps({
    modelValue: {
        type: String,
        default: ''
    },
    // 是否可编辑/删除
    disabled: {
        type: Boolean,
        default: true,
    },
    // 是否显示最后图片
    endShow: {
        type: Boolean,
        default: true,
    },
    fileSize:{
        type: Number,
        default: 1
    },
    // 显示图片高度
    height: {
        type: String,
        default: '150px',
    },
    // 显示图片宽度
    width: {
        type: String,
        default: '150px',
    },
    // 显示图片圆角
    borderRadius: {
        type: String,
        default: '8px',
    },
    //是否裁剪
    isCropper:{
        type:Boolean,
        default:false
    },
    //裁剪宽度
    cropperWidth: {
        type: Number,
        default: 400,
    },
    //裁剪高度
    cropperHeight: {
        type: Number,
        default: 400,
    }
});
const imageSrc = ref<string | ArrayBuffer | null>(null);
const localImageUrl = ref(props.modelValue);
const croppedImageSrc = ref(null);
// 生成组件唯一id
const uuid = ref(generateUUID())
const uploadImage = ref(null);
const previewImage = ref(null);
// 查看图片
const imgViewVisible = ref(false);

// 在组件挂载后执行一些逻辑
onMounted(() => {
    localImageUrl.value = props.modelValue;
    // 可以在这里执行初始化逻辑，比如获取数据等
});
// 自定义值
const state = reactive({
    dialogWidth: '800px',
    dialogTitle:  props.isCropper?"图片裁剪上传":"图片上传",
    isShowDialog: false,
    cropperImg: '',
    cropperImgBase64: '',
    cropper: '' as RefType,
});

// 监听图片地址变化,改变 localImageUrl 的值
watch(() => props.modelValue, (newVal) => {
    localImageUrl.value = newVal;
});
// 获取 el-form 组件上下文
const formContext = inject(formContextKey, void 0)
// 判断是否禁用上传和删除
const self_disabled = computed(() => {
    return !props.disabled || formContext?.disabled
});
// 上传
const handleHttpUpload = async (options: UploadRequestOptions) => {
    let formData = new FormData()
    formData.append('file', options.file)
    try {
        await uploadImg(formData).then(res=>{

            emit('update:modelValue', res.data.url)
            emit('success', res.data)
            state.isShowDialog = false;
            // 调用 el-form 内部的校验方法（可自动校验）
            ///formItemContext?.prop && formContext?.validateField([formItemContext.prop as string])

        })
    } catch (error) {
        options.onError(error as any)
    }
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
//删除
const handleRemove: UploadProps['onRemove'] = (file, uploadFiles) => {
    emit('update:modelValue', '')
}

// ======================================裁剪===========================================

/**
 * 裁剪选择图片后
 * @param file
 */
const beforeUpload: UploadProps['beforeUpload'] = file => {
    const isImage = file.type.startsWith('image/');
    if (!isImage) {
        ElMessage.error('只能上传图片文件');
        return false;
    }
    const reader = new FileReader();
    reader.onload = async (e) => {
        imageSrc.value = e.target ? e.target.result : null;
        await nextTick();
        initCropper();
    };
    reader.readAsDataURL(file);
    return false;
};
/**
 * 裁剪初始化
 */
const initCropper = () => {
    if (state.cropper) {
        state.cropper.destroy();
    }
    if (!uploadImage.value) return;
    state.dialogWidth="60%";
    state.dialogTitle="图片裁剪 ( "+props.cropperWidth+" x "+props.cropperHeight+" )";
    state.cropper = new Cropper(uploadImage.value, {
        aspectRatio: props.cropperWidth / props.cropperHeight, // 设置裁剪框的宽高比
        viewMode: 1, // 限制裁剪框不超过画布大小
    });
};
// 修改图片大小 正数为变大 负数变小
const changeScale = (num:number) => {
    num = num || 1
    state.cropper.zoom(num)
}
// 向左边旋转90度
const rotateLeft = (num:number) => {
    num = num || 1
    state.cropper.rotate(num)
}
// 向右边旋转90度
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
 * 删除
 */
const clearImage = () => {
    debugger
    state.isShowDialog = true;
    imageSrc.value = null;
    croppedImageSrc.value = null;
    state.dialogWidth="600px";
    if (state.cropper) {
        state.cropper.destroy();
        state.cropper = null;
    }
    uuid.value= generateUUID()
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
                emit('update:modelValue',response.data.url)
                ElMessage.success('上传成功');
                state.isShowDialog = false;
                if (props.endShow) {
                    localImageUrl.value =response.data.url;
                }
                emit('success', localImageUrl.value)
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

onBeforeUnmount(() => {
    if (state.cropper) {
        state.cropper.destroy();
    }
});
/**
 * 预览 编辑图片
 * */
const editImg = () => {
    state.dialogWidth="600px";
    state.isShowDialog = true;
    imageSrc.value = null;
}
/**
 * 预览 删除图片
 * */
const deleteImg = () => {
    imageSrc.value = null;
    croppedImageSrc.value = null;
    localImageUrl.value='';
    state.dialogWidth="600px";
    emit('update:modelValue',localImageUrl.value)
    if(!props.isCropper){
        uuid.value= generateUUID()
    }
}
/**
 * 预览 裁剪
 */
const openCropper = async () => {
    imageSrc.value = localImageUrl.value;
    state.dialogWidth = "60%";
    state.isShowDialog = true;
    await nextTick();
    initCropper();
}
/**
 * 显示裁剪
 */
const openDialog = () => {
    imageSrc.value = null;
    croppedImageSrc.value = null;
    localImageUrl.value='';
    state.dialogWidth="600px";
    state.isShowDialog = true;
}
/**
 * 关闭裁剪
 */
const closeDialog = () => {
    state.isShowDialog = false;
}

// 暴露给父组件的方法 可以直接 ref 方式绑定组件 调佣方法
defineExpose({
    openDialog,
});

</script>
<style scoped lang="scss">
.upload-demo{
    display: block;
    width: 100%;
    margin-bottom: 20px;
}
.upload-demo .el-upload {
    display: block;
    width: 100%;
    margin-bottom: 20px;
}
.el-upload__text {
    color: #606266;
    font-size: 14px;
    line-height: 22px;
    margin-top: 5px;
}

.el-upload__tip {
    color: #909399;
    font-size: 12px;
    line-height: 1.5;
    margin-top: 7px;
}

.is-error {
    .upload {
        :deep(.el-upload),
        :deep(.el-upload-dragger) {
            border: 1px dashed var(--el-color-danger) !important;

            &:hover {
                border-color: var(--el-color-primary) !important;
            }
        }
    }
}

:deep(.disabled) {
    .el-upload,
    .el-upload-dragger {
        cursor: not-allowed !important;
        background: var(--el-disabled-bg-color);
        border: 1px dashed var(--el-border-color-darker) !important;

        &:hover {
            border: 1px dashed var(--el-border-color-darker) !important;
        }
    }
}

.upload-box {
    .no-border {
        :deep(.el-upload) {
            border: none !important;
        }
    }
    .upload{
        margin: 10px 10px 10px 0;
    }
    :deep(.upload) {
        .el-upload {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            width: v-bind(width);
            height: v-bind(height);
            overflow: hidden;
            border: 1px dashed var(--el-border-color-darker);
            border-radius: v-bind(borderRadius);
            transition: var(--el-transition-duration-fast);

            &:hover {
                border-color: var(--el-color-primary);

                .upload-handle {
                    opacity: 1;
                }
            }

            .el-upload-dragger {
                display: flex;
                align-items: center;
                justify-content: center;
                width: 100%;
                height: 100%;
                padding: 0;
                overflow: hidden;
                background-color: transparent;
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

            .upload-image {
                width: 100%;
                height: 100%;
                object-fit: contain;
            }

            .upload-empty {
                position: relative;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                font-size: 12px;
                line-height: 30px;
                color: var(--el-color-info);

                .el-icon {
                    font-size: 28px;
                    color: var(--el-text-color-secondary);
                }
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
                        margin-bottom: 40%;
                        font-size: 130%;
                        line-height: 130%;
                    }

                    span {
                        font-size: 85%;
                        line-height: 85%;
                    }
                }
            }
        }
    }

    .el-upload__tip {
        line-height: 18px;
        text-align: center;
    }
}
</style>
