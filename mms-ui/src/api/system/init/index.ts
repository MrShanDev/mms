import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { AxiosPromise } from "axios";

/**
 * 系统详情
 * @returns
 */
export const startBase = ( ): AxiosPromise<SystemBaseEntity> => {
	return request({
		url: getEnv() + '/common/startBase',
		method: 'post'
	});
}
/**
 * 获取微信二维码
 * @returns
 */
export const getWxCode = (key:string): AxiosPromise<String> => {
	return request({
		url: getEnv() + '/common/getWxCode?key='+key,
		method: 'post'
	});
}
/**
 * 查询二维码扫描状态
 * @returns
 */
export const queryWxCodeState = (key:string) => {
	return request({
		url: getEnv() + '/common/queryWxCodeState?key='+key,
		method: 'post'
	});
}
/**
 * 发生短信
 * @returns
 */
export const smsCode = <T = any>(bo: {type:number,phone?:string,code?:string}): AxiosPromise<T> => {
	return request({
		url: getEnv() + '/common/smsCode',
		method: 'post',
		data: bo
	});
}
/**
 * 发生邮件
 * @returns
 */
export const emailCode = <T = any>(bo: {type:number,phone?:string,code?:string}): AxiosPromise<T> => {
	return request({
		url: getEnv() + '/common/emailCode',
		method: 'post',
		data: bo
	});
}

/**
 * 获取所有字典
 * @returns
 */
export function listDictAll() {
	return request({
		url: getEnv() + '/common/listDictAll',
		method: 'post',
	});
}

/**
 * 获取左侧菜单
 * @returns
 */
export function getMenu() {
	return request({
		url: getEnv() + '/common/getMenu',
		method: 'get'
	});
}

