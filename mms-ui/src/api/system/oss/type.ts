
declare interface SysOssState {
    tableData: SysOssTableType;
}
interface SysOssTableType extends TableType {
    data: RowOssType[];
}
declare interface RowOssType extends BaseEntity {
    ossId: string; // 主键
    fileName: string;
    originalName: string;
    fileSuffix: string;
    url: string;
    contentType: string;
    basePath: string;
    platform: string;
}

declare interface SysOssConfigBo extends BaseEntity {
    id: string;
    configKey: string;
    accessKey: string;
    secretKey: string;
    bucketName: string;
    prefix: string;
    endpoint: string;
    domain: string;
    isHttps: string;
    region: string;
    accessPolicy: string;
    ext1: string;
}