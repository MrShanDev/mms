import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';

export function useMenuApi() {
	return {
		list: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/function/list',
				method: 'post',
				data: params,
			});
		},
		edit: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/function',
				method: 'put',
				data: params,
			});
		},
		query: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/function/' + id,
				method: 'get',
			});
		},
		insert: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/function',
				method: 'post',
				data: params,
			});
		},
		delete: <T = any>(id?: number | string): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/function/' + id,
				method: 'delete',
			});
		},
	};
}
