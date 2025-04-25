import {BaseEntity, TableType} from "/@/types/global";

/**
* moduleName: 系统公告
* queryForm：查询对象
* tableData：数据对象
*/
export declare interface NoticeState {
    tableData: NoticeTableData;
}

/**
* 查询对象
* @interface NoticeTableData
* @extends {TableType}
*/
declare interface NoticeTableData extends TableType{
    data: NoticeEntity[];
}

/**
* 对象实体
* @interface Notice
* @extends {BaseEntity}
*/
export declare interface NoticeEntity extends BaseEntity {
      id: string|number;
      title: string|number;
      content: string|number;
      type: string|number;
}
