/*
 * Copyright 2015 www.hyberbin.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Email:hyberbin@qq.com
 */
package com.zlw.generator.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 */
public class YouDaoTranslate {

    public static String getEnglish(String chinese) {
        if(chinese==null){
            return "";
        }
        if (chinese.contains("(")) {
            chinese = chinese.substring(0, chinese.indexOf("("));
        }
        try {
            Connection connect = Jsoup.connect("http://fanyi.youdao.com/openapi.do?keyfrom=hsoj2013&key=1170344442&type=data&doctype=json&version=1.1&q=" + toUtf8String(chinese));
            connect.ignoreContentType(true);
            connect.ignoreHttpErrors(true);
            connect.method(Connection.Method.GET);
            Connection.Response response = connect.execute();
            String data = response.body();
            JSONObject jsonObject = new JSONObject(data);
            String toString = jsonObject.get("translation").toString();
            return toString.replaceAll("[^a-zA-Z ]", "").replaceAll("the ", "").replaceAll("The ", "").replaceAll("Table ", "").replaceAll("table ", "").replaceAll("of ", "");
        } catch (IOException ex) {
            Logger.getLogger(YouDaoTranslate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(YouDaoTranslate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getEnglish("中国(中)"));

    }

    /**
     * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     * @param s 原文件名
     * @return 重新编码后的文件名
     * @author yue
     */
    public static String toUtf8String(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c >= 0 && c <= 255) {
                    sb.append(c);
                } else {
                    byte[] b;

                    b = Character.toString(c).getBytes("utf-8");

                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        if (k < 0) {
                            k += 256;
                        }
                        sb.append("%").append(Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
