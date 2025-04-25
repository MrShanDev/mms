<template>
    <div class="system-dept-dialog-container">
        <el-dialog
            :title="state.dialog.title"
            v-model="state.dialog.isShowDialog"
            :width="dialogWidth">
            <el-form ref="dialogFormRef" :model="state.ruleForm" size="default" label-width="100px">
                <el-row>
                    <el-col v-show="false" class="mt-5" :span="12">
                        <el-form-item v-show="false" label="部门编号" prop="deptId">
                            <el-input v-model="state.ruleForm.deptId" placeholder="部门编号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="12">
                        <el-form-item label="选择上级">
                                <el-cascader :options="state.threeData"
                                             :props="{ checkStrictly: true, value: 'deptId', label: 'deptName' }" placeholder="请选择"
                                             @change="change" clearable class="w100" v-model="state.ruleForm.deptIds">
                                    <template #default="{ node, data }">
                                        <span>{{ data.deptName }}</span>
                                        <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
                                    </template>
                                </el-cascader>
                            </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="12">
                        <el-form-item label="部门名称" prop="deptName">
                            <el-input v-model="state.ruleForm.deptName" placeholder="部门名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="12">
                        <el-form-item label="负责人" prop="leader">
                            <el-input v-model="state.ruleForm.leader" placeholder="负责人"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="12">
                        <el-form-item label="联系方式" prop="phone">
                            <el-input v-model="state.ruleForm.phone" placeholder="联系方式"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="12">
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="state.ruleForm.email" placeholder="邮箱"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="12">
                    <el-form-item label="状态" prop="status">
                        <fast-switch v-model="state.ruleForm.status" dict-type="SYS_STATE" placeholder="状态"></fast-switch>
                    </el-form-item>
                    </el-col>
                    <el-col class="mt-5" :span="12">
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
//ModuleName 系统部门
<script setup lang="ts" name="systemDeptDialog">
    import { reactive, ref, nextTick } from "vue";
    import { ElMessage } from "element-plus";
    import { CURDEnum } from '/@/enums/CURDEnum';
    const dialogWidth = ref('50vw');
    import FastSwitch from "/@/components/fast-switch/src/fast-switch.vue";
    import {deptApi} from '/@/api/system/dept';
    import {Eleme} from "@element-plus/icons-vue";
    import {DeptEntity} from "/@/api/system/dept/type";
    const baseApi = deptApi();
    // 定义子组件向父组件传值/事件
    const emit = defineEmits(['refresh']);
    const dialogFormRef = ref();
    const state = reactive({
        ruleForm: {} as DeptEntity ,
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
            deptId: '',
            parentId: '',
            deptName: '',
            leader: '',
            phone: '',
            email: '',
            status: 0,
            address: '',
            sort: 1,
            remark: '',
            tenantId: '',
            revision: 0,
            createdBy: '',
            createdTime: '',
            updatedBy: '',
            updatedTime: '',
            deptIds:[],
            children:[],
            hasChildren:false,
        }
    }
    const openDialog = (type: string, row: DeptEntity) => {
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
            state.ruleForm.parentId=row.deptId;
            state.ruleForm.deptIds=[...row.deptIds];
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
        baseApi.list({isAll:true}).then(res => {
            state.threeData = res.data;
        }).catch(async err => { ElMessage.warning(err); }).finally(() => { })
    }
    const change = (arr: number[]) => {
        state.ruleForm.parentId = arr[arr.length - 1];
    };

    // 暴露变量
    defineExpose({
        openDialog, closeDialog,resetLoading
    });
</script>
