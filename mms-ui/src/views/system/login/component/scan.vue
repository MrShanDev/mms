<template>
  <div class="login-scan-container">
    <div v-if="succeed" class="flex flex-col f-y-c">
      <div class="fond16 mt-10">
        <el-icon color="#2dac34" :size="80"><ele-CircleCheckFilled /></el-icon>
      </div>
      <div class="fond16">登录成功</div>
    </div>
    <div v-else>
      <div v-if="unCode"  class="flex flex-col f-y-c">
        <div class="fond16 mt-10">
          <el-icon color="#f0a71a" :size="80"><ele-WarningFilled /></el-icon>
        </div>
        <div class="fond16 ">{{unCodeMsg}} <span class="fond16 f-c-1 shou"  @click="initQrcode">刷新</span> </div>
      </div>
      <div v-else >
        <img class="grayscale" :src="codeUrl" @click="initQrcode" style="width: 70%"  alt=""/>
        <div   class="font12 mt10 login-msg flex flex-col">
          <div class="fond16 f-c-2">剩余 {{exitTime+'s'}}</div>
          <div class="mt10">{{ $t("message.scan.text") }}</div>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup lang="ts" name="loginScan">
import {ref, onMounted, nextTick, onUnmounted,} from "vue";
import { getWxCode,queryWxCodeState } from "/@/api/system/init";
import { generateUUID } from "/@/utils/mms"
import {ElMessage} from "element-plus";
import { useI18n } from "vue-i18n";
import Cookies from "js-cookie";
import { storeToRefs } from "pinia";
import { useThemeConfig } from "/@/stores/themeConfig";
import { initFrontEndControlRoutes } from "/@/router/frontEnd";
import { initBackEndControlRoutes } from "/@/router/backEnd";
import { Session } from "/@/utils/storage";
import { formatAxis } from "/@/utils/formatTime";
import { NextLoading } from "/@/utils/loading";
import {SysEnum} from "/@/enums/SysEnum";
// 定义变量内容
// 定义变量内容
const { t } = useI18n();
const storesThemeConfig = useThemeConfig();
const { themeConfig } = storeToRefs(storesThemeConfig);
const route = useRoute();
const router = useRouter();
const succeed=ref(false);
const unCode=ref(false);
const unCodeMsg=ref("二维码已失效");
const reqKey=ref(generateUUID());
const codeUrl=ref("");
let intervalId: number | undefined;
let intervalIdQuery: number | undefined;
const exitTime=ref(60);
const emit = defineEmits<{
  update: [value: any];
}>();
const update=()=>{
  emit("update", "");
}
// 初始化生成二维码
const initQrcode = () => {
  exitTime.value=60;
  unCode.value=false;
  succeed.value=false;
  nextTick(() => {
    getWxCode(reqKey.value).then((res) => {
      if (res.code == 200) {
        codeUrl.value=res.data as string;
        intervalIdQuery= setInterval(()=>{
        getCodeState();
        },3000)

        intervalId= setInterval(()=>{
          if(exitTime.value<=0){
            clearInterval(intervalIdQuery);
            clearInterval(intervalId);
            unCode.value=true;
            return;
          }
          exitTime.value--;
        },1000)
      }
    });
  });
};
//查询结果
const  getCodeState = async ()=>{
    queryWxCodeState(reqKey.value).then(async(res) => {
        //console.log(res)
        if(res.status==0){
          succeed.value=true;
          clearInterval(intervalIdQuery);
          clearInterval(intervalId);
          Session.set(SysEnum.USER_INFO_KEY, res.data.userInfo);
          // 存储 token 到浏览器缓存
          Session.set(SysEnum.TOKEN_KEY, res.data.token);
          // 模拟数据，对接接口时，记得删除多余代码及对应依赖的引入。用于 `/src/stores/userInfo.ts` 中不同用户登录判断（模拟数据）
          Cookies.set(SysEnum.USER_INFO_NAME, res.data.userInfo.username);
          // 是否开启后端控制路由
          if (!themeConfig.value.isRequestRoutes) {
            // 前端控制路由，2、请注意执行顺序
            const isNoPower = await initFrontEndControlRoutes();
            setTimeout(()=>{
              signInSuccess(isNoPower);
            },1500)
          }
          else {
            // 模拟后端控制路由，isRequestRoutes 为 true，则开启后端控制路由
            // 添加完动态路由，再进行 router 跳转，否则可能报错 No match found for location with path "/"
            const isNoPower = await initBackEndControlRoutes();
            // 执行完 initBackEndControlRoutes，再执行 signInSuccess
            setTimeout(()=>{
              signInSuccess(isNoPower);
            },1500)
          }
        }else{
          clearInterval(intervalIdQuery);
          clearInterval(intervalId);
          unCodeMsg.value=res.msg as string;
          succeed.value=false;
          unCode.value=true;
          exitTime.value=0;
        }
    }).catch((err) => {
      //console.log(err)
      //ElMessage.error(err);
    })
}
// 时间获取
const currentTime = computed(() => {
  return formatAxis(new Date());
});
// 登录成功后的跳转
const signInSuccess = (isNoPower: boolean | undefined) => {
  if (isNoPower) {
    ElMessage.warning("抱歉，您没有登录权限");
    Session.clear();
  } else {
    // 初始化登录成功时间问候语
    let currentTimeInfo = currentTime.value;
    // 登录成功，跳到转首页
    // 如果是复制粘贴的路径，非首页/登录页，那么登录成功后重定向到对应的路径中
    //console.log(route.query?.redirect)
    if (route.query?.redirect) {
      router.push({
        path: <string>route.query?.redirect,
        query:
            Object.keys(<string>route.query?.params).length > 0
                ? JSON.parse(<string>route.query?.params)
                : "",
      });
    } else {
      router.push("/index");
    }
    // 登录成功提示
    const signInText = t("message.signInText");
    ElMessage.success(`${currentTimeInfo}，${signInText}`);
    // 添加 loading，防止第一次进入界面时出现短暂空白
    NextLoading.start();
  }
};

onUnmounted(() => {
  // 销毁事件
  clearInterval(intervalIdQuery);
  clearInterval(intervalId);
});
// 页面加载时
onMounted(() => {
  initQrcode();
});
</script>

<style scoped lang="scss">
.login-scan-animation {
  opacity: 0;
  animation-name: error-num;
  animation-duration: 0.5s;
  animation-fill-mode: forwards;
}
.login-scan-container {
  padding: 0 20px 20px;
  display: flex;
  flex-direction: column;
  text-align: center;
  @extend .login-scan-animation;
  animation-delay: 0.1s;
  :deep(img) {
    margin: auto;
  }
  .login-msg {
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--el-text-color-placeholder);
    @extend .login-scan-animation;
    animation-delay: 0.2s;
  }
}
</style>
