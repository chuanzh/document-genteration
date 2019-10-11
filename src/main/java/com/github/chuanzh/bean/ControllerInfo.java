package com.github.chuanzh.bean;

import java.util.List;

/**
 * @author zhangchuan
 */
public class ControllerInfo {


    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求描述
     */
    private String desc;

    /**
     * 所有的方法集合
     */
    private List<MethodInfo> methodInfoList;


    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<MethodInfo> getMethodInfoList() {
        return methodInfoList;
    }

    public void setMethodInfoList(List<MethodInfo> methodInfoList) {
        this.methodInfoList = methodInfoList;
    }
}
