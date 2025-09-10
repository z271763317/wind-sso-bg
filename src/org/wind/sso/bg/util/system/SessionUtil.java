package org.wind.sso.bg.util.system;

import javax.servlet.http.HttpServletRequest;

import org.wind.sso.bg.config.SessionKey;

/**
 * @描述 : 会话工具类（一般指HttpSession）——获取session的数据
 * @作者 : 胡璐璐
 * @时间 : 2020年12月9日 13:03:23
 */
@SuppressWarnings("unchecked")
public final class SessionUtil {
	
	//获取 : 指定Key的值
	private static <T> T getAttribute(HttpServletRequest request,String key) {
		return (T) request.getSession().getAttribute(key);
	}
	
	/**用户ID**/
	public static Long userId(HttpServletRequest request){
		return getAttribute(request, SessionKey.userId);
	}
	/**用户名**/
	public static String userName(HttpServletRequest request){
		return getAttribute(request, SessionKey.userName);
	}
	
}