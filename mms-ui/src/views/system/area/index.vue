<template>
    <div class="block">
                <!-- Table  -->
        <div class="system-sysArea-container layout-padding  mt-5  p-t-0">
            <el-card shadow="hover" class="layout-padding-auto">
                <el-container>
                    <el-header>
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
                    </el-header>
                    <el-main>
                        <!-- Table -->
                        <el-table :data="state.data"
                                  v-loading="state.loading"
                                  style="width: 100%"
                                  row-key="id"
                                  :key="expand.key"
                                  :default-expand-all="expand.state"
                                  :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                        >
                            <el-table-column v-show="false" prop="id" label="ID" header-align="center" align="center"></el-table-column>
                            <el-table-column prop="name" label="名称" header-align="center" align="center"></el-table-column>
                            <el-table-column prop="code" label="CODE" header-align="center" align="center"></el-table-column>
                            <el-table-column prop="parentCode" label="父CODE" header-align="center" align="center"></el-table-column>
                            <el-table-column prop="level" label="级别" header-align="center" align="center"></el-table-column>
                            <fast-table-column prop="status" label="状态" dict-type="SYS_STATE"></fast-table-column>
                            <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
                            <el-table-column fixed="right" label="操作" width=" 120 ">
                                <template #default="scope">
                                    <el-tooltip placement="top" :content="$t('message.form.insertSon')">
                                        <el-icon class="mr10" color="blue" v-auth="'system:sysArea:insert'" @click="onCURD({ type: curdEnum.INSERT, ids: scope.row.id })">
                                            <ele-FolderAdd />
                                        </el-icon>
                                    </el-tooltip>
                                    <el-tooltip placement="top" :content="$t('message.form.edit')">
                                        <el-icon class="mr10" color="blue" v-auths="['system:sysArea:query', 'system:sysArea:edit']" @click="onCURD({ type: curdEnum.EDIT, ids: scope.row.id })">
                                            <ele-Edit />
                                        </el-icon>
                                    </el-tooltip>
                                    <el-tooltip placement="top" :content="$t('message.form.delete')">
                                        <el-icon class="mr10" color="blue"  v-auth="'system:sysArea:delete'" @click="onCURD({ type: curdEnum.DELETE, ids: scope.row.id })" >
                                            <ele-Delete />
                                        </el-icon>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-main>
                </el-container>
            </el-card>
            <SysAreaDialog ref="sysAreaDialogRef" @refresh="formSubmit"/>
        </div>
    </div>
</template>
//ModuleName 系统区域
<script setup lang="ts" name="systemSysArea">
    import { defineAsyncComponent, reactive, onMounted, ref } from "vue";
    import { ElMessageBox, ElMessage } from "element-plus";
    import { CURDEnum } from "/@/enums/CURDEnum";
    import { isEmpty, generateUUID } from "/@/utils/mms";
    import FastSelect from "/@/components/fast-select/src/fast-select.vue";
    import {sysAreaApi} from '/@/api/system/sysArea';
    import {SysAreaBo,SysAreaVo,SysAreaTable } from '/@/api/system/sysArea/type';
    import FastTableColumn from "/@/components/fast-table-column";
    const sysAreaDialogRef = ref();
    const SysAreaDialog = defineAsyncComponent(() => import('/@/views/system/area/dialog.vue'));
    const baseApi = sysAreaApi();
    const curdEnum = CURDEnum;
    const state = reactive < SysAreaTable > ({
            data: [],
            total: 0,
            loading: false,
            param: {
                selectIds: "",
                pageNum: 1,
                pageSize: 10,
                name: ''
                ,isAll:true,
            }
    });
    // 展开/合闭
    const expand = ref({
        state: false,
        key: 0,
    });
    const clickExpand = () => {
        expand.value.key = +new Date();
        expand.value.state = !expand.value.state;
    };
    // 初始化表格数据
    const getTableData = () => {
        state.loading = true;
        baseApi.list(state.param).then(res => {
            state.data = res.data;
        }).catch(async err => {
            ElMessage.warning(err);
        }).finally(() => {
            state.loading = false;
        })
    };
    // 打开修改用户弹窗
    const onCURD = (obj: { type: CURDEnum; ids?: string }) => {
        if (obj.type === CURDEnum.INSERT) {
            baseApi.query(obj.ids).then((res) => {
                sysAreaDialogRef.value.openDialog(obj.type, res.data);
            }).catch(async (err) => {
                ElMessage.warning(err);
            }).finally(() => {});
        }
        // 编辑操作
        if (obj.type === CURDEnum.EDIT) {
            baseApi.query(obj.ids).then((res) => {
              sysAreaDialogRef.value.openDialog(obj.type, res.data);
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
    const formSubmit = (row: SysAreaBo) => {
        if (isEmpty(row.id)) {
            //新增
            baseApi.insert(row).then(row => {
                sysAreaDialogRef.value.closeDialog();
                ElMessage.success(row.msg)
            }).catch(async err => {
                ElMessage.warning(err);
            }).finally(() => {
                setTimeout(() => {
                    getTableData();
                }, 1000)
            })
        } else {
            //更新
            baseApi.edit(row).then(row => {
                sysAreaDialogRef.value.closeDialog();
                ElMessage.success(row.msg)
            }).catch(async err => {
                ElMessage.warning(err);
            }).finally(() => {
                setTimeout(() => {
                    getTableData();
                }, 1000)
            })
        }
    }

    // 页面加载时
    onMounted(() => {
        getTableData();
    });
</script>

<style scoped lang="scss">

</style>
