import { AES, lib, enc, mode, pad } from 'crypto-js'
import { Cookie } from '/@/utils/storage';
import type { App, MethodOptions, Plugin ,Component} from 'vue'
import { useAppStore } from "/@/stores/app";
import { ComputedOptions } from "vue-demi";
import {EmptyObjectType} from "/@/types/global";
import {SysEnum} from "/@/enums/SysEnum";


/**
 * 判断是否是超级管理员
 * @param roleList 角色集合
 */
export const isSuperAdmin =(roleList:string[])=>{
    return roleList.includes(SysEnum.SUPER_ADMIN_CODE)
  };
/**
 * 获取.env文件参数
 * @param key
 */
export const getEnv = (key?: string): string => {
    if (key === undefined || key === null || key.trim() === "") {
        key = "VITE_APP_BASE_API";
    }
    if (!envCache[key]) {
        const envValue = import.meta.env[key];
        if (envValue === undefined) {
            throw new Error(`环境变量 ${key} 未定义`);
        }
        envCache[key] = envValue;
    }
    return envCache[key];
}
/**
 * 全局组件,属性安装
 * @param component 组件对象
 * @param alias 属性，【可选】
 */
export const withInstall = <T>(component: T, alias?: string) => {
    const comp = component as Component<any, any, any, ComputedOptions, MethodOptions> | any
    comp.install = (app: App) => {
        app.component(comp.__name || comp.displayName, comp)
        if (alias) {
            app.config.globalProperties[alias] = component
        }
    }
    return component as T & Plugin
}

/**
 * 日期格式化
 * @param time 时间戳
 * @param pattern 格式化样式 默认：'{y}-{m}-{d} {h}:{i}:{s}'
 * @returns 格式化时间字符串
 */
export function parseTime(time: any, pattern?: string) {
    if (arguments.length === 0 || !time) {
        return null;
    }
    const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}';
    let date;
    if (typeof time === 'object') {
        date = time;
    } else {
        if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
            time = parseInt(time);
        } else if (typeof time === 'string') {
            time = time
                .replace(new RegExp(/-/gm), '/')
                .replace('T', ' ')
                .replace(new RegExp(/\.[\d]{3}/gm), '');
        }
        if (typeof time === 'number' && time.toString().length === 10) {
            time = time * 1000;
        }
        date = new Date(time);
    }
    const formatObj: { [key: string]: any } = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
    };
    return format.replace(/{(y|m|d|h|i|s|a)+}/g, (result: string, key: string) => {
        let value = formatObj[key];
        // Note: getDay() returns 0 on Sunday
        if (key === 'a') {
            return ['日', '一', '二', '三', '四', '五', '六'][value];
        }
        if (result.length > 0 && value < 10) {
            value = '0' + value;
        }
        return value || 0;
    });
}

/**
 * 转换字符串，undefined,null等转化为""
 * @param str 原始字符串
 * @returns   转换后的字符串
 */
export const parseStrEmpty = (str: any) => {
    if (!str || str == 'undefined' || str == 'null') {
        return '';
    }
    return str;
};

/**
 * 随机生成字符串
 * @returns {string}
 */
