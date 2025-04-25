<template>
  <div class="w-100">
    <!-- 工具框 -->
    <div class="table-tool">
      <el-button-group>
        <!-- 新增 -->
        <el-button
          size="small"
          type="primary"
          @click="insert"
          v-auth="modelName+':' + functionName + ':insert'"
        >
          <SvgIcon name="iconfont icon-xinzeng" />
          {{ $t("message.form.insert") }}
        </el-button>
        <!-- 删除 -->
        <el-button
          size="small"
          type="danger"
          @click="deletes"
          v-auth="modelName+':' + functionName + ':delete'"
        >
          <SvgIcon name="iconfont icon-weibiaoti544" />
          {{ $t("message.form.delete") }}
        </el-button>
        <!-- 导入 -->
        <el-button
          size="small"
          type="success"
          @click="openDialog"
          v-auth="modelName+':' + functionName + ':import'"
        >
          <SvgIcon name="iconfont icon-daoru" />
          {{ $t("message.form.import") }}
        </el-button>
        <!-- 导出 -->
        <el-button
          size="small"
          type="warning"
          @click="exportExcel"
          v-auth="modelName+':' + functionName + ':export'"
        >
          <SvgIcon name="iconfont icon-daochu" />
          {{ $t("message.form.export") }}
        </el-button>
        <!-- 打印 -->
        <el-button
          size="small"
          type="info"
          @click="print"
          v-auth="modelName+':' + functionName + ':print'"
        >
          <SvgIcon name="iconfont icon-weibiaoti--" />
          {{ $t("message.form.print") }}
        </el-button>
      </el-button-group>
    </div>
    <!-- 导入 -->
    <el-dialog
      :title="state.dialog.title"
      v-model="state.dialog.isShowDialog"
      width="450px"
      @close="closeDialog"
    >
      <el-upload
        class="upload-file"
        action="#"
        :http-request="handleHttpUpload"
        :headers="httpFileHeaders"
        multiple
        :limit="1"
        :on-exceed="handleExceed"
      >
        <div class="flex">
          <el-button type="primary">
            <SvgIcon name="iconfont icon-daoru" />
            {{ $t("message.export.update") }}</el-button
          >
          <div class="importMsg">{{ state.importMsg }}</div>
        </div>

        <template #tip>
          <div class="flex">
            <div class="el-upload__tip">{{ $t("message.export.msg") }}</div>
            <div class="el-upload__tip template" @click="getTemplate">
              {{ $t("message.export.template") }}
            </div>
          </div>
        </template>
      </el-upload>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, inject } from "vue";
import {NextLoading} from "/@/utils/loading";
import {
  UploadProps,
  UploadRequestOptions,
  formContextKey,
  formItemContextKey,
  ElMessage,
} from "element-plus";
import printJs from "print-js";
import {
  importData,
  downloadTemplate,
  exportData,
  printData,
} from "/@/api/system/upload";
import { Session } from "/@/utils/storage";
import { isEmpty } from "/@/utils/mms";
import { CURDEnum } from "/@/enums/CURDEnum";
import { LoadingInstance } from "element-plus/es/components/loading/src/loading";
let downloadLoadingInstance: LoadingInstance;
const httpFileHeaders = ref({
  Authorization: `${Session.get("token")}`,
});
// 传入对象
interface PropsType {
  fileType?: any[]; // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
  functionName?: string;
  modelName?: string;
  tableComment:string;
  param: any;
}
// Prop 组件传过来的值
const props = withDefaults(defineProps<PropsType>(), {
  fileType: () => ["xlsx"],
  functionName: "",
  modelName: "system",
  tableComment:"",
  param: () => {},
});

