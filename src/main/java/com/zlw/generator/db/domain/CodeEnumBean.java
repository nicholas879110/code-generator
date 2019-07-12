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
package com.zlw.generator.db.domain;

/**
 *
 * @author Hyberbin
 */
public class CodeEnumBean {

    private String zdlx;//字典类型
    private String sjx;//数据项
    private String sjxms;//数据项描述
    private String yyms;//备注

    public CodeEnumBean(String zdlx, String sjx, String sjxms, String yyms) {
        this.zdlx = zdlx;
        this.sjx = sjx;
        this.sjxms = sjxms;
        this.yyms = yyms;
    }

    public CodeEnumBean() {
    }

    public String getZdlx() {
        return zdlx;
    }

    public void setZdlx(String zdlx) {
        this.zdlx = zdlx;
    }

    public String getSjx() {
        return sjx;
    }

    public void setSjx(String sjx) {
        this.sjx = sjx;
    }

    public String getSjxms() {
        return sjxms;
    }

    public void setSjxms(String sjxms) {
        this.sjxms = sjxms;
    }

    public String getYyms() {
        return yyms;
    }

    public void setYyms(String yyms) {
        this.yyms = yyms;
    }

}