const randomString = (num: number): string => {
    if (num < 1) {
        return "";
    }
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    const charactersLength = characters.length;
    for (let i = 0; i < num; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
};
/**
 * 生成uuid
 */
export const generateUUID = () => {
    let uuid = ''
    for (let i = 0; i < 32; i++) {
        let random = (Math.random() * 16) | 0
        if (i === 8 || i === 12 || i === 16 || i === 20) {
            uuid += '-'
        }
        uuid += (i === 12 ? 4 : i === 16 ? (random & 3) | 8 : random).toString(16)
    }
    return uuid
}
/**
 * 字符串截取
 * @param str 字符串
 * @param len 截取结束下标
 * @param start 截取开始下标
 * @param bot 是否追加...
 */
export const substring = (str: string, len: number, start?: number, bot?: boolean) => {
    if (str.length <= len || (start !== undefined && start > len)) {
        return str
    }
    return str.substring(0, len) + bot ? '...' : ''
}
/**
 * 获取浏览器指纹
 * 在缓存先获取，如果不存在则生成
 * @returns {string}
 */
export const getFingerprint = (): string => {
    let appid = Cookie.get('App-Id');
    if (appid != null && appid.length == 16) {
        return appid;
    }
    appid = randomString(16);
    Cookie.set('App-Id', appid)
    return appid;
}

/**
 * 递归地合并两个对象
 * @param source 对象1
 * @param target 对象2
 * @returns   合并后的对象1
 */
export const mergeRecursive = (source: any, target: any) => {
    for (const p in target) {
        try {
            if (target[p].constructor == Object) {
                source[p] = mergeRecursive(source[p], target[p]);
            } else {
                source[p] = target[p];
            }
        } catch (e) {
            source[p] = target[p];
        }
    }
    return source;
};

/**
 * 对象进行 字母顺序排列
 * @param obj
 * @returns
 */
export const paramsSort = (obj: EmptyObjectType) => {
    const newKey = Object.keys(obj).sort();
    const newObj: EmptyObjectType = {};
    for (const i in newKey) {
        newObj[newKey[i]] = obj[newKey[i]];
    }
    return newObj;
}
/**
 * 参数处理，url编码，并转化&拼接
 * @param {*} params  参数
 */
export const tansParams = (params: EmptyObjectType) => {
    params = paramsSort(params);
    let result = '';
    for (const propName of Object.keys(params)) {
        const value = params[propName];
        const part = encodeURIComponent(propName) + '=';
        if (value !== null && value !== '' && typeof value !== 'undefined') {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
                        const params = propName + '[' + key + ']';
                        const subPart = encodeURIComponent(params) + '=';
                        result += subPart + encodeURIComponent(value[key]) + '&';
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + '&';
            }
        }
    }
    if (result.endsWith('&')) {
        result = result.substring(0, result.length - 1)
    }
    return result;
};

/**
 * 路径处理
 * @description 去除多余的斜杠，且保证路径以斜杠结尾
 * @param p 路径
 * @returns
 */
export const getNormalPath = (p: string): string => {
    if (p.length === 0 || !p || p === 'undefined') {
        return p;
    }
    const res = p.replace('//', '/');
    if (res[res.length - 1] === '/') {
        return res.slice(0, res.length - 1);
    }
    return res;
};

/**
 * 把路径转换成驼峰命名
 * @param path 路径
 */
export const pathToCamel = (path: string): string => {
    return path.replace(/\/(\w)/g, (all, letter) => letter.toUpperCase())
}
/**
 * 对象是否为空
 * @returns true:为空，false:不为空
 * @param obj
 */
export const isEmptyObject = (obj: object) => {
    return Object.keys(obj).length === 0;
}
/**
 * 字符串是否为空
 * @param value 字符串
 * @returns true:为空，false:不为空
 */
export const isEmpty = (value: string | number) => {
    return value === null || value === undefined || value === '' || value == 0;
}

/**
 * 图片转base64
 * @param options { el: '#img' }
 * @returns
 */
// <img id="img" src="https://sxpcwlkj.oss-cn-beijing.aliyuncs.com/test/boy.jpg" width="300" height="400" alt="" />
// import { imageToBase64 } from "/@/utils/mms";
// useBase64({ el: '#img' }).then((res) => {
//   console.log('res', res)
// })
export  async function imageToBase64(options: {el: string}): Promise<string> {
    let img: HTMLImageElement = document.querySelector(options.el) as HTMLImageElement
    const response = await fetch(img.src);
    const blob = await response.blob();
    const reader = new FileReader();
    return new Promise((resolve, reject) => {
        reader.onloadend = () => {
            resolve(reader.result as string);
        };
        reader.onerror = reject;
        reader.readAsDataURL(blob);
    });
}

/**
 * 加密方法
 * @returns {string}
 * @param obj
 */
export const encrypt = (obj: EmptyObjectType|string): string => {
    //设置偏移量:（由浏览器生成,一个浏览器多起访问值固定）
    const SECRET_IV = enc.Utf8.parse(<string>Cookie.get('App-Id'));
    //设置秘钥: 由服务端返回，自动放到Cookie中
    const SECRET_KEY = enc.Utf8.parse(<string>Cookie.get('Secret-Key'));
    if (typeof obj === 'object') {
        obj = tansParams(obj);
    }else {
        obj = obj as string;
    }
    let message = enc.Utf8.parse(obj);
    let encrypted = AES.encrypt(message, SECRET_KEY, {
        iv: SECRET_IV,
        mode: mode.CBC,
        padding: pad.ZeroPadding
    })
    return enc.Base64.stringify(encrypted.ciphertext);
}

/**
 * 解密方法
 * @param word
 * @returns {string}
 */
