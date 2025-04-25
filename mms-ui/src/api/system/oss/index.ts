import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';

/**
 * oss
 */
export function ossApi() {
	return {
		list: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss/list',
				method: 'post',
				data: params,
			});
		},
		edit: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss',
				method: 'put',
				data: params,
			});
		},
		query: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss/' + id,
				method: 'get',
			});
		},
		insert: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss',
				method: 'post',
				data: params,
			});
		},
		delete: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss/' + id,
				method: 'delete',
			});
		},
		//=======================OSS配置 Start==========================
		// 存储配置详情
		sysConfigKey: <T = any>(key?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss/config/' + key,
				method: 'get',
			});
		},
		// 查询默认存储配置
		sysOssConfigQuery: <T = any>(): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss/config/queryDef',
				method: 'get',
			});
		},
		// 编辑存储配置
		sysOssConfigEdit: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/oss/config/edit',
				method: 'put',
				data: params,
			});
		},
		//=======================OSS配置 End==========================
	};
}
