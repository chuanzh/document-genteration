package com.github.chuanzh.template;

/**
 * @author zhangchuan
 */
public class MarkdownModel {

    public static String model = "\n" +
            "# ${title}接口文档\n" +
            "\n" +
            "<#list interfaceDetails as interfaceDetail>\n" +
            "### ${interfaceDetail_index+1!}. ${interfaceDetail[\"title\"]!}\n" +
            "${interfaceDetail[\"requestType\"]!}\n" +
            "\n" +
            "##### 请求参数\n" +
            "<#list interfaceDetail[\"request\"] as requestList>\n" +
            "#\n" +
            "|字段名称|类型|是否必填|说明|\n" +
            "|-----  |-------|-----|----- |\n" +
            "<#list requestList as request>\n" +
            "|${request.name!}|${request.type!}|<#if request.isNotNull == 0>否<#else>是</#if>|${request.desc!}|\n" +
            "</#list>\n" +
            "</#list>\n" +
            "\n" +
            "##### 响应参数\n" +
            "<#list interfaceDetail[\"response\"] as responseList>\n" +
            "#\n" +
            "|字段名称|类型|说明                              |\n" +
            "|-----   |------|-----------------------------   |\n" +
            "<#list responseList as response>\n" +
            "|${response.name!}|${response.type!}|${response.desc!}|\n" +
            " </#list>\n" +
            "</#list>\n" +
            "\n" +
            " ##### 请求示例\n" +
            " #\n" +
            " ``` javascript\n" +
            " {\n" +
            "\n" +
            " }\n" +
            " ```\n" +
            "\n" +
            " ##### 响应示例\n" +
            " #\n" +
            " ``` javascript\n" +
            " {\n" +
            " \"code\": 10000,\n" +
            " \"message\": \"成功\"\n" +
            " }\n" +
            " ```\n" +
            "</#list>\n" +
            "\n";

}
