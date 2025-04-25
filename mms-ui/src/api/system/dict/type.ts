// dict
import {TableType} from "/@/types/global";

export declare interface RowDictType {
	id: number;
	name: string;
	fieldName: string;
	label: string;
	status: number;
	createdTime: string;
	list: ListType[];
	remark: string;
}

interface SysDictTableType extends TableType {
	data: RowDictType[];
}

export declare interface SysDictState {
	tableData: SysDictTableType;
}

type ListType = {
	id: number | string;
	label: string;
	value: string;
	dictType: string;
	sort: number;
	status: string;
	colorType: string;
};
