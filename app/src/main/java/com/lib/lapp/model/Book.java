package com.lib.lapp.model;

/**
 * @author wxx
 * @Date 2017/6/5 4:24
 * @Description
 */

public class Book {
    public String FID;
    public String BOOKNAME;
    public String BOOKTYPE;
    public String BOOKADDR;
    public String BOOKMIAN;
    public String BOOKCENG;

    @Override
    public String toString() {
        return BOOKNAME;
    }
}
