<template>
	<div class="system-role-dialog-container">
		<el-dialog :title="state.dialog.title" v-model="state.dialog.isShowDialog" width="769px">
			<el-form ref="roleDialogFormRef" :model="state.ruleForm" size="default" label-width="90px">
				<el-row :gutter="35">
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="角色名称">
							<el-input v-model="state.ruleForm.name" placeholder="请输入角色名称" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="角色标识">
							<template #label>
								<el-tooltip effect="dark" content="用于 `router/route.ts` meta.roles" placement="top-start">
									<span>角色标识</span>
								</el-tooltip>
							</template>
							<el-input v-model="state.ruleForm.code" placeholder="请输入角色标识" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="级别">
							<el-input-number v-model="state.ruleForm.level" :min="1" :max="999" controls-position="right"
								placeholder="请输入级别" class="w100" />
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="角色状态">
								<fast-switch
									v-model="state.ruleForm.status"
									dict-type="SYS_STATE"
									placeholder="角色状态"
								></fast-switch>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
						<el-form-item label="角色描述">
							<el-input v-model="state.ruleForm.remark" type="textarea" placeholder="请输入角色描述"
								maxlength="150"></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
						<el-form-item label="菜单权限">
							<el-tree ref="treeRef" :data="state.menuData" :props="state.menuProps" show-checkbox
								default-expand-all node-key="id" highlight-current class="menu-data-tree" />
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
                               @click="onSubmit" size="default">{{ state.dialog.submitTxt }}</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="systemRoleDialog">
import { reactive, ref, nextTick } from 'vue';
import { ElTree, ElMessage } from 'element-plus'
import { roleApi } from '/@/api/system/role';
import { CURDEnum } from '/@/enums/CURDEnum';
import FastSwitch from "/@/components/fast-switch/src/fast-switch.vue";
import {RowRoleType} from "/@/api/system/role/type";
import {Eleme} from "@element-plus/icons-vue";
const baseApi = roleApi();
// 定义子组件向父组件传值/事件
const emit = defineEmits(['refresh']);

// 定义变量内容
const treeRef = ref<InstanceType<typeof ElTree>>()
const roleDialogFormRef = ref();
const state = reactive({
	ruleForm: {
		name: '', // 角色名称
		code: '', // 角色标识
		level:1,  // 角色级别
		sort: 0, // 排序
		status: 0, // 角色状态
		remark: '', // 角色描述
		defChecked: <any>[]//选中节点id
	},
	menuData: [] as TreeType[],
	menuProps: {
		children: 'children',
		label: 'name',
	},
	defchecked: [-1],
	defexpanded: [-1],
	dialog: {
        loading: false,
		isShowDialog: false,
		type: '',
		title: '',
		submitTxt: '',
	},

});

// 重置
const resetForm=()=>{
    state.dialog.loading = false;
	state.ruleForm={
		name: '', // 角色名称
		code: '', // 角色标识
		sort: 0, // 排序
		level:1,  // 角色级别
		status: 0, // 角色状态
		remark: '', // 角色描述
		defChecked: <any>[]//选中节点id
	}
}

// 打开弹窗
const openDialog = (type: string, row: RowRoleType) => {
	resetForm();
	if (type === CURDEnum.EDIT) {
		state.ruleForm = row;
		state.dialog.title = '修改角色';
		state.dialog.submitTxt = '修 改';
		state.dialog.type = CURDEnum.EDIT;
		getMenuData(row.defChecked);
	}
    else {
		state.dialog.title = '新增角色';
		state.dialog.submitTxt = '新 增';
		state.dialog.type = CURDEnum.INSERT;
		// 清空表单，此项需加表单验证才能使用
		nextTick(() => {
			roleDialogFormRef.value.resetFields();
		});
		getMenuData([]);
	}
	state.dialog.isShowDialog = true;
};
// 关闭弹窗
const closeDialog = () => {
	state.dialog.isShowDialog = false;
    state.dialog.loading = false;
};

const resetLoading = () => {
    state.dialog.loading = false;
};
// 提交
const onSubmit = () => {
    state.dialog.loading = true;
	state.ruleForm.defChecked = treeRef.value!.getCheckedKeys(false)
	emit('refresh', state.ruleForm);
};
// 获取菜单结构数据
const getMenuData = (defid: number[]) => {
	state.defchecked = defid;
	baseApi.menuList().then(res => {
		state.menuData = res.data;
	}).catch(async err => { ElMessage.warning(err); }).finally(() => {
		treeRef.value!.setCheckedKeys([], false)
		treeRef.value!.setCheckedKeys(state.defchecked, false)
	})
};

// 暴露变量
defineExpose({
	openDialog, closeDialog,resetLoading
});
</script>

<style scoped lang="scss">
.system-role-dialog-container {
	.menu-data-tree {
		width: 100%;
		border: 1px solid var(--el-border-color);
		border-radius: var(--el-input-border-radius, var(--el-border-radius-base));
		padding: 5px;
	}
}
</style>
