package com.github.chuanzh.enums;
/**
 * @author zhangchuan
 */
public enum RequestMode {

    POST("POST","POST请求"),
    GET("GET","GET请求"),
    GET_OR_POST("GET|POST","GET或POST请求");

    private String mode;
    private String desc;

    private RequestMode(String mode, String desc) {
        this.mode = mode;
        this.desc = desc;
    }

}
