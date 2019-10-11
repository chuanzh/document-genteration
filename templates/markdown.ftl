
# ${title}接口文档

<#list interfaceDetails as interfaceDetail>
### ${interfaceDetail_index+1!}. ${interfaceDetail["title"]!}
${interfaceDetail["requestType"]!}

##### 请求参数
<#list interfaceDetail["request"] as requestList>
#
|字段名称|类型|是否必填|说明|
|-----  |-------|-----|----- |
<#list requestList as request>
|${request.name!}|${request.type!}|<#if request.isNotNull == 0>否<#else>是</#if>|${request.desc!}|
</#list>
</#list>

##### 响应参数
<#list interfaceDetail["response"] as responseList>
#
|字段名称|类型|说明                              |
|-----   |------|-----------------------------   |
<#list responseList as response>
|${response.name!}|${response.type!}|${response.desc!}|
 </#list>
</#list>

 ##### 请求示例
 #
 ``` javascript
 {

 }
 ```

 ##### 响应示例
 #
 ``` javascript
 {
 "code": 10000,
 "message": "成功"
 }
 ```
</#list>

