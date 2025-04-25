import request from '/@/utils/request';
import { download } from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from 'axios';

export const uploadImg = <T = any>(params: FormData): AxiosPromise<T> => {
	return request.postForm(getEnv() + '/system/oss/elUpload', params);
};

/**
 * 下载导入模版
 * @param modelUrl
 * @param modelName 模块名称
 * @returns
 */
export const downloadTemplate = (modelUrl: string, modelName: string) => {
	return download(getEnv() + '/' + modelUrl + '/importTemplate', {}, `${modelName}_${new Date().getTime()}.xlsx`);
};

/**
 * 导出
 * @param modelUrl
 * @param modelName 模块名称
 * @param params
 * @returns
 */
export const exportData = (modelUrl: string, modelName: string, params: FormData) => {
	return download(getEnv() + '/' + modelUrl + '/export', params, `${modelName}_${new Date().getTime()}.xlsx`);
};
/**
 * 导入
 * @returns
 * @param params
 * @param modelUrl
 */
export const importData = <T = any>(params: FormData, modelUrl: string): AxiosPromise<T> => {
	return request.postForm(getEnv() + '/' + modelUrl + '/import', params);
};
/**
 * 打印
 * @returns
 * @param params
 * @param modelUrl
 */
export const printData = <T = any>(params: FormData, modelUrl: string): AxiosPromise<T> => {
	return request.request({
		url: getEnv() + '/' + modelUrl + '/print',
		method: 'post',
		data: params,
	});
};
