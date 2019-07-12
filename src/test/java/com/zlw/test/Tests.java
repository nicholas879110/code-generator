package com.zlw.test;

import java.util.HashMap;
import java.util.Map;

public class Tests {


    public static Map map(){
        Map<String,Object> map=new HashMap<>();
        map.put("1",2);
        return map;
    }

    public static void main(String[] args) {
        Map<String,Demo> map=Tests.map();
        Demo demo=map.get("1");
        System.out.println(map);
    }
}
