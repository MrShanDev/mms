import request from '/@/utils/request';
import {getEnv} from "/@/utils/mms";
import {AxiosPromise} from "axios";
import {SysEnum} from "/@/enums/SysEnum";
import {EncryptTypeEnum} from "/@/enums/EncryptTypeEnum";
import {${FunctionName}Bo,${FunctionName}Vo,${FunctionName}Table } from '/@/views/${moduleName}/${functionName}/type';
/**
* ${tableComment}-Api
* ${FunctionName}
*/
export function ${functionName}Api() {
    return {
        list: (params?: object): AxiosPromise<${FunctionName}Vo[]> => {
            return request({
                url: getEnv()+'/${moduleName}/${functionName}/list',
                method: 'post',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        edit: (params?: ${FunctionName}Bo): AxiosPromise<boolean> => {
            return request({
                url: getEnv()+'/${moduleName}/${functionName}',
                method: 'put',
                data: params,
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        query: (id?: number | string): AxiosPromise<${FunctionName}Vo> => {
            return request({
                url: getEnv()+'/${moduleName}/${functionName}/'+id,
                method: 'get',
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
        insert: (params?: ${FunctionName}Bo): AxiosPromise<boolean> => {
            return request({
                url: getEnv()+'/${moduleName}/${functionName}',
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
                url: getEnv()+'/${moduleName}/${functionName}/' + id,
                method: 'delete',
                headers: {
                    'Encrypt-State':SysEnum.SYS_COMMON_STATE_CLOSE,
                    'Encrypt-Type': EncryptTypeEnum.AES
                },
            });
        },
    };
}
