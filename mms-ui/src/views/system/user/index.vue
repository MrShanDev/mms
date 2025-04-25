<template>
	<div class="block">
		<!-- 功能栏  -->
		<div class="views-tool">
			<div class="tool-left">
				<el-form :inline="true" size="default" :model="state.tableData.param" class="form-tool" @keyup.enter="getTableData">
					<el-form-item>
						<el-input size="default" v-model="state.tableData.param.userName" placeholder="请输入账户名称" style="max-width: 180px" clearable>
						</el-input>
					</el-form-item>
					<el-form-item>
						<el-button
                            size="default"
                            type="primary"
                            :disabled="state.tableData.loading"
                            :loading-icon="Eleme"
                            :loading="state.tableData.loading"
                            @click="getTableData" v-auth="'system:user:list'">
							<SvgIcon name="iconfont icon-search1" />
							{{ $t('message.form.search') }}
						</el-button>
					</el-form-item>
				</el-form>
			</div>
		</div>
		<div class="system-user-container layout-padding m-t-0 p-t-0">
			<el-card shadow="hover" class="layout-padding-auto">
				<el-container>
					<el-header>
						<!-- 新增/导入/导出/打印 -->
						<TableTool
              v-show="!state.tableData.loading"
							ref="tableToolRef"
							tableComment="系统用户"
							functionName="user"
							:key="componentKey"
							:param="state.tableData.param"
							@close="componentKey = generateUUID()"
							@insert="onCURD"
							@deletes="onCURD"
						/>
					</el-header>
					<el-main>
						<!-- Table -->
						<el-table :data="state.tableData.data" v-loading="state.tableData.loading" @selection-change="handleSelectionChange" style="width: 100%">
							<el-table-column type="selection" label="序号" width="50" />
							<el-table-column prop="userName" label="账户名称" width="100" show-overflow-tooltip></el-table-column>
							<el-table-column prop="nickName" label="用户昵称" show-overflow-tooltip></el-table-column>
							<el-table-column prop="avatar" label="头像" width="100" show-overflow-tooltip>
								<template #default="scope">
									<el-image
										style="width: 50px; height: 50px"
										:src="scope.row.avatar"
										:zoom-rate="1.2"
										:max-scale="7"
										:min-scale="0.2"
										:preview-src-list="[scope.row.avatar]"
										:initial-index="1"
										preview-teleported
										fit="cover"
									/>
								</template>
							</el-table-column>
							<el-table-column prop="roleName" label="关联角色" show-overflow-tooltip></el-table-column>
							<el-table-column prop="department" label="部门" show-overflow-tooltip></el-table-column>
							<el-table-column prop="phoneNumber" label="手机号" show-overflow-tooltip></el-table-column>
							<el-table-column prop="email" label="邮箱" show-overflow-tooltip></el-table-column>
							<el-table-column prop="status" label="用户状态" show-overflow-tooltip>
								<template #default="scope">
									<el-tag type="success" v-if="scope.row.status == 0">启用</el-tag>
									<el-tag type="info" v-else>禁用</el-tag>
								</template>
							</el-table-column>
							<el-table-column prop="remark" label="用户描述"></el-table-column>
							<el-table-column prop="createdTime" width="170" label="创建时间" show-overflow-tooltip>
								<template #default="scope">
									{{ $ut.parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
								</template>
							</el-table-column>
							<el-table-column fixed="right" width="130" label="操作">
								<template #default="scope">
									<el-tooltip  placement="top" :content="$t('message.form.edit')">
										<el-icon
											class="mr10"
											color="blue"
                                            v-auths="['system:user:query', 'system:user:edit']"
											@click="onCURD({ type: curdEnum.EDIT, ids: scope.row.userId })"
										>
											<ele-Edit />
										</el-icon>
									</el-tooltip>
									<el-tooltip v-if="!$ut.isSuperAdmin(scope.row.roleCodes)" placement="top" :content="$t('message.form.delete')">
										<el-icon
											class="mr10"
											color="blue"
											v-auth="'system:user:delete'"
											@click="onCURD({ type: curdEnum.DELETE, ids: scope.row.userId })"
										>
											<ele-Delete />
										</el-icon>
									</el-tooltip>
									<el-tooltip v-if="!$ut.isSuperAdmin(scope.row.roleCodes)" placement="top" :content="$t('message.form.resetPasswords')">
										<el-icon
											class="mr10"
											color="blue"
											v-auth="'system:user:edit'"
											@click="openResetPassword(scope.row)"
										>
											<ele-Key />
										</el-icon>
									</el-tooltip>
									<el-tooltip v-if="!$ut.isSuperAdmin(scope.row.roleCodes)" placement="top" :content="$t('message.form.rolesAllocation')">
										<el-icon
											class="mr10"
											color="blue"
											v-auth="'system:user:delete'"
											@click="openUserRole(scope.row)"
										>
											<ele-CircleCheck />
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
			<!-- 编辑 -->
			<UserDialog ref="dialogRef" @refresh="formSubmit" />
			<!-- 重置密码 -->
			<el-dialog v-model="resetPassword.dialogState" :title="$t('message.form.hint')" width="500">
				<div class="my-4">
					{{ $t('message.form.pleaseInput') }} "{{ resetPassword.form.userName }}"
					{{ $t('message.form.newPassword') }}
				</div>
				<el-input v-model="resetPassword.form.password" autocomplete="off" />
				<template #footer>
					<div class="dialog-footer">
						<el-button @click="resetPassword.dialogState = false">{{ $t('message.form.cancel') }}</el-button>
						<el-button type="primary" @click="primaryPasswordBut">
							{{ $t('message.form.confirm') }}
						</el-button>
					</div>
				</template>
			</el-dialog>
			<!--用户角色-->
			<el-dialog v-model="userRole.dialogState" :title="$t('message.form.userRole')" width="500">
				<div>
					<div class="my-4">
						{{ $t('message.form.setting') }} "{{ userRole.form.userName }}"
						{{ $t('message.form.userRoles') }}
					</div>
					<el-checkbox-group v-model="userRole.form.roleCodes">
						<el-checkbox
							v-for="(item, index) in userRole.roleData"
							:key="index"
							:label="item.code"
							:value="item.code"
							:disabled="item.code === 'super_admin'"
							>{{ item.name }}
						</el-checkbox>
					</el-checkbox-group>
				</div>
				<template #footer>
					<div class="dialog-footer">
						<el-button @click="userRole.dialogState = false">{{ $t('message.form.cancel') }}</el-button>
						<el-button type="primary" @click="primaryUserRoleBut">
							{{ $t('message.form.confirm') }}
						</el-button>
					</div>
				</template>
			</el-dialog>
		</div>
	</div>
</template>

<script setup lang="ts" name="systemUser">
import { defineAsyncComponent, reactive, onMounted, ref } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import { CURDEnum } from '/@/enums/CURDEnum';
import { isEmpty, generateUUID } from '/@/utils/mms';
import { userApi } from '/@/api/system/user';
import { roleApi } from '/@/api/system/role';
import {RowUserType, SysUserState} from "/@/api/system/user/type";
import {NextLoading} from "/@/utils/loading";
import {RowRoleType} from "/@/api/system/role/type";
import {Eleme} from "@element-plus/icons-vue";
// 引入组件
const UserDialog = defineAsyncComponent(() => import('/@/views/system/user/dialog.vue'));
const TableTool = defineAsyncComponent(() => import('/@/components/table-tool/index.vue'));

// 定义变量内容
const baseApi = userApi();
const roleApiObj = roleApi();
const curdEnum = CURDEnum;
const dialogRef = ref();
const tableToolRef = ref();
const componentKey = ref(generateUUID());
const state = reactive<SysUserState>({
	tableData: {
		data: [],
		total: 0,
		loading: false,
		param: {
			selectIds: '',
			pageNum: 1,
			pageSize: 10,
			userName: '',
		},
	},
});

//重置密码
const resetPassword = reactive({
	dialogState: false,
	form: { password: '', userName: '', userId: '' },
});
//用户角色
const userRole = reactive({
	dialogState: false,
	roleData: [] as RowRoleType[], // 角色数据
	form: { roleCodes: [], userId: '', userName: '' } as {
		roleCodes: string[];
		userId: string;
		userName: string;
	},
});
// 初始化表格数据
const getTableData = () => {
	state.tableData.loading = true;
	baseApi
		.getList<RowUserType[]>(state.tableData.param)
		.then((res) => {
			state.tableData.data = res.rows;
			state.tableData.total = res.total;
		})
		.catch(async (err) => {
			ElMessage.warning(err);
		})
		.finally(() => {
			state.tableData.loading = false;
		});
};
// 打开修改用户弹窗
const onCURD = (obj: { type: CURDEnum; ids?: string }) => {
	// 新增操作
	if (obj.type === CURDEnum.INSERT) {
		dialogRef.value.openDialog(obj.type);
	}
	// 编辑操作
	if (obj.type === CURDEnum.EDIT) {
		baseApi
			.query(obj.ids)
			.then((res) => {
				dialogRef.value.openDialog(obj.type, res.data);
			})
			.catch(async (err) => {
				ElMessage.warning(err);
			})
			.finally(() => {});
	}
	// 删除操作
	if (obj.type === CURDEnum.DELETE) {
		ElMessageBox.confirm(`此操作将永久删除，是否继续?`, '提示', {
			confirmButtonText: '确认',
			cancelButtonText: '取消',
			type: 'warning',
		})
			.then(() => {
				baseApi
					.delete(obj.ids)
					.then((res) => {
						getTableData();
						ElMessage.success('删除成功');
					})
					.catch(async (err) => {
						ElMessage.warning(err);
					})
					.finally(() => {
						setTimeout(() => {
							getTableData();
						}, 1000);
					});
			})
			.catch(() => {});
	}
};
// 接受子组件传值
const formSubmit = (row: RowUserType) => {
	if (isEmpty(row.userId)) {
		//新增
        NextLoading.open();
		baseApi
			.insert(row)
			.then((row) => {
				dialogRef.value.closeDialog();
				ElMessage.success(row.msg);
			})
			.catch(async (err) => {
                dialogRef.value.resetLoading();
				ElMessage.warning(err);
			})
			.finally(() => {
                NextLoading.close();
				setTimeout(() => {
					getTableData();
				}, 1000);
			});
	} else {
		//更新
        NextLoading.open();
		baseApi
			.edit(row)
			.then((row) => {
				dialogRef.value.closeDialog();
				ElMessage.success(row.msg);
			})
			.catch(async (err) => {
				ElMessage.warning(err);
			})
			.finally(() => {
                dialogRef.value.resetLoading();
                NextLoading.close();
				setTimeout(() => {
					getTableData();
				}, 1000);
			});
	}
};
// 分页改变大小
const onHandleSizeChange = (val: number) => {
	state.tableData.param.pageSize = val;
	getTableData();
};
// 分页改变页码
const onHandleCurrentChange = (val: number) => {
	state.tableData.param.pageNum = val;
	getTableData();
};
//选择项改变
const handleSelectionChange = (val: RowUserType[]) => {
	state.tableData.param.selectIds = val.map((item: any) => item.userId).join(',');
};
//打开设置密码
const openResetPassword = (row: RowUserType) => {
	resetPassword.dialogState = true;
	resetPassword.form.userName = row.userName;
	resetPassword.form.userId = row.userId;
};
//重置密码
const primaryPasswordBut = () => {
	baseApi
		.resetPwdSuper(resetPassword.form)
		.then((res) => {
			resetPassword.dialogState = false;
			resetPassword.form.userName = '';
			resetPassword.form.userId = '';
			ElMessage.success('重置密码成功');
		})
		.catch(async (err) => {
			ElMessage.warning(err);
		});
};
//打开设置角色
const openUserRole = (row: RowUserType) => {
	userRole.dialogState = true;
	userRole.form.userId = row.userId;
	userRole.form.userName = row.userName;
	userRole.form.roleCodes = row.roleCodes;
	roleApiObj.getList({ state: 0 }).then((res) => {
		userRole.roleData = res.rows;
	});
};
//设置角色
const primaryUserRoleBut = () => {
	baseApi
		.setUserRole(userRole.form)
		.then((res) => {
			userRole.dialogState = false;
			userRole.form.userId = '';
			userRole.form.userName = '';
			userRole.roleData = [];
			getTableData();
			ElMessage.success('设置角色成功');
		})
		.catch(async (err) => {
			ElMessage.warning(err);
		});
};
// 页面加载时
onMounted(() => {
	getTableData();
});
</script>
<style scoped lang="scss"></style>
