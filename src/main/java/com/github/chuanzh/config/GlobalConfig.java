package com.github.chuanzh.config;

import com.github.chuanzh.enums.TemplateType;

/**
 * @author zhangchuan
 * 全局配置
 */
public class GlobalConfig {

    /**
     * 请求参数过滤字段
     */
    private String[] requestFilterFields;

    /**
     * 返回参数过滤字段
     */
    private String[] responseFilterFields;

    /**
     * 输出路径
     */
    private String outputDir;

    /**
     * control包名
     */
    private String packagePath;

    /**
     * 执行完成后是否输出文件目录，默认不打开
     */
    private boolean open = false;

    /**
     * 模板类型，默认为markdown
     */
    private TemplateType templateType = TemplateType.MARKDOWN;

    /**
     * 是否忽略没有注解的字段，默认为否
     */
    private boolean ignoreNoAnnotation = false;

    /**
     * 包含controller文件，为空表示所有
     */
    private String[] include = new String[]{};

    public String[] getRequestFilterFields() {
        return requestFilterFields;
    }

    public void setRequestFilterFields(String[] requestFilterFields) {
        this.requestFilterFields = requestFilterFields;
    }

    public String[] getResponseFilterFields() {
        return responseFilterFields;
    }

    public void setResponseFilterFields(String[] responseFilterFields) {
        this.responseFilterFields = responseFilterFields;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public boolean isIgnoreNoAnnotation() {
        return ignoreNoAnnotation;
    }

    public void setIgnoreNoAnnotation(boolean ignoreNoAnnotation) {
        this.ignoreNoAnnotation = ignoreNoAnnotation;
    }

    public String[] getInclude() {
        return include;
    }

    public void setInclude(String[] include) {
        this.include = include;
    }
}
