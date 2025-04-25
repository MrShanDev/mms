package com.sxpcwlkj.common.code.entity;


import com.sxpcwlkj.common.annotation.PrintColumn;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Table 打印对象
 *
 * @author Xijue
 */
@Data
public class PrintObject<T> {
    /**
     * 打印标题
     */
    private String title;
    /**
     * 打印头
     */
    private List<PrintHeader> header;
    /**
     * 打印数据
     */
    private List<T> data;

    public PrintObject() {

    }

    /**
     * 设置打印标题
     *
     * @param title 打印标题
     */
    public PrintObject<T> setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置打印标题
     *
     * @param header 打印表头
     */
    public PrintObject<T> setHeader(List<PrintHeader> header) {
        this.header = header;
        return this;
    }

    /**
     * 设置打印标题
     *
     * @param data 打印数据
     */
    public PrintObject<T> setData(List<T> data) {
        this.data = data;
        if (data != null && !data.isEmpty()) {
            Field[] fields = data.get(0).getClass().getDeclaredFields();
            List<PrintHeader> header = new ArrayList<>();
            for (Field f : fields) {
                PrintColumn printColumn = f.getAnnotation(PrintColumn.class);
                if (printColumn != null) {
                    PrintHeader printHeader = new PrintHeader();
                    printHeader.setKey(f.getName());
                    printHeader.setTitle(printColumn.title());
                    printHeader.setWidth(printColumn.width());
                    printHeader.setHeight(printColumn.height());
                    printHeader.setType(printColumn.type());
                    printHeader.setShow(printColumn.show());
                    header.add(printHeader);
                }
            }
            this.header = header;
        }
        return this;
    }

    public PrintObject(String title, List<PrintHeader> header, List<T> data) {
        this.title = title;
        this.header = header;
        this.data = data;
    }

}
