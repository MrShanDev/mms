import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';
import { SysEnum } from '/@/enums/SysEnum';
import {EncryptTypeEnum} from "/@/enums/EncryptTypeEnum";

/**
 * 系统用户
 */
export function userApi() {
	return {
		getList: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/list',
				method: 'post',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_OPEN,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		edit: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user',
				method: 'put',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		query: <T = any>(id?: string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/' + id,
				method: 'get',
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		insert: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user',
				method: 'post',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		delete: <T = any>(id?: string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/' + id,
				method: 'delete',
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		resetPwd: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/resetPwd',
				method: 'post',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		editHeaderImg: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/editHeaderImg',
				method: 'get',
                params: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		resetPwdSuper: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/resetPwdSuper',
				method: 'post',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		setUserRole: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/setUserRole',
				method: 'post',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		unbind: <T = any>(type: number): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/unbind?type=' + type,
				method: 'get',
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		bindingPhone: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/bindingPhone',
				method: 'post',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		bindingEmail: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/bindingEmail',
				method: 'post',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		getWxCode: <T = any>(uuid: string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/getWxCode?uuid=' + uuid,
				method: 'get',
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		queryWxCodeState: <T = any>(uuid: string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/queryWxCodeState?uuid=' + uuid,
				method: 'get',
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
		getUserInfo: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/user/getUserInfo',
				method: 'get',
				data: params,
				headers: {
					'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE,
					'Encrypt-Type': EncryptTypeEnum.AES,
				},
			});
		},
	};
}
