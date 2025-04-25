import {BaseEntity, TableType} from "/@/types/global";

/**
* moduleName: 系统配置
* queryForm：查询对象
* tableData：数据对象
*/
declare interface SysConfigState {
    tableData: SysConfigTableType;
}
interface SysConfigTableType extends TableType {
    data: RowSysConfigType[];
}
/**
 * 模块对象
 * @interface RowSysConfigType
 * @extends BaseEntity
 */
declare interface RowSysConfigType  extends BaseEntity  {
    id: string | number,
    configName: string | number,
    configKey: string | number,
    configValue: string | number,
    configType: string | number,
    columnName: string | number
}
