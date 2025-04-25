<template>
    <div class="cascader">
        <el-select placeholder="请选择省份" v-model="selectedProvince" @change="handleProvinceChange">
            <el-option value="">请选择省份</el-option>
            <el-option v-for="province in provinceList" :key="province.code" :value="province.code" :label="province.name" >
                {{ province.name }}
            </el-option>
        </el-select>

        <el-select placeholder="请选择城市" v-model="selectedCity" @change="handleCityChange" :disabled="!selectedProvince">
            <el-option value="">请选择城市</el-option>
            <el-option v-for="city in cityList" :key="city.code" :value="city.code" :label="city.name">
                {{ city.name }}
            </el-option>
        </el-select>

        <el-select placeholder="请选择区县" v-model="selectedDistrict" :disabled="!selectedCity">
            <el-option value="">请选择区县</el-option>
            <el-option v-for="district in districtList" :key="district.code" :value="district.code" :label="district.name">
                {{ district.name }}
            </el-option>
        </el-select>
    </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch,onMounted } from 'vue';
import {sysAreaApi} from '/@/api/system/sysArea';
import {SysAreaBo, SysAreaVo} from "/@/api/system/sysArea/type";

// 向父组件传递消息，可以传递任意类型的数据，需要 @ 符号 监听捕获
const emit = defineEmits<{
    'update:modelValue': [value: any]
    'success': [value: any]
}>();
// 接受父组件参数
const props = defineProps({
    modelValue: {
        type: Array<string>,
        default: ['','','']
    }
});
// 初始化选中的值
const baseApi = sysAreaApi();
// 省份列表
const provinceList = ref<SysAreaVo[]>([]);
// 城市列表
const cityList = ref<SysAreaVo[]>([]);
// 区县列表
const districtList = ref<SysAreaVo[]>([]);

// 初始化选中省的值
const selectedProvince = ref("");
// 初始化选中市的值
const selectedCity = ref("");
// 初始化选中区的值
const selectedDistrict = ref("");

// 根据选择的省份，更新城市列表
const handleProvinceChange = () => {
    selectedCity.value = '';
    selectedDistrict.value = '';
    provinceList.value.forEach(p => {
        if(p.code === selectedProvince.value) {
            cityList.value = p.children;
        }
    })
};
// 根据选择的城市，更新区县列表
const handleCityChange = () => {
    selectedDistrict.value = '';
    cityList.value.forEach(p => {
        if(p.code === selectedCity.value) {
            districtList.value = p.children;
        }
    })
};
// 监听选中的值变化
watch([selectedProvince, selectedCity, selectedDistrict], (newValues) => {
    const [province, city, district] = newValues;
    //console.log('Selected:', { province, city, district });
    // 这里可以触发一个事件，将选中的值传递给父组件
    emit('update:modelValue',[province, city, district]);
    emit('success',[province, city, district]);
});
// 页面加载时
onMounted(() => {
    baseApi.list().then(res => {
        provinceList.value=res.data;
        if(props.modelValue === null ||props.modelValue === undefined || props.modelValue.length === 0) {
            return;
        }
        if(props.modelValue.length>0) {
            selectedProvince.value=props.modelValue[0];
        }
        if(props.modelValue.length>1) {
            selectedCity.value=props.modelValue[1];
            provinceList.value.forEach(p => {
                if(p.code === selectedProvince.value) {
                    cityList.value = p.children;
                }
            })
        }
        if(props.modelValue.length>2) {
            selectedDistrict.value=props.modelValue[2];
            cityList.value.forEach(p => {
                if(p.code === selectedCity.value) {
                    districtList.value = p.children;
                }
            })
        }
    });
});
</script>

<style scoped>
.cascader {
    width: 100%;
    display: flex;
    gap: 10px;
}

select {
    padding: 5px;
    font-size: 16px;
}
</style>
