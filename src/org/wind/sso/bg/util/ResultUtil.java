package org.wind.sso.bg.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wind.orm.util.TableUtil;

/**
 * @描述 : 返回数据工具类——针对Result或其他bean对象
 * @作者 : 胡璐璐
 * @时间 : 2019年11月22日 16:35:32
 */
@SuppressWarnings("unchecked")
public class ResultUtil {

	/**控制器（针对Servlet控制器有设置返回的数据，则可在其他地方，如：过滤器，进行获取相应的数据）**/
	public static final String control_result="result";		//Result数据（请不要在其他地方使用request的这个该attribute数据）
	/**缓存**/
	private static final Map<Class<?>,Field[]> classFieldMap=new HashMap<Class<?>,Field[]>();		//class类的字段集，value=深层次到Object的所有可用字段
	private static final Map<Class<?>,Map<String,Method>> classMethodMap=new HashMap<Class<?>,Map<String, Method>>();		//class类的方法集（次key=方法名+参数）
	
	//获取 : 返回数据，Object式（普通对象）
  	public static Map<Object,Object> getResult_Object(Object t_obj){
      	Map<Object,Object> t_resultMap=new HashMap<Object, Object>();
      	if(t_obj!=null) {
	      	Field t_fieldArr[]=getField(t_obj.getClass(), Object.class);
	  		if(t_fieldArr!=null && t_fieldArr.length>0) {
	  			Map<String,Method> methodMap=getMethodMap(t_obj.getClass(), Object.class);
	  			for(Field t_field:t_fieldArr) {
	  				String t_key=t_field.getName();
	  				Object t_value=TableUtil.get(t_obj, methodMap, t_field);
	  				if(t_value!=null) {
	  					//Map
	  					if(t_value instanceof Map) {
	  						t_resultMap.put(t_key, getResult_Map((Map<Object,Object>) t_value));
	  					//集合
	  					}else if(t_value instanceof Collection) {
	  						t_resultMap.put(t_key, getResult_Collection((Collection<Object>) t_value));
	  					//基础数据类型、String、Boolean
	  					}else if((t_value instanceof Number) || (t_value instanceof String) || (t_value instanceof Boolean)) {
	  						t_resultMap.put(t_key, t_value);
	  					//普通对象
	  					}else{
	  						t_resultMap.put(t_key, getResult_Object(t_value));
	  					}
	  				}
	  			}
	  		}
      	}
  		return t_resultMap;
      }
    //获取 : 返回数据，集合式
  	public static List<Object> getResult_Collection(Collection<? extends Object> collection){
      	List<Object> t_resultList=new ArrayList<Object>();
  		for(Object t_value:collection) {
  			if(t_value!=null) {
  				//Map
  				if(t_value instanceof Map) {
  					t_resultList.add(getResult_Map((Map<Object,Object>) t_value));
  				//集合
  				}else if(t_value instanceof Collection) {
  					t_resultList.add(getResult_Collection((Collection<Object>) t_value));
  				//基础数据类型、String、Boolean
				}else if((t_value instanceof Number) || (t_value instanceof String) || (t_value instanceof Boolean)) {
  					t_resultList.add(t_value);
  				//普通对象
  				}else{
  					t_resultList.add(getResult_Object(t_value));
  				}
  			}
  		}
  		return t_resultList;
      }
  	//获取 : 返回数据，Map式
  	public static Map<String,Object> getResult_Map(Map<Object,Object> t_valueMap){
      	Map<String,Object> t_resultMap=new HashMap<String, Object>();
  		for(Entry<Object,Object> t_entry:t_valueMap.entrySet()) {
  			String t_map_key=t_entry.getKey().toString();
  			Object t_map_value=t_entry.getValue();
  			if(t_map_value!=null) {
  				//Map
  				if(t_map_value instanceof Map) {
  					t_resultMap.put(t_map_key, getResult_Map((Map<Object,Object>) t_map_value));
  				//集合
  				}else if(t_map_value instanceof Collection) {
  					t_resultMap.put(t_map_key, getResult_Collection((Collection<Object>) t_map_value));
  				//基础数据类型、String、Boolean
					}else if((t_map_value instanceof Number) || (t_map_value instanceof String) || (t_map_value instanceof Boolean)) {
  					t_resultMap.put(t_map_key, t_map_value);
  				//普通对象
  				}else{
  					t_resultMap.put(t_map_key, getResult_Object(t_map_value));
  				}
  			}
  		}
  		return t_resultMap;
      }
      
      //获取 : 指定class的方法集（parentClass=停止获取的父class的）
      private static Field[] getField(Class<?> cls,Class<?> parentClass) {
      	Field t_fieldArr[]=classFieldMap.get(cls);
      	if(t_fieldArr==null) {
      		t_fieldArr=TableUtil.getField(cls, parentClass);
      		classFieldMap.put(cls,t_fieldArr);
      	}
      	return t_fieldArr;
      }
      //获取 : 指定class的方法集（parentClass=停止获取的父class的）
      private static Map<String,Method> getMethodMap(Class<?> cls,Class<?> parentClass) {
      	Map<String,Method> t_methodMap=classMethodMap.get(cls);		//key=小写（方法名+参数类型名） value=Method对象
      	if(t_methodMap==null) {
      		t_methodMap=TableUtil.getMethodMap(cls, Object.class, "get");
  			classMethodMap.put(cls, t_methodMap);
      	}
      	return t_methodMap;
      }
	
}
