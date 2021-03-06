package com.github.chuanzh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author zhangchuan
 */
public class ClassHelperUtils {
	private static Logger logger = LoggerFactory.getLogger(ClassHelperUtils.class.getName());

	private static String[] BASE_TYPE_ARR = {"boolean","char","byte","short","int","long","float","double","Boolean","Char","Byte","Short","Integer","Long","Float","Double","String","BigDecimal","Date"};
	private static HashSet BASE_TYPE = new HashSet();
	private static HashSet FILTER_FIELDS = new HashSet();

	static {
		for (String s : BASE_TYPE_ARR) {
			BASE_TYPE.add(s);
		}
		FILTER_FIELDS.add("serialVersionUID");
	}
	 
	/**
	 * 获取某个类及其父类的Field
	 * @param clazz 类
	 * @return 所有字段
	 */
	public static ArrayList<Field> findClassAllField (Class clazz){
		ArrayList<Field> list = new ArrayList<Field>();
		try {
			List<Class>  clazzList = new ArrayList();

			while(!"java.lang.Object".equals(clazz.getName())){
				clazzList.add(clazz);
				clazz = clazz.getSuperclass();
				if (clazz == null) {
					break;
				}
			}
			Collections.reverse(clazzList);
			for(int i=0;i<clazzList.size();i++){
				Field[] fs =  clazzList.get(i).getDeclaredFields();
				for(Field f : fs){
					list.add(f);
				}
			}
		} catch (Exception e) {
			logger.error("获取类变量失败"+clazz.getName(),e);
		}
		return list;
	}

	public static boolean isFilterField(String field) {
		if (FILTER_FIELDS.contains(field)) {
			return true;
		}
		return false;
	}

	public static boolean isBaseType(Type type) {
		String simpleName = type.getTypeName().substring(type.getTypeName().lastIndexOf(".")+1);
		return isBaseTypeName(simpleName);
	}

	public static boolean isListTypeName(String simpleName) {
		if ("java.util.List".equals(simpleName)) {
			return true;
		}
		return false;
	}

	public static boolean isListBaseType(Type type) {
		String simpleName = type.getTypeName();
		String subSimpleName = "Long";
		if (type.getTypeName().indexOf("<") != -1) {
			simpleName = type.getTypeName().substring(0, type.getTypeName().lastIndexOf("<"));
			subSimpleName = type.getTypeName().substring(type.getTypeName().lastIndexOf(".")+1, type.getTypeName().lastIndexOf(">"));
		}
		if ("java.util.List".equals(simpleName) && isBaseTypeName(subSimpleName)) {
			return true;
		}
		return false;
	}

	public static boolean isBaseTypeName(String simpleName) {
		if (BASE_TYPE.contains(simpleName)){
			return true;
		}
		return false;
	}

	public static String getGenericTypeName(Field field) {
		String genericType = field.getGenericType().getTypeName();
		if (genericType.indexOf("<") != -1) {
			String simpleType = field.getType().getSimpleName();
			String simpleSubName = genericType.substring(genericType.lastIndexOf(".")+1);
			return simpleType +"<"+simpleSubName;
		}
		String typeName = genericType.substring(genericType.lastIndexOf(".")+1);
		if (typeName.equals("T")) {
			return "Object";
		}
		return typeName;
	}

	public static String subClass(Type type) {
		String typeName = type.getTypeName();
		String subTypeName = typeName;
		String simpleName = null;
		if (typeName.indexOf("<") != -1){
			subTypeName = typeName.substring(typeName.lastIndexOf("<")+1, typeName.indexOf(">"));
			simpleName = subTypeName.substring(subTypeName.lastIndexOf(".")+1);
		}
		if (simpleName != null && BASE_TYPE.contains(simpleName)){
			return null;
		}
		return subTypeName;
	}

	public static void mkdirs(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			boolean result = dir.mkdirs();
			if (result) {
				logger.debug("创建目录： [" + dirPath + "]");
			}
		}
	}

	public static void open(String dirPath) {
		try {
			String osName = System.getProperty("os.name");
			if (osName != null) {
				if (osName.contains("Mac")) {
					Runtime.getRuntime().exec("open " + dirPath);
				} else if (osName.contains("Windows")) {
					Runtime.getRuntime().exec("cmd /c start " + dirPath);
				} else {
					logger.debug("文件输出目录:" + dirPath);
				}
			}
		} catch (IOException e) {
			logger.error("打开目录失败",e);
		}
	}

}
