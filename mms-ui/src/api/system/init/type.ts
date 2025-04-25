interface SystemBaseEntity {
    /**
     * 系统标题
     */
    globalTitle: string;
    /**
     * 系统描述
     */
    globalDescription: string;
    /**
     * 系统Logo
     */
    logo: string;
    /**
     * 登录页背景
     */
    loginBg: string;
    /**
     * 验证码开启状态
     */
    captchaState: boolean;
    /**
     * 登录方式
     */
    loginType: Array<string>;
    /**
     * 多租户开启状态
     */
    tenantState: boolean;

    codeUrl:string;
}
