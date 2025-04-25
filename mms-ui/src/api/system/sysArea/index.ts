import request from '/@/utils/request';
import {getEnv} from '/@/utils/mms';
import {AxiosPromise} from "axios";
import {SysEnum} from "/@/enums/SysEnum";
import {SysAreaBo,SysAreaVo,SysAreaTable } from '/@/api/system/sysArea/type';
import {EncryptTypeEnum} from "/@/enums/EncryptTypeEnum";
/**
* 系统区域-Api
* SysArea
*/
export function sysAreaApi() {
    return {
        list: (params?: object): AxiosPromise<SysAreaVo[]> => {
            return request({
                url: getEnv()+'/system/sysArea/list',
                method: 'post',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        edit: (params?: SysAreaBo): AxiosPromise<boolean> => {
            return request({
                url: getEnv()+'/system/sysArea',
                method: 'put',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        query: (id?: number | string): AxiosPromise<SysAreaVo> => {
            return request({
                url: getEnv()+'/system/sysArea/'+id,
                method: 'get',
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        insert: (params?: SysAreaBo): AxiosPromise<boolean> => {
            return request({
                url: getEnv()+'/system/sysArea',
                method: 'post',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        delete: (id?: number | string): AxiosPromise<boolean> => {
            return request({
                url: getEnv()+'/system/sysArea/' + id,
                method: 'delete',
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
    };
}
