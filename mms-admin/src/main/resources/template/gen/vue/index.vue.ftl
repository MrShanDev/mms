<template>
    <div class="block">
        <#if (formLayout == 1) && (queryList?? && queryList?size gt 0) >
        <!-- 功能栏  -->
        <div class="views-tool">
            <div class="tool-left">
                <el-form :inline="true" size="default" :model="state.tableData.param" class="form-tool" @keyup.enter="getTableData">
                    <#list queryList as field>
                        <el-form-item>
                            <#if field.queryFormType == 'text' || field.queryFormType == 'textarea' || field.queryFormType == 'editor'>
                            <el-input v-model="state.tableData.param.${field.attrName}" size="default" placeholder="${field.fieldComment!}" style="max-width: 180px" clearable ></el-input>
                            <#elseif field.queryFormType == 'select'>
                                <#if field.formDict??>
                            <fast-select v-model="state.tableData.param.${field.attrName}" dict-type="${field.formDict}" placeholder="${field.fieldComment!}" clearable></fast-select>
                                <#else>
                            <el-select v-model="state.tableData.param.${field.attrName}" placeholder="${field.fieldComment!}">
                                <el-option label="选择" value="0"></el-option>
                            </el-select>
                                </#if>
                            <#elseif field.queryFormType == 'radio'>
                                <#if field.formDict??>
                            <fast-radio-group v-model="state.tableData.param.${field.attrName}" dict-type="${field.formDict}"></fast-radio-group>
                                <#else>
                            <el-radio-group v-model="state.tableData.param.${field.attrName}">
                                <el-radio :label="0">单选</el-radio>
                            </el-radio-group>
                                </#if>
                            <#elseif field.queryFormType == 'date'>
                            <el-date-picker v-model="state.tableData.param.${field.attrName}" type="date" value-format="YYYY-MM-DD"></el-date-picker>
                            <#elseif field.queryFormType == 'datetime'>
                            <el-date-picker v-model="state.tableData.param.${field.attrName}" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"></el-date-picker>
                            <#else>
                            <el-input v-model="state.tableData.param.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
                            </#if>
                        </el-form-item>
                    </#list>
                    <el-form-item>
                        <el-button size="default" type="primary" @click="getTableData" v-auth="'${moduleName}:${functionName}:list'">
                            <SvgIcon name="iconfont icon-search1" />{{ $t("message.form.search") }}
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        </#if>
        <!-- Table  -->
        <div class="${moduleName}-${functionName}-container layout-padding <#if (formLayout == 1) && (queryList?? && queryList?size gt 0) > m-t-0 <#else> mt-5 </#if> p-t-0">
            <el-card shadow="hover" class="layout-padding-auto">
                <el-container>
                    <el-header>
                        <#if formLayout==1 >
                        <!-- 新增/导入/导出/打印 -->
                        <TableTool ref="tableToolRef"
                                   tableComment="${tableComment}"
                                   functionName="${functionName}"
                                   modelName="${moduleName}"
                                   :key="componentKey"
                                   :param="state.tableData.param"
                                   @close="componentKey = generateUUID()"
                                   @insert="onCURD" @deletes="onCURD" />
                        <#elseif formLayout==2 >
                        <!-- 功能栏 -->
                        <div class="system-dept-search">
                            <el-button size="small" type="primary"  @click="clickExpand">
                                <el-icon><ele-Sort /></el-icon>
                                {{ expand.state ? "全部关闭" : "全部展开" }}
                            </el-button>
                            <el-button
                                size="small"
                                type="success"
                                class="ml10"
                                v-auth="'system:dept:insert'"
                                @click="onCURD({ type: curdEnum.EDIT, ids: '1' })"
                            >
                                <el-icon>
                                    <ele-DocumentAdd />
                                </el-icon>
                                新增
                            </el-button>
                        </div>
                        </#if>
                    </el-header>
                    <el-main>
                        <!-- Table -->
                        <el-table :data="state.tableData.data"
                                  v-loading="state.tableData.loading"
                                  style="width: 100%"
                                  <#if formLayout==1 >
                                  @selection-change="handleSelectionChange"
                                  <#else >
                                  row-key="${tableId}"
                                  :key="expand.key"
                                  :default-expand-all="expand.state"
                                  :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                                  </#if>
                        >
                            <#if formLayout==1 >
                            <el-table-column  type="selection" header-align="center" align="center" width="50"></el-table-column>
                            </#if>
                            <#list gridList as field>
                            <#if field.formDict??>
                            <fast-table-column prop="${field.attrName}" label="${field.fieldComment!}" dict-type="${field.formDict}"></fast-table-column>
                            <#elseif field.primaryPk>
                            <el-table-column v-if="false" prop="${field.attrName}" label="${field.fieldComment!}" header-align="center" align="center"></el-table-column>
                            <#elseif field.formType == 'datetime'>
                            <el-table-column prop="${field.attrName}" label="${field.fieldComment!}" header-align="center" align="center" show-overflow-tooltip>
                                <template #default="scope">
                                   {{ $ut.parseTime(scope.row.${field.attrName}, "{y}-{m}-{d} {h}:{i}:{s}") }}
                                </template>
                            </el-table-column>
                            <#elseif field.formType == 'date'>
                            <el-table-column prop="${field.attrName}" label="${field.fieldComment!}" header-align="center" align="center" show-overflow-tooltip>
                                <template #default="scope">
                                   {{ $ut.parseTime(scope.row.${field.attrName}, "{y}-{m}-{d}") }}
                                </template>
                            </el-table-column>
                            <#elseif field.formType == 'image'>
                                <el-table-column prop="${field.attrName}" label="${field.fieldComment!}" header-align="center" align="center" show-overflow-tooltip>
                                    <template #default="scope">
                                        <el-image
                                            style="height: 50px"
                                            :src="scope.row.${field.attrName}"
                                            :zoom-rate="1.2"
                                            :max-scale="7"
                                            :min-scale="0.2"
                                            :preview-src-list="[scope.row.${field.attrName}]"
                                            :initial-index="1"
                                            preview-teleported
                                            fit="cover"
                                        />
                                    </template>
                                </el-table-column>
                            <#else>
                            <el-table-column prop="${field.attrName}" label="${field.fieldComment!}" header-align="center" align="center"></el-table-column>
                            </#if>
                            </#list>
                            <el-table-column fixed="right" label="操作" width="<#if formLayout==1 > 100 <#else> 120 </#if>">
                                <template #default="scope">
                                    <#if formLayout==2 >
                                    <el-tooltip placement="top" :content="$t('message.form.insertSon')">
                                        <el-icon class="mr10" color="blue" v-auth="'${moduleName}:${functionName}:insert'" @click="onCURD({ type: curdEnum.INSERT, ids: scope.row.${tableId} })">
                                            <ele-FolderAdd />
                                        </el-icon>
                                    </el-tooltip>
                                    </#if>
                                    <el-tooltip placement="top" :content="$t('message.form.edit')">
                                        <el-icon class="mr10" color="blue" v-auths="['${moduleName}:${functionName}:query', '${moduleName}:${functionName}:edit']" @click="onCURD({ type: curdEnum.EDIT, ids: scope.row.${tableId} })">
                                            <ele-Edit />
                                        </el-icon>
                                    </el-tooltip>
                                    <el-tooltip placement="top" :content="$t('message.form.delete')">
                                        <el-icon class="mr10" color="blue"  v-auth="'${moduleName}:${functionName}:delete'" @click="onCURD({ type: curdEnum.DELETE, ids: scope.row.${tableId} })" >
                                            <ele-Delete />
                                        </el-icon>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-main>
                    <#if formLayout==1 >
                    <el-footer>
                        <!-- 分页 -->
                        <el-pagination
                            @size-change="onHandleSizeChange"
                            @current-change="onHandleCurrentChange"
                            class="mt15"
                            :pager-count="5"
                            :page-sizes="[10, 20, 30, 50, 100, 500, 1000]"
                            v-model:current-page="state.tableData.param.pageNum"
                            background
                            size="default"
                            v-model:page-size="state.tableData.param.pageSize"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="state.tableData.total"
                        >
                        </el-pagination>
                    </el-footer>
                    </#if>
                </el-container>
            </el-card>
            <${FunctionName}Dialog ref="${functionName}DialogRef" @refresh="formSubmit"/>
        </div>
    </div>
