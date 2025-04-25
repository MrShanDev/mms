import {ElMessage} from "element-plus";
import {Session} from "/@/utils/storage";

interface WebSocketProps {
    url?: string; // websocket地址
    heartTime?: number; // 心跳时间间隔，默认为 50000 ms
    heartMsg?: string; // 心跳信息，默认为'ping'
    reconnectCount?: number; // 重连次数，默认为 10
    reconnectTime?: number; // 重连时间间隔，默认为 10000 ms
    message: (ev: MessageEvent) => any; // 接收消息的回调
    open?: (ev: Event) => any; // 连接成功的回调
    close?: (ev: CloseEvent) => any; // 关闭的回调
    error?: (ev: Event) => any; // 错误的回调
}

// webSocket 对象
let webSocket: WebSocket | null = null;
// webSocket定时器id
let setIntervalId: NodeJS.Timeout | null = null;

export const initWebSocket = (config: WebSocketProps) => {
    if (typeof WebSocket === "undefined") {
        ElMessage.error("您的浏览器不支持Websocket通信协议，请使用Chrome或者其他高版本的浏览器！");
        return;
    }
    if (webSocket != null && webSocket.readyState === webSocket.OPEN) {
        return webSocket;
    }
    createWebSocket(config);
    return webSocket;
};

let falg = false;
// 重连次数
let reconnectCount = 10;
// 重连定时器id
let reconnectId: NodeJS.Timeout | null = null;

/**
 * 创建WebSocket
 * @param config
 */
const createWebSocket = (config: WebSocketProps) => {
    // 初始化 WebSocket
    webSocket = new WebSocket(import.meta.env.VITE_APP_WS_URL + "?token=" + `${Session.get('token')}`);
    webSocket.onopen = (ev: Event) => {
        config.open && config.open(ev);
        /**
         * 发送心跳
         * 使用Nginx代理WebSocket的时候，客户端与服务器握手成功后，如果在60秒内没有数据交互，就会自动断开连接。
         * Nginx默认的断开链接时间为60秒
         */
        sendPing(config.heartTime ?? 50000, config.heartMsg ?? "ping");
    };
    webSocket.onmessage = (ev: MessageEvent) => config.message(ev);
    webSocket.onerror = (ev: Event) => error(config, ev);
    webSocket.onclose = (ev: CloseEvent) => close(config, ev);
};
/**
 * WebSocket 关闭的回调方法
 * @param config
 * @param ev
 */
const close = (config: WebSocketProps, ev: CloseEvent) => {
    config.close && config.close(ev);
    clearInterval(Number(setIntervalId));
};
/**
 * WebSocket 关闭的回调方法
 * @param config
 * @param ev
 */
const error = (config: WebSocketProps, ev: Event) => {
    config.error && config.error(ev);
    if (falg) return;
    reconnectId = setInterval(() => {
        falg = true;
        reconnectCount++;
        console.log("正在重新连接，次数：" + reconnectCount);
        let socket = initWebSocket(config);
        if (socket?.readyState === socket?.OPEN) {
            reconnectCount = 0;
            falg = false;
            clearInterval(Number(reconnectId));
        }
        if (reconnectCount >= 5) {
            clearInterval(Number(reconnectId));
        }
    }, config.reconnectTime ?? 10000);
};
/**
 * 发送心跳
 * @param {number} heartTime 心跳间隔毫秒 默认50000
 * @param {string} heartMsg 心跳名称 默认字符串ping
 */
const sendPing = (heartTime: number, heartMsg: string) => {
    send(new DataMsg(CmdEnum.SYS_PING).addMsgData({
        type: CmdEnum.SYS_PING,
        data: heartMsg
    })).then(r => {})
    setIntervalId = setInterval(() => {
        send(new DataMsg(CmdEnum.SYS_PING).addMsgData({
            type: CmdEnum.SYS_PING,
            data: heartMsg
        })).then(r => {})
    }, heartTime);
};
/**
 * 发送消息
 * @param msg
 */
export const send = (msg: DataMsg):Promise<boolean> => {

    return new Promise<boolean>((resolve, reject)=>{
        webSocket?.send(msg.build());
        resolve(true);
    });


}

/**
 * ==================接收/发送消息对象==================
 */
export class DataMsg {
    /**
     * 通信命令
     */
    public cmd: CmdEnum;

    /**
     * 消息对象
     *
     **/
    public data?: {
        type?: MsgEnum | 0,
        data?: string | object | ChatRoom[] | MsgInfo
    } | string;

    /**
     * 初始化
     * @param cmd
     */
    constructor(cmd: CmdEnum) {
        this.cmd = cmd;
        return this;
    }

    /**
     * 设置data
     * @param data 内容对象
     */
    public addMsgData(data: {
        type: MsgEnum | 0,
        data: string | MsgInfo
    } | string): DataMsg {
        this.data ??= data;
        return this;
    }

    /**
     * 获取发送json字符串
     */
    public build(): string {
        return JSON.stringify({
            cmd: this.cmd,
            data: this.data? this.data:""
        })
    }
}

/**
 * 聊天室
 */
export interface ChatRoom {

    /**
     * 聊天室ID
     */
    chatRoomId: number;
    /**
     * 聊天用户ID
     */
    userId: string | number;
    /**
     * 聊天室用户
     */
    chatUserIds: Array<number>
    /**
     * 类型:0 私聊  1：群聊
     */
    chatRoomType: RoomEnum.PRIVATE | RoomEnum.GROUP;
    /**
     * 聊天室头像
     */
    headPortrait: string;
    /**
     * 聊天室标题
     */
    title: string;
    /**
     * 未读数量
     */
    count: number;
    /**
     * 消息列表
     */
    msgInfoList: MsgInfo[]
    /**
     * 是否当前聊天窗口
     */
    checked: boolean;
}

/**
 * 消息详情
 */
export interface MsgInfo {
    /**
     * 聊天室id
     **/
    chatRoomId: number;
    /**
     * 消息类型
     **/
    msgType: MsgEnum;
    /**
     * 是否是自己
     */
    self:boolean;
    /**
     * 发送者
     */
    sendUser?:{userId:number,headPortrait:string,name:string,sessionId:string,token:string};
    /**
     * 接收人
     */
    recipientUser?:{userId:number,headPortrait:string,name:string,sessionId:string};
    /**
     * 消息内容
     *
     **/
    msgContent?: string;
    /**
     * 消息发送时间（yyyy-MM-dd HH:mm:ss）
     *
     **/
    sendTime?: string;
    /**
     * 是否发送给所有人
     *
     **/
    everyone?: boolean;
}

export enum MsgEnum {
    // 系统消息
    SYSTEM = 0,
    // 普通文本
    TXT = 1,
    // html文本
    HTML = 2,
    // 图片Path
    IMAGE = 3,
    // 文件Path
    FILE = 4,
    // 音频Path
    AUDIO = 5,
    // 视频Path
    VIDEO = 6,
    // 打电话
    CALL_PHONE_VIDEO = 7
}

export enum RoomEnum {
    // 私人，一对一
    PRIVATE = 1,
    // 群组 多对多
    GROUP = 2,
}

export enum CmdEnum {
    /**
     * 心跳检测
     */
    SYS_PING = 0,
    /**
     * 连接成功
     */
    SUCCEED =1,
    /**
     * 获取在线用户列表
     */
    USER_LIST = 100001,
    /**
     * 发送消息
     */
    SEND_MSG = 100002,
    /**
     * 某聊天室有新消息
     */
    UPDATE_CHATROOM = 100003,
}


