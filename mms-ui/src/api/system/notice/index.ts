import request from '/@/utils/request';
import {getEnv} from '/@/utils/mms';
import {AxiosPromise} from "axios";
import {SysEnum} from "/@/enums/SysEnum";
import {EncryptTypeEnum} from "/@/enums/EncryptTypeEnum";

/**
* 系统公告-Api
* Notice
*/
export function noticeApi() {
    return {
        list: <T = any>(params?: object): AxiosPromise<T> => {
            return request({
                url: getEnv()+'/system/notice/list',
                method: 'post',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        edit: <T = any>(params?: object): AxiosPromise<T> => {
            return request({
                url: getEnv()+'/system/notice',
                method: 'put',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        query: <T = any>(id?: number | string): AxiosPromise<T> => {
            return request({
                url: getEnv()+'/system/notice/'+id,
                method: 'get',
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        insert: <T = any>(params?: object): AxiosPromise<T> => {
            return request({
                url: getEnv()+'/system/notice',
                method: 'post',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        delete: <T = any>(id?: number | string): AxiosPromise<T> => {
            return request({
                url: getEnv()+'/system/notice/' + id,
                method: 'delete',
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
    };
}