export const decrypt = (word: string): EmptyObjectType|string => {
    //设置偏移量:（由浏览器生成,一个浏览器多起访问值固定）
    const SECRET_IV = enc.Utf8.parse(<string>Cookie.get('App-Id'));
    //设置秘钥: 由服务端返回，自动放到Cookie中
    const SECRET_KEY = enc.Utf8.parse(<string>Cookie.get('Secret-Key'));
    let base64 = enc.Base64.parse(word);
    let message = enc.Base64.stringify(base64);
    const decrypt = AES.decrypt(message, SECRET_KEY, {
        iv: SECRET_IV,
        mode: mode.CBC,
        padding: pad.ZeroPadding
    });
    if(decrypt){
        try {
            const decryptedStr = decrypt.toString(enc.Utf8);
            if (typeof decryptedStr === 'object') {
                return JSON.parse(decryptedStr);
            }
            return decryptedStr;
        }catch (e) {
            return "";
        }
    }
    return "";


}

/**
 * 获取项目配置属性,默认返回：VITE_APP_BASE_API的值
 * @param key
 */
const envCache: { [key: string]: string } = {};

/**
 * 是否外链
 * @param url 路径
 */
export const isExternalLink = (url: string): boolean => {
    return /^(https?:|\/\/|http?:|\/\/|^{{\s?apiUrl\s?}})/.test(url)
}

/**
 * 替换外链参数
 * @param url 完整的传参地址，匹配{{apiUrl}}保留，其他的替换传入的
 */
export const replaceLinkParam = (url: string): string => {
    return url.replace('{{apiUrl}}', getEnv())
}

/**
 * 转换文件大小格式
 * @param size 文件大小
 */
export const convertSizeFormat = (size: number): string => {
    const unit = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB']
    let index = Math.floor(Math.log(size) / Math.log(1024))
    let newSize = size / Math.pow(1024, index)

    // 保留的小数位数
    return newSize.toFixed(2) + ' ' + unit[index]
}


/**
 * 获取svg图标(id)列表
 */
export const getIconList = (): string[] => {
    const rs: string[] = []
    const list = document.querySelectorAll('svg symbol[id^="icon-"]')
    for (let i = 0; i < list.length; i++) {
        rs.push(list[i].id)
    }
    return rs
}

/**
 * 根据字典值，翻译为Label的el-tag ，支持css类型
 * @param dictType
 * @param dictValue
 */
export const dictTranslate = (dictType: string, dictValue: string): string => {
    if (Number.isInteger(dictValue)) {
        dictValue = dictValue + ''
    }
    if (!dictValue) {
        return ''
    }
    const appStore = useAppStore()
    let result = ''
    dictValue.split(',').forEach(value => {
        const classStyle = getDictLabelClass(appStore.dictList, dictType, value)
        const label = getDictLabel(appStore.dictList, dictType, value)
        if (classStyle) {
            result += `<span class="el-tag el-tag--${classStyle} el-tag--${appStore.componentSize}">${label}</span>`
        } else {
            result += label + '&nbsp;'
        }
    })
    return result
}

/**
 * 根据字典的值，获取字典值Label
 * @param dictList  字典列表
 * @param dictType  字典类型
 * @param dictValue 字典值
 */
export const getDictLabel = (dictList: any[], dictType: string, dictValue: string) => {
    const type = dictList.find((element: any) => element.dictType === dictType)
    if (type) {
        const val = type.dataList.find((element: any) => element.dictValue === dictValue + '')
        if (val) {
            return val.dictLabel
        } else {
            return dictValue
        }
    } else {
        return dictValue
    }
}

/**
 * 获取字典Label样式
 * @param dictList  字典列表
 * @param dictType  字典类型
 * @param dictValue 字典值
 */
export const getDictLabelClass = (dictList: any[], dictType: string, dictValue: string): string => {
    const type = dictList.find((element: any) => element.dictType === dictType)
    if (type) {
        const val = type.dataList.find((element: any) => element.dictValue === dictValue + '')
        // console.log(JSON.stringify(val))
        if (val) {
            return val.labelClass
        } else {
            return ''
        }
    } else {
        return ''
    }
}

/**
 * 获取字典数据列表
 * @param dictList  字典列表
 * @param dictType  字典类型
 */
export function getDictDataList(dictList: any[], dictType: string) {
    const type = dictList.find((element: any) => element.dictType === dictType)
    if (type) {
        return type.dataList
    } else {
        return []
    }
}


