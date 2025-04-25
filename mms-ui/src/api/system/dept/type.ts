import {BaseEntity, TableType} from "/@/types/global";

/**
* moduleName: 系统部门
* queryForm：查询对象
* tableData：数据对象
*/
export declare interface DeptState {
    tableData: DeptTableData;
}

/**
* 查询对象
* @interface DeptTableData
* @extends {TableType}
*/
declare interface DeptTableData extends TableType{
    data: DeptEntity[];
}

/**
* 对象实体
* @interface DeptEntity
* @extends {BaseEntity}
*/
export declare interface DeptEntity extends BaseEntity {
      deptId: string|number;
      parentId: string|number;
      deptName: string|number;
      leader: string|number;
      phone: string|number;
      email: string|number;
      deptIds:string[];
      children?: DeptEntity[];
}
