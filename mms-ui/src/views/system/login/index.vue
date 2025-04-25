<template>
  <div class="login-container flex">
    <div class="login-left">
      <div class="login-left-logo">
        <img :src="getThemeConfig.logo" alt="logo" />
        <div class="login-left-logo-text">
          <span>{{ getThemeConfig.globalTitle }}</span>
          <span class="login-left-logo-text-msg">{{
              getThemeConfig.globalDescription
            }}</span>
        </div>
      </div>
      <div class="login-left-img">
        <img :src="getThemeConfig.loginBg" alt="loginMain" />
      </div>
      <img :src="loginBg" class="login-left-waves" alt="bg" />
    </div>
    <div class="login-right flex">
      <div class="login-right-warp flex-margin">
        <div class="login-right-warp-mian">
          <div class="login-right-warp-main-title">
            {{ getThemeConfig.globalTitle }} 欢迎您！
          </div>
          <div class="login-right-warp-main-form" v-if="getThemeConfig.loginType.length>0" >
            <el-tabs v-model="state.tabsActiveName" @tab-change="changeTab" >
              <el-tab-pane
                  v-if="getThemeConfig.loginType.includes('1')"
                  :label="$t('message.label.one1')"
                  name="account"
              >
                <Account :captchaState="getThemeConfig.captchaState" />
              </el-tab-pane>
              <el-tab-pane
                  v-if="getThemeConfig.loginType.includes('2')"
                  :label="$t('message.label.two2')"
                  name="mobile"
              >
                <Mobile />
              </el-tab-pane>
              <el-tab-pane
                  v-if="getThemeConfig.loginType.includes('3')"
                  label="扫码登录"
                  name="code"
              >
                <Scan v-if="state.isScan"    />
              </el-tab-pane>
            </el-tabs>
            <div class="font12 mt30 login-animation4 login-msg">
              {{ $t("message.mobile.msgText") }}
            </div>
          </div>
          <div class="error mx-auto mt-20" v-else >
            <el-empty :description="state.msg" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="loginIndex">
import { defineAsyncComponent, onMounted, reactive, computed } from "vue";
import { storeToRefs } from "pinia";
import { useThemeConfig } from "/@/stores/themeConfig";
import { NextLoading } from "/@/utils/loading";
import logoMini from "/@/assets/image.svg";
import loginMain from "/@/assets/login_main01.svg";
import loginBg from "/@/assets/login-bg.svg";
import { startBase } from "/@/api/system/init";

// 引入组件
// 账号登录
const Account = defineAsyncComponent(
    () => import("/@/views/system/login/component/account.vue")
);
const Mobile = defineAsyncComponent(
    () => import("/@/views/system/login/component/mobile.vue")
);
const Scan = defineAsyncComponent(
    () => import("/@/views/system/login/component/scan.vue")
);

// 定义变量内容

const storesThemeConfig = useThemeConfig();
const { themeConfig } = storeToRefs(storesThemeConfig);
const state = reactive({
  tabsActiveName: "account",
  isScan: false,
  msg:"~ 后端接口异常！"
});

// 获取布局配置信息
const getThemeConfig = reactive({
  globalTitle: themeConfig.value.globalTitle,
  globalDescription: themeConfig.value.globalViceTitleMsg,
  logo: logoMini,
  loginType: [] as Array<string>,
  loginBg: loginMain,
  captchaState: false,
  codeUrl:''
});

const baseStart = () => {
  startBase().then((res) => {
    if (res.code == 200) {
      getThemeConfig.globalTitle = res.data.globalTitle;
      getThemeConfig.globalDescription = res.data.globalDescription;
      getThemeConfig.logo = res.data.logo;
      getThemeConfig.loginType = res.data.loginType;
      getThemeConfig.captchaState = res.data.captchaState;
      if (res.data.loginBg != null && res.data.loginBg.length > 0) {
        getThemeConfig.loginBg = res.data.loginBg;
      }
      if(res.data.codeUrl!=null && res.data.codeUrl.length>0){
        getThemeConfig.codeUrl=res.data.codeUrl;
      }
    }
  }).catch((err) => {
    state.msg="后端接口异常: "+err;
  });
};

const changeTab=(e:string)=>{
  if(e==="code"){
    state.isScan=true;
  }else {
    state.isScan=false;
  }
}
// 页面加载时
onMounted(() => {
  baseStart();
  NextLoading.done();
});
</script>

<style scoped lang="scss">
.login-container {
  height: 100%;
  background: var(--el-color-white);
  .login-left {
    flex: 1;
    position: relative;
    background-color: rgba(211, 239, 255, 1);
    margin-right: 100px;
    .login-left-logo {
      display: flex;
      align-items: center;
      position: absolute;
      top: 50px;
      left: 80px;
      z-index: 1;
      animation: logoAnimation 0.3s ease;
      img {
        width: 52px;
        height: 52px;
      }
      .login-left-logo-text {
        display: flex;
        flex-direction: column;
        span {
          margin-left: 10px;
          font-size: 24px;
          color: #26a59a;
          margin-bottom: 5px;
        }
        .login-left-logo-text-msg {
          font-size: 12px;
          color: #32a99e;
        }
      }
    }
    .login-left-img {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 100%;
      height: 52%;
      img {
        width: 100%;
        height: 100%;
        animation: error-num 0.6s ease;
      }
    }
    .login-left-waves {
      position: absolute;
      top: 0;
      right: -100px;
    }
  }
  .login-right {
    width: 700px;
    .login-right-warp {
      border-radius: 3px;
      width: 500px;
      height: 500px;
      position: relative;
      overflow: hidden;
      background-color: var(--el-color-white);
      .login-right-warp-mian {
        display: flex;
        flex-direction: column;
        height: 100%;
        .login-right-warp-main-title {
          height: 130px;
          line-height: 130px;
          font-size: 27px;
          text-align: center;
          letter-spacing: 3px;
          animation: logoAnimation 0.3s ease;
          animation-delay: 0.3s;
          color: var(--el-text-color-primary);
        }
        .login-right-warp-main-form {
          flex: 1;
          padding: 0 50px 50px;
          .login-content-main-sacn {
            position: absolute;
            top: 0;
            right: 0;
            width: 50px;
            height: 50px;
            overflow: hidden;
            cursor: pointer;
            transition: all ease 0.3s;
            color: var(--el-color-primary);
            &-delta {
              position: absolute;
              width: 35px;
              height: 70px;
              z-index: 2;
              top: 2px;
              right: 21px;
              background: var(--el-color-white);
              transform: rotate(-45deg);
            }
            &:hover {
              opacity: 1;
              transition: all ease 0.3s;
              color: var(--el-color-primary) !important;
            }
            i {
              width: 47px;
              height: 50px;
              display: inline-block;
              font-size: 48px;
              position: absolute;
              right: 1px;
              top: 0px;
            }
          }
        }
      }
    }
  }
  .login-msg {
    color: var(--el-text-color-placeholder);
  }
  .error{
    font-size: 20px;
  }
  .fa{
    font-size: 30px;
    margin-right: 10px0;
  }
}
</style>
