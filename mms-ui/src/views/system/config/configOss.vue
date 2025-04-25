<template>
  <div class="m25">
    <div class="mb15">
      <el-segmented
        v-model="ossConfigValue"
        :options="options"
        block
        @change="changeOssConfig"
      >
        <template #default="{ item }">
          <div>{{ item.label }}</div>
        </template>
      </el-segmented>
    </div>
    <div class="m-t-10" v-if="ossConfigValue == 'test'">
      <fast-img
        v-model=testImage
        @update:fileUrl="onUrlLoginImage"
        width="400px"
        :cropperHeight="950"
        :cropperWidth="1500"
      />
      <div class="mt15">
        <p>{{ testImage }}</p>
      </div>
    </div>
    <el-form
      v-else
      ref="deptDialogFormRef"
      :model="ossConfig.ruleForm"
      size="default"
      label-width="150px"
    >
      <el-form-item label="是否启用" prop="status">
        <el-switch
          v-model="ossConfig.ruleForm.status"
          active-color="#13ce66"
          inactive-color="#ff4949"
          inline-prompt
          active-text="启用"
          inactive-text="禁用"
          :active-value="0"
          :inactive-value="1"
        >
        </el-switch>
      </el-form-item>
      <el-form-item label="空间域名 Domain" prop="domain">
        <el-input
          v-model="ossConfig.ruleForm.domain"
          placeholder="自定义域名 (以https://开始 , 结尾不用加 /)"
        ></el-input>
      </el-form-item>
      <template v-if="ossConfig.ruleForm.configKey == 'local-plus'">
        <el-form-item label="本地保存路径" prop="region">
          <el-input
            v-model="ossConfig.ruleForm.ext1"
            placeholder="本地保存路径 (本地绝对路径，结尾不用加 /)"
          ></el-input>
        </el-form-item>
      </template>
      <template v-else>
        <el-form-item
          :label="
            ossConfig.ruleForm.configKey == 'tencent-cos' ? 'secretId' : 'accessKey'
          "
          prop="accessKey"
        >
          <el-input
            v-model="ossConfig.ruleForm.accessKey"
            type="textarea"
            placeholder="accessKey"
          ></el-input>
        </el-form-item>
        <el-form-item label="secretKey" prop="secretKey">
          <el-input
            v-model="ossConfig.ruleForm.secretKey"
            type="textarea"
            placeholder="secretKey"
          ></el-input>
        </el-form-item>
        <el-form-item label="存储空间名称" prop="bucketName">
          <el-input
            v-model="ossConfig.ruleForm.bucketName"
            placeholder="存储空间名称"
          ></el-input>
        </el-form-item>
        <el-form-item
          v-if="
            ossConfig.ruleForm.configKey == 'aliyun-oss' ||
            ossConfig.ruleForm.configKey == 'amazon-s3'
          "
          label="地域节点"
          prop="region"
        >
          <el-input
            v-model="ossConfig.ruleForm.endpoint"
            placeholder="地域节点"
          ></el-input>
        </el-form-item>
        <el-form-item v-else label="所属地域" prop="region">
          <el-input v-model="ossConfig.ruleForm.region" placeholder="所属地域"></el-input>
        </el-form-item>
      </template>
      <el-form-item label="基础路径" prop="region">
        <el-input
          v-model="ossConfig.ruleForm.prefix"
          placeholder="基础路径 (首尾都不用加 /)"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmitConfig">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script setup lang="ts" name="systemSysConfigDialog">
import { reactive, defineAsyncComponent, ref } from "vue";
import FastImg from "/@/components/fast-upload/img.vue";
import { ElMessage } from "element-plus";
import { ossApi } from "/@/api/system/oss";
const testImage=ref("");
const props = defineProps({
  value: {
    type: Array,
    default: () => [],
  },
});

