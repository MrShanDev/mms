<template>
	<slot v-if="getUserAuthBtnList" />
</template>

<script setup lang="ts" name="auths">
import { computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useUserInfo } from '/@/stores/userInfo';
import {Session} from "/@/utils/storage";
import {ElMessageBox} from "element-plus";

// 定义父组件传过来的值
const props = defineProps({
	value: {
		type: Array,
		default: () => [],
	},
});

// 定义变量内容
const stores = useUserInfo();
const { userInfos } = storeToRefs(stores);

// 获取 pinia 中的用户权限
const getUserAuthBtnList = computed(() => {
  if(userInfos.value==null){
    Session.remove("token");
    window.location.href = '/'; // 去登录页
    // eslint-disable-next-line vue/no-async-in-computed-properties
    ElMessageBox.alert('登录失效，请重新登录', '提示', {})
        .then(() => {})
        .catch(() => {});
    return ;
  }
	let flag = false;
	userInfos.value.authBtnList.map((val: string) => {
		props.value.map((v) => {
			if (val === v) flag = true;
		});
	});
	return flag;
});
</script>
