<template>
    <div class="block">
        <!-- 功能栏  -->
        <div class="views-tool">
            <div class="tool-left">
                <el-form :inline="true" size="default" :model="state.tableData.param" class="form-tool" @keyup.enter="getTableData">
                        <el-form-item>
                            <el-input v-model="state.tableData.param.title" size="default" placeholder="公告标题" style="max-width: 180px" clearable ></el-input>
                        </el-form-item>
                        <el-form-item>
                             <fast-select v-model="state.tableData.param.type" dict-type="NITICE_TYPE" placeholder="公告类型" clearable></fast-select>
                        </el-form-item>
                    <el-form-item>
                        <el-button
                            size="default"
                            type="primary"
                            :disabled="state.tableData.loading"
                            :loading-icon="Eleme"
                            :loading="state.tableData.loading"
                            @click="getTableData" v-auth="'system:notice:list'">
                            <SvgIcon name="iconfont icon-search1" />{{ $t("message.form.search") }}
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <!-- Table  -->
        <div class="system-notice-container layout-padding  m-t-0  p-t-0">
            <el-card shadow="hover" class="layout-padding-auto">
                <el-container>
                    <el-header>
                        <!-- 新增/导入/导出/打印 -->
                        <TableTool ref="tableToolRef"
                                   tableComment="系统公告"
                                   functionName="notice"
                                   :key="componentKey"
                                   :param="state.tableData.param"
                                   @close="componentKey = generateUUID()"
                                   @insert="onCURD" @deletes="onCURD" />
                    </el-header>
                    <el-main>
                        <!-- Table -->
                        <el-table :data="state.tableData.data"
                                  v-loading="state.tableData.loading"
                                  style="width: 100%"
                                  @selection-change="handleSelectionChange"
                        >
                            <el-table-column  type="selection" header-align="center" align="center" width="50"></el-table-column>
                            <el-table-column v-show="false" prop="id" label="公告ID" header-align="center" align="center"></el-table-column>
                            <el-table-column prop="title" label="公告标题" header-align="center" align="center"></el-table-column>
                            <fast-table-column prop="type" label="公告类型" dict-type="NITICE_TYPE"></fast-table-column>
                            <fast-table-column prop="status" label="公告状态" dict-type="SYS_STATE"></fast-table-column>
                            <el-table-column prop="remark" label="备注" header-align="center" align="center"></el-table-column>
                            <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
                            <el-table-column fixed="right" label="操作" width=" 100 ">
                                <template #default="scope">
                                    <el-tooltip placement="top" :content="$t('message.form.edit')">
                                        <el-icon class="mr10" color="blue" v-auths="['system:notice:query', 'system:notice:edit']" @click="onCURD({ type: curdEnum.EDIT, ids: scope.row.id })">
                                            <ele-Edit />
                                        </el-icon>
                                    </el-tooltip>
                                    <el-tooltip placement="top" :content="$t('message.form.delete')">
                                        <el-icon class="mr10" color="blue"  v-auth="'system:notice:delete'" @click="onCURD({ type: curdEnum.DELETE, ids: scope.row.id })" >
                                            <ele-Delete />
                                        </el-icon>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-main>
                    <el-footer>
                        <!-- 分页 -->
                        <el-pagination
                            @size-change="onHandleSizeChange"
                            @current-change="onHandleCurrentChange"
                            class="mt15"
                            :pager-count="5"
                            :page-sizes="[10, 20, 30, 50, 100]"
                            v-model:current-page="state.tableData.param.pageNum"
                            background
                            size="default"
                            v-model:page-size="state.tableData.param.pageSize"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="state.tableData.total"
                        >
                        </el-pagination>
                    </el-footer>
                </el-container>
            </el-card>
            <NoticeDialog ref="noticeDialogRef" @refresh="formSubmit"/>
        </div>
    </div>
</template>
//ModuleName 系统公告
<script setup lang="ts" name="systemNotice">
    import { defineAsyncComponent, reactive, onMounted, ref } from "vue";
    import { ElMessageBox, ElMessage } from "element-plus";
    import { CURDEnum } from "/@/enums/CURDEnum";
    import { isEmpty, generateUUID } from "/@/utils/mms";
    import {noticeApi} from '/@/api/system/notice';
    import FastTableColumn from "/@/components/fast-table-column";
    import FastSelect from "/@/components/fast-select/src/fast-select.vue";
    import {NoticeEntity, NoticeState} from "/@/api/system/notice/type";
    import {Eleme} from "@element-plus/icons-vue";
    import {NextLoading} from "/@/utils/loading";
    const noticeDialogRef = ref();
    const NoticeDialog = defineAsyncComponent(() => import('/@/views/system/notice/dialog.vue'));
    const TableTool = defineAsyncComponent(() => import("/@/components/table-tool/index.vue"));
    const baseApi = noticeApi();
    const curdEnum = CURDEnum;
    const tableToolRef = ref();
    const componentKey = ref(generateUUID());
    const state = reactive < NoticeState > ({
        tableData: {
            data: [],
            total: 0,
            loading: false,
            param: {
                selectIds: "",
                pageNum: 1,
                pageSize: 10,
                id: '',
                title: '',
                type: ''
            },
        },
    });
    // 初始化表格数据
    const getTableData = () => {
        state.tableData.loading = true;
        baseApi.list(state.tableData.param).then(res => {
            state.tableData.data = res.rows;
            state.tableData.total = res.total;
        }).catch(async err => {
            ElMessage.warning(err);
        }).finally(() => {
            state.tableData.loading = false;
        })
    };

    // 打开修改用户弹窗
    const onCURD = (obj: { type: CURDEnum; ids?: string }) => {
        if (obj.type === CURDEnum.INSERT) {
            noticeDialogRef.value.openDialog(obj.type);
            return false;
        }
        // 编辑操作
        if (obj.type === CURDEnum.EDIT) {
            baseApi.query(obj.ids).then((res) => {
              noticeDialogRef.value.openDialog(obj.type, res.data);
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
    const formSubmit = (row: NoticeEntity) => {
        if (isEmpty(row.id)) {
            //新增
            NextLoading.open();
            baseApi.insert(row).then(row => {
                noticeDialogRef.value.closeDialog();
                ElMessage.success(row.msg)
            }).catch(async err => {
                noticeDialogRef.value.resetLoading();
                ElMessage.warning(err);
            }).finally(() => {
                NextLoading.close();
                setTimeout(() => {
                    getTableData();
                }, 1000)
            })
        } else {
            //更新
            NextLoading.open();
            baseApi.edit(row).then(row => {
                noticeDialogRef.value.closeDialog();
                ElMessage.success(row.msg)
            }).catch(async err => {
                noticeDialogRef.value.resetLoading();
                ElMessage.warning(err);
            }).finally(() => {
                NextLoading.close();
                setTimeout(() => {
                    getTableData();
                }, 1000)
            })
        }
    }

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
    const handleSelectionChange = (val: NoticeEntity[]) => {
        state.tableData.param.selectIds = val.map((item: any) => item.id).join(",");
    };
    // 页面加载时
    onMounted(() => {
        getTableData();
    });
</script>

<style scoped lang="scss">

</style>
