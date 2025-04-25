<template>
    <div class="system-notice-dialog-container">
        <el-dialog
            :title="state.dialog.title"
            v-model="state.dialog.isShowDialog"
            :width="dialogWidth">
            <el-form ref="dialogFormRef" :model="state.ruleForm" size="default" label-width="100px">
                <el-row>
                    <el-col v-show="false" class="mt-5" :span="24">
                        <el-form-item v-show="false" label="公告ID" prop="id">
                            <el-input v-model="state.ruleForm.id" placeholder="公告ID"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="公告标题" prop="title">
                            <el-input v-model="state.ruleForm.title" placeholder="公告标题"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="公告内容" prop="content">
                        <div class="editor-container">
                            <fast-editor v-model:get-html="state.ruleForm.content" v-bind:content="state.ruleForm.content"/>
                        </div>
                    </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                    <el-form-item label="公告类型" prop="type">
                        <fast-select v-model="state.ruleForm.type" dict-type="NITICE_TYPE" placeholder="公告类型"></fast-select>
                    </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                    <el-form-item label="公告状态" prop="status">
                        <fast-switch v-model="state.ruleForm.status" dict-type="SYS_STATE" placeholder="状态"></fast-switch>
                    </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="备注" prop="remark">
                            <fast-file v-model="state.ruleForm.remark"  />
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="排序" prop="sort">
                            <el-input-number v-model="state.ruleForm.sort" :min="1" label="排序"></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                <el-button @click="closeDialog" size="default">取 消</el-button>
                <el-button type="primary"
                           :disabled="state.dialog.loading"
                           :loading-icon="Eleme"
                           :loading="state.dialog.loading"
                           @click="onSubmit"
                           size="default"
                >{{ state.dialog.submitTxt }}</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>
//ModuleName 系统公告
<script setup lang="ts" name="systemNoticeDialog">
    import { reactive, ref, nextTick ,onMounted} from "vue";
    import { ElMessage } from "element-plus";
    import { CURDEnum } from '/@/enums/CURDEnum';
    const dialogWidth = ref('50vw');
    dialogWidth.value = '75vw';
    import FastEditor from "/@/components/fast-editor/src/fast-editor.vue";
    import FastSelect from "/@/components/fast-select/src/fast-select.vue";
    import FastFile from "/@/components/fast-upload/file.vue";
    import FastSwitch from "/@/components/fast-switch/src/fast-switch.vue";
    import {NoticeEntity} from "/@/api/system/notice/type";
    import {DeptEntity} from "/@/api/system/dept/type";
    import {Eleme} from "@element-plus/icons-vue";
    // 定义子组件向父组件传值/事件
    const emit = defineEmits(['refresh']);
    const dialogFormRef = ref();
    const state = reactive({
        ruleForm: {} as NoticeEntity ,
        threeData: [] as DeptEntity[] ,
        dialog: {
            loading:false,
            isShowDialog: false,
            type: "",
            title: "",
            submitTxt: "",
        },
    });

    // 重置
    const resetForm = () => {
        state.dialog.loading = false;
        state.ruleForm = {
            id: '',
            title: '',
            content: '',
            type: 0,
            status: 0,
            remark: '',
            tenantId: '',
            revision: 0,
            createdBy: '',
            createdTime: '',
            updatedBy: '',
            updatedTime: '',
            sort: 1        };
    }
    const openDialog = (type: string, row: NoticeEntity) => {
        resetForm();
        if (type === CURDEnum.EDIT) {
            state.ruleForm = row;
            state.dialog.title = '修改';
            state.dialog.submitTxt = '修 改';
            state.dialog.type = CURDEnum.EDIT;
        }
        if (type === CURDEnum.INSERT) {
            state.dialog.title = '新增';
            state.dialog.submitTxt = '新 增';
            state.dialog.type = CURDEnum.INSERT;
            // 清空表单，此项需加表单验证才能使用
            nextTick(() => {
                dialogFormRef.value.resetFields();
            });
        }
        getMenuData();
        state.dialog.isShowDialog = true;
    };
    // 关闭弹窗
    const closeDialog = () => {
        state.dialog.isShowDialog = false;
    };
    const resetLoading = () => {
        state.dialog.loading = false;
    };
    // 提交
    const onSubmit = () => {
        state.dialog.loading = true;
        emit('refresh', state.ruleForm);
    };
    // 初始化部门数据
    const getMenuData = () => {

    }
    // 暴露变量
    defineExpose({
        openDialog, closeDialog,resetLoading
    });
</script>
