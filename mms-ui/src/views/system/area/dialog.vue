<template>
    <div class="system-sysArea-dialog-container">
        <el-dialog
            :title="state.dialog.title"
            v-model="state.dialog.isShowDialog"
            :width="dialogWidth" draggable>
            <el-form ref="dialogFormRef" :model="state.ruleForm" size="default" label-width="100px">
                <el-row>
                    <el-col v-show="false" class="mt-5" :span="24">
                        <el-form-item v-show="false" label="ID" prop="id">
                            <el-input v-model="state.ruleForm.id" placeholder="ID"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="名称" prop="name">
                            <el-input v-model="state.ruleForm.name" placeholder="名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="CODE" prop="code">
                            <el-input v-model="state.ruleForm.code" placeholder="CODE"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="选择上级">
                                <el-cascader :options="state.threeData"
                                             :props="{ checkStrictly: true, value: 'id', label: 'name' }" placeholder="请选择"
                                             @change="change" clearable class="w100" v-model="state.ruleForm.ids">
                                    <template #default="{ node, data }">
                                        <span>{{ data.name }}</span>
                                        <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
                                    </template>
                                </el-cascader>
                            </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                        <el-form-item label="级别" prop="level">
                            <el-input v-model="state.ruleForm.level" placeholder="级别"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="24">
                    <el-form-item label="状态" prop="status">
                        <fast-select v-model="state.ruleForm.status" dict-type="SYS_STATE" placeholder="状态"></fast-select>
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
                <el-button @click="onCancel" size="default">取 消</el-button>
                <el-button type="primary" @click="onSubmit" size="default">{{ state.dialog.submitTxt }}</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>
//ModuleName 系统区域
<script setup lang="ts" name="systemSysAreaDialog">
    import { reactive, ref, nextTick } from "vue";
    import { ElMessage } from "element-plus";
    import { CURDEnum } from '/@/enums/CURDEnum';
    import {SysAreaBo,SysAreaVo,SysAreaTable } from '/@/api/system/sysArea/type';
    const dialogWidth = ref('50vw');
    import FastSelect from "/@/components/fast-select/src/fast-select.vue";
    import {sysAreaApi} from '/@/api/system/sysArea';
    const baseApi = sysAreaApi();
    // 定义子组件向父组件传值/事件
    const emit = defineEmits(['refresh']);
    const dialogFormRef = ref();
    const state = reactive({
        ruleForm: {} as SysAreaBo ,
        threeData: [] as SysAreaVo[] ,
        dialog: {
            isShowDialog: false,
            type: "",
            title: "",
            submitTxt: "",
        },
    });

    // 重置
    const resetForm = () => {
        state.ruleForm = {
            id: 0,
            name: '',
            code: '',
            parentCode: '',
            level: '',
            status: 0,
            sort: 1,
            revision: '',
            tenantId: '',
            createdBy: '',
            createdTime: '',
            updatedBy: '',
            updatedTime: '',
            remark: ''
        }as any;
    }
    const openDialog = (type: string, row: SysAreaVo) => {
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
            state.ruleForm.parentId=row.id;
            state.ruleForm.ids=[...row.ids];
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
    // 取消
    const onCancel = () => {
        closeDialog();
    };
    // 提交
    const onSubmit = () => {
        emit('refresh', state.ruleForm);
    };
    // 初始化菜单数据
    const getMenuData = () => {
        baseApi.list({isAll:true}).then(res => {
            state.threeData = res.data;
        }).catch(async err => { ElMessage.warning(err); }).finally(() => { })

    }
    const change = (arr: number[]) => {
        state.ruleForm.parentCode = arr[arr.length - 1];
    };

    // 暴露变量
    defineExpose({
        openDialog, closeDialog
    });
</script>
