<template>
	<div class="system-user-dialog-container">
		<el-dialog :title="state.dialog.title" v-model="state.dialog.isShowDialog" width="769px">
			<el-form ref="defDialogFormRef" :model="state.ruleForm" size="default" label-width="90px">
				<el-row :gutter="35">
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="账户名称">
							<el-input :disabled="state.dialog.type === 'edit'" v-model="state.ruleForm.userName" placeholder="请输入账户名称" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="用户昵称">
							<el-input v-model="state.ruleForm.nickName" placeholder="请输入用户昵称" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="部门">
							<el-cascader
								:options="state.deptData"
								:props="{ checkStrictly: true, value: 'deptId', label: 'deptName' }"
								placeholder="请选择部门"
								@change="change"
								clearable
								class="w100"
								v-model="state.ruleForm.deptIds"
							>
								<template #default="{ node, data }">
									<span>{{ data.deptName }}</span>
									<span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
								</template>
							</el-cascader>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="手机号">
							<el-input v-model="state.ruleForm.phoneNumber" placeholder="请输入手机号" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="邮箱">
							<el-input v-model="state.ruleForm.email" placeholder="请输入" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="性别">
							<fast-select v-model="state.ruleForm.sex" dict-type="SYS_SEX" placeholder="性别"></fast-select>
						</el-form-item>
					</el-col>
					<el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="账号类型">
							<fast-select v-model="state.ruleForm.userType" dict-type="USER_TYPE" placeholder="账号类型"></fast-select>
						</el-form-item>
					</el-col>
					<el-col v-if="state.dialog.type === 'add'" :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="账户密码">
							<el-input v-model="state.ruleForm.password" placeholder="请输入" type="password" show-password clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="用户状态">
							<fast-switch
								v-model="state.ruleForm.status"
								dict-type="SYS_STATE"
								placeholder="用户状态"
								:disabled="$ut.isSuperAdmin(state.ruleForm.roleCodes)"
							></fast-switch>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
						<el-form-item label="用户描述">
							<el-input v-model="state.ruleForm.remark" type="textarea" placeholder="请输入用户描述" maxlength="150"></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="排序">
							<el-input-number v-model="state.ruleForm.sort" aria-label="排序" :step="1" :min="1" ></el-input-number>
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

<script setup lang="ts" name="systemUserDialog">
import { reactive, ref, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import { CURDEnum } from '/@/enums/CURDEnum';
import { deptApi } from '/@/api/system/dept';
import FastSelect from '/@/components/fast-select/src/fast-select.vue';
import FastSwitch from '/@/components/fast-switch/src/fast-switch.vue';
import {RowUserType} from "/@/api/system/user/type";
import {Eleme} from "@element-plus/icons-vue";
// 引入 api 请求接口
const deptApiObj = deptApi();
// 定义子组件向父组件传值/事件
const emit = defineEmits(['refresh']);

// 定义变量内容
const defDialogFormRef = ref();
const state = reactive({
	ruleForm: {} as RowUserType,
	deptData: [] as DeptTreeType[], // 部门数据
	dialog: {
        loading: false,
        isShowDialog: false,
        title: '',
        submitTxt: '',
        type: ''
	},
});
// 重置
const resetForm = () => {
    state.dialog.loading = false;
	state.ruleForm = {
        userId: '',
		deptId: '',
		deptIds: [] as string[],
		postIds: '', // 岗位
		userName: '',
		nickName: '',
		password: '123456',
		userType: '1',
		phoneNumber: '',
		email: '',
		wxUnOpenId: '',
		sex: '0',
		avatar: '',
		status: 0,
		roleCodes: [],
		roleSign: '',
		department: '', // 部门
		remark: '', // 用户描述
		sort: 1, // 排序
		revision: 1
    };
};
// 打开弹窗
const openDialog = (type: string, row: RowUserType) => {
    state.dialog.loading = true;  // 确保使用响应式赋值
    state.dialog.isShowDialog = true;  // 确保使用响应式赋值
    resetForm();
    if (type === CURDEnum.EDIT) {
        state.ruleForm = row;
        state.dialog.title = '修改用户';
        state.dialog.submitTxt = '修 改';
        state.dialog.type = CURDEnum.EDIT;
    } else {
        state.dialog.title = '新增用户';
        state.dialog.submitTxt = '新 增';
        state.dialog.type = CURDEnum.INSERT;
        // 清空表单，此项需加表单验证才能使用
        nextTick(() => {
            defDialogFormRef.value.resetFields();
        });
    }
    getMenuData();
};
// 关闭弹窗
const closeDialog = () => {
    state.dialog.loading = false;
	state.dialog.isShowDialog = false;
};
const resetLoading = () => {
    state.dialog.loading = false;
};

// 提交按钮
const onSubmit = () => {
    state.dialog.loading = true;
	emit('refresh', state.ruleForm);
};
// 初始化 数据
const getMenuData = () => {
	deptApiObj
		.list<DeptTreeType[]>()
		.then((res) => {
			state.deptData = res.data;
		})
		.catch(async (err) => {
			ElMessage.warning(err);
		})
		.finally(() => {
            state.dialog.loading=false;
        });
};
// 选择部门
const change = (arr: string[]) => {
	state.ruleForm.deptId = arr[arr.length - 1];
};
// 暴露变量
defineExpose({
	openDialog,
	closeDialog,
    resetLoading
});
</script>
