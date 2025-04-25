package com.sxpcwlkj.common.code.entity;

import cn.hutool.json.JSONUtil;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.enums.WxCodeStatusEnum;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * wxCodeBo 微信二维码常量
 * @author xijue
 */
@Data
public class WxCodeBo {
    public static final String CODE_TYPE_LOGIN="login";
    //类型  binding
    public static final String CODE_TYPE_BINDING="binding";
    //Redis  key 登录
    public static final String REDIS_KEY_LOGIN = "wx:login:";
    //Redis  key 绑定微信
    public static final String REDIS_KEY_BINDING = "wx:binding:";

    private String type;

    private String uuid;

    @Getter
    private String redisKey;

    private Integer state;

    private Map<String, Object> paramData;

    private String codeUrl;

    private String openId;
    //默认60秒
    private int expireTime=60;

    public WxCodeBo(String uuid) {
        this.uuid = uuid;
    }
    public WxCodeBo typeLogin() {
        this.type = CODE_TYPE_LOGIN;
        this.redisKey = REDIS_KEY_LOGIN + uuid;
        if(this.state==null){
            this.state= WxCodeStatusEnum.WAITING.getValue();
        }
        return this;
    }

    public WxCodeBo typeBinding() {
        this.type = CODE_TYPE_BINDING;
        this.redisKey = REDIS_KEY_BINDING + uuid;
        if(this.state==null){
            this.state= WxCodeStatusEnum.WAITING.getValue();
        }
        return this;
    }

    public WxCodeBo state(WxCodeStatusEnum state) {
        this.state = state.getValue();
        return this;
    }
    public WxCodeBo expireTime(int expireTime) {
        this.expireTime = expireTime;
        return this;
    }
    public WxCodeBo paramData(Object paramData) {
        Map<String,Object> data=new HashMap<>();
        data.put("type",this.type);
        data.put("redisKey",this.redisKey);
        //是否是json 对象
        if(paramData instanceof String){
            data.put("data",paramData);
            this.paramData = data;
            return this;
        }
        data.put("data",JSONUtil.toJsonStr(paramData));
        this.paramData = data;
        return this;
    }
    public WxCodeBo codeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
        return this;
    }
    public WxCodeBo openId(String openId) {
        this.openId = openId;
        return this;
    }
    public String toJson() {
        return JSONUtil.toJsonStr(this);
    }

    public String getParamData() {
        return JSONUtil.toJsonStr(this.paramData);
    }

}
