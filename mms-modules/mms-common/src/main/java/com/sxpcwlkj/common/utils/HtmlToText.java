package com.sxpcwlkj.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;

/**
 * Html处理工具类
 *
 * @name: HtmlToText
 * @author: mmsAdmin
 * @date: 2022/12/01
 **/

public class HtmlToText extends HTMLEditorKit.ParserCallback {


    /**
     * Html 代码解析成 txt
     * @param str
     * @return
     */
    public static String getHtmlToText(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            html2Text.parse(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return html2Text.toString();
    }

    //======================== 下面是工具 ======================

    private static HtmlToText html2Text = new HtmlToText();
    StringBuffer s;

    public HtmlToText() {
    }

    public void parse(String str) throws IOException {

        InputStream iin = new ByteArrayInputStream(str.getBytes());
        Reader in = new InputStreamReader(iin);
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(in, this, Boolean.TRUE);
        iin.close();
        in.close();
    }

    @Override
    public void handleText(char[] text, int pos) {
        s.append(text);
    }



}

