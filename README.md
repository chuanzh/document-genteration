# document-genteration
基于Swagger接口文档自动生成，生成格式为MarkDown，后续会支持更多格式

# 使用方法
## 添加maven依赖
```xml
    <dependency>
        <groupId>com.github.chuanzh</groupId>
        <artifactId>document-generation</artifactId>
        <version>1.0.1</version>
    </dependency>
```

## 代码使用示例
```Java
DocGenerateService docGenerateService = new DocGenerateService();
docGenerateService.create("D:/doc/","com.example.springdemo.controller");
```

# 文档展示
![image](https://github.com/chuanzh/document-genteration/blob/master/doc/doc1.png) 
![image](https://github.com/chuanzh/document-genteration/blob/master/doc/doc2.png) 

# 更多
1. 后续会支持HTML、word等更多格式  
2. 支持请求参数自定义过滤规则
3. 支持自定义模板
4. ...

欢迎使用并提供宝贵的意见，也欢迎你一起参与开发  
联系邮箱：zhuangchuan@gmail.com  
