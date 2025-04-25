import axios, { AxiosInstance } from 'axios';
import {ElMessage, ElMessageBox, ElNotification} from 'element-plus';
import { Session } from '/@/utils/storage';
import qs from 'qs';
import { errorCode } from '/@/utils/errorCode';
import FileSaver from 'file-saver';
import { HttpStatus } from '/@/enums/RespEnum';
import { encrypt, decrypt, tansParams, getFingerprint } from "/@/utils/mms"
import { getEnv } from '/@/utils/mms';
import { SysEnum } from "/@/enums/SysEnum";
import {ApiSecurityParam} from "/@/types/global";
import { logout } from '/@/api/system/login';
import {NextLoading} from "/@/utils/loading";
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';
// 设置浏览器指纹 / App-Id
axios.defaults.headers['App-Id'] = getFingerprint();
// 配置新建一个 axios 实例
const request: AxiosInstance = axios.create({
	baseURL: getEnv("VITE_APP_BASE"),
	timeout: 50000,
	headers: { 'Content-Type': 'application/json' },
	paramsSerializer: {
		serialize(params) {
			return qs.stringify(params, { allowDots: true });
		},
	},
});

// 添加请求拦截器
request.interceptors.request.use(
	(config) => {
		// 对应国际化资源文件后缀
		config.headers['Content-Language'] = "CN";
		// 是否需要防止数据重复提交
		const isRepeatSubmit = (Object.keys(config.headers).includes("isRepeatSubmit") && config.headers.isRepeatSubmit === "true");
		// 是否需要加密
		const isEncrypt = (Object.keys(config.headers).includes("Encrypt-Type") && config.headers["Encrypt-State"] == SysEnum.SYS_COMMON_STATE_OPEN);
		// 追加时间戳，防止GET请求缓存
		if (config.method?.toUpperCase() === 'GET') {
			config.params = { ...config.params, t: new Date().getTime() }
		} else {
			if (config.data == null) {
				config.data = { ...config.data, t: new Date().getTime() }
			}
		}
		// 在发送请求之前做些什么 token
		if (Session.get(SysEnum.TOKEN_KEY)) {
			config.headers!['Authorization'] = `${Session.get(SysEnum.TOKEN_KEY)}`;
		}
        // if((config.method?.toUpperCase() === 'PUT'||config.method?.toUpperCase() === 'DELETE')){
        //     if(loadingInstance){
        //         loadingInstance.close();
        //     }
        //     loadingInstance = ElLoading.service({ text: '加载中请稍候...', background: 'rgba(0, 0, 0, 0.7)' });
        // }
		if (isRepeatSubmit && (config.method?.toUpperCase() === 'POST' || config.method?.toUpperCase() === 'PUT')) {
			//用时间戳作为请求唯一标识
			config.data = { ...config.data, nonce: new Date().getTime() }
		}
		if (isEncrypt && (config.method?.toUpperCase() === 'POST' || config.method?.toUpperCase() === 'PUT')) {
			//进行加密
			if (typeof config.data === 'object') {
				// for (let k in config.params) {
				// 	config.data[k]=encrypt(config.data[k])
				// }
				config.data = {
					appId: config.headers['App-Id'],
					data: config.data,
					sign: encrypt(config.data),
					timestamp: new Date().getTime()
				} as ApiSecurityParam
			} else {
				//console.error("The request parameter should be Object");
				config.data = {}
			}
		}
		return config;
	},
	(error) => {
		// 对请求错误做些什么
		return Promise.reject(error);
	}
);

// 添加响应拦截器
request.interceptors.response.use((response) => {
	const res = response.data;
	if (res.code && res.code !== HttpStatus.SUCCESS) {
		// `token` 过期或者账号已在别处登录
        // 修改响应拦截器中的401处理
        if (res.code === 401 || res.code === 4001) {
            ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
                confirmButtonText: '重新登录',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                logout().then(res => {
                    Session.clear();
                    // ✅ 使用原生跳转代替路由跳转
                    window.location.href = '/';  // 完全刷新页面
                    // 或者使用带hash的路由
                    // window.location.hash = '/login';
                });
            }).catch(() => { });
            return Promise.reject('无效的会话，或者会话已过期，请重新登录。');
        }
		if (res.code === 403) {
			ElNotification({
				title: '注意',
				message: '您的请求没有权限~',
				type: 'warning',
			})
			return Promise.reject(res.msg);
		}
		if (res.code === 404) {
			ElNotification({
				title: '注意',
				message: '您的请求不存在~',
				type: 'warning',
			})
			return Promise.reject(res.msg);
		}
		return Promise.reject(res.msg);
	}
	else {
		//解密
		if (res.isSecurity === true) {
			if (res.data.hasOwnProperty("total")) {
				res.total = Number(res.data.total);
				res.rows = decrypt(res.data.rows);
				delete res.data;
			} else {
				res.data = decrypt(res.data)
			}
			delete res.isSecurity;
		}
		if (response.headers["content-type"] === "application/octet-stream;charset=UTF-8") {
			res.fileName = response.headers["content-disposition"].split("=")[1];
		}
		return res;
	}
},
	(error) => {
		// 对响应错误做点什么
		if (error.message.indexOf('timeout') != -1) {
			ElMessage.error('网络超时');
		} else if (error.message == 'Network Error') {
			ElMessage.error('网络连接错误');
		} else {
			if (error.response.data) ElMessage.error(error.response.statusText);
			else ElMessage.error('服务没启动 / 接口路径找不到');
		}
		return Promise.reject(error);
	}
);


// 通用下载方法
export function download(url: string, params: any, fileName?: string) {
    NextLoading.open()
	// prettier-ignore
	return request.post(url, params, {
		transformRequest: [
			(params: any) => {
				return tansParams(params);
			}
		],
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		responseType: 'blob'
	}).then(async (resp: any) => {
		//console.log(fileName ? fileName : resp.fileName);
		if (resp.type !== 'application/json') {
			const blob = new Blob([resp]);
			FileSaver.saveAs(blob, fileName ? fileName : resp.fileName);
		} else {
			const resText = await resp.data.text();
			const rspObj = JSON.parse(resText);
			const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default'];
			ElMessage.error(errMsg);
		}
        NextLoading.close();
	}).catch((r: any) => {
		//console.error(r);
		ElMessage.error('下载文件出现错误，请联系管理员！');
        NextLoading.close();
	});
}



// 导出 axios 实例
export default request;
