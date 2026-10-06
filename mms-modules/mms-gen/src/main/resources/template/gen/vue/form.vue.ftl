<template>
            <el-form ref="dialogFormRef" :model="state.ruleForm" size="default" label-width="110px" :rules="rules">
                <el-row>
                    <#list formList as field>
                    <#if field.attrName != tableParentId || formLayout==2>
                    <#if field.primaryPk>
                    <el-col v-show="false" class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item v-show="false" label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input v-model="state.ruleForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                        </el-form-item>
                    <#elseif field.formType == 'textarea'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <el-input type="textarea" v-model="state.ruleForm.${field.attrName}"></el-input>
                    </el-form-item>
                    <#elseif field.formType == 'editor'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <div class="editor-container">
                            <fast-editor v-model:get-html="state.ruleForm.${field.attrName}" v-bind:content="state.ruleForm.${field.attrName}"/>
                        </div>
                    </el-form-item>
                    <#elseif field.formType == 'radio'>
                        <#if field.attrName == 'status'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                    <el-form-item label="${field.fieldComment!'字典状态'}" prop="${field.attrName}">
                        <fast-switch v-model="state.ruleForm.status" dict-type="SYS_STATE" placeholder="字典状态"></fast-switch>
                    </el-form-item>
                        <#elseif field.formDict??>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <fast-select v-model="state.ruleForm.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}"></fast-select>
                    </el-form-item>
                        <#else>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <el-input v-model="state.ruleForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                    </el-form-item>
                        </#if>
                    <#elseif field.formType == 'select'>
                        <#if field.formDict??>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                        <fast-select v-model="state.ruleForm.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}"></fast-select>
                    </el-form-item>
                        <#else>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                    <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                    <el-select v-model="state.ruleForm.${field.attrName}" placeholder="请选择">
                        <el-option label="请选择" value="0"></el-option>
                    </el-select>
                    </el-form-item>
                        </#if>
                    <#elseif field.formType == 'checkbox'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-select type="checkbox" v-model="state.ruleForm.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}"></fast-select>
                        </el-form-item>
                    <#elseif field.formType == 'date'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-date-picker type="date" value-format="YYYY-MM-DD" placeholder="${field.fieldComment!}" v-model="state.ruleForm.${field.attrName}"></el-date-picker>
                        </el-form-item>
                    <#elseif field.formType == 'datetime'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-date-picker type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="${field.fieldComment!}" v-model="state.ruleForm.${field.attrName}"></el-date-picker>
                        </el-form-item>
                    <#elseif field.formType == 'file'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-file v-model="state.ruleForm.${field.attrName}" :fileUrl="state.ruleForm.${field.attrName}"  />
                        </el-form-item>
                    <#elseif field.formType == 'image'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-img v-model="state.ruleForm.${field.attrName}" :fileUrl="state.ruleForm.${field.attrName}" />
                        </el-form-item>
                    <#elseif field.formType == 'images'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <fast-imgs v-model="state.ruleForm.${field.attrName}" :fileUrl="state.ruleForm.${field.attrName}" />
                        </el-form-item>
                    <#elseif field.attrName =='${tableParentId}'>
                        <#if formLayout==2 >
                    <el-col class="mt-15" :xs="24" :sm="${span}">
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
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input-number v-model="state.ruleForm.sort" :min="1" label="排序"></el-input-number>
                        </el-form-item>
                    <#elseif field.attrName == 'remark'>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input v-model="state.ruleForm.${field.attrName}" type="textarea" placeholder="${field.fieldComment!}"></el-input>
                        </el-form-item>
                    <#else>
                    <el-col class="mt-15" :xs="24" :sm="${span}">
                        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
                            <el-input v-model="state.ruleForm.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                        </el-form-item>
                    </#if>
                    </el-col>
                    </#if>
                    </#list>
                </el-row>
            </el-form>

</template>
<script setup lang="ts">
    import { computed, ref } from 'vue';
    import type { FormRules } from 'element-plus';
    import type { ${FunctionName}Bo, ${FunctionName}Vo } from '/@/views/${moduleName}/${functionName}/type';
    <#list fastList as field>
    <#if field == 'editor'>
    import FastEditor from '/@/components/fast-editor/src/fast-editor.vue';
    <#elseif field == 'select'>
    import FastSelect from '/@/components/fast-select/src/fast-select.vue';
    <#elseif field == 'file'>
    import FastFile from "/@/components/fast-upload/file.vue"
    <#elseif field == 'image'>
    import FastImg from "/@/components/fast-upload/img.vue"
    <#elseif field == 'images'>
    import FastImgs from "/@/components/fast-upload/imgs.vue"
    <#elseif field == 'radio'>
    import FastSwitch from "/@/components/fast-switch/src/fast-switch.vue";
    </#if>
    </#list>




    const props = defineProps<{ modelValue: ${FunctionName}Bo; treeData?: ${FunctionName}Vo[] }>();
    const state = computed(() => ({ ruleForm: props.modelValue, threeData: props.treeData ?? [] }));
    const dialogFormRef = ref();
    const rules: FormRules = {
    <#list formList as field>
      <#if field.formRequired && !field.primaryPk>
        ${field.attrName}: [{ required: true, message: '${field.fieldComment!}不能为空', trigger: 'change' }],
      </#if>
    </#list>
    };
    <#if formLayout==2>
    const change = (arr: string[]) => {
      state.value.ruleForm.${tableParentId} = arr.length ? arr[arr.length - 1] : '0';
    };
    </#if>
    const validate = () => dialogFormRef.value.validate().catch(() => false);
    const clearValidate = () => dialogFormRef.value?.clearValidate();
    defineExpose({ validate, clearValidate });
</script>
<style scoped lang="scss">
  @media (max-width: 600px) {
    :deep(.el-form-item) { flex-direction: column; }
    :deep(.el-form-item__label) { width: auto !important; justify-content: flex-start; }
    :deep(.el-form-item__content) { margin-left: 0 !important; width: 100%; min-width: 0; }
  }
</style>
