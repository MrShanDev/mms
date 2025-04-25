import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';

/**
 * 系统用户
 */
export function roleApi() {
	return {
		getList: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/role/list',
				method: 'post',
				data: params,
			});
		},
		edit: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/role',
				method: 'put',
				data: params,
			});
		},
		query: <T = any>(id?: string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/role/' + id,
				method: 'get',
			});
		},
		insert: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/role',
				method: 'post',
				data: params,
			});
		},
		delete: <T = any>(id?: string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/role/' + id,
				method: 'delete',
			});
		},
		menuList: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/function/menuList',
				method: 'post',
				data: params,
			});
		},
	};
}
