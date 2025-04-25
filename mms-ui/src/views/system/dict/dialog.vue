<template>
	<div class="system-dic-dialog-container">
		<el-dialog :title="state.dialog.title" v-model="state.dialog.isShowDialog" width="769px">
			<!-- <el-alert title="半成品，交互过于复杂，请自行扩展！" type="warning" :closable="false" class="mb20"> </el-alert> -->
			<el-form ref="dicDialogFormRef" :model="state.ruleForm" size="default" label-width="90px">
				<el-row :gutter="35">
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="字典名称">
							<el-input v-model="state.ruleForm.name" placeholder="请输入字典名称" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="字典编码">
							<el-input v-model="state.ruleForm.fieldName" placeholder="请输入字典编码" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
						<el-form-item label="字典状态">
							<el-switch v-model="state.ruleForm.status" inline-prompt active-value="0" inactive-value="1"
								active-text="启" inactive-text="禁"></el-switch>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
						<el-row :gutter="35" v-for="(v, k) in state.ruleForm.list" :key="k">
							<el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" class="mb20">
								<el-form-item :prop="`list[${k}].label`">
									<template #label>
										<el-button type="primary" circle size="small" @click="onAddRow" v-if="k === 0">
											<el-icon>
												<ele-Plus />
											</el-icon>
										</el-button>
										<el-button type="danger" circle size="small" @click="onDelRow(k)" v-else>
											<el-icon>
												<ele-Delete />
											</el-icon>
										</el-button>
										<span class="ml10">字段</span>
									</template>
									<el-input v-model="v.label" style="width: 100%" placeholder="请输入字段名"> </el-input>
								</el-form-item>
							</el-col>
							<el-col :xs="24" :sm="16" :md="8" :lg="8" :xl="8" class="mb20">
								<el-form-item label="属性" :prop="`list[${k}].value`">
									<el-input v-model="v.value" style="width: 100%" placeholder="请输入属性值"> </el-input>
								</el-form-item>
							</el-col>
							<el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" class="mb20">
								<el-form-item label="排序" :prop="`list[${k}].sort`">
									<el-input-number v-model="v.sort" style="width: 100%" :min="0" :max="999"
										controls-position="right" placeholder="请输入属性值" class="w100"> </el-input-number>
								</el-form-item>
							</el-col>
							<el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" class="mb20">
								<el-form-item label="颜色" :prop="`list[${k}].colorType`">
									<el-select v-model="v.colorType" placeholder="请选择" clearable class="w100">
										<el-option label="默认" value=''></el-option>
										<el-option label="主要" value='primary'></el-option>
										<el-option label="成功" value='success'></el-option>
										<el-option label="信息" value='info'></el-option>
										<el-option label="警告" value='warning'></el-option>
										<el-option label="危险" value='danger'></el-option>
									</el-select>
								</el-form-item>
							</el-col>
							<el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" class="mb20">
								<el-form-item label="状态" :prop="`list[${k}].value`">
									<el-switch v-model="v.status" inline-prompt active-value="0" inactive-value="1"
										active-text="启" inactive-text="禁"></el-switch>
								</el-form-item>
							</el-col>
							<el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" class="mb20">
								<el-form-item label="类型" :prop="`list[${k}].value`">
									<el-switch v-model="v.dictType" inline-prompt active-value="0" inactive-value="1"
										active-text="系统" inactive-text="外置"></el-switch>
								</el-form-item>
							</el-col>
						</el-row>
					</el-col>
					<el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
						<el-form-item label="字典描述">
							<el-input v-model="state.ruleForm.remark" type="textarea" placeholder="请输入字典描述"
								maxlength="150"></el-input>
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

<script setup lang="ts" name="systemDicDialog">
import { reactive, ref } from 'vue';
import { CURDEnum } from "/@/enums/CURDEnum";
import {Eleme} from "@element-plus/icons-vue";
// 定义子组件向父组件传值/事件
const emit = defineEmits(['refresh']);

// 定义变量内容
const dicDialogFormRef = ref();
const state = reactive({
	ruleForm: {
		name: '', // 字典名称
		fieldName: '', // 字段名
		status: 0, // 字典状态
		list: [] as ListType[], // 子集字段 + 属性值
		remark: '', // 字典描述
	},
	dialog: {
        loading:false,
		isShowDialog: false,
		type: '',
		title: '',
		submitTxt: '',
	},
});

// 重置
const resetForm = () => {
	state.ruleForm = {
		name: '', // 字典名称
		fieldName: '', // 字段名
		status: 0, // 字典状态
		list: [{ id: '', label: '', value: '', dictType: "0", sort: 0, status: "0", colorType: '' }], // 子集字段 + 属性值
		remark: '', // 字典描述
	}
}

// 打开弹窗
const openDialog = (type: string, row: RowDictType) => {
	resetForm();
	if (type === CURDEnum.EDIT) {
		state.ruleForm = row;
		if (row.list === null || row.list.length === 0) {
			state.ruleForm.list = [{id: '', label: '', value: '', dictType: "0", sort: 0, status: "0", colorType: '' }]
		}
		state.dialog.title = '修改字典';
		state.dialog.submitTxt = '修 改';
		state.dialog.type = CURDEnum.INSERT;
	} else {
		state.dialog.title = '新增字典';
		state.dialog.submitTxt = '新 增';
		state.dialog.type = CURDEnum.INSERT;
	}
	state.dialog.isShowDialog = true;
};
// 关闭弹窗
const closeDialog = () => {
	state.dialog.isShowDialog = false;
};
const resetLoading = () => {
    state.dialog.loading = false;
};
// 提交
const onSubmit = () => {
    state.dialog.loading = true;
	emit('refresh', state.ruleForm);
};
// 新增行
const onAddRow = () => {
	state.ruleForm.list.push({
		id: '', label: '', value: '', dictType: "0", sort: 0, status: "0", colorType: ''
	});
};
// 删除行
const onDelRow = (k: number) => {
	state.ruleForm.list.splice(k, 1);
};

// 暴露变量
defineExpose({
	openDialog, closeDialog,resetLoading
});
</script>
