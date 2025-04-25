import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';

/**
 * 控制台
 * @param code
 * @returns
 */
export function homeApi() {
	return {
		info: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/home/info',
				method: 'get',
				params,
			});
		},
		menu: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/home/menu',
				method: 'get',
				params,
			});
		},
		orderNum: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/home/orderNum',
				method: 'get',
				params,
			});
		},
		orderPrice: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/home/orderPrice',
				method: 'get',
				params,
			});
		},
		memberSex: <T = any>(params?: object): AxiosPromise<T> => {
			return request({
				url: getEnv() + '/system/home/memberSex',
				method: 'get',
				params,
			});
		},
	};
}
