import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';

/**
 * 系统详情
 * @returns
 */
export function sysInfo() {
	return request({
		url: getEnv() + '/system/visitor/sysInfo',
		method: 'post',
	});
}

/**
 * 获取验证码
 * @param code
 * @returns
 */
export function captcha(code: string) {
	return request({
		url: getEnv() + '/system/visitor/' + code,
		method: 'get',
	});
}

/**
 * 登录
 * @param params
 * @returns
 */
export function login(params: object) {
	return request({
		url: getEnv() + '/system/auth/login',
		method: 'post',
		headers: {
			encrypt: false,
			repeatSubmit: true,
		},
		data: params,
	});
}

export function loginByPhone(params: object) {
    return request({
        url: getEnv() + '/system/auth/loginByPhone',
        method: 'post',
        headers: {
            encrypt: false,
            repeatSubmit: true,
        },
        data: params,
    });
}

/**
 * 退出登录
 * @returns
 */
export function logout() {
	return request({
		url: getEnv() + '/system/auth/logout',
		method: 'post',
	});
}
