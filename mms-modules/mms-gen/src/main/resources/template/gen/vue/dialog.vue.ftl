<template>
    <div class="${moduleName}-${functionName}-dialog-container">
        <el-dialog
            :title="state.dialog.title"
            v-model="state.dialog.isShowDialog"
            :width="dialogWidth" draggable destroy-on-close>
            <${FunctionName}Form ref="dialogFormRef" :model-value="state.ruleForm" :tree-data="state.threeData" />
            <template #footer>
                <span class="dialog-footer">
                <el-button @click="closeDialog" size="default">取 消</el-button>
                <el-button type="primary" @click="onSubmit" :loading="state.dialog.loading" size="default">{{ state.dialog.submitTxt }}</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>
//ModuleName ${tableComment}
<script setup lang="ts" name="${moduleName}${FunctionName}Dialog">
    import {nextTick, reactive, ref} from "vue";
    import {CURDEnum} from '/@/enums/CURDEnum';
    import {ElMessage} from "element-plus";
    import {${FunctionName}Bo, ${FunctionName}Vo} from '/@/views/${moduleName}/${functionName}/type';
    <#if formLayout==2 >
    import {${functionName}Api} from '/@/views/${moduleName}/${functionName}';
    const baseApi = ${functionName}Api();
    </#if>

    const dialogWidth = ref('min(760px, 94vw)');
    import ${FunctionName}Form from './${FunctionName}Form.vue';
    // 定义子组件向父组件传值/事件
    const emit = defineEmits(['refresh']);
    const dialogFormRef = ref();
    const state = reactive({
        ruleForm: {} as ${FunctionName}Bo ,
        threeData: [] as ${FunctionName}Vo[] ,
        dialog: {
            loading: false,
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
        <#list fieldList as field>
            <#if !field.baseField||field.attrName =='status'||field.attrName =='sort'||field.attrName =='remark'>
            <#if field.fieldType == 'int'>
                <#if field.attrName =='status'>
            ${field.attrName}: 1<#sep>,
                <#elseif field.attrName =='sort'>
            ${field.attrName}: 1<#sep>,
                <#else>
            ${field.attrName}: 0<#sep>,
                </#if>
            <#else>
            ${field.attrName}: ''<#sep>,</#sep>
            </#if>
            </#if>
        </#list>
        }as ${FunctionName}Bo;
    }
    // 打开弹窗
    const openDialog = (type: string, row?: ${FunctionName}Vo) => {
        resetForm();
        if (type === CURDEnum.EDIT && row) {
            state.ruleForm = { ...row };
            state.dialog.title = '修改';
            state.dialog.submitTxt = '修 改';
            state.dialog.type = CURDEnum.EDIT;
        }
        if (type === CURDEnum.INSERT) {
            state.dialog.title = '新增';
            state.dialog.submitTxt = '新 增';
            state.dialog.type = CURDEnum.INSERT;
            <#if formLayout==2 >
            state.ruleForm.${tableParentId} = row ? String(row.${tableId}) : '0';
            state.ruleForm.${tableId}s = row ? [...(row.${tableId}s ?? []), String(row.${tableId})] : [];
            </#if>
            // 清空表单，此项需加表单验证才能使用
            nextTick(() => {
                dialogFormRef.value?.clearValidate();
            });
        }
        getMenuData();
        state.dialog.isShowDialog = true;
    };
    // 关闭弹窗
    const closeDialog = () => {
        state.dialog.loading = false;
        state.dialog.isShowDialog = false;
    };
    // 重置Loading
    const resetLoading = () => {
        state.dialog.loading = false;
    };
    // 提交
    const onSubmit = async () => {
        if (state.dialog.loading || !await dialogFormRef.value.validate()) return;
        state.dialog.loading = true;
        emit('refresh', state.ruleForm);
    };
    // 初始化菜单数据
    const getMenuData = () => {
        <#if formLayout==2 >
        baseApi.list({isAll:true}).then(res => {
            state.threeData = res.data;
        }).catch(async err => { ElMessage.warning(err); }).finally(() => { })
        </#if>
    }
    <#if formLayout==2 >
    // 选择监听
    const change = (arr: string[]) => {
        state.ruleForm.${tableParentId} = arr.length > 0 ? arr[arr.length - 1] : undefined;
    };
    </#if>
    // 暴露变量
    defineExpose({
        openDialog, closeDialog, resetLoading
    });
</script>
