import Cookies from 'js-cookie';

/**
 * window.localStorage 浏览器永久缓存
 * @method set 设置永久缓存
 * @method get 获取永久缓存
 * @method remove 移除永久缓存
 * @method clear 移除全部永久缓存
 */
export const Local = {
	// 查看 v2.4.3版本更新日志
	setKey(key: string) {
		// @ts-ignore
		return `${__NEXT_NAME__}:${key}`;
	},
	// 设置永久缓存
	set<T>(key: string, val: T) {
		window.localStorage.setItem(Local.setKey(key), JSON.stringify(val));
	},
	// 获取永久缓存
	get(key: string) {
		let json = <string>window.localStorage.getItem(Local.setKey(key));
		return JSON.parse(json);
	},
	// 移除永久缓存
	remove(key: string) {
		window.localStorage.removeItem(Local.setKey(key));
	},
	// 移除全部永久缓存
	clear() {
		window.localStorage.clear();
	},
};

/**
 * window.sessionStorage 浏览器临时缓存
 * 关闭窗口就被清除
 * @method set 设置临时缓存
 * @method get 获取临时缓存
 * @method remove 移除临时缓存
 * @method clear 移除全部临时缓存
 */
export const Session = {
	// 设置临时缓存
	set<T>(key: string, val: T) {
		if (key === 'token') return Cookies.set(key, val as string);
		window.sessionStorage.setItem(Local.setKey(key), JSON.stringify(val));
	},
	// 获取临时缓存
	get(key: string) {
		if (key === 'token') return Cookies.get(key);
		let json = <string>window.sessionStorage.getItem(Local.setKey(key));
		return JSON.parse(json);
	},
	// 移除临时缓存
	remove(key: string) {
		if (key === 'token') return Cookies.remove(key);
		window.sessionStorage.removeItem(Local.setKey(key));
	},
	// 移除全部临时缓存
	clear() {
		Cookies.remove('token');
		window.sessionStorage.clear();
	},
};


export const Cookie = {
	// 设置cookie
	set<T>(key: string, val: T, expires?: number) {
		return Cookies.set(key, JSON.stringify(val), { expires: expires ? expires : 1, path: '/' });
	},
	// 获取cookie
	get(key: string) {
		return Cookies.get(key);
	},
	// 移除cookie
	remove(key: string) {
		Cookies.remove(key);
	},
	//清空全部cookie
	clearAllCookie() {
		var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
		if (keys) {
			for (var i = keys.length; i--;) {
				document.cookie =
					keys[i] + "=0;path=/;expires=" + new Date(0).toUTCString(); //清除当前域名下
				document.cookie =
					keys[i] + "=0;path=/;domain=" + document.domain + ";expires=" + new Date(0).toUTCString();
				document.cookie =
					keys[i] + "=0;path=示例：/index.vue（不同域的path,也就是你清除不了的cookie）;domain=示例：10.10.10.208（不同域的domain,也就是你清除不了的cookie）;expires=" +
					new Date(0).toUTCString();
			}
		}
	},

}
