<template>
	<div class="personal layout-pd mt10">
		<el-row>
			<!-- 个人信息 -->
			<el-col :xs="24" :sm="16">
				<el-card shadow="hover" header="个人信息">
					<div class="personal-user">
						<div class="personal-user-left">
							<el-upload
								class="h100 personal-user-left-upload"
								:id="uuid"
								action="#"
								:http-request="handleHttpUpload"
								:auto-upload="true"
								:show-file-list="false"
								multiple
								:limit="1"
							>
								<img :src="userInfos.photo || 'https://sxpcwlkj.oss-cn-beijing.aliyuncs.com/defimg.png'" alt="" />
							</el-upload>
						</div>
						<div class="personal-user-right">
							<el-row>
								<el-col :span="24" class="personal-title mb18"
									>{{ currentTime }}，{{ userInfos.userName }}，生活变的再糟糕，也不妨碍我变得更好！
								</el-col>
								<el-col :span="24">
									<el-row>
										<el-col :xs="24" :sm="8" class="personal-item mb6">
											<div class="personal-item-label">昵称：</div>
											<div class="personal-item-value">{{ userInfos.nickName }}</div>
										</el-col>
										<el-col :xs="24" :sm="16" class="personal-item mb6">
											<div class="personal-item-label">身份：</div>
											<div class="personal-item-value">{{ userInfos.roleName }}</div>
										</el-col>
									</el-row>
								</el-col>
								<el-col :span="24">
									<el-row>
										<el-col :xs="24" :sm="8" class="personal-item mb6">
											<div class="personal-item-label">登录IP：</div>
											<div class="personal-item-value">{{ userInfos.loginIp }}</div>
										</el-col>
										<el-col :xs="24" :sm="16" class="personal-item mb6">
											<div class="personal-item-label">登录时间：</div>
											<div class="personal-item-value">{{ userInfos.loginDate }}</div>
										</el-col>
									</el-row>
								</el-col>
							</el-row>
						</div>
					</div>
				</el-card>
			</el-col>

			<!-- 消息通知 -->
			<el-col :xs="24" :sm="8" class="pl15 personal-info">
				<el-card shadow="hover">
					<template #header>
						<span>消息通知</span>
						<span class="personal-info-more">更多</span>
					</template>
					<div class="personal-info-box">
						<ul class="personal-info-ul">
							<li v-for="(v, k) in stateNews.newsList" :key="k" class="personal-info-li">
								<p @click="openNews(v)" target="_block" class="personal-info-li-title">{{ v.title }}</p>
							</li>
						</ul>
					</div>
				</el-card>
			</el-col>

			<!-- 更新信息 -->
			<el-col :span="24">
				<el-card shadow="hover" class="mt15 personal-edit" header="账号安全">
					<div class="personal-edit-safe-box">
						<div class="personal-edit-safe-item">
							<div class="personal-edit-safe-item-left">
								<div class="personal-edit-safe-item-left-label">账户密码</div>
								<div class="personal-edit-safe-item-left-value">当前密码强度：{{ userInfos.passwordStrength }}</div>
							</div>
							<div class="personal-edit-safe-item-right">
								<el-button text type="primary" @click="openDialog">立即修改</el-button>
							</div>
						</div>
					</div>
					<div class="personal-edit-safe-box">
						<div class="personal-edit-safe-item">
							<div class="personal-edit-safe-item-left">
								<div class="personal-edit-safe-item-left-label">密保手机</div>
								<div class="personal-edit-safe-item-left-value">
									<span v-if="userInfos.phoneNumber.length === 0">未绑定手机</span>
									<span v-else> 已绑定手机：{{ userInfos.phoneNumber }}</span>
									<el-popconfirm title="是否继续要解除绑定手机号?" @confirm="confirmEvent(1)">
										<template #reference>
											<span class="unbind" v-show="userInfos.phoneNumber.length > 0">解绑</span>
										</template>
									</el-popconfirm>
								</div>
							</div>
							<div class="personal-edit-safe-item-right">
								<el-button text type="primary" @click="state.dialog = true">
									{{ userInfos.phoneNumber.length > 0 ? '立即修改' : '立即绑定' }}
								</el-button>
							</div>
						</div>
					</div>
					<div class="personal-edit-safe-box">
						<div class="personal-edit-safe-item">
							<div class="personal-edit-safe-item-left">
								<div class="personal-edit-safe-item-left-label">绑定邮箱</div>
								<div class="personal-edit-safe-item-left-value">
									<span v-if="userInfos.email.length === 0">设置邮箱，接收系统消息通知</span>
									<span v-else> 已设置邮箱：{{ userInfos.email }}</span>
                  <el-popconfirm title="是否继续要解除绑定邮箱?" @confirm="confirmEvent(2)">
                    <template #reference>
                      <span class="unbind" v-show="userInfos.email.length > 0">解绑</span>
                    </template>
                  </el-popconfirm>
								</div>
							</div>
							<div class="personal-edit-safe-item-right">
								<el-button @click="stateEmail.dialog = true" text type="primary">{{ userInfos.email.length > 0 ? '立即修改' : '立即设置' }}</el-button>
							</div>
						</div>
					</div>
					<div class="personal-edit-safe-box">
						<div class="personal-edit-safe-item">
							<div class="personal-edit-safe-item-left">
								<div class="personal-edit-safe-item-left-label">绑定微信</div>
								<div class="personal-edit-safe-item-left-value">
                  <span v-if="userInfos.wxOpenid.length === 0">绑定微信账号后，可以使用微信登录</span>
                  <span v-else> 已设置微信：{{ userInfos.wxOpenid }}</span>
                  <el-popconfirm title="是否继续要解除绑定微信?" @confirm="confirmEvent(3)">
                    <template #reference>
                      <span class="unbind" v-show="userInfos.wxOpenid.length > 0">解绑</span>
                    </template>
                  </el-popconfirm>
                </div>
							</div>
							<div class="personal-edit-safe-item-right">
								<el-button  @click="openWxCode" text type="primary">{{ userInfos.wxOpenid.length > 0 ? '立即修改' : '立即绑定' }} </el-button>
							</div>
						</div>
					</div>
				</el-card>
			</el-col>
		</el-row>
		<!--修改密码-->
		<el-dialog v-model="dialogFormVisible" title="修改密码" width="500">
			<el-form :model="form">
				<el-form-item label="原密码" label-width="100">
					<el-input show-password v-model="form.oldPassword" autocomplete="off" />
				</el-form-item>
				<el-form-item label="新密码" label-width="100">
					<el-input show-password v-model="form.password" autocomplete="off" />
				</el-form-item>
				<el-form-item label="确认密码" label-width="100">
					<el-input show-password v-model="form.passwordTwo" autocomplete="off" />
				</el-form-item>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取消</el-button>
					<el-button type="primary" @click="updatePress">确定</el-button>
				</div>
			</template>
		</el-dialog>
		<!--绑定手机号-->
		<el-dialog
			draggable
			v-model="state.dialog"
			:key="state.key"
			@close="
				state.dialog = false;
				state.key = generateUUID();
			"
			:title="state.title"
			width="450"
		>
			<el-form ref="ruleFormRef" :model="state.form" :rules="state.rules" label-width="auto" status-icon>
				<el-row>
					<el-col :span="24">
						<el-form-item label="手机号" prop="phone">
							<el-input v-model="state.form.phone" autocomplete="off" placeholder="请输入手机号" maxlength="11" />
						</el-form-item>
					</el-col>
					<el-col :span="14" class="mt25">
						<el-form-item label="短信码" prop="code">
							<el-input v-model="state.form.code" autocomplete="off"  placeholder="请输入短信码" />
						</el-form-item>
					</el-col>
					<el-col :span="10" class="mt25 text-right">
						<el-button type="primary" :disabled="!(exitTime == 60)" @click="getSmsCode">
							{{ exitTime == 60 ? '获取验证码' : exitTime + 's后重新获取' }}
						</el-button>
					</el-col>
				</el-row>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
					<el-button @click="resetForm(ruleFormRef)">重置</el-button>
					<el-button type="primary" @click="submitForm(ruleFormRef)">确定</el-button>
				</div>
			</template>
		</el-dialog>
    <!--绑定邮箱-->
    <el-dialog
        draggable
        v-model="stateEmail.dialog"
        :key="stateEmail.key"
        @close="
				stateEmail.dialog = false;
				stateEmail.key = generateUUID();
			"
        :title="stateEmail.title"
        width="450"
    >
      <el-form ref="ruleFormEmailRef" :model="stateEmail.form" :rules="stateEmail.rules" label-width="auto" status-icon>
        <el-row>
          <el-col :span="24">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="stateEmail.form.email" autocomplete="off" placeholder="请输入邮箱"   />
            </el-form-item>
          </el-col>
          <el-col :span="14" class="mt25">
            <el-form-item label="验证码" prop="code">
              <el-input v-model="stateEmail.form.code" autocomplete="off"  placeholder="请输入验证码"  />
            </el-form-item>
          </el-col>
          <el-col :span="10" class="mt25 text-right">
            <el-button type="primary" :disabled="!(exitTime == 60)" @click="getEmailCode">
              {{ exitTime == 60 ? '获取验证码' : exitTime + 's后重新获取' }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="resetForm(ruleFormEmailRef)">重置</el-button>
          <el-button type="primary" @click="submitEmailForm(ruleFormEmailRef)">确定</el-button>
        </div>
      </template>
    </el-dialog>
    <!--绑定微信-->
    <el-dialog
        draggable
        v-model="stateWx.dialog"
        :key="stateWx.key"
        @close="wxClose"
        :title="stateWx.title"
        width="450"
    >
    <div v-if="!succeed" class="flex flex-col">
      <div v-if="unCode"  class="flex flex-col f-c">
        <div class="fond16 mt-10">
          <el-icon color="#f0a71a" :size="80"><ele-WarningFilled /></el-icon>
        </div>
        <div class="fond16 ">{{unCodeMsg}} <span class="fond16 f-c-1 shou"  @click="openWxCode">刷新</span> </div>
      </div>
      <div v-else class="flex flex-col f-c" >
        <img  :src="stateWx.form.wxCode" alt="" style="width: 50%" />
        <div  class="mt10 text-center" >请用微信扫一扫进行绑定 <span v-if="exitTime>0" >{{exitTime+'s'}}</span></div>
      </div>

    </div>
    <div  v-else class="flex flex-col f-c">
      <div class="fond16 mt-10">
        <el-icon color="#2dac34" :size="80"><ele-CircleCheckFilled /></el-icon>
      </div>
      <div class="fond16">绑定成功</div>
    </div>
    </el-dialog>
	</div>
</template>

<script setup lang="ts" name="personal">
import {reactive, computed, ref, onMounted, onUnmounted} from 'vue';
import { formatAxis } from '/@/utils/formatTime';
import { useUserInfo } from '/@/stores/userInfo';
import { storeToRefs } from 'pinia';
import { uploadImg } from '/@/api/system/upload';
import { Action, ElMessage, ElMessageBox, UploadRequestOptions } from 'element-plus';
import { generateUUID } from '/@/utils/mms';
import { smsCode,emailCode } from '/@/api/system/init';
import { userApi } from '/@/api/system/user';
import { noticeApi } from '/@/api/system/notice';
import { Session } from '/@/utils/storage';
import {elEmail, elPhone, email, phone} from '/@/utils/toolsValidate';
import type { ComponentSize, FormInstance, FormRules } from 'element-plus';
import { VerifyType } from '/@/types/global';
import {Message} from "@element-plus/icons-vue";
const succeed=ref(false);
const unCode=ref(false);
const unCodeMsg=ref("二维码已失效");
const stores = useUserInfo();
const { userInfos } = storeToRefs(stores);
const baseSysNoticeApi = noticeApi();
// 引入 api 请求接口
const baseUserApi = userApi();
// 倒计时
const exitTime = ref(60);
let intervalId: number | undefined;
// 生成组件唯一id
const uuid = ref('id-' + generateUUID());
const dialogFormVisible = ref(false);
const form = reactive({
	password: '',
	passwordTwo: '',
	oldPassword: '',
});

// 绑定手机号码
const ruleFormRef = ref<FormInstance>();
interface TypeForm {
	phone: string;
	code: string;
}
const state = reactive<VerifyType<TypeForm>>({
	dialog: false,
	key: generateUUID(),
	title: '绑定手机号',
	form: {
		phone: '',
		code: '',
	},
	rules: {
		phone: [{ required: true, validator: elPhone, trigger: 'blur' }],
		code: [
			{ required: true, message: '请输入短信验证码', trigger: 'blur' },
			{ required: true, min: 6, max: 6, message: '请输入正确的短信验证码', trigger: 'blur' },
		],
	},
});
//解除手机号
const confirmEvent = (type: number) => {
	baseUserApi
		.unbind(type)
		.then((res) => {
			if (res.code === 200) {
				ElMessage.success(res.msg);
				state.dialog = false;
				updateUserInfo();
			}
		})
		.catch((err) => {
			ElMessage.error(err);
		});
};


// 发送短信
const getSmsCode = () => {
	if (!phone(state.form.phone)) {
		ElMessage.error('请正确输入手机号');
		return;
	}
	smsCode({ ...state.form, ...{ type: 5 } })
		.then((res) => {
			if (res.code === 200) {
				ElMessage.success(res.msg);
				state.form.code = '';
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
// 验证
const submitForm = async (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	await formEl.validate((valid, fields) => {
		if (valid) {
			baseUserApi
				.bindingPhone(state.form)
				.then((res) => {
					if (res.code === 200) {
						ElMessage.success(res.msg);
						state.dialog = false;
						updateUserInfo();
					}
				})
				.catch((err) => {
					ElMessage.error(err);
				});
		} else {
			console.log('error submit!', fields);
		}
	});
};
// 重置
const resetForm = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.resetFields();
};
// 设置密码
const updatePress = () => {
	if (form.password !== form.passwordTwo) {
		ElMessage.error('两次密码输入不一致');
		return;
	}
	baseUserApi
		.resetPwd(form)
		.then((res) => {
			if (res.code === 200) {
				ElMessage.success(res.msg);
				updateUserInfo();
				dialogFormVisible.value = false;
			}
		})
		.catch((err) => {
			ElMessage.error(err);
		});
};
// 获取用户信息
const openDialog = () => {
	form.password = '';
	form.passwordTwo = '';
	form.oldPassword = '';
	dialogFormVisible.value = true;
};
// 上传图片
const handleHttpUpload = async (options: UploadRequestOptions) => {
	let formData = new FormData();
	formData.append('file', options.file);
	try {
		await uploadImg(formData).then((res) => {
			if (res.code === 200) {
				options.onSuccess(res.msg);
				userInfos.value.photo = res.data.url;
				Session.set('userInfo', userInfos.value);
				// 更新用户信息
				baseUserApi
					.editHeaderImg({ avatar: res.data.url })
					.then((res) => {
						if (res.code === 200) {
							options.onSuccess(res.msg);
							ElMessage.success(res.msg);
						}
					})
					.catch((err) => {
						ElMessage.error(err);
					});
			}
		});
	} catch (error) {
		options.onError(error as any);
	}
};
// 公告列表
const getListData = () => {
	baseSysNoticeApi
		.list({ pageNum: 1, pageSize: 2 })
		.then((res) => {
			stateNews.newsList = res.rows;
		})
		.catch(async (err) => {})
		.finally(() => {});
};
// 定义变量内容
const stateNews = reactive({
	newsList: [],
});
const openNews = (v?: { content: string; title: string }) => {
	ElMessageBox.alert(v?.content, v?.title, {
		dangerouslyUseHTMLString: true,
		confirmButtonText: 'OK',
		callback: (action: Action) => {},
	});
};
// 当前时间提示语
const currentTime = computed(() => {
	return formatAxis(new Date());
});

// 绑定手机号码
const ruleFormEmailRef = ref<FormInstance>();
interface TypeEmailForm {
  email: string;
  code: string;
}

const stateEmail = reactive<VerifyType<TypeEmailForm>>({
  dialog: false,
  key: generateUUID(),
  title: '绑定邮箱',
  form: {
    email: '',
    code: '',
  },
  rules: {
    email: [{ required: true, validator: elEmail, trigger: 'blur' }],
    code: [
      { required: true, message: '请输入邮箱验证码', trigger: 'blur' },
      { required: true, min: 6, max: 6, message: '请输入正确的邮箱验证码', trigger: 'blur' },
    ],
  },
});
// 发送邮件
const getEmailCode = () => {
  if (!email(stateEmail.form.email)) {
    ElMessage.error('请正确输入邮箱');
    return;
  }
  emailCode({ ...stateEmail.form, ...{ type: 1 } })
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success(res.msg);
          state.form.code = '';
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
// 验证
const submitEmailForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      baseUserApi
          .bindingEmail(stateEmail.form)
          .then((res) => {
            if (res.code === 200) {
              ElMessage.success(res.msg);
              stateEmail.dialog = false;
              updateUserInfo();
            }
          })
          .catch((err) => {
            ElMessage.error(err);
          });
    } else {
      console.log('error submit!', fields);
    }
  });
};

// 更新用户信息
const updateUserInfo = () => {
	baseUserApi.getUserInfo().then((res) => {
		if (res.code === 200) {
			userInfos.value = res.data;
			Session.set('userInfo', userInfos.value);
		}
	});
};

// 绑定微信
const wxUuid=ref(generateUUID() as string) ;
// 二维码
const openWxCode=()=>{
  // 获取二维码
  wxUuid.value=generateUUID() as string;
  unCode.value=false;
  succeed.value=false;
  exitTime.value=60;
  baseUserApi.getWxCode(wxUuid.value).then((res) => {
    stateWx.dialog=true;
    stateWx.form.wxCode=res.data as string;
    queryWxCodeState();
    intervalId = setInterval(() => {
      if (exitTime.value <= 0) {
        unCode.value=true;
        unCodeMsg.value="二维码已失效";
        succeed.value=false;
        clearInterval(intervalId);
        clearInterval(intervalIdWxState);
        return;
      }
      exitTime.value --;
    },1000)
  })
}
interface TypeWxForm {
  wxCode: string;
  state:number;
}
const stateWx = reactive<VerifyType<TypeWxForm>>({
  dialog: false,
  key: generateUUID(),
  title: '绑定微信',
  form: {
    wxCode: '',
    state: 1,
  },
  rules: {},
});
let intervalIdWxState: number | undefined;
// 查询二维码状态
const queryWxCodeState=()=>{
  intervalIdWxState = setInterval(() => {
    baseUserApi.queryWxCodeState(wxUuid.value).then((res) => {
      if(res.status==0){
        succeed.value=true;
        unCode.value=false;
        updateUserInfo();
        clearInterval(intervalIdWxState);
        clearInterval(intervalId);
      }else{
        unCodeMsg.value=res.msg as string;
        succeed.value=false;
        unCode.value=true;
        exitTime.value=0;
        clearInterval(intervalId);
        clearInterval(intervalIdWxState);
      }
    });
  },3000)
}
const wxClose=()=>{
  stateWx.dialog = false;
  stateWx.key = generateUUID();
  clearInterval(intervalIdWxState);
  clearInterval(intervalId);
}
onUnmounted(() => {
  // 销毁事件
  clearInterval(intervalIdWxState);
  clearInterval(intervalId);
});
// 页面加载时
onMounted(() => {
	getListData();
  updateUserInfo();
});
</script>

<style scoped lang="scss">
@use '/src/theme/mixins/index.scss' as v;

.personal {
	.personal-user {
		height: 130px;
		display: flex;
		align-items: center;

		.personal-user-left {
			width: 130px;
			height: 130px;
			border-radius: 3px;

			:deep(.el-upload) {
				height: 100%;
			}

			.personal-user-left-upload {
				img {
					width: 100%;
					height: 100%;
					border-radius: 3px;
					min-height: 130px;
					min-width: 130px;
				}

				&:hover {
					img {
						animation: logoAnimation 0.3s ease-in-out;
					}
				}
			}
		}

		.personal-user-right {
			flex: 1;
			padding: 0 15px;

			.personal-title {
				font-size: 18px;
				@include v.text-ellipsis(1);
			}

			.personal-item {
				display: flex;
				align-items: center;
				font-size: 13px;

				.personal-item-label {
					color: var(--el-text-color-secondary);
					@include v.text-ellipsis(1);
				}

				.personal-item-value {
					@include v.text-ellipsis(1);
				}
			}
		}
	}

	.personal-info {
		.personal-info-more {
			float: right;
			color: var(--el-text-color-secondary);
			font-size: 13px;

			&:hover {
				color: var(--el-color-primary);
				cursor: pointer;
			}
		}

		.personal-info-box {
			height: 130px;
			overflow: hidden;

			.personal-info-ul {
				list-style: none;

				.personal-info-li {
					font-size: 13px;
					padding-bottom: 10px;

					.personal-info-li-title {
						display: inline-block;
						@include v.text-ellipsis(1);
						color: var(--el-text-color-secondary);
						text-decoration: none;
					}

					& a:hover {
						color: var(--el-color-primary);
						cursor: pointer;
					}
				}
			}
		}
	}

	.personal-recommend-row {
		.personal-recommend-col {
			.personal-recommend {
				position: relative;
				height: 100px;
				border-radius: 3px;
				overflow: hidden;
				cursor: pointer;

				&:hover {
					i {
						right: 0px !important;
						bottom: 0px !important;
						transition: all ease 0.3s;
					}
				}

				i {
					position: absolute;
					right: -10px;
					bottom: -10px;
					font-size: 70px;
					transform: rotate(-30deg);
					transition: all ease 0.3s;
				}

				.personal-recommend-auto {
					padding: 15px;
					position: absolute;
					left: 0;
					top: 5%;
					color: var(--next-color-white);

					.personal-recommend-msg {
						font-size: 12px;
						margin-top: 10px;
					}
				}
			}
		}
	}

	.personal-edit {
		.personal-edit-title {
			position: relative;
			padding-left: 10px;
			color: var(--el-text-color-regular);

			&::after {
				content: '';
				width: 2px;
				height: 10px;
				position: absolute;
				left: 0;
				top: 50%;
				transform: translateY(-50%);
				background: var(--el-color-primary);
			}
		}

		.personal-edit-safe-box {
			border-bottom: 1px solid var(--el-border-color-light, #ebeef5);
			padding: 15px 0;

			.personal-edit-safe-item {
				width: 100%;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.personal-edit-safe-item-left {
					flex: 1;
					overflow: hidden;

					.personal-edit-safe-item-left-label {
						color: var(--el-text-color-regular);
						margin-bottom: 5px;
					}

					.personal-edit-safe-item-left-value {
						color: var(--el-text-color-secondary);
						@include v.text-ellipsis(1);
						margin-right: 15px;
					}
				}
			}

			&:last-of-type {
				padding-bottom: 0;
				border-bottom: none;
			}
		}
	}

	.unbind {
		color: #1e97f8;
		font-size: 14px;
		font-weight: 500;
		margin-left: 10px;
		cursor: pointer;
	}
}
</style>
