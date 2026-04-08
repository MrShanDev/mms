package com.sxpcwlkj.plugin.wechatbot;

/**
 * 企业微信机器人插件（{@code mms.plugin.wechat-bot}）对外发送能力契约。
 * <p>
 * 业务插件应在 {@code plugin.json} 中声明对该插件的依赖（保证加载顺序），在运行期通过
 * {@link WechatBotPeers#bind(com.sxpcwlkj.plugin.PluginRuntimeContext)} 取得本接口实例；底层由宿主
 * {@link com.sxpcwlkj.plugin.PluginRuntimeContext#tryInvokePeerPlugin} 转发至已加载的
 * {@link com.sxpcwlkj.plugin.MmsPlugin} 入口实现。
 * </p>
 */
public interface WechatBotMessaging {

    String sendText(String content);

    String sendMarkdown(String content);

    String sendPluginLifecycleMarkdown(String content);

    String sendImage(String base64, String md5Hex);

    String sendRawJson(String jsonBody);

    String sendHtmlAsPlainText(String html);

    String sendTemplateCardJson(String templateCardJson) throws Exception;

    String sendNewsJson(String jsonArrayArticles) throws Exception;

    String sendOneNews(String title, String description, String url, String picurl);
}
