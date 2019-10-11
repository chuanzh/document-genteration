package com.github.chuanzh.util;


import com.github.chuanzh.enums.RequestMode;
import com.github.chuanzh.po.ControllerInfo;
import com.github.chuanzh.po.MethodInfo;
import com.github.chuanzh.po.Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangchuan
 */
public class ControllerInfoBuilder {

	private static Logger logger = LoggerFactory.getLogger(ControllerInfoBuilder.class);
	
	public List<ControllerInfo> handle(String packageName){
		List<ControllerInfo> resultList = new ArrayList<ControllerInfo>();
		try {
			List<String> classNames = getClassName(packageName);
			for (String className : classNames) {
				ControllerInfo controllerInfo = new ControllerInfo();
				Class clazz = Class.forName(className);
				if (clazz.getAnnotation(Api.class) != null) {
					controllerInfo.setDesc(((Api) clazz.getAnnotation(Api.class)).tags()[0]);
				} else {
					controllerInfo.setDesc(clazz.getSimpleName());
				}
				controllerInfo.setMethodInfoList(getMethodInfoByClass(clazz));
				resultList.add(controllerInfo);
			}
		} catch (Exception e) {
			logger.error("获取control类失败",e);
		}
		return resultList;
	}
	
	private List<String> getClassName(String packageName) {
		List<String> classNames = null;
		String filePath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "/");
		classNames = getClassName(filePath, null);
		return classNames;
	}

	private List<String> getClassName(String filePath, List<String> classNameList) {
		filePath = filePath.replace("\\", "/");
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				myClassName.addAll(getClassName(childFile.getPath(), myClassName));
			} else {
				String childFilePath = childFile.getPath();
				if (!childFilePath.startsWith("/")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
				} else {
					childFilePath = childFilePath.substring(childFilePath.indexOf("/classes") + 9, childFilePath.lastIndexOf("."));
				}
				childFilePath = childFilePath.replace("\\", ".").replace("/", ".");
				String lastString = childFilePath.substring(childFilePath.length()-1, childFilePath.length());
				if(childFilePath.indexOf(".svn")<0&&!".".equals(lastString)){
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
	}

	private List<MethodInfo> getMethodInfoByClass(Class clazz) {
		String classRequestMapping = "";
		if(clazz.getAnnotation(RequestMapping.class)!=null){
			classRequestMapping = ((RequestMapping)clazz.getAnnotation(RequestMapping.class)).value()[0];
		}
		List<MethodInfo> methodInfoList = new ArrayList<>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			MethodInfo methodInfo = new MethodInfo();
			methodInfo.setRequestUrl(classRequestMapping+getRequestUrl(method));
			methodInfo.setRequestType(getRequestType(method));

			if (method.getAnnotation(ApiOperation.class) != null) {
				methodInfo.setDesc(((ApiOperation)method.getAnnotation(ApiOperation.class)).value());
			}

			Parameter[] parameters = method.getParameters();
			List<Request> requests = new ArrayList<>();
			Request request = null;
			for (Parameter parameter : parameters) {
				if (ClassHelperUtils.isBaseType(parameter.getParameterizedType())
						|| ClassHelperUtils.isListBaseType(parameter.getParameterizedType())) {
					request = new Request();
					request.setName(parameter.getName());
					request.setType(parameter.getType().getSimpleName());
					request.setIsNotNull(1);
					if (parameter.getAnnotation(ApiParam.class) !=null) {
						ApiParam apiParam = parameter.getAnnotation(ApiParam.class);
						request.setDesc(apiParam.value());
					}
					requests.add(request);
				} else {
					methodInfo.setRequestBeanName(parameter.getParameterizedType().getTypeName());
				}
			}
			if (requests.size()>0) {
				methodInfo.setRequests(requests);
			}
			methodInfo.setResponseBeanName(method.getGenericReturnType().getTypeName());
			methodInfoList.add(methodInfo);
		}
		return methodInfoList;
	}

	private String getRequestUrl(Method method) {
		if (method.getAnnotation(PostMapping.class) != null) {
			return ((PostMapping) method.getAnnotation(PostMapping.class)).value()[0];
		}

		if (method.getAnnotation(GetMapping.class) != null) {
			return ((GetMapping) method.getAnnotation(GetMapping.class)).value()[0];
		}

		if (method.getAnnotation(RequestMapping.class) != null) {
			return ((RequestMapping) method.getAnnotation(RequestMapping.class)).value()[0];
		}

		return "";
	}

	private String getRequestType(Method method) {
		if (method.getAnnotation(PostMapping.class) != null) {
			return RequestMode.POST.name();
		}

		if (method.getAnnotation(GetMapping.class) != null) {
			return RequestMode.GET.name();
		}

		if (method.getAnnotation(RequestMapping.class) != null) {
			return RequestMode.GET_OR_POST.name();
		}

		return "";
	}
	
}