<template>
  <div class="block">
    <!-- 功能栏  -->
    <div class="views-tool" >
      <div class="tool-left">
        <el-form
          :inline="true"
          size="default"
          :model="state.queryForm"
          @keyup.enter="getDataList()"
        >
        <el-form-item>
              <el-input v-model="state.queryForm.code" placeholder="基类编码"></el-input>
            </el-form-item>
          <el-form-item>
            <el-button @click="getDataList()">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div class="layout-padding m-t-0 p-t-0">
      <el-card>
        <el-table
          v-loading="state.dataListLoading"
          :data="state.dataList"
          border
          style="width: 100%"
          @selection-change="selectionChangeHandle"
        >
          <el-table-column
            type="selection"
            header-align="center"
            align="center"
            width="50"
          ></el-table-column>
          <el-table-column
            prop="code"
            label="基类编码"
            header-align="center"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="packageName"
            label="基类包名"
            show-overflow-tooltip
            header-align="center"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="fields"
            label="基类字段"
            show-overflow-tooltip
            header-align="center"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            show-overflow-tooltip
            header-align="center"
            align="center"
          ></el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            header-align="center"
            align="center"
            width="150"
          >
            <template #default="scope">
              <el-button type="primary" link @click="addOrUpdateHandle(scope.row.id)"
                >编辑</el-button
              >
              <el-button type="primary" link @click="deleteBatchHandle(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="m-t-5"
          :current-page="state.page"
          :page-sizes="state.pageSizes"
          :page-size="state.limit"
          :total="state.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
        >
        </el-pagination>

        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update
          ref="addOrUpdateRef"
          @refresh-data-list="getDataList"
        ></add-or-update>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { IHooksOptions } from "/@/hooks/interface";
import { useCrud } from "/@/hooks";
import AddOrUpdate from "./add-or-update.vue";
import {getEnv} from '/@/utils/mms';
const state: IHooksOptions = reactive({
  dataListUrl: getEnv() + "/gen/baseClass/page",
  deleteUrl: getEnv() + "/gen/baseClass",
  queryForm: {
    code: "",
  },
});

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const {
  getDataList,
  selectionChangeHandle,
  sizeChangeHandle,
  currentChangeHandle,
  deleteBatchHandle,
} = useCrud(state);
const init = (id: number) => {
  getDataList();
};
defineExpose({
  init,
});
</script>
