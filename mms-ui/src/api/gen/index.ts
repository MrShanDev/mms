import request from '/@/utils/request';
import {getEnv} from '/@/utils/mms';
import { download } from '/@/utils/request';
export const useTableApi = (id: number) => {
	return request.get(getEnv()+'/gen/table/' + id)
}

export const useTableSubmitApi = (dataForm: any) => {
	return request.put(getEnv()+'/gen/table', dataForm)
}

export const useTableImportSubmitApi = (datasourceId: string, tableNameList: string) => {
	return request.post(getEnv()+'/gen/table/import/' + datasourceId, tableNameList)
}

export const useTableFieldSubmitApi = (tableId: number, fieldList: any) => {
	return request.put(getEnv()+'/gen/table/field/' + tableId, fieldList)
}

export const useTableSyncApi = (id: number) => {
	return request.post(getEnv()+'/gen/table/sync/' + id)
}

export const useBaseClassApi = (id: Number) => {
	return request.get(getEnv()+'/gen/baseClass/' + id)
}

export const useBaseClassListApi = () => {
	return request.get(getEnv()+'/gen/baseClass/list')
}

export const useBaseClassSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return request.put(getEnv()+'/gen/baseClass', dataForm)
	} else {
		return request.post(getEnv()+'/gen/baseClass', dataForm)
	}
}

export const useDataSourceTestApi = (id: Number) => {
	return request.get(getEnv()+'/gen/dataSource/test/' + id)
}

export const useDataSourceApi = (id: Number) => {
	return request.get(getEnv()+'/gen/dataSource/' + id)
}

export const useDataSourceListApi = () => {
	return request.get(getEnv()+'/gen/dataSource/list')
}

export const useDataSourceSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return request.put(getEnv()+'/gen/dataSource', dataForm)
	} else {
		return request.post(getEnv()+'/gen/dataSource', dataForm)
	}
}

export const useDataSourceTableListApi = (id: string) => {
	return request.get(getEnv()+'/gen/dataSource/table/list/' + id)
}

export const useFieldTypeApi = (id: Number) => {
	return request.get(getEnv()+'/gen/fieldType/' + id)
}

export const useFieldTypeListApi = () => {
	return request.get(getEnv()+'/gen/fieldType/list')
}

export const useFieldTypeSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return request.put(getEnv()+'/gen/fieldType', dataForm)
	} else {
		return request.post(getEnv()+'/gen/fieldType', dataForm)
	}
}

// 生成代码（zip压缩包）
export const useDownloadApi = (tableIds: any[]) => {
	return download(getEnv() + '/gen/generator/download?tableIds=' + tableIds.join(','), {})
}

// 生成代码（自定义目录）
export const useGeneratorApi = (tableIds: any[]) => {
	return request.post(getEnv()+'/gen/generator/code', tableIds)
}
// 代码预览
export const usePreviewApi = (id: number) => {
	return request.get(getEnv()+'/gen/generator/preview/' + id)
}

export const useProjectApi = (id: number) => {
	return request.get(getEnv()+'/gen/project/' + id)
}

export const useProjectSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return request.put(getEnv()+'/gen/project', dataForm)
	} else {
		return request.post(getEnv()+'/gen/project', dataForm)
	}
}
// 源码下载
export const useSourceDownloadApi = (id: string) => {
	location.href = getEnv() + '/gen/project/download/' + id
}
