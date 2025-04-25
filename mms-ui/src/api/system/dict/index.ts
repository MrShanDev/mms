import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';

/**
 * 字典
 */
export function dictApi() {
	return {
		list: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/dict/list',
				method: 'post',
				data: params,
			});
		},
		edit: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/dict',
				method: 'put',
				data: params,
			});
		},
		query: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/dict/' + id,
				method: 'get',
			});
		},
		insert: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/dict',
				method: 'post',
				data: params,
			});
		},
		delete: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/dict/' + id,
				method: 'delete',
			});
		},
	};
}