const configApi = ossApi();
// 配置
const ossConfigValue = ref("local-plus");
const options = [
  {
    label: "测试",
    value: "test",
  },
  {
    label: "本地",
    value: "local-plus",
  },
  {
    label: "阿里云",
    value: "aliyun-oss",
  },
  {
    label: "腾讯云",
    value: "tencent-cos",
  },
  {
    label: "华为云",
    value: "huawei-obs",
  },
  {
    label: "七牛云",
    value: "qiniu-kodo",
  },
  {
    label: "百度云",
    value: "baidu-bos",
  },
  {
    label: "MinIo",
    value: "minio",
  },
  {
    label: "AmazonS3",
    value: "amazon-s3",
  },
];
const onUrlLoginImage = (data: string) => {
  testImage.value = data;
};
// 查询配置
const queryConfig = () => {
  configApi.sysConfigKey(ossConfigValue.value).then((res) => {
    if (res.data == null) {
      return;
    }
    ossConfig.ruleForm.configKey = res.data.configKey;
    ossConfig.ruleForm.accessKey = res.data.accessKey;
    ossConfig.ruleForm.secretKey = res.data.secretKey;
    ossConfig.ruleForm.bucketName = res.data.bucketName;
    ossConfig.ruleForm.domain = res.data.domain;
    ossConfig.ruleForm.region = res.data.region;
    ossConfig.ruleForm.status = res.data.status;
    ossConfig.ruleForm.endpoint = res.data.endpoint;
    ossConfig.ruleForm.prefix = res.data.prefix;
    ossConfig.ruleForm.accessPolicy = res.data.accessPolicy;
    ossConfig.ruleForm.isHttps = res.data.isHttps;
    ossConfig.ruleForm.ext1 = res.data.ext1;
  });
};
// 选择配置
const changeOssConfig = (obj: string) => {
  resetForm();
  ossConfigValue.value = obj;
  ossConfig.ruleForm.configKey = obj;
  queryConfig();
};
//变量
const ruleForm = reactive<SysOssConfigBo>({
  id: "",
  configKey: "",
  accessKey: "",
  secretKey: "",
  bucketName: "",
  prefix: "",
  endpoint: "",
  domain: "",
  isHttps: "N",
  region: "",
  accessPolicy: "public-read",
  ext1: "",
  sort: 1,
  status: 1,
  revision: "",
  tenantId: "",
  createdBy: "",
  createdTime: "",
  updatedBy: "",
  updatedTime: "",
  remark: "",
});
const resetForm = () => {
  ruleForm.id = "";
  ruleForm.configKey = "";
  ruleForm.accessKey = "";
  ruleForm.secretKey = "";
  ruleForm.bucketName = "";
  ruleForm.prefix = "";
  ruleForm.endpoint = "";
  ruleForm.domain = "";
  ruleForm.isHttps = "N";
  ruleForm.region = "";
  ruleForm.accessPolicy = "public-read";
  ruleForm.status = 1;
  ruleForm.ext1 = "";
  ruleForm.sort = 1;
  ruleForm.revision = "";
  ruleForm.tenantId = "";
  ruleForm.createdBy = "";
  ruleForm.createdTime = "";
  ruleForm.updatedBy = "";
  ruleForm.updatedTime = "";
  ruleForm.remark = "";
};
const ossConfig = reactive({
  ruleForm: ruleForm,
  dialog: {
    isShowDialog: false,
    title: "对象存储配置",
  },
});
// 提交配置
const onSubmitConfig = () => {
  ossConfig.ruleForm.configKey = ossConfigValue.value;
  configApi
    .sysOssConfigEdit(ossConfig.ruleForm)
    .then((res) => {
      ElMessage.success(res.msg);
    })
    .catch((err) => {
      ElMessage.warning(err);
    })
    .finally(() => {});
};
// 打开配置
const initData = () => {
  console.log("initData");
  configApi
    .sysOssConfigQuery()
    .then((res) => {
      if (res.data == null) {
        return;
      }
      ossConfigValue.value = res.data;
    })
    .finally(() => {
      queryConfig();
    });
};
// 暴露变量
defineExpose({
  initData,
});
</script>
<style scoped lang="scss">
.ml-10 {
  margin-left: 10px;
}
.m25 {
  margin: 25px;
}
.el-segmented {
  --el-segmented-item-selected-color: #fff;
  --el-segmented-item-selected-bg-color: #666dff;
  --el-border-radius-base: 16px;
}
</style>
