<template>
  <el-form size="large" class="login-content-form">
    <el-form-item class="login-animation1">
      <el-input
        text
        :placeholder="$t('message.mobile.placeholder1')"
        v-model="state.ruleForm.phone"
        clearable
        autocomplete="off"
      >
        <template #prefix>
          <i class="iconfont icon-dianhua el-input__icon"></i>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item class="login-animation2">
      <el-col :span="15">
        <el-input
          text
          maxlength="6"
          :placeholder="$t('message.mobile.placeholder2')"
          v-model="state.ruleForm.code"
          clearable
          autocomplete="off"
        >
          <template #prefix>
            <el-icon class="el-input__icon"><ele-Position /></el-icon>
          </template>
        </el-input>
      </el-col>
      <el-col :span="1"></el-col>
      <el-col :span="8">
        <el-button :disabled="exitTime !== 60" @click="getSmsCode" v-waves class="login-content-code">{{exitTime!=60? exitTime+'S':$t("message.mobile.codeText")}}</el-button>
      </el-col>
    </el-form-item>
    <el-form-item class="login-animation3">
      <el-button round type="primary" @click="loginByPhoneHttp" v-waves class="login-content-submit">
        <span>{{ $t("message.mobile.btnText") }}</span>
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts" name="loginMobile">
import { reactive } from "vue";
import {phone} from "/@/utils/toolsValidate";
import {ElMessage} from "element-plus";
import {smsCode} from "/@/api/system/init";
import {loginByPhone} from "/@/api/system/login";
import {Session} from "/@/utils/storage";
import Cookies from "js-cookie";
import { useThemeConfig } from "/@/stores/themeConfig";
import {storeToRefs} from "pinia";
const storesThemeConfig = useThemeConfig();
const { themeConfig } = storeToRefs(storesThemeConfig);
import { initFrontEndControlRoutes } from "/@/router/frontEnd";
import { initBackEndControlRoutes } from "/@/router/backEnd";
import {formatAxis} from "/@/utils/formatTime";
import {NextLoading} from "/@/utils/loading";
import {SysEnum} from "/@/enums/SysEnum";
const route = useRoute();
const router = useRouter();
// 倒计时
const exitTime = ref(60);
let intervalId: number;
// 定义变量内容
const state = reactive({
  ruleForm: {
      phone: '',
      code: '',
  },
});

// 发送短信
const getSmsCode = () => {
    if (!phone(state.ruleForm.phone)) {
        ElMessage.error('请正确输入手机号');
        return;
    }
    smsCode({ ...state.ruleForm, ...{ type: 2 } })
        .then((res) => {
            if (res.code === 200) {
                ElMessage.success(res.msg);
                state.ruleForm.code = '';
                intervalId = setInterval(() => {
                    if (exitTime.value <= 0) {
                        clearInterval(intervalId);
                        exitTime.value = 60;
                        return;
                    }
                    exitTime.value--;
                }, 1000);
            }
        })
        .catch((e) => {
            ElMessage.error(e);
        });
};

const loginByPhoneHttp = async ()=>{
    loginByPhone(state.ruleForm).then(async (res) => {
        if (res.code === 200) {
            Session.set(SysEnum.USER_INFO_KEY, res.data.userInfo);
            // 存储 token 到浏览器缓存
            Session.set(SysEnum.TOKEN_KEY, res.data.token);
            // 模拟数据，对接接口时，记得删除多余代码及对应依赖的引入。用于 `/src/stores/userInfo.ts` 中不同用户登录判断（模拟数据）
            Cookies.set(SysEnum.USER_INFO_NAME, res.data.userInfo.username);
            // 是否开启后端控制路由
            if (!themeConfig.value.isRequestRoutes) {
                // 前端控制路由，2、请注意执行顺序
                const isNoPower = await initFrontEndControlRoutes();
                setTimeout(() => {
                    signInSuccess(isNoPower);
                }, 1500)
            } else {
                // 模拟后端控制路由，isRequestRoutes 为 true，则开启后端控制路由
                // 添加完动态路由，再进行 router 跳转，否则可能报错 No match found for location with path "/"
                const isNoPower = await initBackEndControlRoutes();
                // 执行完 initBackEndControlRoutes，再执行 signInSuccess
                setTimeout(() => {
                    signInSuccess(isNoPower);
                }, 1500)
            }
        }
    }).catch((e) => {
        ElMessage.error(e);
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
    }
    else {
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
  .login-content-code {
    width: 100%;
    padding: 0;
  }
  .login-content-submit {
    width: 100%;
    letter-spacing: 2px;
    font-weight: 300;
    margin-top: 15px;
  }
}
</style>
