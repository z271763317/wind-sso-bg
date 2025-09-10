package org.wind.sso.bg.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wind.mvc.annotation.controller.An_Controller;
import org.wind.mvc.annotation.controller.An_URL;
import org.wind.orm.Table;
import org.wind.sso.bg.model.User;
import org.wind.sso.bg.util.EncryptUtil;
import org.wind.sso.bg.util.ValidateUtil;
import org.wind.sso.bg.util.system.SessionUtil;

/**
 * @描述 : 用户Action
 * @作者 : 胡璐璐
 * @时间 : 2020年9月3日 18:06:43
 */
@An_Controller("/user")
public class UserAction {

	/**主页**/
	@An_URL
	public String main(HttpServletRequest request){
		request.setAttribute("userName", SessionUtil.userName(request));
		return "";
	}
	/**首页**/
	@An_URL("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "/index";
	}
	/**修改密码**/
	@An_URL("/modifyPassWord")
	public void modifyPassWord(HttpServletRequest request,HttpServletResponse response,String oldPassWord,String newPassWord,String confirmNewPassWord) throws Exception{
		ValidateUtil.notEmpty(oldPassWord, "缺少【旧密码】");
		ValidateUtil.notEmpty(newPassWord, "缺少【新密码】");
		ValidateUtil.notEmpty(confirmNewPassWord, "缺少【确认密码】");
		//
		if(newPassWord.length()<8) {
			throw new IllegalArgumentException("新密码长度不能少于8位数");
		}
		if(!newPassWord.equals(confirmNewPassWord)) {
			throw new IllegalArgumentException("新密码与确认密码不一致");
		}
		String userName=SessionUtil.userName(request);
		User obj=	Table.findOne(User.class, "user_name=?", new Object[] {userName}, false, "passWord");
		if(obj!=null) {
			String passWord_source=obj.getPassWord();
			//
			if(!EncryptUtil.getMD5(oldPassWord).equals(passWord_source)) {
				throw new IllegalArgumentException("旧密码错误");
			}
		}else{
			throw new IllegalArgumentException("当前用户不存在");
		}
		String newPassWord_cipher=EncryptUtil.getMD5(newPassWord);		//密文密码
		obj.setPassWord(newPassWord_cipher);
		obj.save();
	}
	/**验证当前用户密码（可做解锁用）**/
	@An_URL("/validPassWord")
	public void validPassWord(HttpServletRequest request,HttpServletResponse response,String passWord) throws Exception{
		ValidateUtil.notEmpty(passWord, "没有输入密码");
		//
		String userName=SessionUtil.userName(request);
		User obj=Table.findOne(User.class, "user_name=?", new Object[] {userName}, false,"passWord");
		if(obj!=null) {
			String passWord_cipher=EncryptUtil.getMD5(passWord);
			String t_passWord=obj.getPassWord();
			if(!passWord_cipher.equals(t_passWord)) {
				throw new IllegalArgumentException("密码不正确");
			}
		}else{
			throw new IllegalArgumentException("用户名不存在");
		}
	}
	/**退出**/
	@An_URL("/exit")
	public Map<String,Object> exit(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().invalidate();
		Map<String,Object> resultMap=new HashMap<String, Object>();
		String href=request.getContextPath();
		if(href==null || href.length()<=0) {
			href="/";
		}
		resultMap.put("href", href);
		return resultMap;
	}
	
}
