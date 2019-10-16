package com.github.chuanzh;

import com.github.chuanzh.config.GlobalConfig;
import com.github.chuanzh.engine.AbstractTemplateEngine;
import com.github.chuanzh.engine.FreemarkerTemplateEngine;
import com.github.chuanzh.po.ControllerInfo;
import com.github.chuanzh.po.MethodInfo;
import com.github.chuanzh.po.Request;
import com.github.chuanzh.po.Response;
import com.github.chuanzh.util.ClassHelperUtils;
import com.github.chuanzh.util.ControllerInfoBuilder;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhangchuan
 */
public class DocAutoGenerator {

    private Logger logger = LoggerFactory.getLogger(DocAutoGenerator.class);

    private ControllerInfoBuilder controllerInfoBuilder = new ControllerInfoBuilder();
    private GlobalConfig globalConfig;
    private AbstractTemplateEngine abstractTemplateEngine;

    public void execute() {
        if (!checkConfig()) {
            return;
        }
        List<Map<String, Object>> params = null;
        try {
            params = buildTemplateParam(globalConfig.getOutputDir(), globalConfig.getPackagePath(), Arrays.asList(globalConfig.getInclude()));
        } catch (IOException e) {
            logger.info("构建模板参数失败", e);
            return;
        }

        if (null == abstractTemplateEngine) {
            abstractTemplateEngine = new FreemarkerTemplateEngine();
        }
        ClassHelperUtils.mkdirs(globalConfig.getOutputDir());
        for (Map<String, Object> param : params) {
            abstractTemplateEngine.process(param);
            logger.info("生成接口文档完成："+param.get("path"));
        }
        if (globalConfig.isOpen()){
            ClassHelperUtils.open(globalConfig.getOutputDir());
        }
    }

    private boolean checkConfig() {
        if (null == globalConfig) {
            logger.info("请配置globalConfig参数");
            return false;
        }
        if (globalConfig.getOutputDir() == null) {
            logger.info("请配置输出目录");
            return false;

        }
        if (globalConfig.getPackagePath() == null) {
            logger.info("请配置Controller包目录");
            return false;
        }
        return true;
    }

    private List<Map<String, Object>> buildTemplateParam(String docPath, String packageName, List<String> includes) throws IOException {
        List<Map<String, Object>> params = new ArrayList<>();
        Map<String, Object> param = null;
        List<ControllerInfo> controllerInfoList = controllerInfoBuilder.handle(packageName, includes);
        for (ControllerInfo controllerInfo : controllerInfoList) {
            List<String> interfaceTitles = new ArrayList<String>();
            List<HashMap<String, Object>> interfaceDetails = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> interfaceDetail = null;

            for (MethodInfo methodInfo : controllerInfo.getMethodInfoList()) {
                interfaceTitles.add(methodInfo.getDesc());
                interfaceDetail = new HashMap<String, Object>();
                interfaceDetail.put("title", methodInfo.getDesc());
                interfaceDetail.put("requestType", String.format("(%s)%s",methodInfo.getRequestType(), methodInfo.getRequestUrl()));
                interfaceDetail.put("request", buildRequestInfo(methodInfo));
                interfaceDetail.put("response", buildResponseInfo(methodInfo));
                interfaceDetails.add(interfaceDetail);
            }

            param = new HashMap<>();
            param.put("path", docPath+controllerInfo.getDesc()+".md");
            param.put("title", controllerInfo.getDesc());
            param.put("interfaceTitles", interfaceTitles);
            param.put("interfaceDetails", interfaceDetails);
            params.add(param);
        }
        return params;
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
                    subRequestBeanName = methodInfo.getRequestBeanName().substring(methodInfo.getRequestBeanName().lastIndexOf("<")+1, methodInfo.getRequestBeanName().indexOf(">"));
                }
                if (!ClassHelperUtils.isListTypeName(requestBeanName)) {
                    doRequestInfo(requestBeanName, requestList);
                }
                if (subRequestBeanName != null
                        && !ClassHelperUtils.isBaseTypeName(subRequestBeanName.substring(subRequestBeanName.lastIndexOf(".")+1))) {
                    doRequestInfo(subRequestBeanName, requestList);
                }
            }
        } catch (Exception e) {
            logger.error("构建请求参数失败",e);
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
        List<Field> requestFields = ClassHelperUtils.findClassAllField(requestClass);
        List<Request> requests = new ArrayList<>();
        requestList.add(requests);
        for (Field field : requestFields) {
            if (ClassHelperUtils.isFilterField(field.getName())) {
                continue;
            }
            Request request = new Request();
            request.setName(field.getName());
            request.setType(field.getType().getSimpleName());
            if (field.getAnnotation(ApiModelProperty.class) !=null) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                request.setDesc(apiModelProperty.value());
                request.setIsNotNull(apiModelProperty.required()?1:0);
            }
            requests.add(request);
            if (!ClassHelperUtils.isBaseType(field.getGenericType())){
                String subClass = ClassHelperUtils.subClass(field.getGenericType());
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
        List<Field> responseFields = ClassHelperUtils.findClassAllField(responseClass);
        List<Response> responses = new ArrayList<>();
        responseList.add(responses);
        for (Field field : responseFields) {
            if (ClassHelperUtils.isFilterField(field.getName())) {
                continue;
            }
            Response response = new Response();
            response.setName(field.getName());
            response.setType(field.getType().getSimpleName());
            if (field.getAnnotation(ApiModelProperty.class) !=null) {
                ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
                response.setDesc(apiModelProperty.value());
            }
            responses.add(response);
            if (!ClassHelperUtils.isBaseType(field.getGenericType())){
                String subClass = ClassHelperUtils.subClass(field.getGenericType());
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
                subResponseBeanName = methodInfo.getResponseBeanName().substring(methodInfo.getResponseBeanName().lastIndexOf("<")+1, methodInfo.getResponseBeanName().indexOf(">"));
            }

            if (!ClassHelperUtils.isListTypeName(responseBeanName)) {
                doResponseInfo(responseBeanName, responseList);
            }
            if (subResponseBeanName != null
                    && !ClassHelperUtils.isBaseTypeName(subResponseBeanName.substring(subResponseBeanName.lastIndexOf(".")+1))) {
                doResponseInfo(subResponseBeanName, responseList);
            }

        } catch (Exception e) {
            logger.error("构建请求参数失败",e);
        }
        return responseList;
    }


    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }
}
