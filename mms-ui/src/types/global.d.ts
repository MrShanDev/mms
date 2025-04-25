// 申明外部 npm 插件模块
import type {FormRules} from "element-plus";

declare module 'vue-grid-layout';
declare module 'qrcodejs2-fixes';
declare module 'splitpanes';
declare module 'js-cookie';
declare module '@wangeditor/editor-for-vue';
declare module 'js-table2excel';
declare module 'qs';
declare module 'sortablejs';

// 声明一个模块，防止引入文件时报错
declare module '*.json';
declare module '*.png';
declare module '*.jpg';
declare module '*.scss';
declare module '*.ts';
declare module '*.js';

// 声明文件，*.vue 后缀的文件交给 vue 模块来处理
declare module '*.vue' {
	import type { DefineComponent } from 'vue';
	const component: DefineComponent<{}, {}, any>;
	export default component;
}

declare module '@vue/runtime-core' {
    interface ComponentCustomProperties {
        $ut: typeof import('/@/utils/mms');
    }
}

// 声明文件，定义全局变量
/* eslint-disable */
declare interface Window {
	nextLoading: boolean;
	BMAP_SATELLITE_MAP: any;
	BMap: any;
}
// 声明接口响应
declare type R<T = any> = {
	code: number;
	msg: string;
	data: T;
	state: boolean;
}
// 声明路由当前项类型
declare type RouteItem<T = any> = {
	path: string;
	name?: string | symbol | undefined | null;
	redirect?: string;
	k?: T;
	meta?: {
		title?: string;
		isLink?: string;
		isHide?: boolean;
		isKeepAlive?: boolean;
		isAffix?: boolean;
		isIframe?: boolean;
		roles?: string[];
		icon?: string;
		isDynamic?: boolean;
		isDynamicPath?: string;
		isIframeOpen?: string;
		loading?: boolean;
	};
	children: T[];
	query?: { [key: string]: T };
	params?: { [key: string]: T };
	contextMenuClickId?: string | number;
	commonUrl?: string;
	isFnClick?: boolean;
	url?: string;
	transUrl?: string;
	title?: string;
	id?: string | number;
};

// 声明路由 to from
declare interface RouteToFrom<T = any> extends RouteItem {
	path?: string;
	children?: T[];
}

// 声明路由当前项类型集合
declare type RouteItems<T extends RouteItem = any> = T[];

// 声明 ref
declare type RefType<T = any> = T | null;

// 声明 HTMLElement
declare type HtmlType = HTMLElement | string | undefined | null;

// 申明 children 可选
declare type ChilType<T = any> = {
	children?: T[];
};

// 申明 数组
declare type EmptyArrayType<T = any> = T[];

// 申明 对象
declare type EmptyObjectType<T = any> = {
	[key: string]: T;
};

// 申明 select option
declare type SelectOptionType = {
	value: string | number;
	label: string | number;
};

// 鼠标滚轮滚动类型
declare interface WheelEventType extends WheelEvent {
	wheelDelta: number;
}

// table 数据格式公共类型
declare interface TableType<T = any> {
	total: number;
	loading: boolean;
	param: {
		pageNum: number;
		pageSize: number;
		[key: string]: T;
	};
}
// 表单验证
declare interface  VerifyType<T = any> {
	dialog?:boolean;
	key?:string;
	title?:string;
	form:T;
	rules:FormRules<T>
}

/**
 * 基础对象
 * @interface BaseEntity
 * @template T
 */
declare interface BaseEntity<T = any> {
	status: number;
	sort: number;
	remark: string;
	tenantId?: string;
	revision:? number;
	createdBy?: string;
	createdTime?: T;
	updatedBy?: string;
	updatedTime?: T;
	[key: string]: T;
}
// 接口加密请求参数
declare interface ApiSecurityParam {
	appId: string; //应用id
	key: string;   //RSA加密后的aes秘钥
	data: EmptyObjectType; //加密原串
	sign: string; //签名
	timestamp: number; //时间戳
	nonce: string; //请求唯一标识

}
declare global {
    interface Window {
        nextLoading: boolean;
    }
}