</template>
//ModuleName ${tableComment}
<script setup lang="ts" name="${moduleName}${FunctionName}">
    import { defineAsyncComponent, reactive, onMounted, ref } from "vue";
    import { ElMessageBox, ElMessage } from "element-plus";
    import { CURDEnum } from "/@/enums/CURDEnum";
    import { isEmpty, generateUUID } from "/@/utils/mms";
    import {NextLoading} from "/@/utils/loading";
    import FastSelect from "/@/components/fast-select/src/fast-select.vue";
    import {${functionName}Api} from '/@/api/${moduleName}/${functionName}';
    import {${FunctionName}Bo,${FunctionName}Vo } from '/@/api/${moduleName}/${functionName}/type';
    const baseApi = ${functionName}Api();
    <#list formList as field>
    <#if field.formDict??>
    import FastTableColumn from "/@/components/fast-table-column";
    <#break>
    </#if>
    </#list>
    const ${functionName}DialogRef = ref();
    const ${FunctionName}Dialog = defineAsyncComponent(() => import('/@/views/${moduleName}/${functionName}/dialog.vue'));
    const TableTool = defineAsyncComponent(() => import("/@/components/table-tool/index.vue"));

    const curdEnum = CURDEnum;
    const tableToolRef = ref();
    const componentKey = ref(generateUUID());
    const state = reactive({
        tableData:{
            data: [] as ${FunctionName}Vo[],
            total: 0,
            loading: false,
            param: {
                selectIds: "",
                pageNum: 1,
                pageSize: 10,
                <#list queryList as field>
                <#if field.fieldType == 'Integer'>
                <#if field.attrName =='sort' >
                ${field.attrName}: 1<#sep>,
                <#else>
                ${field.attrName}: 0<#sep>,
                </#if>
                <#else>
                ${field.attrName}: ''<#sep>,</#sep>
            </#if>
            </#list>
            <#if formLayout==2 >
            ,isAll:true,
            </#if>
            }
        }
    });
    <#if formLayout==2 >
    // 展开/合闭
    const expand = ref({
        state: true,
        key: 0,
    });
    const clickExpand = () => {
        expand.value.key = +new Date();
        expand.value.state = !expand.value.state;
    };
    </#if>
    // 初始化表格数据
    const getTableData = () => {
        state.tableData.loading = true;
        baseApi.list(state.tableData.param).then(res => {
            <#if formLayout==1 >
            state.tableData.data = res.rows;
            state.tableData.total = res.total;
            </#if>
            <#if formLayout==2 >
            state.tableData.data = res.data;
            </#if>
        }).catch(async err => {
            ElMessage.warning(err);
        }).finally(() => {
            state.tableData.loading = false;
        })
    };
    // 打开修改用户弹窗
    const onCURD = (obj: { type: CURDEnum; ids?: string }) => {
        if (obj.type === CURDEnum.INSERT) {
            <#if formLayout==1 >
            ${functionName}DialogRef.value.openDialog(obj.type);
            return false;
            </#if>
            <#if formLayout==2 >
            baseApi.query(obj.ids).then((res) => {
                ${functionName}DialogRef.value.openDialog(obj.type, res.data);
            }).catch(async (err) => {
                ElMessage.warning(err);
            }).finally(() => {});
            </#if>
        }
        // 编辑操作
        if (obj.type === CURDEnum.EDIT) {
            baseApi.query(obj.ids).then((res) => {
              ${functionName}DialogRef.value.openDialog(obj.type, res.data);
           }).catch(async (err) => {
              ElMessage.warning(err);
           }).finally(() => {});
        }
        // 删除操作
        if (obj.type === CURDEnum.DELETE) {
            ElMessageBox.confirm(`此操作将永久删除，是否继续?`, "提示", {
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                baseApi.delete(obj.ids).then((res) => {
                     getTableData();
                     ElMessage.success("删除成功");
                }).catch(async (err) => {
                     ElMessage.warning(err);
                }).finally(() => {
                    setTimeout(() => {
                       getTableData();
                    }, 1000);
                });
           }).catch(() => {});
        }
    }
    // 接收子组件传值
    const formSubmit = (row: ${FunctionName}Bo) => {
        if (isEmpty(row.${tableId})) {
            //新增
            NextLoading.open();
            baseApi.insert(row).then(row => {
                ${functionName}DialogRef.value.closeDialog();
                ElMessage.success(row.msg)
                setTimeout(() => {
                    getTableData();
                }, 1000)
            }).catch(async err => {
                ${functionName}DialogRef.value.resetLoading();
                ElMessage.warning(err);
            }).finally(() => {
                NextLoading.close();
            })
        } else {
            //更新
            NextLoading.open();
            baseApi.edit(row).then(row => {
                ${functionName}DialogRef.value.closeDialog();
                ElMessage.success(row.msg)
            }).catch(async err => {
                ${functionName}DialogRef.value.resetLoading();
                ElMessage.warning(err);
            }).finally(() => {
                NextLoading.close();
            })
        }
    }
    <#if formLayout==1 >
    // 分页改变
    const onHandleSizeChange = (val: number) => {
        state.tableData.param.pageSize = val;
        getTableData();
    };
    // 分页改变
    const onHandleCurrentChange = (val: number) => {
        state.tableData.param.pageNum = val;
        getTableData();
    };
    //选择项改变
    const handleSelectionChange = (val: ${FunctionName}Vo[]) => {
        state.tableData.param.selectIds = val.map((item: any) => item.id).join(",");
    };
    </#if>
    // 页面加载时
    onMounted(() => {
        getTableData();
    });
</script>

<style scoped lang="scss">

</style>
