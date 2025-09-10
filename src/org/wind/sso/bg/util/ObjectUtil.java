package org.wind.sso.bg.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.wind.orm.util.TableUtil;

/**
 * @描述 : 对象工具类
 * @作者 : 胡璐璐
 * @时间 : 2021年4月11日 18:44:09
 */
@SuppressWarnings("unchecked")
public class ObjectUtil {

	/************正则表达式************/
	public static final String regex_object="[a-zA-Z_]{1}[a-zA-Z\\d_]*\\.{1}[a-zA-Z_]{1}[a-zA-Z\\d_\\.]*";		//对象
	public static final String regex_array="[a-zA-Z_]{1}[a-zA-Z\\d_]*\\[\\d+]";		//数组
	public static final String regex_array_object="[a-zA-Z_]{1}[a-zA-Z\\d_]*\\[\\d+]\\.{1}[a-zA-Z_]{1}[a-zA-Z\\d_\\.]*";		//数组对象
	
	/**获取 : 请求参数转换后的对象（JDK的BeanInfo式）——paramNamePrefix=参数名前缀**/
	public static <T> T getRequestParamToObject(HttpServletRequest request, Class<T> clszz,String paramNamePrefix) throws Exception{
		Map<String,String[]> paramMap=request.getParameterMap();
		if(paramMap.size()>0) {
			//对象：主参数—>次参数—>值【对象Map—>参数—>值【对象Map...】......如此循环】
			Map<String, Object> paramMap_object=new HashMap<String, Object>();
			Iterator<Entry<String,String[]>> iter=paramMap.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String,String[]> entry=iter.next();
				String t_key=entry.getKey();
				//指定前缀
				if(t_key.indexOf(paramNamePrefix+".")==0) {
					String t_valueArr[]=entry.getValue();
					if(t_valueArr!=null && t_valueArr.length>0) {
						String t_fieldName=t_key.substring(t_key.indexOf(".")+1);		//字段（如：obj.name，则这里是name、obj.sex.id，则是sex.id）
						//对象
						if(t_fieldName.matches(regex_object)){
							generateLayerParam(paramMap_object, t_fieldName, t_valueArr[0]);
						//基础数据类型
						}else{
							paramMap_object.put(t_fieldName, t_valueArr[0]);
						}
					}
				}
			}
			if(paramMap_object.size()>0) {
				return getRequestParamToObjectTemp(paramMap_object, clszz);
			}
		}
		return null;
	}
	//生成 : 层级Map对应的值（paramMap_object_before=上一层map）
	private static void generateLayerParam(Map<String,Object> paramMap_object_before ,String t_key,String t_value) {
		String t_fieldKey=t_key.substring(0,t_key.indexOf("."));		//当前主参数名
		String t_fieldName=t_key.substring(t_key.indexOf(".")+1);		//字段（如：sex.name，则这里是name、sex.ss.id，则是ss.id）
		Map<String,Object> paramMap_object=(Map<String, Object>) paramMap_object_before.get(t_fieldKey);		//主参数下的子参数
		if(paramMap_object==null) {
			paramMap_object=new HashMap<String, Object>();
			paramMap_object_before.put(t_fieldKey, paramMap_object);
		}
		//子对象
		if(t_fieldName.matches(regex_object)){
			generateLayerParam(paramMap_object, t_fieldName, t_value);
		//基础数据类型（伪）
		}else{
			paramMap_object.put(t_fieldName, t_value);
		}
	}
	
	//获取 : 请求参数转换后的对象（临时）
	private static <T> T getRequestParamToObjectTemp(Map<String, Object> paramMap_object, Class<T> clszz) throws Exception{
		// 获取该类的信息
		BeanInfo beanInfo = Introspector.getBeanInfo(clszz);
		// 实例化该class
		T obj = clszz.newInstance();
		// 获取该类属性的描述（字段名【属性名】、字段对应的可读取、写入的Method方法，一般是getXXX()和setXXX()）
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		Map<String,PropertyDescriptor> pdMap=new HashMap<String,PropertyDescriptor>();
		for (PropertyDescriptor descriptor:propertyDescriptors) {
			String fieldName=descriptor.getName();		//字段名
			if(!pdMap.containsKey(fieldName)) {
				pdMap.put(fieldName, descriptor);
			}
		}
		//
		boolean isHaveValue=false;		//是否有设置值
		Iterator<Entry<String, Object>> iter_value=paramMap_object.entrySet().iterator();
		while(iter_value.hasNext()) {
			Entry<String,Object> entry=iter_value.next();
			String t_key=entry.getKey();
			Object t_value=entry.getValue();
			PropertyDescriptor descriptor=pdMap.get(t_key);
			//存在
			if(descriptor!=null) {
				Object value_set=null;
				Method method = descriptor.getWriteMethod();		//可写入（设置）的方法，一般是setXXX()
				if(method!=null) {
					//子对象
					if(t_value instanceof Map) {
						Map<String,Object> t_valueMap=(Map<String, Object>) t_value;
						value_set=getRequestParamToObjectTemp(t_valueMap, descriptor.getPropertyType());
					//基础数据类型（伪）
					}else if(t_value.toString().length()>0) {
						value_set=TableUtil.cast(t_value, descriptor.getPropertyType());
					}
					if(value_set!=null) {
						//反射执行
						method.invoke(obj, new Object[] {value_set});
						isHaveValue=true;
					}
				}
			}
		}
		if(isHaveValue) {
			return obj;
		}else {
			return null;
		}
	}
	
}
