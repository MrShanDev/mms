import {BaseEntity, TableType} from "/@/types/global";

/**
* 查询对象
* @interface SysAreaTable
* @extends {TableType}
*/
export declare interface SysAreaTable extends TableType{
    data: SysAreaVo[];
}

/**
* 对象实体Vo
* @extends {BaseEntity}
*/
export declare interface SysAreaVo extends BaseEntity {
      id: string|number;
      name: string|number;
      code: string|number;
      parentCode: string|number;
      level: string|number;
      ids:string;
      children: SysAreaBo[];
}

/**
* 对象实体Bo
* @extends {BaseEntity}
*/
export declare interface SysAreaBo extends BaseEntity {
        id: string|number;
        name: string|number;
        code: string|number;
        parentCode: string|number;
        level: string|number;
        ids:string;
        children: SysAreaBo[];
}
