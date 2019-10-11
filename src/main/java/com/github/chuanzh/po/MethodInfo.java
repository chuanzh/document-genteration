package com.github.chuanzh.po;

import java.util.List;

/**
 * @author zhangchuan
 */
public class MethodInfo {

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private String requestType;

    /**
     * 请求方法描述
     */
    private String desc;


    /**
     * 请求参数，如果请求参数都是基本类型，则封装在这个集合中
     */
    private List<Request> requests;

    /**
     * 请求对象
     */
    private String requestBeanName;

    /**
     * 返回对象
     */
    private String responseBeanName;


    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRequestBeanName() {
        return requestBeanName;
    }

    public void setRequestBeanName(String requestBeanName) {
        this.requestBeanName = requestBeanName;
    }

    public String getResponseBeanName() {
        return responseBeanName;
    }

    public void setResponseBeanName(String responseBeanName) {
        this.responseBeanName = responseBeanName;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
