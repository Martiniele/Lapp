package com.lib.lapp.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lib.lapp.net.utils.JSONUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxx
 * @Date 2017/6/5 4:25
 * @Description
 */

public class BookUtils {
    public static final String json = "[{\"BOOKADDR\":\"B区第16排\",\"BOOKCENG\":\"5\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"Java程序设计\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801222\"},{\"BOOKADDR\":\"C区左9排\",\"BOOKCENG\":\"3\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"Python网络爬虫\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801100\"},{\"BOOKADDR\":\"C区左7排\",\"BOOKCENG\":\"4\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"用Python写网络爬虫\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801102\"},{\"BOOKADDR\":\"C区左9排\",\"BOOKCENG\":\"6\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"Python爬虫实战\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801100\"},{\"BOOKADDR\":\"C区左8排\",\"BOOKCENG\":\"1\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"利用Python进行数据分析\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801101\"},{\"BOOKADDR\":\"C区左7排\",\"BOOKCENG\":\"2\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"操作系统概念\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801102\"},{\"BOOKADDR\":\"C区左6排\",\"BOOKCENG\":\"1\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"数据结构\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801103\"},{\"BOOKADDR\":\"C区左2排\",\"BOOKCENG\":\"2\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"C++数据结构与程序设计\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801107\"},{\"BOOKADDR\":\"C区右5排\",\"BOOKCENG\":\"4\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"数据结构（C语言版）\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801118\"},{\"BOOKADDR\":\"C区右7排\",\"BOOKCENG\":\"5\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"软件工程\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801117\"},{\"BOOKADDR\":\"C区右9排\",\"BOOKCENG\":\"1\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"Java语言程序设计\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801116\"},{\"BOOKADDR\":\"C区右2排\",\"BOOKCENG\":\"3\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"TCP/IP\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801115\"},{\"BOOKADDR\":\"C区右3排\",\"BOOKCENG\":\"1\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"图解HTTP\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801114\"},{\"BOOKADDR\":\"C区右8排\",\"BOOKCENG\":\"3\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"人月神话\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801113\"},{\"BOOKADDR\":\"C区右3排\",\"BOOKCENG\":\"2\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"TCP/IP详解\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801114\"},{\"BOOKADDR\":\"C区右11排\",\"BOOKCENG\":\"4\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"UML基础与Rose建模案例\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801111\"},{\"BOOKADDR\":\"C区右11排\",\"BOOKCENG\":\"3\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"计算机程序设计艺术\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801111\"},{\"BOOKADDR\":\"C区右6排\",\"BOOKCENG\":\"5\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"JavaEE项目开发教程\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801110\"},{\"BOOKADDR\":\"C区右1排\",\"BOOKCENG\":\"5\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"Jave Web企业级应用开发\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801112\"},{\"BOOKADDR\":\"C区右4排\",\"BOOKCENG\":\"2\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"Django Web开发\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801109\"},{\"BOOKADDR\":\"C区左1排\",\"BOOKCENG\":\"4\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"Bootstrap\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801108\"},{\"BOOKADDR\":\"C区左1排\",\"BOOKCENG\":\"4\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"计算机网络\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801108\"},{\"BOOKADDR\":\"C区左1排\",\"BOOKCENG\":\"4\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"计算机网络\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801108\"},{\"BOOKADDR\":\"C区左2排\",\"BOOKCENG\":\"5\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"Spring MVC\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801107\"},{\"BOOKADDR\":\"C区右4排\",\"BOOKCENG\":\"5\",\"BOOKMIAN\":\"A\",\"BOOKNAME\":\"Hibernate开发实战\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801109\"},{\"BOOKADDR\":\"C区左3排\",\"BOOKCENG\":\"3\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"Struct 2开发教程\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801106\"},{\"BOOKADDR\":\"C区左5排\",\"BOOKCENG\":\"4\",\"BOOKMIAN\":\"B\",\"BOOKNAME\":\"数据库系统概论\",\"BOOKTYPE\":\"E类\",\"FID\":\"9007801104\"}]";

    public static List<Book> getBooks() {
        return JSONUtils.fromJson(json, new TypeToken<ArrayList<Book>>() {
        });
    }
}
