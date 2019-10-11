package com.github.chuanzh.service;

import com.github.chuanzh.bean.ControllerInfo;
import com.github.chuanzh.bean.MethodInfo;
import com.github.chuanzh.bean.Request;
import com.github.chuanzh.bean.Response;
import com.github.chuanzh.template.MarkdownModel;
import com.github.chuanzh.template.StringTemplateLoader;
import com.github.chuanzh.util.DgFunc;
import com.github.chuanzh.util.PackageUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangchuan
 */
public class DocGenerateService {


    private Logger logger = LoggerFactory.getLogger(DocGenerateService.class);

    private static Configuration cfg = new Configuration();
    private Template temp = null;
    private static String encode = "UTF-8";
    private PackageUtil packageUtil = new PackageUtil();

    private void initTemplatePath() {
        cfg.setTemplateLoader(new StringTemplateLoader(MarkdownModel.model));
        //cfg.setClassForTemplateLoading(DocGenerateService.class,"../template/");
        cfg.setDefaultEncoding(encode);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setNumberFormat("#.######");
    }

    public void create(String docPath, String packageName) throws IOException {
        initTemplatePath();

        File filePath = new File(docPath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }

        List<ControllerInfo> controllerInfoList = packageUtil.handle(packageName);
        for (ControllerInfo controllerInfo : controllerInfoList) {
            List<String> interfaceTitles = new ArrayList<String>();
            List<HashMap<String, Object>> interfaceDetails = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> interfaceDetail = null;
            File file = new File(docPath + controllerInfo.getDesc() + ".md");
            if (!file.exists()) {
                file.createNewFile();
            }

            for (MethodInfo methodInfo : controllerInfo.getMethodInfoList()) {
                interfaceTitles.add(methodInfo.getDesc());
                interfaceDetail = new HashMap<String, Object>();
                interfaceDetail.put("title", methodInfo.getDesc());
                interfaceDetail.put("requestType", String.format("(%s)%s", methodInfo.getRequestType(), methodInfo.getRequestUrl()));
                interfaceDetail.put("request", buildRequestInfo(methodInfo));
                interfaceDetail.put("response", buildResponseInfo(methodInfo));
                interfaceDetails.add(interfaceDetail);
            }

            StringWriter sw = processTemplate(controllerInfo.getDesc(), interfaceTitles, interfaceDetails);

            readToFile(sw, file.getPath());
            logger.info("创建文档完成：" + file.getPath());
        }

    }

    private StringWriter processTemplate(String title, List<String> interfaceTitles, List<HashMap<String, Object>> interfaceDetails) {
        StringWriter sw = new StringWriter();
        try {
            this.temp = cfg.getTemplate("");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", title);
            map.put("interfaceTitles", interfaceTitles);
            map.put("interfaceDetails", interfaceDetails);
            this.temp.process(map, sw);
        } catch (Exception e) {
            logger.error("处理模板信息失败", e);
        }

        return sw;
    }

    private void readToFile(StringWriter sw, String filePath) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath);
            fw.write(sw.toString());
        } catch (IOException e) {
            logger.error("写入文件失败", e);
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                logger.error("关闭文件流失败", e);
            }
        }
    }

    private List<List<Request>> buildRequestInfo(MethodInfo methodInfo) {
        List<List<Request>> requestList = new ArrayList<>();
        Request request = null;
        try {
            if (methodInfo.getRequests() != null) {
                requestList.add(methodInfo.getRequests());
            }
            if (methodInfo.getRequestBeanName() != null) {
                String requestBeanName = methodInfo.getRequestBeanName();
                String subRequestBeanName = null;
                if (requestBeanName.indexOf("<") != -1) {
                    requestBeanName = requestBeanName.substring(0, requestBeanName.indexOf("<"));
                    subRequestBeanName = methodInfo.getRequestBeanName().substring(methodInfo.getRequestBeanName().lastIndexOf("<") + 1, methodInfo.getRequestBeanName().indexOf(">"));
                }
                if (!DgFunc.isListTypeName(requestBeanName)) {
                    doRequestInfo(requestBeanName, requestList);
                }
                if (subRequestBeanName != null) {
                    doRequestInfo(subRequestBeanName, requestList);
                }
            }
        } catch (Exception e) {
            logger.error("构建请求参数失败", e);
        }
        return requestList;
    }

    private void doRequestInfo(String beanName, List<List<Request>> requestList) {
        Class requestClass = null;
        try {
            requestClass = Class.forName(beanName);
        } catch (ClassNotFoundException e) {
            return;
        }
        List<Field> requestFields = DgFunc.findClassAllField(requestClass);
        List<Request> requests = new ArrayList<>();
        requestList.add(requests);
        for (Field field : requestFields) {
            if (DgFunc.isFilterField(field.getName())) {
                continue;
            }
            Request request = new Request();
            request.setName(field.getName());
            request.setType(field.getType().getSimpleName());
            if (field.getAnnotation(ApiModelProperty.class) != null) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                request.setDesc(apiModelProperty.value());
                request.setIsNotNull(apiModelProperty.required() ? 1 : 0);
            }
            requests.add(request);
            if (!DgFunc.isBaseType(field.getGenericType())) {
                String subClass = DgFunc.subClass(field.getGenericType());
                /** 子类不能等于父类，避免死循环 */
                if (subClass != null && !subClass.equals(beanName)) {
                    doRequestInfo(subClass, requestList);
                }
            }
        }
    }

    private void doResponseInfo(String beanName, List<List<Response>> responseList) {
        Class responseClass = null;
        try {
            responseClass = Class.forName(beanName);
        } catch (ClassNotFoundException e) {
            return;
        }
        List<Field> responseFields = DgFunc.findClassAllField(responseClass);
        List<Response> responses = new ArrayList<>();
        responseList.add(responses);
        for (Field field : responseFields) {
            if (DgFunc.isFilterField(field.getName())) {
                continue;
            }
            Response response = new Response();
            response.setName(field.getName());
            response.setType(field.getType().getSimpleName());
            if (field.getAnnotation(ApiModelProperty.class) != null) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                response.setDesc(apiModelProperty.value());
            }
            responses.add(response);
            if (!DgFunc.isBaseType(field.getGenericType())) {
                String subClass = DgFunc.subClass(field.getGenericType());
                /** 子类不能等于父类，避免死循环 */
                if (subClass != null && !subClass.equals(beanName)) {
                    doResponseInfo(subClass, responseList);
                }
            }
        }
    }

    private List<List<Response>> buildResponseInfo(MethodInfo methodInfo) {
        List<List<Response>> responseList = new ArrayList<>();
        try {
            String responseBeanName = methodInfo.getResponseBeanName();
            String subResponseBeanName = null;
            if (responseBeanName.indexOf("<") != -1) {
                responseBeanName = responseBeanName.substring(0, responseBeanName.indexOf("<"));
                subResponseBeanName = methodInfo.getResponseBeanName().substring(methodInfo.getResponseBeanName().lastIndexOf("<") + 1, methodInfo.getResponseBeanName().indexOf(">"));
            }

            if (!DgFunc.isListTypeName(responseBeanName)) {
                doResponseInfo(responseBeanName, responseList);
            }
            if (subResponseBeanName != null) {
                doResponseInfo(subResponseBeanName, responseList);
            }

        } catch (Exception e) {
            logger.error("构建请求参数失败", e);
        }
        return responseList;
    }


}
