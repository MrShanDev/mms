package com.sxpcwlkj.demo.mail.impl;

import com.sxpcwlkj.demo.mail.DemoMailEntity;
import com.sxpcwlkj.demo.mail.DemoMailService;
import org.dromara.email.jakarta.api.MailClient;
import org.dromara.email.jakarta.comm.entity.MailMessage;
import org.dromara.email.jakarta.core.factory.MailFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DemoMailServiceImpl implements DemoMailService {
    @Override
    public Boolean sendMail(DemoMailEntity demoMailEntity) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", demoMailEntity.getCode());
        MailMessage message = MailMessage.Builder()
                //收件人地址，此处可以为字符串的单个地址也可以为一个List<String>的群发地址
                //例如  .mailAddress (new ArrayList<String>().add("XXXXXXXXX@qq.com"))
                .mailAddress(demoMailEntity.getMailAddress())
                // 邮件的标题
                .title(demoMailEntity.getMailTitle())
                // 邮件的文字正文，但是如果发送html邮件时候，大多数的邮件服务商会隐藏掉body的信息
                .body(demoMailEntity.getMailContent())
                //html模板来源，此处与mailAddress相似可以为一个字符串形式的html文件名，此时默认读取resources/template目录下的文件
                //也可以为一个 InputStream 输入流
                //例如 .html(Files.newInputStream(new FileUtil("D:\\index.html").toPath()))
                //.html("index.html")
                /* html模板变量，如果你的html不需要变量，则可以为空，这里有多种的用法
                Map map的key为变量名称，map的value为变量的值
                例如 Map<String,String> map = new HashMap<String,String>().put("变量名称","变量值");    .htmlValues(map)
                还可以直接写入变量名称和变量值 如.htmlValues("变量名称",“变量值”)
                此处也可以使用接口进行，接口形式在下方详细介绍
                */
                //.htmlValues(map)
                //抄送人，可以添加一个或多个，使用方式与.mailAddress一致
                //.cc("xxxxxx@qq.com")
                // 密送人，可以添加一个或多个，使用方式与.mailAddress一致
                //.bcc("xxxxxx@qq.com")
                // 附件，此处与.htmlValues使用方法相似，如果你只有一个附件，可以直接填写key value 如果你有多个附件，这里可以接受一个map
                //.files(“文件名”,"文件路径")
                //3.0版本支持发送文件自动压缩，你可以选择指定这里的压缩文件名称，框架将会自动将 .files中的所有文件打为一个压缩包并发送
                //.zipName("压缩文件名称")
                .build();

        MailClient mail = MailFactory.createMailClient("smsMail");
        mail.send(message);
        return true;
    }
}
