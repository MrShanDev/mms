import { createApp } from 'vue';

// 状态管理
import pinia from '/@/stores/index';
// 核心组件
import App from '/@/App.vue';
// 路由
import router from '/@/router';
// 自定义指令
import { directive } from '/@/directive';
// 国际化
import { i18n } from '/@/i18n';
// ELement Plus
import ElementPlus from 'element-plus';
// 栅格布局（Vue3版本）
import { GridLayout, GridItem } from 'vue-grid-layout'
// 图标系统
import SvgIcon from '/@/components/svg-icon';
import '/@/icons/iconfont/iconfont'
import '/@/styles/myStyle.scss'

// 样式
import '/@/theme/index.scss';
import 'virtual:windi.css';

// VXE Table
// ... 纯表格
import VxeTable from 'vxe-table'
import 'vxe-table/lib/style.css'
// ...

// ...可选 UI
import VxeUI from 'vxe-pc-ui'
import 'vxe-pc-ui/lib/style.css'

// 工具库
import * as mms from '/@/utils/mms';
import other from "/@/utils/other";

const app = createApp(App);
app.component('GridLayout', GridLayout)
app.component('GridItem', GridItem)
// 自定义指令
directive(app);
//自定义标签
other.elSvg(app);


// 全局属性
app.config.globalProperties.$ut = mms;

// 生产环境忽略警告
if (import.meta.env.PROD) {
    app.config.warnHandler = () => null;
}

app
    .use(pinia)
    .use(router)
    .use(i18n)
    .use(ElementPlus)
    .use(VxeUI).use(VxeTable)
    .use(SvgIcon)
    .mount('#app');
