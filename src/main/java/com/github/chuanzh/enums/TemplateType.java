package com.github.chuanzh.enums;

public enum TemplateType {

    MARKDOWN("md", "markdown文档格式");
    //HTML("html", "html文档格式");

    private String type;
    private String desc;

    private TemplateType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
