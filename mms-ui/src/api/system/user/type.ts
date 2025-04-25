// user
import {BaseEntity, TableType} from "/@/types/global";

export declare interface SysUserState {
	tableData: SysUserTableType;
}
interface SysUserTableType extends TableType {
	data: RowUserType[];
}

export declare interface RowUserType  extends BaseEntity  {
    userId: string;
	deptId: string;
	deptIds: string[];
	postIds: string;
	userName: string;
	nickName: string;
	password: string;
	userType: string;
	phoneNumber: string;
	email: string;
	wxUnOpenId: string;
	sex: string;
	avatar: string;
	roleCodes: string[];
	roleSign: string;
	department: string;

}



