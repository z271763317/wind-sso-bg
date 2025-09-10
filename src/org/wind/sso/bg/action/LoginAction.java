package org.wind.sso.bg.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.sso.bg.annotation.An_NotLogin;
import org.wind.sso.bg.config.SessionKey;
import org.wind.sso.bg.model.User;
import org.wind.sso.bg.util.EncryptUtil;
import org.wind.sso.bg.util.RegexUtil;
import org.wind.sso.bg.util.ValidateUtil;
import org.wind.sso.bg.util.system.SessionUtil;

/**
 * @描述 : 登录Action
 * @作者 : 胡璐璐
 * @时间 : 2020年8月2日 12:39:36
 */
@An_Controller("/login")
public class LoginAction {
	
	/**登录**/
	@An_URL@An_NotLogin
	public Map<String,Object> index(HttpServletRequest request, HttpServletResponse response,HttpSession session,String userName,String passWord) throws Exception{
		userName=RegexUtil.clearEmpty(userName);
		ValidateUtil.notEmpty(userName, "缺少【用户名】");
		ValidateUtil.notEmpty(passWord, "缺少【密码】");
		//返回数据
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Long userId=SessionUtil.userId(request);
		//未登录
		if(userId==null) {
			String passWord_encrypt=EncryptUtil.getMD5(passWord);
			User objUser=Table.findOne(User.class, "user_name=? and pass_word=?", new Object[] {userName,passWord_encrypt}, false);
			if(objUser!=null) {
				/*登录成功数据*/
				request.getSession().setAttribute(SessionKey.userId,objUser.getId());
				request.getSession().setAttribute(SessionKey.userName,objUser.getUserName());
			}else {
				throw new IllegalArgumentException("用户名或密码错误");
			}
		}
		return resultMap;
	}

}
