import request from '/@/utils/request';
import { getEnv } from '/@/utils/mms';
import { SysEnum } from '/@/enums/SysEnum';
import { EncryptTypeEnum } from '/@/enums/EncryptTypeEnum';
import type { ${FunctionName}Bo, ${FunctionName}Vo } from './type';
type Result<T> = { code: number; msg: string; data: T };
const headers = { 'Encrypt-State': SysEnum.SYS_COMMON_STATE_CLOSE, 'Encrypt-Type': EncryptTypeEnum.AES };
// 宿主请求拦截器返回 JSON 响应体，而非 AxiosResponse。
const call = <T>(config: object): Promise<T> => request(config) as unknown as Promise<T>;
export function ${functionName}Api() {
  const prefix = getEnv() + '/${moduleName}/${functionName}';
  return {
    <#if formLayout==3>
    singleton: () => call<Result<${FunctionName}Vo | null>>({ url: prefix + '/singleton', method: 'get', headers }),
    saveSingleton: (data: ${FunctionName}Bo) => call<Result<boolean>>({ url: prefix + '/singleton', method: 'put', data, headers }),
    <#else>
    list: (data?: object) => call<<#if formLayout==1>{ rows: ${FunctionName}Vo[]; total: number }<#else>Result<${FunctionName}Vo[]></#if>>({ url: prefix + '/list', method: 'post', data, headers }),
    query: (id?: string | number) => call<Result<${FunctionName}Vo>>({ url: prefix + '/' + id, method: 'get', headers }),
    insert: (data?: ${FunctionName}Bo) => call<Result<boolean>>({ url: prefix, method: 'post', data, headers }),
    edit: (data?: ${FunctionName}Bo) => call<Result<boolean>>({ url: prefix, method: 'put', data, headers }),
    delete: (ids?: string | number) => call<Result<boolean>>({ url: prefix + '/' + ids, method: 'delete', headers }),
    </#if>
  };
}