// emit 通信
const emit = defineEmits<{
  insert: [value: any];
  success: [value: any];
  close: [value: any];
  print: [value: any];
  deletes: [value: any];
}>();
// 定义变量内容
const defDialogFormRef = ref();
const state = reactive({
  dialog: {
    isShowDialog: false,
    type: "",
    title: "",
    submitTxt: "",
  },
  importMsg: "",
});
// 获取 el-form 组件上下文
const formContext = inject(formContextKey, void 0);
// 获取 el-form-item 组件上下文
const formItemContext = inject(formItemContextKey, void 0);
// 超出限制
const handleExceed: UploadProps["onExceed"] = (files, uploadFiles) => {
  ElMessage.warning(`最多可选择 ${files.length} 个文件！`);
};
// 打开弹窗
const openDialog = (type: string) => {
  state.dialog.isShowDialog = true;
};
// 关闭弹窗
const closeDialog = () => {
  emit("close", "");
  state.dialog.isShowDialog = false;
};
//新增
const insert = () => {
  emit("insert", { type: CURDEnum.INSERT });
};
//删除
const deletes = () => {
  if (isEmpty(props.param.selectIds)) {
    ElMessage.warning("请选择要删除的数据");
  } else {
    emit("deletes", { type: CURDEnum.DELETE, ids: props.param.selectIds });
  }
};
//保存
const save = () => {
  emit("success", "success");
};

//打印
const print = () => {
    NextLoading.open()
  printData(props.param, props.modelName+"/"+props.functionName)
    .then((res) => {
      printTable(res.data);
    })
    .catch(async (err) => {
      ElMessage.warning(err);
    })
    .finally(() => {
        NextLoading.close();
    });
};
//下载模版
const getTemplate = () => {
  downloadTemplate(props.modelName+"/"+props.functionName, props.tableComment);
};
//导出数据
const exportExcel = () => {
  exportData(props.modelName+"/"+props.functionName, props.tableComment, props.param);
};
//上传导入数据
const handleHttpUpload = async (options: UploadRequestOptions) => {
  let formData = new FormData();
  formData.append("file", options.file);
  try {
      NextLoading.open();
    await importData(formData, props.modelName+"/"+props.functionName).then((res) => {
      state.importMsg = res.msg;
      // 调用 el-form 内部的校验方法（可自动校验）
      formItemContext?.prop && formContext?.validateField([formItemContext.prop as string]);

        NextLoading.close();
    });
  } catch (error) {
      NextLoading.close();
    options.onError(error as any);
  }
};
// 打印数据
const printTable = (printData: PrintObject) => {
  // https://printjs.crabbly.com/#documentation
  // 自定义打印
  let tableTh = "";
  let tableTrTd = "";
  let tableTd: any = {};
  // 表头
  printData.header.forEach((v) => {
    tableTh += `<th class="table-th">${v.title}</th>`;
  });
  printData.header.forEach((v) => {});
  // 表格内容
  printData.data.forEach((val, key) => {
    if (!tableTd[key]) tableTd[key] = [];
    let html = "";
    let index = 0;
    printData.header.forEach((v) => {
      for (let k in val) {
        if (k === v.key) {
          let header = printData.header.filter((h) => h.key == k);
          if (header[0].type === "TEXT") {
            html = html + `<td class="table-th table-center">${val[k]}</td>`;
          }
          if (header[0].type === "IMAGE") {
            html =
              html +
              `<td class="table-th table-center"><img src="${val[k]}" style="width:${header[0].width}px;height:${header[0].height}px;"/></td>`;
          }
        }
        index++;
      }
    });

    tableTd[key].push(html);
    tableTrTd += `<tr>${tableTd[key].join("")}</tr>`;
  });
  // 打印
  printJs({
    printable: `<div style=display:flex;flex-direction:column;text-align:center><h3>${printData.title}</h3></div><table border=1 cellspacing=0 width="100%"><tr>${tableTh}${tableTrTd}</table>`,
    type: "raw-html",
    css: [
      "//at.alicdn.com/t/c/font_4740715_kjhdas71qeq.css",
      "//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css",
      "//unpkg.com/element-plus/dist/index.css",
    ],
    style: `@media print{.mb15{margin-bottom:15px;}.el-button--small i.iconfont{font-size: 12px !important;margin-right: 5px;}}; .table-th{word-break: break-all;white-space: pre-wrap;}.table-center{text-align: center;}`,
  });
};
// 暴露变量
defineExpose({
  openDialog, //打开导入框
  closeDialog, //关闭到入口
  exportExcel, //导出数据
  printTable, //打印数据
});
</script>
<style lang="css">
.w-100 {
  width: 100%;
}
.template {
  font-size: 12px;
  font-weight: 800;
  margin-left: 10px;
  cursor: pointer;
  color: rgb(237, 122, 80);
}
.importMsg {
  height: 40px;
  line-height: 40px;
  padding-left: 15px;
}
</style>
