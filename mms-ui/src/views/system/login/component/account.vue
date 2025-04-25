<template>
  <el-form size="large" class="login-content-form">
    <el-form-item class="login-animation1">
      <el-input
          text
          :placeholder="$t('message.account.accountPlaceholder1')"
          v-model="state.ruleForm.username"
          clearable
          autocomplete="off"
      >
        <template #prefix>
          <el-icon class="el-input__icon"><ele-User /></el-icon>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item class="login-animation2">
      <el-input
          :type="state.isShowPassword ? 'text' : 'password'"
          :placeholder="$t('message.account.accountPlaceholder2')"
          v-model="state.ruleForm.password"
          autocomplete="off"
      >
        <template #prefix>
          <el-icon class="el-input__icon"><ele-Unlock /></el-icon>
        </template>
        <template #suffix>
          <i
              class="iconfont el-input__icon login-content-password"
              :class="state.isShowPassword ? 'icon-eye' : 'icon-eye-slash'"
              @click="state.isShowPassword = !state.isShowPassword"
          >
          </i>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item class="login-animation3" v-if="captchaState">
      <el-col :span="15">
        <el-input
            text
            maxlength="5"
            :placeholder="$t('message.account.accountPlaceholder3')"
            v-model="state.ruleForm.code"
            clearable
            autocomplete="off"
            @focus="captchaIn(0)"
        >
          <template #prefix>
            <el-icon class="el-input__icon"><ele-Position /></el-icon>
          </template>
        </el-input>
      </el-col>
      <el-col :span="1"></el-col>
      <el-col :span="8">
        <el-image
            style="width: 100px; padding-top: 10px"
            :src="state.ruleForm.codeImg"
            @click="captchaIn(1)"
        ></el-image>
      </el-col>
    </el-form-item>
    <el-form-item class="login-animation4">
      <el-button
          v-waves
          type="primary"
          class="login-content-submit"
          round
          @click="onSignIn"
          @keydown.enter="keyDown"
          :loading="state.loading.signIn"
      >
        <span>{{ $t("message.account.accountBtnText") }}</span>
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts" name="loginAccount">
import { onMounted, reactive, computed, ref, onUnmounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";
import Cookies from "js-cookie";
import { storeToRefs } from "pinia";
import { useThemeConfig } from "/@/stores/themeConfig";
import { initFrontEndControlRoutes } from "/@/router/frontEnd";
import { initBackEndControlRoutes } from "/@/router/backEnd";
import { Session } from "/@/utils/storage";
import { formatAxis } from "/@/utils/formatTime";
import { NextLoading } from "/@/utils/loading";

import { sysInfo, captcha, login } from "/@/api/system/login";
import {SysEnum} from "/@/enums/SysEnum";
// 接受父组件参数
const props = defineProps({
  captchaState: {
    type: Boolean,
    default: () => false,
  },
});
// 定义变量内容
const { t } = useI18n();
const storesThemeConfig = useThemeConfig();
const { themeConfig } = storeToRefs(storesThemeConfig);
const route = useRoute();
const router = useRouter();
const state = reactive({
  isShowPassword: false,
  ruleForm: {
    username: "",
    password: "",
    code: "",
    codeKey: import.meta.env.VITE_APP_CLIENT_ID,
    codeImg: "",
    uuid: "",
    rememberMe: true,
  },
  loading: {
    signIn: false,
  },
});
// 时间获取
const currentTime = computed(() => {
  return formatAxis(new Date());
});
// 获取验证码
const updateCode = ref(true);
const captchaIn = async (type: number) => {
  if (updateCode.value == false) {
    return;
  }
  if (type == 1) {
    updateCode.value = false;
  }
  captcha(state.ruleForm.codeKey)
      .then((res) => {
        state.ruleForm.codeImg = "data:image/png;base64," + res.data.captcha;
        state.ruleForm.uuid = res.data.uuid;
      })
      .catch((err) => {
        ElMessage.warning("生成验证码出错了！");
      })
      .finally(() => {
        if (type == 1) {
          setTimeout(() => {
            updateCode.value = true;
          }, 3000);
        }
      });
};
// 登录
const onSignIn = async () => {
  if (state.loading.signIn) {
    return;
  }
  state.loading.signIn = true;
  login(state.ruleForm)
      .then(async (res) => {
        state.loading.signIn = true;
        Session.set(SysEnum.USER_INFO_KEY, res.data.userInfo);
        // 存储 token 到浏览器缓存
        Session.set(SysEnum.TOKEN_KEY, res.data.token);
        // 模拟数据，对接接口时，记得删除多余代码及对应依赖的引入。用于 `/src/stores/userInfo.ts` 中不同用户登录判断（模拟数据）
        Cookies.set(SysEnum.USER_INFO_NAME, state.ruleForm.username);
        // 是否开启后端控制路由
        if (!themeConfig.value.isRequestRoutes) {
          // 前端控制路由，2、请注意执行顺序
          const isNoPower = await initFrontEndControlRoutes();
          signInSuccess(isNoPower);
        } else {
          // 模拟后端控制路由，isRequestRoutes 为 true，则开启后端控制路由
          // 添加完动态路由，再进行 router 跳转，否则可能报错 No match found for location with path "/"
          const isNoPower = await initBackEndControlRoutes();
          // 执行完 initBackEndControlRoutes，再执行 signInSuccess
          signInSuccess(isNoPower);
        }
      })
      .catch(async (err) => {
        ElMessage.warning(err);
        if(props.captchaState){
           await captchaIn(0);
        }
      })
      .finally(() => {
        state.loading.signIn = false;
      });
};
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
  state.loading.signIn = false;
};
// 点击回车键登录
const keyDown = (e: any) => {
  if (e.keyCode === 13) {
    if (props.captchaState) {
      if (state.ruleForm.username.length < 4 || state.ruleForm.password.length < 4) {
        return false;
      }
      if (state.ruleForm.code.length != 5) {
        ElMessage.warning("请先正确输入验证码！");
        return false;
      }
    }
    onSignIn();
  }
};
onUnmounted(() => {
  // 销毁事件
  window.removeEventListener("keydown", keyDown, false);
});
// 页面加载时
onMounted(() => {
  // 绑定监听事件
  window.addEventListener("keydown", keyDown);
  if (props.captchaState) {
    captchaIn(0);
  }
});
</script>

<style scoped lang="scss">
.login-content-form {
  margin-top: 20px;

  @for $i from 1 through 4 {
    .login-animation#{$i} {
      opacity: 0;
      animation-name: error-num;
      animation-duration: 0.5s;
      animation-fill-mode: forwards;
      animation-delay: calc($i/10) + s;
    }
  }

  .login-content-password {
    display: inline-block;
    width: 20px;
    cursor: pointer;

    &:hover {
      color: #909399;
    }
  }

  .login-content-code {
    width: 100%;
    padding: 0;
    font-weight: bold;
    letter-spacing: 5px;
  }

  .login-content-submit {
    width: 100%;
    letter-spacing: 2px;
    font-weight: 300;
    margin-top: 15px;
  }
}
</style>
