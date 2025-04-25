import { defineStore } from 'pinia';
import { Session } from '/@/utils/storage';

/**
 * 用户信息
 * @methods setUserInfos 设置用户信息
 */
export const useUserInfo = defineStore('userInfo', {
	state: (): UserInfosState => ({
		userInfos: {
			userName: '',
			photo: '',
			time: 0,
			email:'',
			phoneNumber: '',
			sex: '',
			loginIp:'',
			loginDate:'',
			roleName:'',
			roles: [],
			passwordStrength:"",
			authBtnList: [],
		},
	}),
	actions: {
		async setUserInfos() {
			// 存储用户信息到浏览器缓存
			if (Session.get('userInfo')) {
				this.userInfos = Session.get('userInfo');
			} else {
				const userInfos=Session.get('userInfo');
				this.userInfos = userInfos;
			}
		},

	},
});
