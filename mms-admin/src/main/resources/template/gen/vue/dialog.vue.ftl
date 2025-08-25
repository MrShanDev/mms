<template>
    <div class="${moduleName}-${functionName}-dialog-container">
        <el-dialog
            :title="state.dialog.title"
            v-model="state.dialog.isShowDialog"
            :width="dialogWidth" draggable>
            <el-form ref="dialogFormRef" :model="state.ruleForm" size="default" label-width="100px">
                <el-row>
                    <#list formList as field>
                    <#if field.primaryPk>
                    <el-col v-show="false" class="mt-5" :span="${span}">
                        <el-form-item v-show="false" label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input v-model="state.ruleForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                        </el-form-item>
                    <#elseif field.formType == 'textarea'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <el-input type="textarea" v-model="state.ruleForm.${field.attrName}"></el-input>
                    </el-form-item>
                    <#elseif field.formType == 'editor'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <div class="editor-container">
                            <fast-editor v-model:get-html="state.ruleForm.${field.attrName}" v-bind:content="state.ruleForm.${field.attrName}"/>
                        </div>
                    </el-form-item>
                    <#elseif field.formType == 'radio'>
                        <#if field.formDict??>
                            <#if field.attrName == 'status'>
                    <el-col class="mt-5" :span="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <fast-switch v-model="state.ruleForm.status" dict-type="SYS_STATE" placeholder="状态"></fast-switch>
                    </el-form-item>
                            <#else>
                    <el-col class="mt-5" :span="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <el-radio-group v-model="state.ruleForm.${field.attrName}">
                            <el-radio :label="0">启用</el-radio>
                            <el-radio :label="1">禁用</el-radio>
                        </el-radio-group>
                    </el-form-item>
                            </#if>
                        <#else>
                    <el-col class="mt-5" :span="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <el-input v-model="state.ruleForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                    </el-form-item>
                        </#if>
                    <#elseif field.formType == 'select'>
                        <#if field.formDict??>
                    <el-col class="mt-5" :span="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <fast-select v-model="state.ruleForm.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}"></fast-select>
                    </el-form-item>
                        <#else>
                    <el-col class="mt-5" :span="${span}">
                    <el-select v-model="state.ruleForm.${field.attrName}" placeholder="请选择">
                        <el-option label="请选择" value="0"></el-option>
                    </el-select>
                        </#if>
                    <#elseif field.formType == 'checkbox'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-select type="checkbox" v-model="state.ruleForm.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}"></fast-select>
                        </el-form-item>
                    <#elseif field.formType == 'date'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-date-picker type="date" value-format="YYYY-MM-DD" placeholder="${field.fieldComment!}" v-model="state.ruleForm.${field.attrName}"></el-date-picker>
                        </el-form-item>
                    <#elseif field.formType == 'datetime'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-date-picker type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="${field.fieldComment!}" v-model="state.ruleForm.${field.attrName}"></el-date-picker>
                        </el-form-item>
                    <#elseif field.formType == 'file'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-file v-model="state.ruleForm.${field.attrName}" :fileUrl="state.ruleForm.${field.attrName}"  />
                        </el-form-item>
                    <#elseif field.formType == 'image'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-img v-model="state.ruleForm.${field.attrName}" :fileUrl="state.ruleForm.${field.attrName}" />
                        </el-form-item>
                    <#elseif field.formType == 'images'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-imgs v-model="state.ruleForm.${field.attrName}" :fileUrl="state.ruleForm.${field.attrName}" />
                        </el-form-item>
                    <#elseif field.attrName =='${tableParentId}'>
                        <#if formLayout==2 >
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="选择上级">
                                <el-cascader :options="state.threeData"
                                             :props="{ checkStrictly: true, value: '${tableId}', label: '${tableLabel}' }" placeholder="请选择"
                                             @change="change" clearable class="w100" v-model="state.ruleForm.${tableId}s">
                                    <template #default="{ node, data }">
                                        <span>{{ data.${tableLabel} }}</span>
                                        <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
                                    </template>
                                </el-cascader>
                            </el-form-item>
                        </#if>
                    <#elseif field.attrName == 'sort'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input-number v-model="state.ruleForm.sort" :min="1" label="排序"></el-input-number>
                        </el-form-item>
                    <#elseif field.attrName == 'remark'>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input v-model="state.ruleForm.${field.attrName}" type="textarea" placeholder="${field.fieldComment!}"></el-input>
                        </el-form-item>
                    <#else>
                    <el-col class="mt-5" :span="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input v-model="state.ruleForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                        </el-form-item>
                    </#if>
                    </el-col>
                    </#list>
                </el-row>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                <el-button @click="closeDialog" size="default">取 消</el-button>
                <el-button type="primary" @click="onSubmit" size="default">{{ state.dialog.submitTxt }}</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>
//ModuleName ${tableComment}
<script setup lang="ts" name="${moduleName}${FunctionName}Dialog">
    import {nextTick, reactive, ref} from "vue";
    import {CURDEnum} from '/@/enums/CURDEnum';
    import {${FunctionName}Bo, ${FunctionName}Vo} from '/@/views/';

    const dialogWidth = ref('50vw');
    <#list fastList as field>
    <#if field == 'editor'>
    dialogWidth.value = '75vw';
    <#elseif field == 'select'>
    <#elseif field == 'file'>
    <#elseif field == 'image'>
    <#elseif field == 'images'>
    <#elseif field == 'radio'>
    </#if>
    </#list>
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
                <#if field.attrName =='sort'>
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
    const openDialog = (type: string, row: ${FunctionName}Vo) => {
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
            <#if formLayout==2 >
            state.ruleForm.parentId=row.${tableId};
            state.ruleForm.${tableId}s=[...row.${tableId}s];
            </#if>
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
        state.dialog.loading = false;
        state.dialog.isShowDialog = false;
    };
    // 重置Loading
    const resetLoading = () => {
        state.dialog.loading = false;
    };
    // 提交
    const onSubmit = () => {
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
    const change = (arr: number[]) => {
        state.ruleForm.${tableParentId} = arr[arr.length - 1];
    };
    </#if>
    // 暴露变量
    defineExpose({
        openDialog, closeDialog, resetLoading
    });
</script>
