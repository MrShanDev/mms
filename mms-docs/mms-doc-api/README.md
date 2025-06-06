 # 声明
 
本项目是用于 [[VitePress 会员主题]](https://vitepress.mosong.cc/) 的用户注册/登录，支付等接口服务。

> 详细文档大家请访问： 官方 [[VitePress 会员主题]](https://vitepress.mosong.cc/) 官方是Go语言服务接口工程 。

> 以下是用Java扩展 ‘会员主题’ 服务接口，具体看下面的接口列表：


# 接口列表

## 一、用户信息

POST /vpapi/meb/userinfo

### 请求参数

|名称|位置|类型|必选| 值          |
|---|---|---|---|------------|
|cookie|header|string| 否 | mss= |

> 返回示例

未登录

```json
{
    "body":{},
    "errmsg":"会话过期",
    "errno":"99910",
    "host_time":"20250528093635"
}
```
已登录

```json
{
    "body": {
        "uid": "674fe3bb2c8b61045f6eaff6",
        "nickname": "单先生-软件开发",
        "avatar": "https://thirdwx.qlogo.cn/mmopen/vi_32/gWYQezqOl40WauDqyA7c5HGPQQgUQmXg0aLJVhMwdAIxD2DHs19urHkzB5CJm12vraHGT9a1lQIBNyGYBzCJ4yJuib8bctnE9rS5CH9GY3NY/132",
        "ctime": "20241204130811",
        "type": "vip",
        "vip_date": "2025-05-28"
    },
    "errmsg": "ok",
    "errno": "00000",
    "host_time": "20250528095709"
}
```


## 二、获取登录二维码

POST /vpapi/meb/oauth-authorize

### 请求参数

|名称|位置|类型|必选| 值 |
|---|---|---|---|---|
|cookie|header|string| 否 | mss=  |

> 返回示例

```json
{
    "body": {
        "state": "68366991f2d7af56647fb645",
        "url": "https://vpapi.mosong.cc/vpapi/meb/weixin-authorize?scope=snsapi_userinfo&state=68366991f2d7af56647fb645"
    },
    "errmsg": "ok",
    "errno": "00000",
    "host_time": "20250528094033"
}
```

## 三、微信授权跳转

GET /vpapi/meb/weixin-authorize?state=x&scope=x

### 请求参数

|名称|位置|类型|必选| 值 |
|---|---|---|---|---|
|cookie|header|string| 否 | mss=  |
|state| param  |string| 是  | 68366991f2d7af56647fb645  |
|scope| param  |string| 是  | snsapi_userinfo  |

> 返回示例
> '状态 1=正常 0=重试'

```json
{
    "body": {
        "status":1
    },
    "errmsg": "ok",
    "errno": "00000",
    "host_time": "20250528094033"
}
```

## 四、查询扫码状态

POST /vpapi/meb/oauth-polling

### 请求参数

|名称| 位置     |类型| 必选 | 值 |
|---|--------|---|----|---|
|cookie| header |string| 否  | mss=  |
|state| param  |string| 是  | 68366991f2d7af56647fb645  |


> 返回示例

未扫码

```json
{
    "body": {
        "status": "0",
        "token": ""
    },
    "errmsg": "ok",
    "errno": "00000",
    "host_time": "20250528094039"
}
```
扫码成功
```json
{
    "body": {
        "status": "1",
        "token": "3092447f3f26snCUIBpCShl3_kRxkaJsephz_DfFtPWtrFNCz6IiMAjYYlU1bN1YbQ9Vs_3HFCUGYzRlSoE-eKGpPmFsTBlxIjGueCALtm3wkn9grjM6UPFC_C8W5sgCAZLIdOLvLYwu"
    },
    "errmsg": "ok",
    "errno": "00000",
    "host_time": "20250528095709"
}
```

## 五、开通会员

POST /vpapi/meb/product-list

### 请求参数

|名称| 位置     |类型| 必选 | 值                                                       |
|---|--------|---|----|---------------------------------------------------------|
|cookie| header |string| 是  | mss=3092447f3f26snCUIBpCShl3_kRxkaJsephz*************** |


> 返回示例

```json
{
    "body": {
        "items": [
            {
                "prod_id": "240003",
                "prod_name": "天卡会员",
                "unit_price": 990,
                "mark_price": 990,
                "intro": "",
                "buy_url": "付款二维码Url"
            },
            {
                "prod_id": "240002",
                "prod_name": "月卡会员",
                "unit_price": 3000,
                "mark_price": 4900,
                "intro": "",
                "buy_url": "付款二维码Url"
            },
            {
                "prod_id": "240001",
                "prod_name": "年费会员",
                "unit_price": 9800,
                "mark_price": 36500,
                "intro": "",
                "buy_url": "付款二维码Url"
            }
        ]
    },
    "errmsg": "ok",
    "errno": "00000",
    "host_time": "20250528102206"
}
```

## 六、查询付款状态

POST /vpapi/meb/product-buy-qry

### 请求参数

|名称| 位置     |类型| 必选 | 值                                                       |
|---|--------|---|----|---------------------------------------------------------|
|cookie| header |string| 是  | mss=3092447f3f26snCUIBpCShl3_kRxkaJsephz*************** |
|prod_id| param  |string| 是  | 240002  |

> 返回示例

未支付

```json
{
    "body":{},
    "errmsg":"没有查到订单",
    "errno":"ERROR",
    "host_time":"20250528102227"
}
```
已支付

```json
{
    "body":{
        "status":"paysuc"
    },
    "errmsg":"没有查到订单",
    "errno":"ERROR",
    "host_time":"20250528102227"
}
```

## 七、查询付款状态

POST /vpapi/meb/product-buy-qry

### 请求参数

|名称| 位置     |类型| 必选 | 值                                                       |
|---|--------|---|----|---------------------------------------------------------|
|cookie| header |string| 是  | mss=3092447f3f26snCUIBpCShl3_kRxkaJsephz*************** |
|prod_id| param  |string| 是  | 240002  |

> 返回示例

未支付

```json
{
    "body":{},
    "errmsg":"没有查到订单",
    "errno":"ERROR",
    "host_time":"20250528102227"
}
```
已支付

```json
{
    "body":{
        "status":"paysuc"
    },
    "errmsg":"没有查到订单",
    "errno":"ERROR",
    "host_time":"20250528102227"
}
```

八、退出

POST /vpapi/meb/logout

### 请求参数

|名称| 位置     |类型| 必选 | 值                                                       |
|---|--------|---|----|---------------------------------------------------------|
|cookie| header |string| 是  | mss=3092447f3f26snCUIBpCShl3_kRxkaJsephz*************** |

> 返回示例

```json
{
    "body":{},
    "errmsg":"ok",
    "errno":"00000",
    "host_time":"20250528104040"
}

```