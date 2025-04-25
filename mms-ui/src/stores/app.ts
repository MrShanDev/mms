import { defineStore } from 'pinia';
import { listDictAll } from '/@/api/system/init';
 
export const useAppStore = defineStore('appStore', {
    state: () => ({
        // 字典列表
        dictList: [],
        // 组件大小
        componentSize: 14,
    }),
    actions: {
        async getDictListAction() {
            listDictAll().then(res=>{
                this.dictList = res.data || []
            })
        }
    }
})