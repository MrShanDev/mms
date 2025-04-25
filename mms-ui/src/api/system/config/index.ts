import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';

/**
 * 系统配置
 * config
 */
export function sysConfigApi() {
	return {
		list: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config/list',
				method: 'post',
				data: params,
			});
		},
		edit: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config',
				method: 'put',
				data: params,
			});
		},
		query: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config/' + id,
				method: 'get',
			});
		},
		insert: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config',
				method: 'post',
				data: params,
			});
		},
		delete: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config/' + id,
				method: 'delete',
			});
		},
		/**
		 * 查询 配置列表明细
		 * @param params 配置对象
		 * @returns
		 */
		getConfigs: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config/getConfigs',
				method: 'post',
				data: params,
			});
		},
		/**
		 * 编辑 配置
		 * @param params 配置对象集
		 * @returns
		 */
		configs: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config/configs',
				method: 'post',
				data: params,
			});
		},
		sendSms: <T = any>(phone: string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/config/sendSms/' + phone,
				method: 'get',
			});
		},
	};
}
